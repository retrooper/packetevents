package io.github.retrooper.packetevents.handler;

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

@ChannelHandler.Sharable
public class ClientPacketDecoder extends ServerPacketDecoder {
    public ClientPacketDecoder(User user) {
         super(user);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (msg.isReadable()) {
            ByteBuf outputBuffer = ctx.alloc().buffer().writeBytes(msg);
            boolean recompress = handleCompression(ctx, outputBuffer);
            PacketEventsImplHelper.handleClientBoundPacket(ctx.channel(), user, player, outputBuffer, false);
            //TODO hasTasksAfter
            if (outputBuffer.isReadable()) {
                if (recompress) {
                    recompress(ctx, outputBuffer);
                }
                out.add(outputBuffer.retain());
            }
        }
    }
}