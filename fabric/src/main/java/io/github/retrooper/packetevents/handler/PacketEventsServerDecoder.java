package io.github.retrooper.packetevents.handler;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

@ChannelHandler.Sharable
public class PacketEventsServerDecoder extends MessageToMessageDecoder<ByteBuf> {
    public User user;
    public ServerPlayer player;
    public boolean hasBeenRelocated;

    public PacketEventsServerDecoder(User user) {
        this.user = user;
    }

    public PacketEventsServerDecoder(PacketEventsServerDecoder decoder) {
        user = decoder.user;
        player = decoder.player;
        hasBeenRelocated = decoder.hasBeenRelocated;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
        Object buffer = PacketEventsImplHelper.handleServerBoundPacket(ctx.channel(), user, player, input, true);
        out.add(ByteBufHelper.retain(buffer));
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (buffer.isReadable()) {
            read(ctx, buffer, out);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //Check if the minecraft server will already print our exception for us.
        //Don't print errors during handshake
        boolean didWeCauseThis = ExceptionUtil.isException(cause, PacketProcessException.class);
        if (didWeCauseThis
                && (user == null || user.getDecoderState() != ConnectionState.HANDSHAKING)) {
            if (PacketEvents.getAPI().getSettings().isFullStackTraceEnabled()) {
                cause.printStackTrace();
            } else {
                PacketEvents.getAPI().getLogManager().warn(cause.getMessage());
            }

            if (PacketEvents.getAPI().getSettings().isKickOnPacketExceptionEnabled()) {
                try {
                    if (user != null) {
                        user.sendPacket(new WrapperPlayServerDisconnect(Component.text("Invalid packet")));
                    }
                } catch (Exception ignored) { // There may (?) be an exception if the player is in the wrong state...
                    // Do nothing.
                }
                ctx.channel().close();
                if (player != null) {
                    player.connection.disconnect(net.minecraft.network.chat.Component.literal("Invalid packet"));
                }

                if (user != null) {
                    PacketEvents.getAPI().getLogManager().warn("Disconnected " + user.getProfile().getName() + " due to invalid packet!");
                }
            }
        }
    }

    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object event) throws Exception {
        if (PacketEventsServerEncoder.COMPRESSION_ENABLED_EVENT == null || event != PacketEventsServerEncoder.COMPRESSION_ENABLED_EVENT) {
            super.userEventTriggered(ctx, event);
            return;
        }

        // Via changes the order of handlers in this event, so we must respond to Via changing their stuff
        ServerConnectionInitializer.relocateHandlers(ctx.channel(), this, user);
        super.userEventTriggered(ctx, event);
    }

}
