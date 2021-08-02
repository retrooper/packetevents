package io.github.retrooper.packetevents.injector.legacy;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketDecodeEvent;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoderLagacy extends ByteToMessageDecoder {
    private final PacketEvents packetEvents;

    public PacketDecoderLagacy(){
        this.packetEvents = PacketEvents.get();
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        PacketDecodeEvent packetDecodeEvent = new PacketDecodeEvent(byteBuf);
        this.packetEvents.getEventManager().callEvent(packetDecodeEvent);

        if(packetDecodeEvent.isCancelled()){
            byteBuf.skipBytes(byteBuf.readableBytes());
            return;
        }

        list.add(byteBuf.readBytes(byteBuf.readableBytes()));
    }
}
