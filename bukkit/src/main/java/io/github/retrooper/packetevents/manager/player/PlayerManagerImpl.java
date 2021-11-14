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
import com.github.retrooper.packetevents.manager.player.PlayerAttributeObject;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.gameprofile.WrappedGameProfile;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.github.retrooper.packetevents.utils.GeyserUtil;
import io.github.retrooper.packetevents.utils.MinecraftReflectionUtil;
import io.github.retrooper.packetevents.utils.PlayerPingAccessorModern;
import io.github.retrooper.packetevents.utils.dependencies.DependencyUtil;
import io.github.retrooper.packetevents.utils.dependencies.protocolsupport.ProtocolSupportUtil;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.ViaVersionUtil;
import io.github.retrooper.packetevents.utils.v1_7.SpigotVersionLookup_1_7;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManagerImpl implements PlayerManager {

    @Override
    public <T extends PlayerAttributeObject> T getAttributeOrDefault(UUID uuid, Class<T> clazz, T defaultReturnValue) {
        Map<Class<? extends PlayerAttributeObject>, PlayerAttributeObject> attributes = PLAYER_ATTRIBUTES.get(uuid);
        if (attributes != null) {
            return (T) attributes.get(clazz);
        }
        else {
            attributes = new HashMap<>();
            attributes.put(defaultReturnValue.getClass(), defaultReturnValue);
            PLAYER_ATTRIBUTES.put(uuid, attributes);
            return defaultReturnValue;
        }
    }

    @Override
    public <T extends PlayerAttributeObject> T getAttribute(UUID uuid, Class<T> clazz) {
        Map<Class<? extends PlayerAttributeObject>, PlayerAttributeObject> attributes = PLAYER_ATTRIBUTES.get(uuid);
        if (attributes != null) {
            return (T) attributes.get(clazz);
        }
        else {
            PLAYER_ATTRIBUTES.put(uuid, new HashMap<>());
            return null;
        }
    }

    @Override
    public <T extends PlayerAttributeObject> void setAttribute(UUID uuid, T attribute) {
        Map<Class<? extends PlayerAttributeObject>, PlayerAttributeObject> attributes = PLAYER_ATTRIBUTES.computeIfAbsent(uuid, k -> new HashMap<>());
        attributes.put(attribute.getClass(), attribute);
    }

    @Override
    public ConnectionState getConnectionState(Object player) {
        return getConnectionState(getChannel(player));
    }

    @Override
    public ConnectionState getConnectionState(ChannelAbstract channel) {
        ConnectionState connectionState = CONNECTION_STATES.get(channel);
        if (connectionState == null) {
            connectionState = PacketEvents.getAPI().getInjector().getConnectionState(channel);
            if (connectionState == null) {
                connectionState = ConnectionState.PLAY;
            }
            CONNECTION_STATES.put(channel, connectionState);
        }
        return connectionState;
    }

    @Override
    public void changeConnectionState(ChannelAbstract channel, ConnectionState connectionState) {
        CONNECTION_STATES.put(channel, connectionState);
        PacketEvents.getAPI().getInjector().changeConnectionState(channel, connectionState);
    }

    @Override
    public int getPing(Object player) {
        if (MinecraftReflectionUtil.V_1_17_OR_HIGHER) {
            return PlayerPingAccessorModern.getPing((Player) player);
        } else {
            return MinecraftReflectionUtil.getPlayerPing((Player) player);
        }
    }

    @Override
    public @NotNull ClientVersion getClientVersion(Object pl) {
        Player player = (Player) pl;
        if (player.getAddress() == null) {
            return ClientVersion.UNKNOWN;
        }
        ChannelAbstract channel = getChannel(player);
        ClientVersion version = CLIENT_VERSIONS.get(channel);
        if (version == null || !version.isResolved()) {
            //Asking ViaVersion or ProtocolSupport for the protocol version.
            if (DependencyUtil.isProtocolTranslationDependencyAvailable()) {
                try {
                    version = ClientVersion.getClientVersionByProtocolVersion(DependencyUtil.getProtocolVersion(player));
                    CLIENT_VERSIONS.put(channel, version);
                    return version;
                } catch (Exception ex) {
                    //Try ask the dependency again the next time, for now it is temporarily unresolved...
                    //Temporary unresolved means there is still hope, an exception was thrown on the dependency's end.
                    return ClientVersion.TEMP_UNRESOLVED;
                }
            } else {
                short protocolVersion;
                //Luckily 1.7.10 provides a method for us to access a player's protocol version(because 1.7.10 servers support 1.8 clients too)
                if (PacketEvents.getAPI().getServerManager().getVersion() == ServerVersion.V_1_7_10) {
                    protocolVersion = (short) SpigotVersionLookup_1_7.getProtocolVersion(player);
                } else {
                    //No dependency available, couldn't snatch the version from the packet AND server version is not 1.7.10
                    //We are pretty safe to assume the version is the same as the server, as ViaVersion AND ProtocolSupport could not be found.
                    //If you aren't using ViaVersion or ProtocolSupport, how are you supporting multiple protocol versions?
                    protocolVersion = PacketEvents.getAPI().getServerManager().getVersion().getProtocolVersion();
                }
                version = ClientVersion.getClientVersionByProtocolVersion(protocolVersion);
                CLIENT_VERSIONS.put(channel, version);
            }
        }
        return version;
    }


    @Override
    public ClientVersion getClientVersion(ChannelAbstract channel) {
        return CLIENT_VERSIONS.get(channel);
    }

    @Override
    public void setClientVersion(ChannelAbstract channel, ClientVersion version) {
        CLIENT_VERSIONS.put(channel, version);
    }

    @Override
    public void setClientVersion(Object player, ClientVersion version) {
        setClientVersion(getChannel(player), version);
    }

    @Override
    public void sendPacket(ChannelAbstract channel, ByteBufAbstract byteBuf) {
        //TODO Also check if our encoder is RIGHT before minecraft's,
        //if it is, then don't use context to writeflush, otherwise use it (to support multiple packetevents instances)
        if (ViaVersionUtil.isAvailable() && !ProtocolSupportUtil.isAvailable()) {
            channel.writeAndFlush(byteBuf);
        } else {
            channel.pipeline().context(PacketEvents.ENCODER_NAME).writeAndFlush(byteBuf);
        }
    }

    @Override
    public void sendPacket(ChannelAbstract channel, PacketWrapper<?> wrapper) {
        wrapper.createPacket();
        sendPacket(channel, wrapper.byteBuf);
    }

    @Override
    public void sendPacket(Object player, ByteBufAbstract byteBuf) {
        ChannelAbstract channel = getChannel(player);
        sendPacket(channel, byteBuf);
    }

    @Override
    public void sendPacket(Object player, PacketWrapper<?> wrapper) {
        wrapper.createPacket();
        ChannelAbstract channel = getChannel(player);
        sendPacket(channel, wrapper.byteBuf);
    }

    @Override
    public WrappedGameProfile getGameProfile(Object pl) {
        Player player = (Player) pl;
        Object gameProfile = DependencyUtil.getGameProfile(player.getUniqueId(), player.getName());
        return DependencyUtil.getWrappedGameProfile(gameProfile);
    }

    @Override
    public boolean isGeyserPlayer(Object pl) {
        Player player = (Player) pl;
        return isGeyserPlayer(player.getUniqueId());
    }

    @Override
    public boolean isGeyserPlayer(UUID uuid) {
        if (!PacketEvents.getAPI().getServerManager().isGeyserAvailable()) {
            return false;
        }
        return GeyserUtil.isGeyserPlayer(uuid);
    }

    @Override
    public ChannelAbstract getChannel(Object player) {
        String username = ((Player) player).getName();
        ChannelAbstract channel = getChannel(username);
        if (channel == null) {
            Object ch = MinecraftReflectionUtil.getChannel((Player) player);
            if (ch != null) {
                channel = PacketEvents.getAPI().getNettyManager().wrapChannel(ch);
                CHANNELS.put(username, channel);
            }
        }
        return channel;
    }

    @Override
    public ChannelAbstract getChannel(String username) {
        return CHANNELS.get(username);
    }

    @Override
    public void setChannel(String username, ChannelAbstract channel) {
        CHANNELS.put(username, channel);
    }

    @Override
    public void setChannel(Object player, ChannelAbstract channel) {
        setChannel(((Player) player).getName(), channel);
    }
}
