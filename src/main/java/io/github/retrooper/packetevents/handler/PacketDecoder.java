package io.github.retrooper.packetevents.handler;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import net.minecraft.client.player.LocalPlayer;

public class PacketDecoder extends ByteToMessageDecoder {
    public User user;
    public final LocalPlayer player;

    public PacketDecoder(User user, LocalPlayer player) {
        this.user = user;
        this.player = player;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> output) throws Exception {
        ByteBuf transformed = ctx.alloc().buffer().writeBytes(byteBuf);
        try {
            int firstReaderIndex = transformed.readerIndex();
            PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(ctx.channel(), user, player, transformed);
            int readerIndex = transformed.readerIndex();
            PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> transformed.readerIndex(readerIndex));
            if (!packetSendEvent.isCancelled()) {
                if (packetSendEvent.getLastUsedWrapper() != null) {
                    ByteBufHelper.clear(packetSendEvent.getByteBuf());
                    packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
                    packetSendEvent.getLastUsedWrapper().writeData();
                }
                transformed.readerIndex(firstReaderIndex);
                output.add(transformed.retain());
            }
            if (packetSendEvent.hasPostTasks()) {
                for (Runnable task : packetSendEvent.getPostTasks()) {
                    task.run();
                }
            }
        } finally {
            transformed.release();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() != 0) {
            read(ctx, byteBuf, out);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
}