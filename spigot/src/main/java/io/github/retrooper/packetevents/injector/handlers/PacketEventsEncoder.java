/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.injector.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.exception.InvalidDisconnectPacketSend;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Queue;

public class PacketEventsEncoder extends ChannelOutboundHandlerAdapter {

    public static final Object COMPRESSION_ENABLED_EVENT = paperCompressionEnabledEvent();
    private static final boolean NETTY_4_1_0;

    static {
        // ancient Minecraft versions don't support this api
        // https://howoldisminecraft188.today/
        boolean netty410 = false;
        try {
            ChannelPromise.class.getDeclaredMethod("unvoid");
            netty410 = true;
        } catch (NoSuchMethodException ignored) {
        }
        NETTY_4_1_0 = netty410;
    }

    public User user;
    public Player player;
    private boolean handledCompression = COMPRESSION_ENABLED_EVENT != null;
    private ChannelPromise promise;

    private final Queue<QueuedMessage> queuedMessages = new ArrayDeque<>();
    private boolean hold = false;

    public PacketEventsEncoder(User user) {
        this.user = user;
    }

    public PacketEventsEncoder(ChannelHandler encoder) {
        user = ((PacketEventsEncoder) encoder).user;
        player = ((PacketEventsEncoder) encoder).player;
        handledCompression = ((PacketEventsEncoder) encoder).handledCompression;
        promise = ((PacketEventsEncoder) encoder).promise;
    }

    public void setHold(Channel ch, boolean hold) throws Exception {
        if (this.hold == hold) {
            return;
        }
        this.hold = hold;
        // write all queued messages
        if (!hold && !this.queuedMessages.isEmpty()) {
            ChannelHandlerContext ctx = ch.pipeline().context(this);
            QueuedMessage queued;
            while ((queued = this.queuedMessages.poll()) != null) {
                this.write(ctx, queued.message, queued.promise);
            }
        }
    }

