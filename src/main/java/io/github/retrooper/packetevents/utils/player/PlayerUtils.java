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

package io.github.retrooper.packetevents.utils.player;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.gameprofile.GameProfileUtil;
import io.github.retrooper.packetevents.utils.gameprofile.WrappedGameProfile;
import io.github.retrooper.packetevents.utils.geyser.GeyserUtils;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import io.github.retrooper.packetevents.utils.versionlookup.v_1_7_10.SpigotVersionLookup_1_7;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Useful player utilities.
 *
 * @author retrooper
 * @since 1.6.8
 */
//TODO Cache clientVersions map by channels and not inetsocketaddresses :)
public final class PlayerUtils {
    public final Map<Object, ClientVersion> clientVersions = new ConcurrentHashMap<>();
    public final Map<UUID, Long> keepAliveMap = new ConcurrentHashMap<>();
    public final Map<String, Object> channels = new ConcurrentHashMap<>();

    /**
     * Use the ping PacketEvents calculates for the player. (Updates every incoming Keep Alive packet)
     *
     * @param player Target player.
     * @return Non-smoothed ping.
     */
    public int getPing(Player player) {
        return NMSUtils.getPlayerPing(player);
    }

    /**
     * Get a player's client version.
     *
     * @param player Target player.
     * @return Client Version.
     * @see #clientVersions
     */
    @NotNull
    public ClientVersion getClientVersion(@NotNull final Player player) {
        if (player.getAddress() == null) {
            return ClientVersion.UNKNOWN;
        }
        Object channel = PacketEvents.get().getPlayerUtils().getChannel(player);
        ClientVersion version = clientVersions.get(channel);
        if (version == null) {
            //Prioritize asking ViaVersion and ProtocolSupport as they modify the protocol version in the packet we access it from.
            if (VersionLookupUtils.isDependencyAvailable()) {
                try {
                    version = ClientVersion.getClientVersion(VersionLookupUtils.getProtocolVersion(player));
                    clientVersions.put(channel, version);
                    return version;
                } catch (Exception ex) {
                    //Try ask the dependency again the next time, for now it is temporarily unresolved...
                    //Temporary unresolved means there is still hope, an exception was thrown on the dependency's end.
                    return ClientVersion.TEMP_UNRESOLVED;
                }
            } else {
                //We can trust the version we retrieved from the packet.
                short protocolVersion;
                //Handle 1.7.10, luckily 1.7.10 provides a method for us to access a player's protocol version(because 1.7.10 servers support 1.8 clients too)
                if (PacketEvents.get().getServerUtils().getVersion().isOlderThan(ServerVersion.v_1_8)) {
                    protocolVersion = (short) SpigotVersionLookup_1_7.getProtocolVersion(player);
                } else {
                    //No dependency available, couldn't snatch the version from the packet AND server version is not 1.7.10
                    //We are pretty safe to assume the version is the same as the server, as ViaVersion AND ProtocolSupport could not be found.
                    //If you aren't using ViaVersion or ProtocolSupport, how are you supporting multiple protocol versions?
                    //(WE DONT SUPPORT CUSTOM PROTOCOL VERSION HACKS other than viaversion & protocolsupport)
                    protocolVersion = PacketEvents.get().getServerUtils().getVersion().getProtocolVersion();
                }
                version = ClientVersion.getClientVersion(protocolVersion);
                clientVersions.put(channel, version);
            }
        }
        return version;
    }
/*
    public void writePacket(Player player, SendableWrapper wrapper) {
        try {
            Object nmsPacket = wrapper.asNMSPacket();
            PacketEvents.get().getInjector().writePacket(getChannel(player), nmsPacket);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void flushPackets(Player player) {
        try {
            PacketEvents.get().getInjector().flushPackets(getChannel(player));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void sendPacket(Player player, SendableWrapper wrapper) {
        try {
            Object nmsPacket = wrapper.asNMSPacket();
            PacketEvents.get().getInjector().sendPacket(getChannel(player), nmsPacket);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Deprecated
    public void sendPacket(Object channel, SendableWrapper wrapper) {
        try {
            Object nmsPacket = wrapper.asNMSPacket();
            PacketEvents.get().getInjector().sendPacket(channel, nmsPacket);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/

    public void sendNMSPacket(Player player, Object packet) {
        PacketEvents.get().getInjector().sendPacket(getChannel(player), packet);
    }


    public WrappedGameProfile getGameProfile(Player player) {
        Object gameProfile = GameProfileUtil.getGameProfile(player.getUniqueId(), player.getName());
        return GameProfileUtil.getWrappedGameProfile(gameProfile);
    }

    public boolean isGeyserPlayer(Player player) {
        if (!PacketEvents.get().getServerUtils().isGeyserAvailable()) {
            return false;
        }
        return GeyserUtils.isGeyserPlayer(player.getUniqueId());
    }

    public boolean isGeyserPlayer(UUID uuid) {
        if (!PacketEvents.get().getServerUtils().isGeyserAvailable()) {
            return false;
        }
        return GeyserUtils.isGeyserPlayer(uuid);
    }

    public void changeSkinProperty(Player player, Skin skin) {
        Object gameProfile = NMSUtils.getGameProfile(player);
        GameProfileUtil.setGameProfileSkin(gameProfile, skin);
    }

    public Skin getSkin(Player player) {
        Object gameProfile = NMSUtils.getGameProfile(player);
        return GameProfileUtil.getGameProfileSkin(gameProfile);
    }

    public Object getChannel(Player player) {
        String name = player.getName();
        Object channel = channels.get(name);
        if (channel == null) {
            channel = NMSUtils.getChannel(player);
            if (channel != null) {
                channels.put(name, channel);
            }
        }
        return channel;
    }
}
