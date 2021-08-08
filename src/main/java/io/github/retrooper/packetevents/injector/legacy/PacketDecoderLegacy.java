package io.github.retrooper.packetevents.injector.legacy;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketDecodeEvent;
import io.github.retrooper.packetevents.packettype.PacketState;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.handler.codec.ByteToMessageDecoder;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.List;

public class PacketDecoderLegacy extends ByteToMessageDecoder {
    public volatile Player player;
    public volatile PacketState packetState;

    @Override
    protected void decode(net.minecraft.util.io.netty.channel.ChannelHandlerContext channelHandlerContext, net.minecraft.util.io.netty.buffer.ByteBuf byteBuf, List<Object> list) throws Exception {
        ByteBuf buf = byteBuf.copy();

        PacketDecodeEvent packetDecodeEvent = new PacketDecodeEvent(channelHandlerContext.channel(), player, buf);
        PacketEvents.get().getEventManager().callEvent(packetDecodeEvent);

        if (packetDecodeEvent.isCancelled()) {
            byteBuf.skipBytes(byteBuf.readableBytes());
        }

    }
}
