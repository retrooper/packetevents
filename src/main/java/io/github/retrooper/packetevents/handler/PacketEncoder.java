package io.github.retrooper.packetevents.handler;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.player.LocalPlayer;

public class PacketEncoder extends MessageToByteEncoder<ByteBuf> {
    public final LocalPlayer player;
    public User user;
    private final List<Runnable> promisedTasks = new ArrayList<>();

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
                packetReceiveEvent.getByteBuf().clear();
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
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!promisedTasks.isEmpty()) {
            List<Runnable> postTasks = new ArrayList<>(this.promisedTasks);
            this.promisedTasks.clear();
            promise.addListener(f -> {
                for (Runnable task : postTasks) {
                    task.run();
                }
            });
        }
        super.write(ctx, msg, promise);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        if (msg.readableBytes() == 0)return;
        out.writeBytes(msg);
        read(ctx, out);
    }
}