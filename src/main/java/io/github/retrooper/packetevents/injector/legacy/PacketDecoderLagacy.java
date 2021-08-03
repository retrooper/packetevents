package io.github.retrooper.packetevents.injector.legacy;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketDecodeEvent;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.handler.codec.ByteToMessageDecoder;
import org.bukkit.entity.Player;

import java.util.List;

public class PacketDecoderLagacy extends ByteToMessageDecoder {
    public volatile Player player;
    private final PacketEvents packetEvents;

    public PacketDecoderLagacy(){
        this.packetEvents = PacketEvents.get();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (!byteBuf.isReadable())
            return;
        PacketDecodeEvent packetDecodeEvent = new PacketDecodeEvent(player, byteBuf);
        this.packetEvents.getEventManager().callEvent(packetDecodeEvent);

        if(packetDecodeEvent.isCancelled()){
            byteBuf.skipBytes(byteBuf.readableBytes());
            return;
        }

        list.add(byteBuf.readBytes(byteBuf.readableBytes()));
    }
}
