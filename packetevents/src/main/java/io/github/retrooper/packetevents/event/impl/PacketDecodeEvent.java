package io.github.retrooper.packetevents.event.impl;

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.netty.buffer.ByteBuf;

public class PacketDecodeEvent extends PacketEvent implements CancellableEvent {
    private boolean cancel;
    private ByteBuf byteBuf;
    private net.minecraft.util.io.netty.buffer.ByteBuf lagacyByteBuf;


    public PacketDecodeEvent(ByteBuf byteBuf){
        this.byteBuf = byteBuf;
    }

    public PacketDecodeEvent(net.minecraft.util.io.netty.buffer.ByteBuf lagacyByteBuf){
        this.lagacyByteBuf = lagacyByteBuf;
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

    /**
     * ByteBuffer can be null if the server is running lagacy version of minecraft
     * @return
     */
    public ByteBuf getByteBuf(){
        return this.byteBuf;
    }

    /**
     * ByteBuffer can be null if the server is running modern version of minecraft
     * @return
     */
    public net.minecraft.util.io.netty.buffer.ByteBuf getLagacyByteBuf(){
        return this.lagacyByteBuf;
    }
}
