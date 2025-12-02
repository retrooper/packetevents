/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.util.PlayerPingAccessorModern;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManagerImpl implements PlayerManager {

    // we associate the user object with a UUID upon receiving the ClientboundGameProfilePacket;
    // but, as packets are send asynchronously, this causes a race condition between the packet being sent
    // and the main thread login logic; if the packet gets processed by packetevents before the main thread login
    // logic reaches the event where we process things, everything is fine; if not, the event won't know what
    // to do with the player and will kick them
    //
    // this map gets used early on in the login/join process, were we can't extract
    // a reference to the player's connection from the player;
    // this player reference may then get used by the packet handler to associate the user with a player
    // from inside the packet handler, ensuring consistent results regardless of which thread is faster
    @ApiStatus.Internal
    public final Map<UUID, WeakReference<Player>> joiningPlayers = new ConcurrentHashMap<>();

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
    public @NotNull ClientVersion getClientVersion(@NotNull Object p) {
        Player player = (Player) p;
        User user = getUser(player);
        if (user == null) return ClientVersion.UNKNOWN;
        if (user.getClientVersion() == null) {
            int protocolVersion;
            if (ProtocolSupportUtil.isAvailable()) {
                protocolVersion = ProtocolSupportUtil.getProtocolVersion(user.getAddress());
                PacketEvents.getAPI().getLogManager().debug("Requested ProtocolSupport for user " + user.getName() + "'s protocol version. Protocol version: " + protocolVersion);
            } else if (ViaVersionUtil.isAvailable()) {
                protocolVersion = ViaVersionUtil.getProtocolVersion(player);
                PacketEvents.getAPI().getLogManager().debug("Requested ViaVersion for " + player.getName() + "'s protocol version. Protocol version: " + protocolVersion);

            } else {
                //No protocol translation plugins available, the client must be the same version as the server.
                protocolVersion = PacketEvents.getAPI().getServerManager().getVersion().getProtocolVersion();
                PacketEvents.getAPI().getLogManager().debug("No protocol translation plugins are available. We will assume " + user.getName() + "'s protocol version is the same as the server's protocol version. Protocol version: " + protocolVersion);
            }
            ClientVersion version = ClientVersion.getById(protocolVersion);
            user.setClientVersion(version);
        }
        return user.getClientVersion();
    }

    @Override
    public Object getChannel(@NotNull Object player) {
        UUID uuid = ((Player) player).getUniqueId();
        ProtocolManager protocolManager = PacketEvents.getAPI().getProtocolManager();
        Object channel = protocolManager.getChannel(uuid);
        if (channel == null) {
            channel = SpigotReflectionUtil.getChannel((Player) player);
            // This is removed from the HashMap on channel close
            // So if the channel is already closed, there will be a memory leak if we add an offline player
            if (channel != null) {
                synchronized (channel) {
                    if (ChannelHelper.isOpen(channel)) {
                        protocolManager.setChannel(uuid, channel);
                    }
                }
            }
        }
        return channel;
    }

    @Override
    public User getUser(@NotNull Object player) {
        Player p = (Player) player;
        Object channel = getChannel(p);

        if (channel == null) return null;
        return PacketEvents.getAPI().getProtocolManager().getUser(channel);
    }
}
