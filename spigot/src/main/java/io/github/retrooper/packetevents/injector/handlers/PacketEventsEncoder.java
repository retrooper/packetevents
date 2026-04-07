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
import io.github.retrooper.packetevents.injector.SpigotChannelInjector.PreViaState;
import io.github.retrooper.packetevents.injector.connection.PacketHandlerRelocation;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Queue;

@ApiStatus.Internal
public class PacketEventsEncoder extends ChannelOutboundHandlerAdapter {

    private static final boolean NETTY_4_1_0;

    static {
        // ancient Minecraft versions don't support this api
        // https://howoldisminecraft188.today/
        boolean netty410 = false;
        try {
            ChannelFuture.class.getDeclaredMethod("isVoid");
            netty410 = true;
        } catch (NoSuchMethodException ignored) {
        }
        NETTY_4_1_0 = netty410;
    }

    public User user;
    public Player player;
    public PreViaState preVia;

    public boolean relocateOnCompression;
    public boolean compressionHandled = PacketHandlerRelocation.COMPRESSION_ENABLED_EVENT != null;

    private final Queue<QueuedMessage> queuedMessages = new ArrayDeque<>();
    private boolean hold = false;

    public PacketEventsEncoder(User user, PreViaState preVia) {
        this.user = user;
        this.preVia = preVia;
    }

    public PacketEventsEncoder(PacketEventsEncoder encoder) {
        this.user = encoder.user;
        this.player = encoder.player;
        this.relocateOnCompression = encoder.relocateOnCompression;
        this.compressionHandled = encoder.compressionHandled;
        this.hold = encoder.hold;
        this.preVia = encoder.preVia;
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

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // if we are told to hold all messages, add them to the queue
        if (this.hold && msg instanceof ByteBuf) {
            this.queuedMessages.add(new QueuedMessage(msg, promise));
            return;
        }

        PacketSendEvent packetSendEvent = null;
        PacketSendEvent fallbackSendEvent = null;
        if (msg instanceof ByteBuf) {
            boolean needsRecompression = this.maybeRelocateForCompression(ctx, (ByteBuf) msg);
            ConnectionState connectionState = this.preVia == PreViaState.PRE_VIA ? this.user.getClientEncoderState() : this.user.getEncoderState();
            packetSendEvent = PacketEventsImplHelper.handleClientBoundPacket(ctx.channel(), this.user, this.player, msg,
                    this.preVia != PreViaState.PRE_VIA, connectionState, this.preVia == PreViaState.PRE_VIA);

            // check if the packet got canceled
            if (!((ByteBuf) msg).isReadable()) {
                ReferenceCountUtil.release(msg);
                promise.trySuccess(); // TODO how to properly handle this?
                return; // abort handling
            }

            // no viaversion present, still call fallback handlers
            if (this.preVia == PreViaState.FALLBACK) {
                fallbackSendEvent = PacketEventsImplHelper.handleClientBoundPacket(ctx.channel(), this.user, this.player, msg,
                        false, this.user.getClientEncoderState(), true);

                // check if the packet got canceled by a client-versioned listener
                if (!((ByteBuf) msg).isReadable()) {
                    ReferenceCountUtil.release(msg);
                    promise.trySuccess();
                    return;
                }
            }

            if (needsRecompression) {
                PacketHandlerRelocation.compressBuffer(ctx, (ByteBuf) msg);
            }
        }

        ctx.write(msg, promise);

        runTasksAfterSend(packetSendEvent);
        runTasksAfterSend(fallbackSendEvent);
    }

    private static void runTasksAfterSend(@Nullable PacketSendEvent event) {
        if (event == null || !event.hasTasksAfterSend()) {
            return;
        }
        for (Runnable task : event.getTasksAfterSend()) {
            try {
                task.run();
            } catch (Throwable throwable) {
                throw new PacketProcessException("Error while handling post-send-task " + task + " for " + event, throwable);
            }
        }
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

    private boolean maybeRelocateForCompression(ChannelHandlerContext ctx, ByteBuf buffer) throws InvocationTargetException {
        if (this.compressionHandled || !this.relocateOnCompression
                || !PacketHandlerRelocation.isCompressionEnabled(ctx.pipeline())) {
            return false;
        }
        this.compressionHandled = true;
        return PacketHandlerRelocation.relocateForCompression(
                ctx, this.user, PacketHandlerRelocation.slotOf(this.preVia), buffer);
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
