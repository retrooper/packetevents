package io.github.retrooper.packetevents.handler;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.minecraft.client.player.LocalPlayer;

@ChannelHandler.Sharable
public class PacketEncoder extends MessageToByteEncoder<ByteBuf> {
    public final LocalPlayer player;
    public User user;

    public PacketEncoder(User user, LocalPlayer player) {
        this.user = user;
        this.player = player;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        int firstReaderIndex = byteBuf.readerIndex();
        PacketReceiveEvent packetReceiveEvent = EventCreationUtil.createReceiveEvent(ctx.channel(), user, player, byteBuf);
        int readerIndex = byteBuf.readerIndex();
        PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent, () -> byteBuf.readerIndex(readerIndex));
        if (!packetReceiveEvent.isCancelled()) {
            if (packetReceiveEvent.getLastUsedWrapper() != null) {
                ByteBufHelper.clear(packetReceiveEvent.getByteBuf());
                packetReceiveEvent.getLastUsedWrapper().writeVarInt(packetReceiveEvent.getPacketId());
                packetReceiveEvent.getLastUsedWrapper().writeData();
            }
            byteBuf.readerIndex(firstReaderIndex);
        }
        if (packetReceiveEvent.hasPostTasks()) {
            for (Runnable task : packetReceiveEvent.getPostTasks()) {
                task.run();
            }
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        if (msg.readableBytes() == 0) return;
        read(ctx, msg);
        out.writeBytes(msg);
    }
}