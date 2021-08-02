package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.netty.buffer.ByteBuf;

public class PacketDecodeEvent extends PacketEvent implements CancellableEvent {
    private boolean cancel;
    private final ByteBuf byteBuf;


    public PacketDecodeEvent(ByteBuf byteBuf){
        this.byteBuf = byteBuf;
    }

    @Override
    public void call(PacketListenerAbstract listener) {
        listener.onPacketDecode(this);
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean val) {
        this.cancel = val;
    }

    public ByteBuf getByteBuf(){
        return this.byteBuf;
    }
}
