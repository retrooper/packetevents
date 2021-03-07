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

package io.github.retrooper.packetevents.processor;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.*;
import io.github.retrooper.packetevents.packettype.PacketState;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.handshaking.setprotocol.WrappedPacketHandshakingInSetProtocol;
import io.github.retrooper.packetevents.packetwrappers.login.in.start.WrappedPacketLoginInStart;
import io.github.retrooper.packetevents.utils.gameprofile.WrappedGameProfile;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Internal packet processor.
 * This class mainly manages channel caching and internal processing. Do NOT use this class, it is only meant to be used internally.
 *
 * @author retrooper
 * @see <a href="http://netty.io">http://netty.io</a>
 * @since 1.7.9
 */
public class PacketProcessorInternal {
    public final Map<UUID, Long> keepAliveMap = new HashMap<>();
    public final Map<String, Object> channelMap = new HashMap<>();

    public Object getChannel(Player player) {
        String name = player.getName();
        Object channel = channelMap.get(name);
        if (channel == null) {
            channel = NMSUtils.getChannel(player);
            if (channel != null) {
                channelMap.put(name, channel);
            }
        }
        return channel;
    }

    /**
     * Force PacketEvents to process an incoming packet.
     * This method could be used to spoof an incoming packet to the PacketEvents API.
     *
     * @param player  Packet sender.
     * @param channel Packet sender's netty channel.
     * @param packet  NMS Packet.
     * @return NMS Packet, null if the event was cancelled.
     */
    public Object read(Player player, Object channel, Object packet) {
        PacketState state = getPacketState(player, packet);
        if (state == null) {
            return packet;
        }
        switch (state) {
            case STATUS:
                PacketStatusReceiveEvent statusEvent = new PacketStatusReceiveEvent(channel, new NMSPacket(packet));
                PacketEvents.get().getEventManager().callEvent(statusEvent);
                //Apply modifications to the packet
                packet = statusEvent.getNMSPacket().getRawNMSPacket();
                //Process internally
                interceptStatusReceive(statusEvent);
                if (statusEvent.isCancelled()) {
                    //Set the packet to null if the event was cancelled
                    packet = null;
                }
                break;
            case HANDSHAKING:
                PacketHandshakeReceiveEvent handshakeEvent = new PacketHandshakeReceiveEvent(channel, new NMSPacket(packet));
                PacketEvents.get().getEventManager().callEvent(handshakeEvent);
                //Apply modifications to the packet
                packet = handshakeEvent.getNMSPacket().getRawNMSPacket();
                //Process internally
                interceptHandshakeReceive(handshakeEvent);
                if (handshakeEvent.isCancelled()) {
                    //Set the packet to null if the event was cancelled
                    packet = null;
                }
                break;
            case LOGIN:
                PacketLoginReceiveEvent loginEvent = new PacketLoginReceiveEvent(channel, new NMSPacket(packet));//Cache the channel
                if (loginEvent.getPacketId() == PacketType.Login.Client.START) {
                    WrappedPacketLoginInStart startWrapper = new WrappedPacketLoginInStart(loginEvent.getNMSPacket());
                    WrappedGameProfile gameProfile = startWrapper.getGameProfile();
                    channelMap.put(gameProfile.name, channel);
                }
                PacketEvents.get().getEventManager().callEvent(loginEvent);
                packet = loginEvent.getNMSPacket().getRawNMSPacket();
                interceptLoginReceive(loginEvent);
                if (loginEvent.isCancelled()) {
                    packet = null;
                }
                break;
            case PLAY:
                PacketPlayReceiveEvent event = new PacketPlayReceiveEvent(player, channel, new NMSPacket(packet));
                PacketEvents.get().getEventManager().callEvent(event);
                packet = event.getNMSPacket().getRawNMSPacket();
                interceptPlayReceive(event);
                if (event.isCancelled()) {
                    packet = null;
                }
                break;
        }
        return packet;
    }

    /**
     * Force PacketEvents to process an outgoing packet.
     * This method could be used to spoof an outgoing packet to the PacketEvents API.
     *
     * @param player  Packet receiver.
     * @param channel Packet receiver's netty channel.
     * @param packet  NMS Packet.
     * @return NMS Packet, null if the event was cancelled.
     */
    public Object write(Player player, Object channel, Object packet) {
        PacketState state = getPacketState(player, packet);
        if (state == null) {
            return packet;
        }
        switch (state) {
            case STATUS:
                PacketStatusSendEvent statusEvent = new PacketStatusSendEvent(channel, new NMSPacket(packet));
                PacketEvents.get().getEventManager().callEvent(statusEvent);
                packet = statusEvent.getNMSPacket().getRawNMSPacket();
                interceptStatusSend(statusEvent);
                if (statusEvent.isCancelled()) {
                    packet = null;
                }
                break;
            case LOGIN:
                PacketLoginSendEvent loginEvent = new PacketLoginSendEvent(channel, new NMSPacket(packet));
                PacketEvents.get().getEventManager().callEvent(loginEvent);
                packet = loginEvent.getNMSPacket().getRawNMSPacket();
                interceptLoginSend(loginEvent);
                if (loginEvent.isCancelled()) {
                    packet = null;
                }
                break;
            case PLAY:
                PacketPlaySendEvent playEvent = new PacketPlaySendEvent(player, channel, new NMSPacket(packet));
                PacketEvents.get().getEventManager().callEvent(playEvent);
                packet = playEvent.getNMSPacket().getRawNMSPacket();
                interceptPlaySend(playEvent);
                if (playEvent.isCancelled()) {
                    packet = null;
                }
                break;
        }
        return packet;
    }

