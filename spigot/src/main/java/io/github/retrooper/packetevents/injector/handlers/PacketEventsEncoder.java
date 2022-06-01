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

package io.github.retrooper.packetevents.injector.handlers;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class PacketEventsEncoder extends MessageToByteEncoder<Object> {
    public User user;
    public volatile Player player;
    public MessageToByteEncoder<?> vanillaEncoder;
    public final List<Runnable> queuedPostTasks = new ArrayList<>();

    public PacketEventsEncoder(User user) {
        this.user = user;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, ByteBuf out) throws Exception {
        if (!(o instanceof ByteBuf)) {
            //Convert NMS object to bytes, so we can process it right away.
            if (vanillaEncoder == null) return;
            CustomPipelineUtil.callEncode(vanillaEncoder, ctx, o, out);
            //Failed to translate it into ByteBuf form (which we can process)
            if (!out.isReadable()) return;
        } else {
            ByteBuf in = (ByteBuf) o;
            //Empty packets?
            if (!in.isReadable()) return;
            out.writeBytes(in);
        }
        PacketSendEvent sendEvent = PacketEventsImplHelper.handleClientBoundPacket(ctx.channel(), user, player, out, true, false);
        if (sendEvent.hasPostTasks()) {
            queuedPostTasks.addAll(sendEvent.getPostTasks());
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //This is netty code of the write method, and we are just "injecting" into it.
        ByteBuf buf = null;
        try {
            if (this.acceptOutboundMessage(msg)) {
                buf = this.allocateBuffer(ctx, msg, true);
                try {
                    this.encode(ctx, msg, buf);
                } finally {
                    ReferenceCountUtil.release(msg);
                    //PacketEvents - Start
                    //Now we added the post tasks to the queuedPostTasks list, so let us execute them after we send the packet.
                    if (!queuedPostTasks.isEmpty()) {
                        List<Runnable> tasks = new ArrayList<>(queuedPostTasks);
                        queuedPostTasks.clear();
                        promise.addListener(f -> {
                            for (Runnable task : tasks) {
                                task.run();
                            }
                        });
                    }
                    //PacketEvents - End
                }
                if (buf.isReadable()) {
                    ctx.write(buf, promise);
                } else {
                    buf.release();
                    ctx.write(Unpooled.EMPTY_BUFFER, promise);
                }
                buf = null;
            } else {
                ctx.write(msg, promise);
            }
        } catch (EncoderException e) {
            throw e;
        } catch (Throwable e2) {
            throw new EncoderException(e2);
        } finally {
            if (buf != null) {
                buf.release();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //Check if the minecraft server will already print our exception for us.
        if (ExceptionUtil.isException(cause, PacketProcessException.class) && !SpigotReflectionUtil.isMinecraftServerInstanceDebugging()
                && (user == null || user.getConnectionState() != ConnectionState.HANDSHAKING)) {
            cause.printStackTrace();
        }
    }
}
