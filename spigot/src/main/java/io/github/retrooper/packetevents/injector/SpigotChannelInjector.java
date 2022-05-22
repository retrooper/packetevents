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

package io.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.reflection.ClassUtil;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import io.github.retrooper.packetevents.injector.connection.ServerChannelHandler;
import io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import io.github.retrooper.packetevents.injector.handlers.PacketDecoder;
import io.github.retrooper.packetevents.injector.handlers.PacketEncoder;
import io.github.retrooper.packetevents.util.InjectedList;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpigotChannelInjector implements ChannelInjector {
    //Channels that process connecting clients.
    public final Set<Channel> injectedConnectionChannels = new HashSet<>();
    public List<Object> networkManagers;
    private int connectionChannelsListIndex = -1;
    public boolean inboundAheadProtocolTranslation = false;
    public boolean outboundAheadProtocolTranslation = false;

    public void updatePlayer(User user, Object player) {
        PacketEvents.getAPI().getEventManager().callEvent(new UserLoginEvent(user, player));
        Object channel = user.getChannel();
        if (channel == null) {
            channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        }
        setPlayer(channel, player);
    }

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
                injectServerChannel(channel);
                //Make sure to store it, so we can uninject later on.
                injectedConnectionChannels.add(channel);
            });
            //Replace the list with our wrapped one.
            reflectServerConnection.writeList(connectionChannelsListIndex, wrappedList);

            //Player channels might have been registered already. Let us add our handlers. We are a little late though.
            if (networkManagers == null) {
                networkManagers = SpigotReflectionUtil.getNetworkManagers();
            }
            synchronized (networkManagers) {
                for (Object networkManager : networkManagers) {
                    ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
                    Channel channel = networkManagerWrapper.readObject(0, Channel.class);
                    if (channel == null) {
                        continue;
                    }
                    try {
                        ServerConnectionInitializer.initChannel(channel, ConnectionState.PLAY);
                    } catch (Exception e) {
                        System.out.println("Spigot injector failed to inject into an existing channel.");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void uninject() {
        //Uninject our connection handler from these connection channels.
        for (Channel connectionChannel : injectedConnectionChannels) {
            uninjectServerChannel(connectionChannel);
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

    private void injectServerChannel(Channel serverChannel) {
        ChannelPipeline pipeline = serverChannel.pipeline();
        ChannelHandler connectionHandler = pipeline.get(PacketEvents.CONNECTION_HANDLER_NAME);
        if (connectionHandler != null) {
            //Why does it already exist? Remove it.
            pipeline.remove(PacketEvents.CONNECTION_HANDLER_NAME);
        }
        //Make sure we handle connections after ProtocolSupport.
        if (pipeline.get("SpigotNettyServerChannelHandler#0") != null) {
            pipeline.addAfter("SpigotNettyServerChannelHandler#0", PacketEvents.CONNECTION_HANDLER_NAME, new ServerChannelHandler());
        }
        //Make sure we handle connections after Geyser.
        else if (pipeline.get("floodgate-init") != null) {
            pipeline.addAfter("floodgate-init", PacketEvents.CONNECTION_HANDLER_NAME, new ServerChannelHandler());
        }
        //Otherwise, make sure we are first.
        else {
            pipeline.addFirst(PacketEvents.CONNECTION_HANDLER_NAME, new ServerChannelHandler());
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
                    if (channel.localAddress().equals(serverChannel.localAddress())) {
                        channel.close();
                    }
                }
            }
        }
    }

    private void uninjectServerChannel(Channel serverChannel) {
        serverChannel.pipeline().remove(PacketEvents.CONNECTION_HANDLER_NAME);
    }


    @Override
    public User getUser(Object channel) {
        return getEncoder((Channel) channel).user;
    }

    @Override
    public void changeConnectionState(Object ch, @Nullable ConnectionState connectionState) {
        Channel channel = (Channel) ch;
        PacketEncoder encoder = getEncoder(channel);
        if (encoder != null) {
            //Change connection state in encoder
            encoder.user.setConnectionState(connectionState);
        }

        PacketDecoder decoder = getDecoder(channel);
        if (decoder != null) {
            //Change connection state in decoder
            decoder.user.setConnectionState(connectionState);
            if (connectionState == ConnectionState.PLAY) {
                if (ViaVersionUtil.isAvailable()) {
                    channel.pipeline().remove(PacketEvents.DECODER_NAME);
                    decoder.handledCompression = true;
                    //Clone our decoder(as it is not sharable)
                    decoder = new PacketDecoder(decoder);
                    //Inject our decoder into ViaVersion's decoder (because we have to)
                    ChannelHandler viaDecoder = channel.pipeline().get("decoder");
                    ReflectionObject reflectionObject = new ReflectionObject(viaDecoder);
                    ByteToMessageDecoder mcDecoder = reflectionObject.readObject(0, ByteToMessageDecoder.class);
                    String decoderClassName = ClassUtil.getClassSimpleName(mcDecoder.getClass());
                    if (decoderClassName.equals("PacketDecoderModern")
                            || decoderClassName.equals("PacketDecoderLatest")) {
                        //We aren't the first packetevents instance to inject into ViaVersion's decoder
                        ReflectionObject reflectPacketDecoderModern = new ReflectionObject(mcDecoder);
                        List<ByteToMessageDecoder> decoders = reflectPacketDecoderModern.readList(0);
                        decoders.add(decoder);
                    } else {
                        //We are the first packetevents instance to inject into ViaVersion's decoder
                        decoder.mcDecoder = mcDecoder;
                        reflectionObject.write(ByteToMessageDecoder.class, 0, decoder);
                    }
                } else if (ProtocolSupportUtil.isAvailable()) {
                    channel.pipeline().remove(PacketEvents.DECODER_NAME);
                    channel.pipeline().addAfter("ps_decoder_transformer", PacketEvents.DECODER_NAME, new PacketDecoder(decoder));
                }
            }
        }
    }

    @Override
    public void updateUser(Object channel, User user) {
        PacketEncoder encoder = getEncoder((Channel) channel);
        if (encoder != null) {
            encoder.user = user;
        }

        PacketDecoder decoder = getDecoder((Channel) channel);
        if (decoder != null) {
            decoder.user = user;
        }
    }

    @Override
    public void setPlayer(Object channel, Object player) {
        PacketEncoder encoder = getEncoder((Channel) channel);
        if (encoder != null) {
            encoder.player = (Player) player;
        }

        PacketDecoder decoder = getDecoder((Channel) channel);
        if (decoder != null) {
            decoder.player = (Player) player;
            decoder.user.getProfile().setUUID(((Player) player).getUniqueId());
            decoder.user.getProfile().setName(((Player) player).getName());
        }
    }

    @Override
    public boolean hasPlayer(Object player) {
        Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        PacketDecoder decoder = getDecoder((Channel) channel);
        return decoder != null && decoder.player != null;
    }

    private PacketEncoder getEncoder(Channel channel) {
        return (PacketEncoder) channel.pipeline().get(PacketEvents.ENCODER_NAME);
    }

    private PacketDecoder getDecoder(Channel channel) {
        ChannelHandler decoder = channel.pipeline().get(PacketEvents.DECODER_NAME);
        if (decoder instanceof PacketDecoder) {
            return (PacketDecoder) decoder;
        } else if (ViaVersionUtil.isAvailable()) {
            decoder = channel.pipeline().get("decoder");
            if (ViaVersionUtil.getBukkitDecodeHandlerClass().equals(decoder.getClass())) {
                ReflectionObject reflectMCDecoder = new ReflectionObject(decoder);
                ByteToMessageDecoder injectedDecoder = reflectMCDecoder.readObject(0, ByteToMessageDecoder.class);
                //We are the father decoder
                if (injectedDecoder instanceof PacketDecoder) {
                    return (PacketDecoder) injectedDecoder;
                } else if (ClassUtil.getClassSimpleName(injectedDecoder.getClass()).equals("PacketDecoder")) {
                    //Some other packetevents instance already injected. Let us find our child decoder somewhere in here.
                    ReflectionObject reflectInjectedDecoder = new ReflectionObject(injectedDecoder);
                    List<Object> decoders = reflectInjectedDecoder.readList(0);
                    for (Object customDecoder : decoders) {
                        if (customDecoder instanceof PacketDecoder) {
                            return (PacketDecoder) customDecoder;
                        }
                    }
                }
            }
        }
        //We honestly have no idea where our decoder is?
        return null;
    }

}
