package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.ProtocolPacketEvent;
import io.github.retrooper.packetevents.protocol.PacketSide;
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.channel.ChannelAbstract;
import org.bukkit.entity.Player;

public class PacketReceiveEvent extends ProtocolPacketEvent {

    public PacketReceiveEvent(ChannelAbstract channel, Player player, ByteBufAbstract byteBuf) {
        super(PacketSide.CLIENT, channel, player, byteBuf, true);
    }

    public PacketReceiveEvent(Object channel, Player player, Object rawByteBuf) {
        super(PacketSide.CLIENT, channel, player, rawByteBuf, true);
    }

    public PacketReceiveEvent(Object channel, Player player, Object rawByteBuf, boolean ignoreBufferLength) {
        super(PacketSide.CLIENT, channel, player, rawByteBuf, ignoreBufferLength);
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        listener.onPacketReceive(this);
    }
}
