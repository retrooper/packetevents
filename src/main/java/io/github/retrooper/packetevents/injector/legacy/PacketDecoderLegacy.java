package io.github.retrooper.packetevents.injector.legacy;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.protocol.PacketState;
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
    public final ByteToMessageDecoder previousDecoder;
    public volatile Player player;
    public PacketState packetState;

    private void load() {
        if (DECODE_METHOD == null) {
            try {
                DECODE_METHOD = ByteToMessageDecoder.class.getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class);
                DECODE_METHOD.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public PacketDecoderLegacy(ByteToMessageDecoder previousDecoder) {
        load();
        this.previousDecoder = previousDecoder;
    }

    private List<Object> callDecoder(ByteToMessageDecoder decoder, ChannelHandlerContext ctx, Object input) throws InvocationTargetException {
        List<Object> list = new ArrayList<>();
        try {
            DECODE_METHOD.invoke(decoder, ctx, input, list);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(ctx.channel(), player, byteBuf.copy());
        PacketEvents.get().getEventManager().callEvent(packetReceiveEvent);

        if (packetReceiveEvent.isCancelled()) {
            byteBuf.skipBytes(byteBuf.readableBytes());
            return;
        }
        try {
            list.addAll(callDecoder(previousDecoder, ctx, byteBuf));
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Exception) {
                throw (Exception) e.getCause();
            } else if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            }
        }
    }
}
