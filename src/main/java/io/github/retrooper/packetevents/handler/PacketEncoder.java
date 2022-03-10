package io.github.retrooper.packetevents.handler;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.client.player.LocalPlayer;

@ChannelHandler.Sharable
public class PacketEncoder extends MessageToByteEncoder<ByteBuf> {
    public User user;
    public LocalPlayer player;

    public PacketEncoder(User user) {
        this.user = user;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        int firstReaderIndex = buffer.readerIndex();
        PacketReceiveEvent packetReceiveEvent = EventCreationUtil.createReceiveEvent(ctx.channel(), user, player, buffer);
        int readerIndex = buffer.readerIndex();
        PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent, () -> buffer.readerIndex(readerIndex));
        if (!packetReceiveEvent.isCancelled()) {
            if (packetReceiveEvent.getLastUsedWrapper() != null) {
                ByteBufHelper.clear(packetReceiveEvent.getByteBuf());
                packetReceiveEvent.getLastUsedWrapper().writeVarInt(packetReceiveEvent.getPacketId());
                packetReceiveEvent.getLastUsedWrapper().writeData();
            }
            buffer.readerIndex(firstReaderIndex);
        } else {
            //Clear the buffer, our custom write method will discard the packet for us.
            buffer.clear();
        }
        if (packetReceiveEvent.hasPostTasks()) {
            for (Runnable task : packetReceiveEvent.getPostTasks()) {
                task.run();
            }
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
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
            read(ctx, msg);
            out.writeBytes(msg);
        }
    }
}