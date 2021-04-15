/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.utils.player;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.gameprofile.GameProfileUtil;
import io.github.retrooper.packetevents.utils.gameprofile.WrappedGameProfile;
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
public final class PlayerUtils {
    public final Map<UUID, Long> loginTime = new ConcurrentHashMap<>();

    /**
     * This is where the most recent non-smoothed player ping that PacketEvents calculates is stored.
     */
    public final Map<UUID, Integer> playerPingMap = new ConcurrentHashMap<>();

    /**
     * This is where the most recent smoothed player ping that PacketEvents calculates is stored.
     * When accessing minecraft for a player's ping, minecraft will return a smoothed value.
     * Use this to receive a smoothed ping value.
     * PacketEvents smooths in the same way minecraft does.
     */
    public final Map<UUID, Integer> playerSmoothedPingMap = new ConcurrentHashMap<>();

    /**
     * This map stores the client version of a player only when it has been confirmed.
     * Since some plugins like ViaBackwards take around one tick from the PlayerJoinEvent to resolve a player's
     * client version, we need to compare the version we stored from the packet and the one ViaBackwards specifies.
     * Why do we need to do this?
     * Plugins like ViaVersion modify the Handshaking packet containing the protocol version
     * to allow the client to join.
     * Plugins that listen to the handshaking packet will receive the modified protocol version. (=server version)
     * So we have to go out of our way and use the API of ViaVersion or such similar plugins
     * to have support for client version resolving.
     * I can't do this for every single plugin like ViaVersion, so for now ViaVersion, ViaBackwards and ViaRewind are
     * guaranteed to work!
     * ProtocolSupport MIGHT work.
     */
    public final Map<InetSocketAddress, ClientVersion> clientVersionsMap = new ConcurrentHashMap<>();

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
    public int getNMSPing(final Player player) {
        return NMSUtils.getPlayerPing(player);
    }

    /**
     * Use the ping PacketEvents calculates for the player. (Updates every incoming Keep Alive packet)
     *
     * @param player Target player.
     * @return Non-smoothed ping.
     */
    public int getPing(final Player player) {
        return getPing(player.getUniqueId());
    }

    /**
     * Use the ping PacketEvents calculates and smooths in the same way NMS does (Updates every incoming Keep Alive packet)
     *
     * @param player Target player.
     * @return Smoothed ping.
     */
    public int getSmoothedPing(final Player player) {
        return getSmoothedPing(player.getUniqueId());
    }

    /**
     * Use the ping PacketEvents calculates for the player. (Updates every incoming Keep Alive packet)
     *
     * @param uuid Target player UUID.
     * @return Non-smoothed ping.
     */
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
                }
                return ClientVersion.TEMP_UNRESOLVED;
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

    /**
     * High level player injection method.
     * This method will call the {@link io.github.retrooper.packetevents.event.impl.PlayerInjectEvent}
     * event and if the event gets cancelled, this action will cancel too.
     * The injection action and event call will be async or sync depending on the PacketEvents settings.
     * Recommended to use this as this most likely won't ever change/refactor compared to using our internal utils.
     *
     * @param player Target player.
     */
    public void injectPlayer(Player player) {
        PacketEvents.get().injector.injectPlayer(player);
    }

    public void ejectPlayer(Player player) {
        PacketEvents.get().injector.ejectPlayer(player);
    }

    /**
     * Send a client-bound(server-sided) wrapper that supports sending to a player.
     *
     * @param player  Packet receiver.
     * @param wrapper Client-bound wrapper supporting sending.
     */
    public void sendPacket(Player player, SendableWrapper wrapper) {
        PacketEvents.get().injector.sendPacket(NMSUtils.getChannel(player), wrapper.asNMSPacket());
    }

    /**
     * Send a client-bound(server-sided) wrapper that supports sending to a netty channel.
     *
     * @param channel Netty channel as object(due to netty package changes)
     * @param wrapper Client-bound raw NMS packet.
     */
    public void sendPacket(Object channel, SendableWrapper wrapper) {
        PacketEvents.get().injector.sendPacket(channel, wrapper.asNMSPacket());
    }

    /**
     * Send a client-bound(server-sided) raw NMS Packet without any wrapper to a player.
     *
     * @param player Packet receiver.
     * @param packet Client-bound raw NMS packet.
     */
    public void sendNMSPacket(Player player, Object packet) {
        PacketEvents.get().injector.sendPacket(NMSUtils.getChannel(player), packet);
    }

    /**
     * Send a client-bound(server-sided) raw NMS Packet without any wrapper to a netty channel.
     *
     * @param channel Netty channel as object(due to netty package changes)
     * @param packet  Client-bound raw NMS packet.
     */
    public void sendNMSPacket(Object channel, Object packet) {
        PacketEvents.get().injector.sendPacket(channel, packet);
    }

    public WrappedGameProfile getGameProfile(Player player) {
        Object gameProfile = GameProfileUtil.getGameProfile(player.getUniqueId(), player.getName());
        return GameProfileUtil.getWrappedGameProfile(gameProfile);
    }
}
