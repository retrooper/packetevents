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
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.play.out.entitydestroy.WrappedPacketOutEntityDestroy;
import io.github.retrooper.packetevents.packetwrappers.play.out.namedentityspawn.WrappedPacketOutNamedEntitySpawn;
import io.github.retrooper.packetevents.utils.gameprofile.GameProfileUtil;
import io.github.retrooper.packetevents.utils.gameprofile.WrappedGameProfile;
import io.github.retrooper.packetevents.utils.geyser.GeyserUtils;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.versionlookup.VersionLookupUtils;
import io.github.retrooper.packetevents.utils.versionlookup.v_1_7_10.SpigotVersionLookup_1_7;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Useful player utilities.
 *
 * @author retrooper
 * @since 1.6.8
 */
public final class PlayerUtils {
    public final Map<UUID, Long> loginTime = new ConcurrentHashMap<>();
    public final Map<UUID, Integer> playerPingMap = new ConcurrentHashMap<>();
    public final Map<UUID, Integer> playerSmoothedPingMap = new ConcurrentHashMap<>();
    public final Map<InetSocketAddress, ClientVersion> clientVersionsMap = new ConcurrentHashMap<>();
    public final Map<UUID, Long> keepAliveMap = new ConcurrentHashMap<>();
    public final Map<String, Object> channels = new ConcurrentHashMap<>();
    /**
     * This is a temporary client version.
     * This is the client version we receive from the handshaking packet.
     * This might not be the actual client version of the player since plugins like ViaVersion modify the packet to allow
     * users to join servers that aren't usually compatible with their client version.(Described in more detail above)
     * We will compare this version(received from the packet) and the one from the ViaVersion API and the one from the ProtocolSupport API.
     * ProtocolSupport compatibility might not work.
     * If ViaVersion or ProtocolSupport aren't available, we will trust this one.
     */
    public final Map<InetSocketAddress, ClientVersion> tempClientVersionMap = new ConcurrentHashMap<>();

    /**
     * Use reflection to read the ping value NMS calculates for the player.
     * NMS smooths the player ping.
     *
     * @param player Target player.
     * @return NMS smoothed ping.
     */
    @Deprecated
    public int getNMSPing(final Player player) {
        return NMSUtils.getPlayerPing(player);
    }

    /**
     * Use the ping PacketEvents calculates for the player. (Updates every incoming Keep Alive packet)
     *
     * @param player Target player.
     * @return Non-smoothed ping.
     */
    public int getPing(Player player) {
        return getPing(player.getUniqueId());
    }

    /**
     * Use the ping PacketEvents calculates and smooths in the same way NMS does (Updates every incoming Keep Alive packet)
     *
     * @param player Target player.
     * @return Smoothed ping.
     */
    @Deprecated
    public int getSmoothedPing(final Player player) {
        return getSmoothedPing(player.getUniqueId());
    }

    //TODO Don't calculate ping internally, use NMS' smoothed ping. On 1.17 use the Player#getPing which you contributed.

    /**
     * Use the ping PacketEvents calculates for the player. (Updates every incoming Keep Alive packet)
     *
     * @param uuid Target player UUID.
     * @return Non-smoothed ping.
     * @deprecated Please use {@link #getPing(Player)}
     */
    @Deprecated
    public int getPing(UUID uuid) {
        Integer ping = playerPingMap.get(uuid);
        if (ping == null) {
            Long joinTime = loginTime.get(uuid);
            if (joinTime == null) {
                return 0;
            }
            return (int) (System.currentTimeMillis() - joinTime);
        }
        return ping;
    }

    /**
     * Use the ping PacketEvents calculates and smooths in the same way NMS does (Updates every incoming Keep Alive packet)
     *
     * @param uuid Target player UUID.
     * @return Smoothed ping.
     */
    @Deprecated
    public int getSmoothedPing(UUID uuid) {
        Integer smoothedPing = playerSmoothedPingMap.get(uuid);
        if (smoothedPing == null) {
            Long joinTime = loginTime.get(uuid);
            if (joinTime == null) {
                return 0;
            }
            return (int) (System.currentTimeMillis() - joinTime);
        }
        return smoothedPing;
    }

    /**
     * Get a player's client version.
     *
     * @param player Target player.
     * @return Client Version.
     * @see #clientVersionsMap
     */
    @NotNull
    public ClientVersion getClientVersion(@NotNull final Player player) {
        if (player.getAddress() == null) {
            return ClientVersion.UNKNOWN;
        }
        ClientVersion version = clientVersionsMap.get(player.getAddress());
        if (version == null) {
            //Prioritize asking ViaVersion and ProtocolSupport as they modify the protocol version in the packet we access it from.
            if (VersionLookupUtils.isDependencyAvailable()) {
                try {
                    version = ClientVersion.getClientVersion(VersionLookupUtils.getProtocolVersion(player));
                    clientVersionsMap.put(player.getAddress(), version);
                } catch (Exception ex) {
                    //Try ask the dependency again the next time, for now it is temporarily unresolved...
                    //Temporary unresolved means there is still hope, an exception was thrown on the dependency's end.
                    return ClientVersion.TEMP_UNRESOLVED;
                }
            } else {
                //We can trust the version we retrieved from the packet.
                version = tempClientVersionMap.get(player.getAddress());
                if (version == null) {
                    //We couldn't snatch that version from the packet.
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
                }
                clientVersionsMap.put(player.getAddress(), version);
            }
        }
        return version;
    }

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

    /**
     * Send a client-bound(server-sided) wrapper that supports sending to a player.
     *
     * @param player  Packet receiver.
     * @param wrapper Client-bound wrapper supporting sending.
     */
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
    }

    /**
     * Send a client-bound(server-sided) raw NMS Packet without any wrapper to a player.
     *
     * @param player Packet receiver.
     * @param packet Client-bound raw NMS packet.
     */
    public void sendNMSPacket(Player player, Object packet) {
        PacketEvents.get().getInjector().sendPacket(getChannel(player), packet);
    }

    @Deprecated
    public void sendNMSPacket(Object channel, Object packet) {
        PacketEvents.get().getInjector().sendPacket(channel, packet);
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

    //TODO FINISH APPLY SKIN. ITS NOT WORKING?
    private void applySkinChangeAsync(Player player) {
        Plugin plugin = PacketEvents.get().getPlugin();
        CompletableFuture.runAsync(() -> {
            System.out.println("STARTING");

        }).thenRunAsync(() -> {
            WrappedPacketOutEntityDestroy destroyPacket = new WrappedPacketOutEntityDestroy(player.getEntityId());
            WrappedPacketOutNamedEntitySpawn spawnPacket = new WrappedPacketOutNamedEntitySpawn(player);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getEntityId() != player.getEntityId()) {
                    PacketEvents.get().getPlayerUtils().sendPacket(p, destroyPacket);
                    PacketEvents.get().getPlayerUtils().sendPacket(p, spawnPacket);

                    Bukkit.getServer().getScheduler().runTask(plugin, () -> p.hidePlayer(plugin, player));
                    Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> p.showPlayer(plugin, player), 4L);
                }
            }

        }).thenRunAsync(() -> {
            System.out.println("DONE");
        });
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
