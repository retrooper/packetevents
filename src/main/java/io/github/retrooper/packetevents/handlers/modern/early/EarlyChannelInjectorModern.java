/*
 * This file is part of ViaVersion - https://github.com/ViaVersion/ViaVersion
 * Copyright (C) 2016-2021 ViaVersion and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.handlers.modern.early;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.handlers.EarlyInjector;
import io.github.retrooper.packetevents.handlers.modern.PacketDecoderModern;
import io.github.retrooper.packetevents.handlers.modern.PacketEncoderModern;
import io.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.utils.MinecraftReflection;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.CustomBukkitDecodeHandler;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.ViaVersionAccessorImpl;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.ViaVersionLookupUtils;
import io.github.retrooper.packetevents.utils.list.ListWrapper;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.reflection.ReflectionObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

public class EarlyChannelInjectorModern implements EarlyInjector {
    private final List<ChannelFuture> injectedFutures = new ArrayList<>();
    private final List<Map<Field, Object>> injectedLists = new ArrayList<>();
    public boolean viaAvailable = false;

    @Override
    public boolean isBound() {
        try {
            Object connection = MinecraftReflection.getMinecraftServerConnectionInstance();
            if (connection == null) {
                return false;
            }
            for (Field field : connection.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                final Object value = field.get(connection);
                if (value instanceof List) {
                    // Inject the list
                    synchronized (value) {
                        for (Object o : (List) value) {
                            if (o instanceof ChannelFuture) {
                                return true;
                            } else {
                                break; // not the right list.
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    @Override
    public void inject() {
        try {
            if (PaperChannelInjector.PAPER_INJECTION_METHOD) {
                PaperChannelInjector.setPaperChannelInitializeListener();
                return;
            }
            Object serverConnection = MinecraftReflection.getMinecraftServerConnectionInstance();
            for (Field field : serverConnection.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(serverConnection);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (value instanceof List) {
                    //Get the list.
                    List listWrapper = new ListWrapper((List) value) {
                        @Override
                        public void processAdd(Object o) {
                            if (o instanceof ChannelFuture) {
                                try {
                                    injectChannelFuture((ChannelFuture) o);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    };
                    HashMap<Field, Object> map = new HashMap<>();
                    map.put(field, serverConnection);
                    injectedLists.add(map);

                    field.set(serverConnection, listWrapper);

                    synchronized (listWrapper) {
                        for (Object serverChannel : (List) value) {
                            //Is this the server channel future list?
                            if (serverChannel instanceof ChannelFuture) {
                                //Yes it is...
                                injectChannelFuture((ChannelFuture) serverChannel);
                            } else {
                                break;//Wrong list
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new IllegalStateException("PacketEvents failed to inject!", ex);
        }

        //Player channels might have been registered already. Let us add our handlers. We are a little late though.
        //This only happens when you join extremely early on older versions of minecraft.
        List<Object> networkManagers = MinecraftReflection.getNetworkManagers();
        synchronized (networkManagers) {
            for (Object networkManager : networkManagers) {
                ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
                Channel channel = networkManagerWrapper.readObject(0, Channel.class);
                if (channel == null) {
                    continue;
                }
                ServerConnectionInitializerModern.postInitChannel(channel);
            }
        }
    }


    private void injectChannelFuture(ChannelFuture future) {
        ChannelPipeline pipeline = future.channel().pipeline();
        if (pipeline.get("SpigotNettyServerChannelHandler#0") != null) {
            pipeline.addAfter("SpigotNettyServerChannelHandler#0", PacketEvents.get().connectionName, new ServerChannelHandlerModern());
        } else {
            pipeline.addFirst(PacketEvents.get().connectionName, new ServerChannelHandlerModern());
        }

        List<Object> networkManagers = MinecraftReflection.getNetworkManagers();
        synchronized (networkManagers) {
            for (Object networkManager : networkManagers) {
                ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
                Channel channel = networkManagerWrapper.readObject(0, Channel.class);
                if (channel.isOpen()) {
                    if (channel.localAddress().equals(future.channel().localAddress())) {
                        channel.close();
                    }
                }
            }
        }
        injectedFutures.add(future);
    }

    private void ejectChannelFuture(ChannelFuture future) {
        future.channel().pipeline().remove(PacketEvents.get().connectionName);
    }

    @Override
    public void eject() {
        if (PaperChannelInjector.PAPER_INJECTION_METHOD) {
            try {
                PaperChannelInjector.removePaperChannelInitializeListener();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (ChannelFuture future : injectedFutures) {
            ejectChannelFuture(future);
        }
        injectedFutures.clear();

        for (Map<Field, Object> map : injectedLists) {
            try {
                for (Field key : map.keySet()) {
                    key.setAccessible(true);
                    Object o = map.get(key);
                    if (o instanceof ListWrapper) {
                        key.set(o, ((ListWrapper) o).getOriginalList());
                    }
                }

            } catch (IllegalAccessException e) {
                PacketEvents.get().getPlugin().getLogger().severe("PacketEvents failed to eject the injection handler! Please reboot!!");
            }
        }

        injectedLists.clear();
    }

    @Override
    public void injectPlayer(Player player) {
        Object channel = PacketEvents.get().getPlayerManager().getChannel(player);
        if (channel != null) {
            updatePlayerObject(player, channel);
        }
    }

    @Override
    public void ejectPlayer(Player player) {
        Object channel = PacketEvents.get().getPlayerManager().getChannel(player);
        if (channel != null) {
            ServerConnectionInitializerModern.postDestroyChannel((Channel) channel);
        }
    }

    @Override
    public boolean hasInjected(Player player) {
        Object channel = PacketEvents.get().getPlayerManager().getChannel(player);
        if (channel == null) {
            return false;
        }
        PacketDecoderModern decoder = getDecoder(channel);
        PacketEncoderModern encoder = getEncoder(channel);
        return decoder != null && decoder.player != null
                && encoder != null && encoder.player != null;
    }

    private PacketDecoderModern getDecoder(Object rawChannel) {
        Channel channel = (Channel) rawChannel;
        ChannelHandler decoder = channel.pipeline().get(PacketEvents.get().decoderName);
        if (decoder != null) {
            return (PacketDecoderModern) decoder;
        } else if (viaAvailable) {
            ChannelHandler mcDecoder = channel.pipeline().get("decoder");
            if (mcDecoder instanceof CustomBukkitDecodeHandler) {
                return (PacketDecoderModern) ((CustomBukkitDecodeHandler)mcDecoder).getCustomDecoder(PacketEvents.get().decoderName);
            }
            else if (ClassUtil.getClassSimpleName(mcDecoder.getClass()).equals("CustomBukkitDecodeHandler")) {
                List<MessageToMessageDecoder<?>> customDecoders = new ReflectionObject(mcDecoder).readList(0);
                for (MessageToMessageDecoder<?> customDecoder : customDecoders) {
                    ReflectionObject reflectionObject = new ReflectionObject(customDecoder);
                    String customDecoderHandlerName = reflectionObject.readString(0);
                    if (customDecoderHandlerName.equals(PacketEvents.get().decoderName)) {
                        return (PacketDecoderModern) customDecoder;
                    }
                }
            }
        }
        return null;
    }

    private PacketEncoderModern getEncoder(Object rawChannel) {
        Channel channel = (Channel) rawChannel;
        ChannelHandler encoder = channel.pipeline().get(PacketEvents.get().encoderName);
        if (encoder instanceof PacketEncoderModern) {
            return (PacketEncoderModern) encoder;
        } else {
            return null;
        }
    }

    @Override
    public void updatePlayerObject(Player player, Object rawChannel) {
        PacketDecoderModern decoder = getDecoder(rawChannel);
        if (decoder != null) {
            decoder.player = player;
        }
        PacketEncoderModern encoder = getEncoder(rawChannel);
        if (encoder != null) {
            encoder.player = player;
        }
    }

    @Override
    public ConnectionState getConnectionState(Object channel) {
        ConnectionState state = PacketEvents.get().getPlayerManager().connectionStates.get(channel);
        if (state == null) {
            System.out.println("STATE WAS NOT CACHED!");
            PacketDecoderModern decoder = getDecoder(channel);
            if (decoder != null) {
                state = decoder.connectionState;
                //Cache connection state in map
                PacketEvents.get().getPlayerManager().connectionStates.put(channel, state);
            } else {
                return null;
            }
        }
        return state;
    }

    @Override
    public void changeConnectionState(Object channel, ConnectionState connectionState) {
        PacketDecoderModern decoder = getDecoder(channel);
        if (decoder != null) {
            //Change connection state in decoder
            decoder.connectionState = connectionState;
            //Change connection state in map
            PacketEvents.get().getPlayerManager().connectionStates.put(channel, connectionState);
            if (connectionState == ConnectionState.GAME && (viaAvailable || ViaVersionLookupUtils.isAvailable())) {
                viaAvailable = true;
                ((Channel) channel).pipeline().remove(PacketEvents.get().decoderName);
                decoder.handleViaVersion = true;
                decoder.handlerName = PacketEvents.get().decoderName;
                PacketEncoderModern encoder = (PacketEncoderModern) ((Channel) channel).pipeline().remove(PacketEvents.get().encoderName);
                encoder.handleViaVersion = true;
                //If via is present, replace their decode handler with my custom one
                ((ViaVersionAccessorImpl) ViaVersionLookupUtils.getViaVersionAccessor()).addDecoderAfterVia(channel, decoder);

                //TODO Confirm
                ((Channel) channel).pipeline().addAfter("encoder", PacketEvents.get().encoderName, encoder);
                System.out.println("CHANGED HANDLERS: " + Arrays.toString(((Channel) channel).pipeline().names().toArray(new String[0])));
            }
        }

    }
}
