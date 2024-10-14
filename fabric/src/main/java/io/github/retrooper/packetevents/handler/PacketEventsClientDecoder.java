package io.github.retrooper.packetevents.handler;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.injector.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.client.player.LocalPlayer;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@ChannelHandler.Sharable
public class PacketEventsClientDecoder extends MessageToMessageDecoder<ByteBuf> {
    public User user;
    public LocalPlayer player;
    public boolean checkedCompression;

    public PacketEventsClientDecoder(User user) {
        this.user = user;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (msg.isReadable()) {
            ByteBuf outputBuffer = ctx.alloc().buffer().writeBytes(msg);
            boolean recompress = handleCompression(ctx, outputBuffer);
            PacketEventsImplHelper.handleClientBoundPacket(ctx.channel(), user, player, outputBuffer, false);
            if (outputBuffer.isReadable()) {
                if (recompress) {
                    recompress(ctx, outputBuffer);
                }
                out.add(outputBuffer.retain());
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
                List<?> list = CustomPipelineUtil.callDecode(decompressor, ctx, buffer);
                ByteBuf decompressed = (ByteBuf) list.get(0);
                if (buffer != decompressed) {
                    try {
                        buffer.clear().writeBytes(decompressed);
                    } finally {
                        decompressed.release();
                    }
                }
                //Relocate handlers
                PacketEventsClientDecoder decoder = (PacketEventsClientDecoder) ctx.pipeline().remove(PacketEvents.DECODER_NAME);
                ctx.pipeline().addAfter("decompress", PacketEvents.DECODER_NAME, decoder);
                PacketEventsClientEncoder encoder = (PacketEventsClientEncoder) ctx.pipeline().remove(PacketEvents.ENCODER_NAME);
                ctx.pipeline().addAfter("compress", PacketEvents.ENCODER_NAME, encoder);
                checkedCompression = true;
                return true;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void recompress(ChannelHandlerContext ctx, ByteBuf buffer) {
        ByteBuf compressed = ctx.alloc().buffer();
        try {
            ChannelHandler compressor = ctx.pipeline().get("compress");
            CustomPipelineUtil.callEncode(compressor, ctx, buffer, compressed);
        } catch (InvocationTargetException e) {
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