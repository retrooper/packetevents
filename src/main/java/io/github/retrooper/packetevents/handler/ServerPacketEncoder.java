package io.github.retrooper.packetevents.handler;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public class ServerPacketEncoder extends PacketEncoder {
    public ServerPacketEncoder(User user) {
        super(user);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        if (msg.isReadable()) {
            PacketEventsImplHelper.handleClientBoundPacket(ctx.channel(), user, player, msg, false);
            out.writeBytes(msg);
        }
    }
}