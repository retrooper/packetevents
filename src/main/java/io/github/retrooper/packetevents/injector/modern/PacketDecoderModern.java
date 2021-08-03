package io.github.retrooper.packetevents.injector.modern;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketDecodeEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.bukkit.entity.Player;

import java.util.List;

public class PacketDecoderModern extends ByteToMessageDecoder {
    public volatile Player player;
    private final PacketEvents packetEvents;


    public PacketDecoderModern() {
        this.packetEvents = PacketEvents.get();
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        if (!byteBuf.isReadable())
            return;
        PacketDecodeEvent packetDecodeEvent = new PacketDecodeEvent(player, byteBuf);
        this.packetEvents.getEventManager().callEvent(packetDecodeEvent);

        if (packetDecodeEvent.isCancelled()) {
            byteBuf.skipBytes(byteBuf.readableBytes());
            return;
        }

        list.add(byteBuf.readBytes(byteBuf.readableBytes()));
    }
}