    /**
     * Make PacketEvents process an incoming PLAY packet after minecraft has processed it.
     * As minecraft has already processed the packet, we cannot cancel the action, nor the event.
     *
     * @param player  Packet sender.
     * @param channel Netty channel of the packet sender.
     * @param packet  NMS Packet.
     */
    public void postRead(Player player, Object channel, Object packet) {
        if (getPacketState(player, packet) == PacketState.PLAY) {
            PostPacketPlayReceiveEvent event = new PostPacketPlayReceiveEvent(player, channel, new NMSPacket(packet));
            PacketEvents.get().getEventManager().callEvent(event);
            interceptPostPlayReceive(event);
        }
    }

    /**
     * Make PacketEvents process an outgoing PLAY packet after minecraft has already sent the packet.
     * This doesn't necessarily mean the client already received the packet,
     * but the server has sent it for sure by this time.
     * As minecraft has already processed the packet, we cannot cancel the action, nor the event.
     *
     * @param player  Packet receiver.
     * @param channel Netty channel of the packet receiver.
     * @param packet  NMS Packet.
     */
    public void postWrite(Player player, Object channel, Object packet) {
        if (getPacketState(player, packet) == PacketState.PLAY) {
            PostPacketPlaySendEvent event = new PostPacketPlaySendEvent(player, channel, new NMSPacket(packet));
            PacketEvents.get().getEventManager().callEvent(event);
            interceptPostPlaySend(event);
        }
    }

    /**
     * Internal processing of an incoming PLAY packet.
     *
     * @param event PLAY server-bound packet event.
     */
    private void interceptPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketId() == PacketType.Play.Client.KEEP_ALIVE) {
            UUID uuid = event.getPlayer().getUniqueId();
            long timestamp = keepAliveMap.getOrDefault(uuid, event.getTimestamp());
            long currentTime = event.getTimestamp();
            long ping = currentTime - timestamp;
            long smoothedPing = (PacketEvents.get().getPlayerUtils().getSmoothedPing(event.getPlayer().getUniqueId()) * 3L + ping) / 4;
            PacketEvents.get().getPlayerUtils().playerPingMap.put(uuid, (int) ping);
            PacketEvents.get().getPlayerUtils().playerSmoothedPingMap.put(uuid, (int) smoothedPing);
        }
    }

    /**
     * Internal processing of an outgoing PLAY packet.
     *
     * @param event PLAY client-bound packet event.
     */
    private void interceptPlaySend(PacketPlaySendEvent event) {

    }

    /**
     * Internal processing of an incoming LOGIN packet.
     *
     * @param event LOGIN server-bound packet event.
     */
    private void interceptLoginReceive(PacketLoginReceiveEvent event) {

    }

    /**
     * Internal processing of an outgoing LOGIN packet.
     *
     * @param event client-bound LOGIN packet event.
     */
    private void interceptLoginSend(PacketLoginSendEvent event) {

    }

    /**
     * Internal processing of an incoming HANDSHAKE packet.
     *
     * @param event server-bound HANDSHAKE packet event.
     */
    private void interceptHandshakeReceive(PacketHandshakeReceiveEvent event) {
        if (event.getPacketId() == PacketType.Handshaking.Client.SET_PROTOCOL) {
            WrappedPacketHandshakingInSetProtocol handshake = new WrappedPacketHandshakingInSetProtocol(event.getNMSPacket());
            int protocolVersion = handshake.getProtocolVersion();
            ClientVersion version = ClientVersion.getClientVersion(protocolVersion);
            PacketEvents.get().getPlayerUtils().tempClientVersionMap.put(event.getSocketAddress(), version);
        }
    }


    /**
     * Internal processing of an incoming STATUS packet.
     *
     * @param event server-bound STATUS packet event.
     */
    private void interceptStatusReceive(PacketStatusReceiveEvent event) {

    }

    /**
     * Internal processing of an outgoing STATUS packet.
     *
     * @param event client-bound STATUS packet event.
     */
    private void interceptStatusSend(PacketStatusSendEvent event) {

    }

    /**
     * Internal processing of a packet that has already been processed by minecraft.
     *
     * @param event post server-bound play packet event.
     */
    private void interceptPostPlayReceive(PostPacketPlayReceiveEvent event) {

    }

    /**
     * Internal processing of a packet that has already been sent to a client.
     * Doesn't necessarily mean the client has received it yet.
     *
     * @param event post client-bound play packet event.
     */
    private void interceptPostPlaySend(PostPacketPlaySendEvent event) {
        if (event.getPacketId() == PacketType.Play.Server.KEEP_ALIVE) {
            if (event.getPlayer() != null) {
                keepAliveMap.put(event.getPlayer().getUniqueId(), event.getTimestamp());
            }
        }
    }

    @Nullable
    private PacketState getPacketState(Player player, Object packet) {
        if (packet == null) {
            return null;
        }
        if (player != null) {
            return PacketState.PLAY;
        } else {
            String packetName = ClassUtil.getClassSimpleName(packet.getClass());//Cached string name so it is faster
            if (packetName.startsWith("PacketH")) {
                return PacketState.HANDSHAKING;
            } else if (packetName.startsWith("PacketL")) {
                return PacketState.LOGIN;
            } else if (packetName.startsWith("PacketS")) {
                return PacketState.STATUS;
            } else {
                return null;
            }
        }
    }
}
