package io.github.retrooper.packetevents.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.minecraft.network.FriendlyByteBuf;

import java.util.zip.Deflater;

public class BetterPacketCompressionEncoder extends MessageToByteEncoder<ByteBuf> {
    private final byte[] encodeBuf = new byte[8192];
    private final Deflater deflater;
    private int threshold;

    public BetterPacketCompressionEncoder(int threshold) {
        this.threshold = threshold;
        this.deflater = new Deflater();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        if (msg.isReadable()) {
            int i = msg.readableBytes();
            FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(out);
            if (i < this.threshold) {
                friendlyByteBuf.writeVarInt(0);
                friendlyByteBuf.writeBytes(msg);
            } else {
                byte[] bs = new byte[i];
                msg.readBytes(bs);
                friendlyByteBuf.writeVarInt(bs.length);
                this.deflater.setInput(bs, 0, i);
                this.deflater.finish();

                while (!this.deflater.finished()) {
                    int j = this.deflater.deflate(this.encodeBuf);
                    friendlyByteBuf.writeBytes(this.encodeBuf, 0, j);
                }

                this.deflater.reset();
            }
        }
    }

    public int getThreshold() {
        return this.threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
