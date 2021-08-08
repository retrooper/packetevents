package io.github.retrooper.packetevents.injector.modern;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketDecodeEvent;
import io.github.retrooper.packetevents.packettype.PacketState;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.List;

public class PacketDecoderModern extends ByteToMessageDecoder {
    private static Method DECODE_METHOD;
    private final Channel channel;
    public volatile Player player;
    public PacketState packetState;

    private void load() {
        if (DECODE_METHOD== null) {
            try {
                DECODE_METHOD = ByteToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class);
                DECODE_METHOD.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public PacketDecoderModern(Channel channel) {
        this.channel = channel;
        load();
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        ByteBuf buf = byteBuf.copy();

        PacketDecodeEvent packetDecodeEvent = new PacketDecodeEvent(channelHandlerContext.channel(), player, buf);
        PacketEvents.get().getEventManager().callEvent(packetDecodeEvent);

        if (packetDecodeEvent.isCancelled()) {
            byteBuf.skipBytes(byteBuf.readableBytes());
            return;
        }

        try {
            DECODE_METHOD.invoke((ByteToMessageDecoder) channel.pipeline().get("decoder"), channelHandlerContext, byteBuf, list);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //TODO COMPLETE PACKETDECODERLEGACY
    }
}
