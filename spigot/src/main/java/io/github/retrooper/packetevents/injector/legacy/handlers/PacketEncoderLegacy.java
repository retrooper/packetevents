/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.injector.legacy.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.injector.PacketCompressionUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.channel.ChannelHandler;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.channel.ChannelPromise;
import net.minecraft.util.io.netty.handler.codec.MessageToByteEncoder;
import org.bukkit.entity.Player;

@ChannelHandler.Sharable
public class PacketEncoderLegacy extends MessageToByteEncoder<ByteBuf> {
    public volatile Player player;
    public User user;
    private boolean handledCompression;

    public PacketEncoderLegacy(User user) {
        this.user = user;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        boolean doCompression = handleCompressionOrder(ctx, buffer);

        int preProcessIndex = buffer.readerIndex();
        PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(ctx.channel(), user, player, buffer);
        int processIndex = buffer.readerIndex();
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> {
            buffer.readerIndex(processIndex);
        });
        if (!packetSendEvent.isCancelled()) {
            if (packetSendEvent.getLastUsedWrapper() != null) {
                ByteBufHelper.clear(packetSendEvent.getByteBuf());
                packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
                packetSendEvent.getLastUsedWrapper().write();
            }
            buffer.readerIndex(preProcessIndex);
        } else {
            //Make the buffer unreadable for the next handlers
            buffer.clear();
            doCompression = false;
        }
        if (packetSendEvent.hasPostTasks()) {
            for (Runnable task : packetSendEvent.getPostTasks()) {
                task.run();
            }
        }
        if (doCompression) {
            PacketCompressionUtil.recompress(ctx, buffer);
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
        if (!in.isReadable()) return;
        read(ctx, in);
        out.writeBytes(in);
    }

    private boolean handleCompressionOrder(ChannelHandlerContext ctx, ByteBuf buffer) {
        if (handledCompression) return false;
        int encoderIndex = ctx.pipeline().names().indexOf("compress");
        if (encoderIndex == -1) return false;
        handledCompression = true;
        if (encoderIndex > ctx.pipeline().names().indexOf(PacketEvents.ENCODER_NAME)) {
            // Need to decompress this packet due to bad order
            ByteBuf decompressed = ctx.alloc().buffer();
            PacketCompressionUtil.decompress(ctx.pipeline(), buffer, decompressed);
            PacketCompressionUtil.relocateHandlers(ctx.pipeline(), buffer, decompressed);
            return true;
        }
        return false;
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        PacketEventsImplHelper.handleDisconnection(ctx.channel(), null);
        super.close(ctx, promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //}
        //Check if the minecraft server will already print our exception for us.
        if (ExceptionUtil.isException(cause, PacketProcessException.class)
                && !SpigotReflectionUtil.isMinecraftServerInstanceDebugging()
                && (user == null || user.getConnectionState() != ConnectionState.HANDSHAKING)) {
            cause.printStackTrace();
        }
    }
}