    private @Nullable PacketSendEvent handleClientBoundPacket(Channel channel, User user, Object player, ByteBuf buffer, ChannelPromise promise) throws Exception {
        PacketSendEvent packetSendEvent = PacketEventsImplHelper.handleClientBoundPacket(channel, user, player, buffer, true);
        if (packetSendEvent != null && packetSendEvent.hasTasksAfterSend()) {
            promise.addListener((p) -> {
                for (Runnable task : packetSendEvent.getTasksAfterSend()) {
                    task.run();
                }
            });
        }
        return packetSendEvent;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // if we are told to hold all messages, add them to the queue
        if (this.hold && msg instanceof ByteBuf) {
            this.queuedMessages.add(new QueuedMessage(msg, promise));
            return;
        }

        // We must restore the old promise (in case we are stacking promises such as sending packets on send event)
        // If the old promise was successful, set it to null to avoid memory leaks.
        ChannelPromise oldPromise = this.promise != null && !this.promise.isSuccess() ? this.promise : null;
        if (NETTY_4_1_0) {
            // "unvoid" will just make sure we can actually add listeners to this promise...
            // since 1.21.6, mojang will give us void promises when they don't care about the result
            promise = promise.unvoid();
        }
        promise.addListener(p -> this.promise = oldPromise);
        this.promise = promise;

        if (msg instanceof ByteBuf) {
            boolean needsRecompression = !this.handledCompression && this.handleCompression(ctx, (ByteBuf) msg);
            this.handleClientBoundPacket(ctx.channel(), this.user, this.player, (ByteBuf) msg, this.promise);

            // check if the packet got cancelled
            if (!((ByteBuf) msg).isReadable()) {
                ReferenceCountUtil.release(msg);
                promise.trySuccess(); // TODO how to properly handle this?
                return; // abort handling
            }

            if (needsRecompression) {
                this.compress(ctx, (ByteBuf) msg);
            }
        }

        ctx.write(msg, promise);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        // release queued messages to prevent memory leaks
        // when this handler gets removed from the pipeline
        QueuedMessage entry;
        while ((entry = this.queuedMessages.poll()) != null) {
            ReferenceCountUtil.release(entry.message);
            if (NETTY_4_1_0 && entry.promise != null && !entry.promise.isVoid()) {
                entry.promise.setFailure(new IllegalStateException(this + " got dropped from pipeline " + ctx.channel()));
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Ignore how mojang sends DISCONNECT packets in the wrong state
        if (ExceptionUtil.isException(cause, InvalidDisconnectPacketSend.class)) {
            return;
        }

        boolean didWeCauseThis = ExceptionUtil.isException(cause, PacketProcessException.class);
        if (didWeCauseThis
                && (user == null || user.getEncoderState() != ConnectionState.HANDSHAKING)) {
            if (!SpigotReflectionUtil.isMinecraftServerInstanceDebugging()) {
                if (PacketEvents.getAPI().getSettings().isFullStackTraceEnabled()) {
                    cause.printStackTrace();
                } else {
                    PacketEvents.getAPI().getLogManager().warn(cause.getMessage());
                }
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
                    FoliaScheduler.getEntityScheduler().runDelayed(player, (Plugin) PacketEvents.getAPI().getPlugin(), (o) -> player.kickPlayer("Invalid packet"), null, 1);
                }

                if (user != null && user.getProfile().getName() != null) {
                    PacketEvents.getAPI().getLogManager().warn("Disconnected " + user.getProfile().getName() + " due to an invalid packet!");
                }
            }
        }

        super.exceptionCaught(ctx, cause);
    }

    private static Object paperCompressionEnabledEvent() {
        try {
            final Class<?> eventClass = Class.forName("io.papermc.paper.network.ConnectionEvent");
            return eventClass.getDeclaredField("COMPRESSION_THRESHOLD_SET").get(null);
        } catch (final ReflectiveOperationException e) {
            return null;
        }
    }

    private void compress(ChannelHandlerContext ctx, ByteBuf input) throws InvocationTargetException {
        ChannelHandler compressor = ctx.pipeline().get("compress");
        ByteBuf temp = ctx.alloc().buffer();
        try {
            if (compressor != null) {
                CustomPipelineUtil.callEncode(compressor, ctx, input, temp);
            }
        } finally {
            input.clear().writeBytes(temp);
            temp.release();
        }
    }

    private void decompress(ChannelHandlerContext ctx, ByteBuf input, ByteBuf output) throws InvocationTargetException {
        ChannelHandler decompressor = ctx.pipeline().get("decompress");
        if (decompressor != null) {
            ByteBuf temp = (ByteBuf) CustomPipelineUtil.callDecode(decompressor, ctx, input).get(0);
            try {
                output.clear().writeBytes(temp);
            } finally {
                temp.release();
            }
        }
    }

    private boolean handleCompression(ChannelHandlerContext ctx, ByteBuf buffer) throws InvocationTargetException {
        if (handledCompression) return false;
        int compressIndex = ctx.pipeline().names().indexOf("compress");
        if (compressIndex == -1) return false;
        handledCompression = true;
        int peEncoderIndex = ctx.pipeline().names().indexOf(PacketEvents.ENCODER_NAME);
        if (peEncoderIndex == -1) return false;
        if (compressIndex > peEncoderIndex) {
            //We are ahead of the decompression handler (they are added dynamically) so let us relocate.
            //But first we need to compress the data and re-compress it after we do all our processing to avoid issues.
            decompress(ctx, buffer, buffer);
            //Let us relocate and no longer deal with compression.
            PacketEventsDecoder decoder = (PacketEventsDecoder) ctx.pipeline().get(PacketEvents.DECODER_NAME);
            ServerConnectionInitializer.relocateHandlers(ctx.channel(), decoder, user);
            return true;
        }
        return false;
    }

    private static final class QueuedMessage {

        private final Object message;
        private final ChannelPromise promise;

        public QueuedMessage(Object message, ChannelPromise promise) {
            this.message = message;
            this.promise = promise;
        }
    }
}
