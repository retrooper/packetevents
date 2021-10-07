package com.retrooper.packetevents.event.impl;

import com.retrooper.packetevents.event.PacketListenerAbstract;
import com.retrooper.packetevents.event.ProtocolPacketEvent;
import com.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.retrooper.packetevents.protocol.ConnectionState;
import com.retrooper.packetevents.protocol.PacketSide;
import com.retrooper.packetevents.wrapper.PacketWrapper;

public class PacketReceiveEvent extends ProtocolPacketEvent {
    private PacketWrapper<?> lastUsedWrapper;

    public PacketReceiveEvent(ChannelAbstract channel, Object player, ByteBufAbstract byteBuf) {
        super(PacketSide.CLIENT, channel, player, byteBuf);
    }

    public PacketReceiveEvent(ConnectionState connectionState, ChannelAbstract channel, Object player, ByteBufAbstract byteBuf) {
        super(PacketSide.CLIENT, connectionState, channel, player, byteBuf);
    }

    public PacketReceiveEvent(Object channel, Object player, Object rawByteBuf) {
        super(PacketSide.CLIENT, channel, player, rawByteBuf);
    }

    public PacketReceiveEvent(ConnectionState connectionState, Object channel, Object player, Object rawByteBuf) {
        super(PacketSide.CLIENT, connectionState, channel, player, rawByteBuf);
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        listener.onPacketReceive(this);
    }

    @Deprecated
    public PacketWrapper<?> getLastUsedWrapper() {
        return lastUsedWrapper;
    }

    @Deprecated
    public void setLastUsedWrapper(PacketWrapper<?> currentPacketWrapper) {
        this.lastUsedWrapper = currentPacketWrapper;
    }
}
