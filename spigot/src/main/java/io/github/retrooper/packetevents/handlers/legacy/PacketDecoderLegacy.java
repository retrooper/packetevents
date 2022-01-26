package io.github.retrooper.packetevents.handlers.legacy;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.handlers.compression.PacketCompressionUtil;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.handler.codec.ByteToMessageDecoder;
import org.bukkit.entity.Player;

import java.util.List;

public class PacketDecoderLegacy extends ByteToMessageDecoder {
    public volatile Player player;
    public User user;
    public boolean bypassCompression = false;
    private boolean handledCompression;
    private boolean skipDoubleTransform;

    public PacketDecoderLegacy(User user) {
        this.user = user;
    }

    public PacketDecoderLegacy(PacketDecoderLegacy decoder) {
        this.player = decoder.player;
        this.user = decoder.user;
        this.bypassCompression = decoder.bypassCompression;
        this.handledCompression = decoder.handledCompression;
        this.skipDoubleTransform = decoder.skipDoubleTransform;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf input, List<Object> output) {
        if (skipDoubleTransform) {
            skipDoubleTransform = false;
            output.add(input.retain());
        }
        ByteBuf transformed = ctx.alloc().buffer().writeBytes(input);
        try {
            boolean doCompression =
                    !bypassCompression && handleCompressionOrder(ctx, transformed);
            int preProcessIndex = transformed.readerIndex();
            PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(user.getConnectionState(),
                    ctx.channel(), user, player, transformed);
            int processIndex = transformed.readerIndex();
            PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent, () -> {
                transformed.readerIndex(processIndex);
            });
            if (!packetReceiveEvent.isCancelled()) {
                if (packetReceiveEvent.getLastUsedWrapper() != null) {
                    packetReceiveEvent.getByteBuf().clear();
                    packetReceiveEvent.getLastUsedWrapper().writeVarInt(packetReceiveEvent.getPacketId());
                    packetReceiveEvent.getLastUsedWrapper().writeData();
                }
                transformed.readerIndex(preProcessIndex);
                if (doCompression) {
                    PacketCompressionUtil.recompress(ctx, transformed);
                    skipDoubleTransform = true;
                }
                output.add(transformed.retain());
            }

        } finally {
            transformed.release();
        }
    }


    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        read(ctx, byteBuf, out);
    }

    private boolean handleCompressionOrder(ChannelHandlerContext ctx, ByteBuf buffer) {
        if (handledCompression) return false;
        int decoderIndex = ctx.pipeline().names().indexOf("decompress");
        if (decoderIndex == -1) return false;
        handledCompression = true;
        if (decoderIndex > ctx.pipeline().names().indexOf(PacketEvents.DECODER_NAME)) {
            // Need to decompress this packet due to bad order
            ByteBuf decompressed = ctx.alloc().buffer();
            PacketCompressionUtil.decompress(ctx.pipeline(), buffer, decompressed);
            PacketCompressionUtil.relocateHandlers(ctx.pipeline(), buffer, decompressed);
            return true;
        }
        return false;
    }
}
