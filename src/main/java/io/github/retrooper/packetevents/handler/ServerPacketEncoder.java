package io.github.retrooper.packetevents.handler;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.world.entity.player.Player;

@ChannelHandler.Sharable
public class ServerPacketEncoder extends MessageToByteEncoder<ByteBuf> {
    public User user;
    public Player player;

    public ServerPacketEncoder(User user) {
        this.user = user;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ByteBuf buf = null;
        try {
            if (acceptOutboundMessage(msg)) {
                ByteBuf in = (ByteBuf) msg;
                buf = allocateBuffer(ctx, in, true);
                try {
                    encode(ctx, in, buf);
                } finally {
                    ReferenceCountUtil.release(in);
                }
                if (buf.isReadable()) {
                    ctx.write(buf, promise);
                } else {
                    buf.release();
                    //We cancelled this packet, do not pass it on to the next handler.
                    //ctx.write(Unpooled.EMPTY_BUFFER, promise);
                }
                buf = null;
            } else {
                ctx.write(msg, promise);
            }
        } catch (EncoderException e) {
            throw e;
        } catch (Throwable e) {
            throw new EncoderException(e);
        } finally {
            if (buf != null) {
                buf.release();
            }
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        if (msg.isReadable()) {
            PacketEventsImplHelper.handleClientBoundPacket(ctx.channel(), user, player, msg, false);
            out.writeBytes(msg);
        }
    }
}