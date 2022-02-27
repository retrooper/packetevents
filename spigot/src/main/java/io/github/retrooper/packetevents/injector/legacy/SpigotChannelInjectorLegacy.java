/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.injector.legacy;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import io.github.retrooper.packetevents.injector.legacy.handlers.PacketDecoderLegacy;
import io.github.retrooper.packetevents.injector.legacy.handlers.PacketEncoderLegacy;
import io.github.retrooper.packetevents.injector.legacy.connection.ServerConnectionInitializerLegacy;
import io.github.retrooper.packetevents.injector.legacy.connection.ServerChannelHandlerLegacy;
import io.github.retrooper.packetevents.util.InjectedList;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelFuture;
import net.minecraft.util.io.netty.channel.ChannelHandler;
import net.minecraft.util.io.netty.channel.ChannelPipeline;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpigotChannelInjectorLegacy implements ChannelInjector {
    //Channels that process connecting clients.
    private final Set<Channel> injectedConnectionChannels = new HashSet<>();
    private List<Object> networkManagers;
    private int connectionChannelsListIndex = -1;


    @Override
    public boolean isServerBound() {
        //We want to check if the server has been bound to the port already.
        Object serverConnection = SpigotReflectionUtil.getMinecraftServerConnectionInstance();
        if (serverConnection != null) {
            ReflectionObject reflectServerConnection = new ReflectionObject(serverConnection);
            //There should only be 2 lists.
            for (int i = 0; i < 2; i++) {
                List<?> list = reflectServerConnection.readList(i);
                for (Object value : list) {
                    if (value instanceof ChannelFuture) {
                        connectionChannelsListIndex = i;
                        //Found the right list.
                        //It has connection channels, so the server has been bound.
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void inject() {
        Object serverConnection = SpigotReflectionUtil.getMinecraftServerConnectionInstance();
        if (serverConnection != null) {
            ReflectionObject reflectServerConnection = new ReflectionObject(serverConnection);
            List<ChannelFuture> connectionChannelFutures = reflectServerConnection.readList(connectionChannelsListIndex);
            InjectedList<ChannelFuture> wrappedList = new InjectedList<>(connectionChannelFutures, future -> {
                //Each time a channel future is added, we run this.
                //This is automatically also ran for the elements already added before we wrapped the list.
                Channel channel = future.channel();
                //Inject into the server connection channel.
                injectConnectionChannel(channel);
                //Make sure to store it, so we can uninject later on.
                injectedConnectionChannels.add(channel);
            });
            //Replace the list with our wrapped one.
            reflectServerConnection.writeList(connectionChannelsListIndex, wrappedList);

            //Player channels might have been registered already. Let us add our handlers. We are a little late though.
            List<Object> networkManagers = SpigotReflectionUtil.getNetworkManagers();
            for (Object networkManager : networkManagers) {
                ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
                Channel channel = networkManagerWrapper.readObject(0, Channel.class);
                if (channel == null) {
                    continue;
                }
                ServerConnectionInitializerLegacy.initChannel(channel, ConnectionState.PLAY);
            }
        }
    }

    @Override
    public void uninject() {
        //Uninject our connection handler from these connection channels.
        for (Channel connectionChannel : injectedConnectionChannels) {
            uninjectConnectionChannel(connectionChannel);
        }
        injectedConnectionChannels.clear();
        Object serverConnection = SpigotReflectionUtil.getMinecraftServerConnectionInstance();
        if (serverConnection != null) {
            ReflectionObject reflectServerConnection = new ReflectionObject(serverConnection);
            List<ChannelFuture> connectionChannelFutures = reflectServerConnection.readList(connectionChannelsListIndex);
            if (connectionChannelFutures instanceof InjectedList) {
                //Let us unwrap this. We no longer want to listen to connecting channels.
                reflectServerConnection.writeList(connectionChannelsListIndex,
                        ((InjectedList<ChannelFuture>) connectionChannelFutures).originalList());
            }
        }
    }

    private void injectConnectionChannel(Channel connectionChannel) {
        ChannelPipeline pipeline = connectionChannel.pipeline();
        ChannelHandler connectionHandler = pipeline.get(PacketEvents.CONNECTION_HANDLER_NAME);
        if (connectionHandler != null) {
            //Why does it already exist?
            pipeline.remove(PacketEvents.CONNECTION_HANDLER_NAME);
        }
        //Make sure we handle connections after ProtocolSupport.
        if (pipeline.get("SpigotNettyServerChannelHandler#0") != null) {
            pipeline.addAfter("SpigotNettyServerChannelHandler#0", PacketEvents.CONNECTION_HANDLER_NAME, new ServerChannelHandlerLegacy());
        }
        //Make sure we handle connections after Geyser.
        else if (pipeline.get("floodgate-init") != null) {
            pipeline.addAfter("floodgate-init", PacketEvents.CONNECTION_HANDLER_NAME, new ServerChannelHandlerLegacy());
        }
        //Otherwise, we just don't care and make sure we are first.
        else {
            pipeline.addFirst(PacketEvents.CONNECTION_HANDLER_NAME, new ServerChannelHandlerLegacy());
        }

        if (networkManagers == null) {
            networkManagers = SpigotReflectionUtil.getNetworkManagers();
        }
        //Make sure we handled all connected clients.
        synchronized (networkManagers) {
            for (Object networkManager : networkManagers) {
                ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
                Channel channel = networkManagerWrapper.readObject(0, Channel.class);
                if (channel.isOpen()) {
                    if (channel.localAddress().equals(connectionChannel.localAddress())) {
                        channel.close();
                    }
                }
            }
        }
    }

    private void uninjectConnectionChannel(Channel connectionChannel) {
        connectionChannel.pipeline().remove(PacketEvents.CONNECTION_HANDLER_NAME);
    }


    @Override
    public User getUser(Object channel) {
        return getEncoder((Channel) channel).user;
    }

    @Override
    public void changeConnectionState(Object ch, @Nullable ConnectionState connectionState) {
        Channel channel = (Channel) ch;
        PacketEncoderLegacy encoder = getEncoder(channel);
        if (encoder != null) {
            //Change connection state in encoder
            encoder.user.setConnectionState(connectionState);
        }

        //On 1.7 this decoder is sharable, we can re-add it to the pipeline without cloning.
        PacketDecoderLegacy decoder = getDecoder(channel);
        if (decoder != null) {
            //Change connection state in decoder
            decoder.user.setConnectionState(connectionState);
            if (connectionState == ConnectionState.PLAY) {
                if (ProtocolSupportUtil.isAvailable()) {
                    channel.pipeline().remove(PacketEvents.DECODER_NAME);
                    channel.pipeline().addAfter("ps_decoder_transformer", PacketEvents.DECODER_NAME, decoder);
                }
            }
        }
    }

    @Override
    public void updateUser(Object channel, User user) {
        PacketEncoderLegacy encoder = getEncoder((Channel) channel);
        if (encoder != null) {
            encoder.user = user;
        }

        PacketDecoderLegacy decoder = getDecoder((Channel) channel);
        if (decoder != null) {
            decoder.user = user;
        }
    }

    @Override
    public void setPlayer(Object channel, Object player) {
        PacketEncoderLegacy encoder = getEncoder((Channel) channel);
        if (encoder != null) {
            encoder.player = (Player) player;
        }

        PacketDecoderLegacy decoder = getDecoder((Channel) channel);
        if (decoder != null) {
            decoder.player = (Player) player;
            decoder.user.getProfile().setUUID(((Player) player).getUniqueId());
            decoder.user.getProfile().setName(((Player) player).getName());
        }
    }

    @Override
    public boolean hasPlayer(Object player) {
        Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        return channel != null && getEncoder((Channel) channel).player != null;
    }

    private PacketEncoderLegacy getEncoder(Channel channel) {
        return (PacketEncoderLegacy) channel.pipeline().get(PacketEvents.ENCODER_NAME);
    }

    private PacketDecoderLegacy getDecoder(Channel channel) {
        return (PacketDecoderLegacy) channel.pipeline().get(PacketEvents.DECODER_NAME);
    }

}
