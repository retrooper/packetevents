package io.github.retrooper.packetevents.handler;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public class ClientPacketEncoder extends ServerPacketEncoder {
    public ClientPacketEncoder(User user) {
        super(user);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        if (msg.isReadable()) {
            PacketEventsImplHelper.handleServerBoundPacket(ctx.channel(), user, player, msg, false);
            out.writeBytes(msg);
        }
    }
}