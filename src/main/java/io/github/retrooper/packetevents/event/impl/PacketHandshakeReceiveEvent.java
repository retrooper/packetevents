package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.eventtypes.CancellableNMSPacketEvent;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;

import java.net.InetSocketAddress;

/**
 * The {@code PacketHandshakeReceiveEvent} event is fired whenever the a HANDSHAKE packet is received from a client.
 * The {@code PacketHandshakeReceiveEvent} does not have to do with a bukkit player object due to the player object being null in this state.
 * Use the {@link #getSocketAddress()} to identify who sends the packet.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Handshaking">https://wiki.vg/Protocol#Handshaking</a>
 * @since 1.8
 */
public class PacketHandshakeReceiveEvent extends CancellableNMSPacketEvent {
    public PacketHandshakeReceiveEvent(final Object channel, final NMSPacket packet) {
        super(channel, packet);
    }

    public PacketHandshakeReceiveEvent(final InetSocketAddress socketAddress, final NMSPacket packet) {
        super(socketAddress, packet);
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        if (listener.clientSidedLoginAllowance == null || listener.clientSidedLoginAllowance.contains(getPacketId())) {
            listener.onPacketHandshakeReceive(this);
        }
    }
}

