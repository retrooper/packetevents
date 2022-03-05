package io.github.retrooper.packetevents.handler;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.CompressionDecoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {
    private static Method DECOMPRESSOR_METHOD, COMPRESSOR_METHOD;
    public User user;
    public final LocalPlayer player;
    public boolean checkedCompression;

    public PacketDecoder(User user, LocalPlayer player) {
        this.user = user;
        this.player = player;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        int firstReaderIndex = buffer.readerIndex();
        PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(ctx.channel(), user, player, buffer);
        int readerIndex = buffer.readerIndex();
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> buffer.readerIndex(readerIndex));
        if (!packetSendEvent.isCancelled()) {
            if (packetSendEvent.getLastUsedWrapper() != null) {
                ByteBufHelper.clear(packetSendEvent.getByteBuf());
                packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
                packetSendEvent.getLastUsedWrapper().writeData();
            }
            buffer.readerIndex(firstReaderIndex);
        }
        else {
            buffer.clear();
        }
        if (packetSendEvent.hasPostTasks()) {
            for (Runnable task : packetSendEvent.getPostTasks()) {
                task.run();
            }
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (msg.readableBytes() != 0) {
            boolean recompress = handleCompression(ctx, msg);
            read(ctx, msg);
            if (recompress) {
                recompress(ctx, msg);
            }
            if (msg.isReadable()) {
                out.add(msg.retain());
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }


    private boolean handleCompression(ChannelHandlerContext ctx, ByteBuf buffer) {
        if (checkedCompression) return false;
        if (ctx.pipeline().names().indexOf("decompress") > ctx.pipeline().names().indexOf(PacketEvents.DECODER_NAME)) {
            // Need to decompress this packet due to bad order
            ChannelHandler decompressor = ctx.pipeline().get("decompress");
            //CompressionDecoder
            try {
                if (DECOMPRESSOR_METHOD == null) {
                    DECOMPRESSOR_METHOD = decompressor.getClass().getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class);
                }
                List<?> list = new ArrayList<>(1);
                DECOMPRESSOR_METHOD.invoke(decompressor, ctx, buffer, list);
                ByteBuf decompressed = (ByteBuf) list.get(0);
                if (buffer != decompressed) {
                    try {
                        buffer.clear().writeBytes(decompressed);
                    } finally {
                        decompressed.release();
                    }
                }
                //Relocate handlers
                PacketDecoder decoder = (PacketDecoder) ctx.pipeline().remove(PacketEvents.DECODER_NAME);
                ctx.pipeline().addAfter("decompress", PacketEvents.DECODER_NAME, decoder);
                PacketEncoder encoder = (PacketEncoder) ctx.pipeline().remove(PacketEvents.ENCODER_NAME);
                ctx.pipeline().addAfter("compress", PacketEvents.ENCODER_NAME, encoder);
                checkedCompression = true;
                return true;
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void recompress(ChannelHandlerContext ctx, ByteBuf buffer) {
        ByteBuf compressed = ctx.alloc().buffer();
        try {
            ChannelHandler compressor = ctx.pipeline().get("compress");
            if (COMPRESSOR_METHOD == null) {
                COMPRESSOR_METHOD = compressor.getClass().getDeclaredMethod("encode", ChannelHandlerContext.class, ByteBuf.class, ByteBuf.class);
            }
            COMPRESSOR_METHOD.invoke(compressor, ctx, buffer, compressed);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            buffer.clear().writeBytes(compressed);
            PacketEvents.getAPI().getLogManager().debug("Recompressed packet!");
        } finally {
            compressed.release();
        }
    }
}