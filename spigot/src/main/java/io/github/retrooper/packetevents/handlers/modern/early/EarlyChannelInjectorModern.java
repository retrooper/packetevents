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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.ListWrapper;
import com.github.retrooper.packetevents.util.reflection.ClassUtil;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import io.github.retrooper.packetevents.handlers.EarlyInjector;
import io.github.retrooper.packetevents.handlers.modern.PacketDecoderModern;
import io.github.retrooper.packetevents.handlers.modern.PacketEncoderModern;
import io.github.retrooper.packetevents.handlers.modern.ServerConnectionInitializerModern;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import io.github.retrooper.packetevents.utils.dependencies.protocolsupport.ProtocolSupportUtil;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.ViaVersionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EarlyChannelInjectorModern implements EarlyInjector {
    private final List<ChannelFuture> injectedFutures = new ArrayList<>();
    private final List<Map<Field, Object>> injectedLists = new ArrayList<>();

    @Override
    public boolean isBound() {
        try {
            Object connection = SpigotReflectionUtil.getMinecraftServerConnectionInstance();
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
            Object serverConnection = SpigotReflectionUtil.getMinecraftServerConnectionInstance();
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
        List<Object> networkManagers = SpigotReflectionUtil.getNetworkManagers();
        //TODO We are synchronizing a local variable, lets optimize this
        for (Object networkManager : networkManagers) {
            ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
            Channel channel = networkManagerWrapper.readObject(0, Channel.class);
            //TODO Check for NioSocketChannel or EpollSocketChannel
            if (channel == null) {
                continue;
            }
            ServerConnectionInitializerModern.postInitChannel(channel, ConnectionState.PLAY);
        }
    }


    private void injectChannelFuture(ChannelFuture future) {
        ChannelPipeline pipeline = future.channel().pipeline();
        ChannelHandler connectionHandler = pipeline.get(PacketEvents.CONNECTION_NAME);
        if (connectionHandler != null) {
            pipeline.remove(PacketEvents.CONNECTION_NAME);
        }
        if (pipeline.get("SpigotNettyServerChannelHandler#0") != null) {
            pipeline.addAfter("SpigotNettyServerChannelHandler#0", PacketEvents.CONNECTION_NAME, new ServerChannelHandlerModern());
        } else if (pipeline.get("floodgate-init") != null) {
            pipeline.addAfter("floodgate-init", PacketEvents.CONNECTION_NAME, new ServerChannelHandlerModern());
        } else {
            pipeline.addFirst(PacketEvents.CONNECTION_NAME, new ServerChannelHandlerModern());
        }
        List<Object> networkManagers = SpigotReflectionUtil.getNetworkManagers();
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
        future.channel().pipeline().remove(PacketEvents.CONNECTION_NAME);
    }

    @Override
    public void eject() {
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
                PacketEvents.getAPI().getLogger().severe("PacketEvents failed to eject the injection handler! Please reboot!!");
            }
        }

        injectedLists.clear();
    }

    @Override
    public void updateUser(Object channel, User user) {
        PacketEncoderModern encoder = getEncoder(channel);
        if (encoder != null) {
            encoder.user = user;
        }

        PacketDecoderModern decoder = getDecoder(channel);
        if (decoder != null) {
            decoder.user = user;
        }
    }

    @Override
    public void injectPlayer(Object player, @Nullable ConnectionState connectionState) {
        Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        updatePlayerObject(channel, player, connectionState);
    }

    @Override
    public void ejectPlayer(Object player) {
        Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        ServerConnectionInitializerModern.postDestroyChannel(channel);
    }

    @Override
    public boolean hasInjected(Object player) {
        Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        if (channel != null) {
            PacketDecoderModern decoder = getDecoder(channel);
            PacketEncoderModern encoder = getEncoder(channel);
            return decoder != null && decoder.player != null
                    && encoder != null && encoder.player != null;
        } else {
            return false;
        }
    }

    private PacketDecoderModern getDecoder(Object ch) {
        Channel channel = (Channel) ch;
        ChannelHandler decoder = channel.pipeline().get(PacketEvents.DECODER_NAME);
        if (decoder instanceof PacketDecoderModern) {
            return (PacketDecoderModern) decoder;
        } else if (ViaVersionUtil.isAvailable()) {
            ChannelHandler mcDecoder = channel.pipeline().get("decoder");
            if (ViaVersionUtil.getBukkitDecodeHandlerClass().isInstance(mcDecoder)) {
                ReflectionObject reflectMCDecoder = new ReflectionObject(mcDecoder);
                ByteToMessageDecoder injectedDecoder = reflectMCDecoder.readObject(0, ByteToMessageDecoder.class);
                if (injectedDecoder instanceof PacketDecoderModern) {
                    return (PacketDecoderModern) injectedDecoder;
                } else {
                    ReflectionObject reflectInjectedDecoder = new ReflectionObject(injectedDecoder);
                    List<Object> decoders = reflectInjectedDecoder.readList(0);
                    for (Object customDecoder : decoders) {
                        if (customDecoder instanceof PacketDecoderModern) {
                            return (PacketDecoderModern) customDecoder;
                        }
                    }
                }
            }
        }
        return null;
    }

    private PacketEncoderModern getEncoder(Object ch) {
        Channel channel = (Channel) ch;
        ChannelHandler encoder = channel.pipeline().get(PacketEvents.ENCODER_NAME);
        if (encoder instanceof PacketEncoderModern) {
            return (PacketEncoderModern) encoder;
        }
        return null;
    }

    @Override
    public void updatePlayerObject(Object ch, Object player, @Nullable ConnectionState newConnectionState) {
        Channel channel = (Channel) ch;
        PacketDecoderModern decoder = getDecoder(ch);
        if (decoder != null) {
            decoder.player = (Player) player;
            decoder.user.getProfile().setUUID(((Player) player).getUniqueId());
            decoder.user.getProfile().setName(((Player) player).getName());
            if (newConnectionState == ConnectionState.PLAY) {
                if (ViaVersionUtil.isAvailable()) {
                    ChannelHandler handler = channel.pipeline().get(PacketEvents.DECODER_NAME);
                    if (handler != null) {
                        channel.pipeline().remove(PacketEvents.DECODER_NAME);
                        addCustomViaDecoder(channel, new PacketDecoderModern(decoder));
                    }
                } else if (ProtocolSupportUtil.isAvailable()) {
                    channel.pipeline().remove(PacketEvents.DECODER_NAME);
                    channel.pipeline().addAfter("ps_decoder_transformer", PacketEvents.DECODER_NAME, new PacketDecoderModern(decoder));
                }
            }
            if (newConnectionState != null) {
                decoder.user.setConnectionState(newConnectionState);
            }
        }
        PacketEncoderModern encoder = getEncoder(ch);
        if (encoder != null) {
            encoder.player = (Player) player;
            if (newConnectionState == ConnectionState.PLAY) {
                encoder.handledCompression = true;
            }
        }
    }

    @Override
    public ConnectionState getConnectionState(Object channel) {
        PacketEncoderModern encoder = getEncoder(channel);
        if (encoder != null) {
            return encoder.user.getConnectionState();
        }
        return null;
    }

    private void addCustomViaDecoder(Object ch, PacketDecoderModern decoder) {
        Channel channel = (Channel) ch;
        ChannelHandler viaDecoder = channel.pipeline().get("decoder");
        ReflectionObject reflectionObject = new ReflectionObject(viaDecoder);
        ByteToMessageDecoder mcDecoder = reflectionObject.readObject(0, ByteToMessageDecoder.class);
        if (ClassUtil.getClassSimpleName(mcDecoder.getClass()).equals("PacketDecoderModern")) {
            //We aren't the first decoder to inject into ViaVersion's decoder
            ReflectionObject reflectPacketDecoderModern = new ReflectionObject(mcDecoder);
            List<Object> decoders = reflectPacketDecoderModern.readList(0);
            decoders.add(decoder);
            reflectPacketDecoderModern.writeList(0, decoders);
        } else {
            //We are the first decoder to inject into ViaVersion's decoder
            decoder.mcDecoder = mcDecoder;
            reflectionObject.write(ByteToMessageDecoder.class, 0, decoder);
        }
    }

    @Override
    public void changeConnectionState(Object ch, ConnectionState connectionState) {
        Channel channel = (Channel) ch;
        PacketDecoderModern decoder = getDecoder(ch);
        if (decoder != null) {
            //Change connection state in decoder
            decoder.user.setConnectionState(connectionState);
            if (connectionState == ConnectionState.PLAY) {
                if (ViaVersionUtil.isAvailable()) {
                    channel.pipeline().remove(PacketEvents.DECODER_NAME);
                    decoder.handledCompression = true;
                    addCustomViaDecoder(channel, new PacketDecoderModern(decoder));
                } else if (ProtocolSupportUtil.isAvailable()) {
                    channel.pipeline().remove(PacketEvents.DECODER_NAME);
                    channel.pipeline().addAfter("ps_decoder_transformer", PacketEvents.DECODER_NAME, new PacketDecoderModern(decoder));
                }
            }
        }

    }
}