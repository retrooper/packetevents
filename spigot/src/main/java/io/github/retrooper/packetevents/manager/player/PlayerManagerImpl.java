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

package io.github.retrooper.packetevents.manager.player;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import io.github.retrooper.packetevents.utils.PlayerPingAccessorModern;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import io.github.retrooper.packetevents.utils.dependencies.DependencyUtil;
import io.github.retrooper.packetevents.utils.dependencies.protocolsupport.ProtocolSupportUtil;
import io.github.retrooper.packetevents.utils.v1_7.SpigotVersionLookup_1_7;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerManagerImpl implements PlayerManager {
    @Override
    public int getPing(@NotNull Object player) {
        //Yay, we contributed this to Spigot and now we can use it on 1.17+ servers.
        if (SpigotReflectionUtil.V_1_17_OR_HIGHER) {
            return PlayerPingAccessorModern.getPing((Player) player);
        } else {
            return SpigotReflectionUtil.getPlayerPingLegacy((Player) player);
        }
    }

    @Override
    public @NotNull ClientVersion getClientVersion(@NotNull Object player) {
        Player p = (Player) player;
        if (p.getAddress() == null) {
            return ClientVersion.UNKNOWN;
        }
        ChannelAbstract channel = getChannel(p);
        User user = getUser(channel);
        if (user.getClientVersion() == null
                || !user.getClientVersion().isResolved()) {
            //Asking ViaVersion or ProtocolSupport for the protocol version.
            if (DependencyUtil.isProtocolTranslationDependencyAvailable()) {
                try {
                    user.setClientVersion(ClientVersion
                            .getClientVersionByProtocolVersion(DependencyUtil
                                    .getProtocolVersion(p)));
                } catch (Exception ex) {
                    //Try ask the dependency again the next time, for now it is temporarily unresolved...
                    //Temporary unresolved means there is still hope, an exception was thrown on the dependency's end.
                    return ClientVersion.TEMP_UNRESOLVED;
                }
            } else {
                int protocolVersion;
                //Luckily 1.7.10 provides a method for us to access a player's protocol version(because 1.7.10 servers support 1.8 clients too)
                if (PacketEvents.getAPI().getServerManager().getVersion() == ServerVersion.V_1_7_10) {
                    protocolVersion = SpigotVersionLookup_1_7.getProtocolVersion(p);
                } else {
                    //No dependency available, couldn't snatch the version from the packet AND server version is not 1.7.10
                    //We are pretty safe to assume the version is the same as the server, as ViaVersion AND ProtocolSupport could not be found.
                    //If you aren't using ViaVersion or ProtocolSupport, how are you supporting multiple protocol versions?
                    protocolVersion = PacketEvents.getAPI().getServerManager().getVersion().getProtocolVersion();
                }
                user.setClientVersion(ClientVersion
                        .getClientVersionByProtocolVersion(protocolVersion));
            }
        }
        return user.getClientVersion();
    }


    @Override
    public ClientVersion getClientVersion(ChannelAbstract channel) {
        User user = getUser(channel);
        if (user.getClientVersion() == null || !user.getClientVersion().isResolved()) {
            if (!DependencyUtil.isProtocolTranslationDependencyAvailable()) {
                if (PacketEvents.getAPI().getServerManager().getVersion() == ServerVersion.V_1_7_10) {
                    return user.getClientVersion();
                } else {
                    int protocolVersion = PacketEvents.getAPI().getServerManager().getVersion().getProtocolVersion();
                    user.setClientVersion(ClientVersion.getClientVersionByProtocolVersion(protocolVersion));
                }
            }
        }
        return user.getClientVersion();
    }

    @Override
    public void sendPacket(ChannelAbstract channel, ByteBufAbstract byteBuf) {
        if (channel.isOpen()) {
            if (ProtocolSupportUtil.isAvailable()) {
                //ProtocolSupport has a MessageToMessageCodec handler named "ps_logic" in the pipeline.
                //The Netty documentation explicitly mentions that you need to retain buffers before passing them through such handlers.
                byteBuf.retain();
            }
            channel.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void sendPacketSilently(ChannelAbstract channel, ByteBufAbstract byteBuf) {
        if (channel.isOpen()) {
            //Only call the encoders after ours in the pipeline
            channel.pipeline().context(PacketEvents.ENCODER_NAME).writeAndFlush(byteBuf);
        }
    }

    @Override
    public User getUser(@NotNull Object player) {
        Player p = (Player) player;
        ChannelAbstract channel = getChannel(p);
        User user = getUser(channel);
        if (user == null && channel != null) {
            //TODO Extract texture properties and pass into user profile(not priority)
            user = new User(channel, ConnectionState.PLAY,
                    null, new UserProfile(p.getUniqueId(),
                    p.getName()));
            USERS.put(channel, user);
            PacketEvents.getAPI().getInjector().updateUser(channel, user);
        }
        return user;
    }

    @Override
    public ChannelAbstract getChannel(@NotNull Object player) {
        String username = ((Player) player).getName();
        ChannelAbstract channel = getChannel(username);
        if (channel == null) {
            Object ch = SpigotReflectionUtil.getChannel((Player) player);
            if (ch != null) {
                channel = PacketEvents.getAPI().getNettyManager().wrapChannel(ch);
                CHANNELS.put(username, channel);
            }
        }
        return channel;
    }
}
