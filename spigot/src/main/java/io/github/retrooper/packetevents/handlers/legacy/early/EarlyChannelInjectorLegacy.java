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

package io.github.retrooper.packetevents.handlers.legacy.early;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.util.ListWrapper;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import io.github.retrooper.packetevents.handlers.EarlyInjector;
import io.github.retrooper.packetevents.handlers.legacy.PacketDecoderLegacy;
import io.github.retrooper.packetevents.handlers.legacy.PacketEncoderLegacy;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import io.github.retrooper.packetevents.utils.dependencies.protocolsupport.ProtocolSupportUtil;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelFuture;
import net.minecraft.util.io.netty.channel.ChannelHandler;
import net.minecraft.util.io.netty.channel.ChannelPipeline;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EarlyChannelInjectorLegacy implements EarlyInjector {
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
                        for (Object o : (List<?>) value) {
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
        synchronized (networkManagers) {
            for (Object networkManager : networkManagers) {
                ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
                Channel channel = networkManagerWrapper.readObject(0, Channel.class);
                if (channel == null) {
                    continue;
                }
                ServerConnectionInitializerLegacy.postInitChannel(channel, ConnectionState.PLAY);
            }
        }
    }


    private void injectChannelFuture(ChannelFuture future) {
        ChannelPipeline pipeline = future.channel().pipeline();
        ChannelHandler connectionHandler = pipeline.get(PacketEvents.CONNECTION_NAME);
        if (connectionHandler != null) {
            pipeline.remove(PacketEvents.CONNECTION_NAME);
        }
        if (pipeline.get("SpigotNettyServerChannelHandler#0") != null) {
            pipeline.addAfter("SpigotNettyServerChannelHandler#0", PacketEvents.CONNECTION_NAME, new ServerChannelHandlerLegacy());
        } else {
            pipeline.addFirst(PacketEvents.CONNECTION_NAME, new ServerChannelHandlerLegacy());
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
    public void injectPlayer(Object player, @Nullable ConnectionState connectionState) {
        ChannelAbstract channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        if (channel != null) {
            updatePlayerObject(channel, player, connectionState);
        }
    }

    @Override
    public void ejectPlayer(Object player) {
        ChannelAbstract channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        if (channel != null) {
            try {
                ServerConnectionInitializerLegacy.postDestroyChannel(channel.rawChannel());
            } catch (Exception ignored) {

            }
        }
    }

    @Override
    public boolean hasInjected(Object player) {
        ChannelAbstract channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        if (channel == null) {
            return false;
        }
        PacketDecoderLegacy decoder = getDecoder(channel);
        PacketEncoderLegacy encoder = getEncoder(channel);
        return decoder != null && decoder.player != null &&
                encoder != null && encoder.player != null;
    }

    private PacketDecoderLegacy getDecoder(ChannelAbstract ch) {
        Channel channel = (Channel) ch.rawChannel();
        ChannelHandler decoder = channel.pipeline().get(PacketEvents.DECODER_NAME);
        if (decoder != null) {
            return (PacketDecoderLegacy) decoder;
        } else {
            return null;
        }
    }

    private PacketEncoderLegacy getEncoder(ChannelAbstract ch) {
        Channel channel = (Channel) ch.rawChannel();
        ChannelHandler encoder = channel.pipeline().get(PacketEvents.ENCODER_NAME);
        if (encoder instanceof PacketEncoderLegacy) {
            return (PacketEncoderLegacy) encoder;
        }
        return null;
    }

    @Override
    public void updatePlayerObject(ChannelAbstract ch, Object player, @Nullable ConnectionState newConnectionState) {
        PacketDecoderLegacy decoder = getDecoder(ch);
        if (decoder == null) {
            if (newConnectionState == null) {
                newConnectionState = ConnectionState.PLAY;
            }
            ServerConnectionInitializerLegacy.postInitChannel(ch.rawChannel(), newConnectionState);
            decoder = getDecoder(ch);
        }

        if (decoder != null) {
            decoder.player = (Player) player;
            if (newConnectionState != null) {
                decoder.connectionState = newConnectionState;
            }
        }

        PacketEncoderLegacy encoder = getEncoder(ch);
        if (encoder != null) {
            encoder.player = (Player) player;
        }
    }

    @Override
    public ConnectionState getConnectionState(ChannelAbstract channel) {
        PacketDecoderLegacy decoder = getDecoder(channel);
        if (decoder != null) {
            return decoder.connectionState;
        } else {
            return null;
        }
    }

    @Override
    public void changeConnectionState(ChannelAbstract ch, ConnectionState connectionState) {
        Channel channel = (Channel) ch.rawChannel();
        PacketDecoderLegacy decoder = getDecoder(ch);
        if (decoder != null) {
            //Change connection state in decoder
            decoder.connectionState = connectionState;
            if (connectionState == ConnectionState.PLAY) {
                if (ProtocolSupportUtil.isAvailable()) {
                    channel.pipeline().remove(PacketEvents.DECODER_NAME);
                    decoder.bypassCompression = true;
                    channel.pipeline().addAfter("ps_decoder_transformer", PacketEvents.DECODER_NAME, decoder);
                }
            }
        }

    }
}
