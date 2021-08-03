package io.github.retrooper.packetevents.injector.legacy;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketDecodeEvent;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.handler.codec.ByteToMessageDecoder;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PacketDecoderLegacy extends ByteToMessageDecoder {
    private static Method DECODE_METHOD;
    public volatile Player player;
    public final ByteToMessageDecoder minecraftDecoder;

    public PacketDecoderLegacy(ByteToMessageDecoder minecraftDecoder) {
        this.minecraftDecoder = minecraftDecoder;
    }

    private List<Object> callDecode(ByteToMessageDecoder decoder, ChannelHandlerContext channelHandlerContext, Object input) {
        if (DECODE_METHOD== null) {
            try {
                DECODE_METHOD = ByteToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class);
                DECODE_METHOD.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        List<Object> output = new ArrayList<>();
        try {
            DECODE_METHOD.invoke(decoder, channelHandlerContext, input, output);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return output;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        if (!byteBuf.isReadable())
            return;
        PacketDecodeEvent packetDecodeEvent = new PacketDecodeEvent(player, byteBuf);
        PacketEvents.get().getEventManager().callEvent(packetDecodeEvent);

        if (packetDecodeEvent.isCancelled()) {
            byteBuf.skipBytes(byteBuf.readableBytes());
            return;
        }

        list.addAll(callDecode(minecraftDecoder, channelHandlerContext, byteBuf));
    }
}
