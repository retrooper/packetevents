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

package io.github.retrooper.packetevents.utils.dependencies.viaversion;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.bukkit.util.NMSUtil;
import com.viaversion.viaversion.exception.CancelCodecException;
import com.viaversion.viaversion.exception.CancelDecoderException;
import com.viaversion.viaversion.exception.InformativeException;
import com.viaversion.viaversion.util.PipelineUtil;
import io.github.retrooper.packetevents.utils.reflection.ReflectionObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

//TODO Maybe make it extend BukkitDecodeHandler
public class CustomBukkitDecodeHandler extends ByteToMessageDecoder {
    public final ChannelHandler oldBukkitDecodeHandler;
    public final List<Object> customDecoders = new ArrayList<>();
    public final ByteToMessageDecoder minecraftDecoder;
    private final UserConnection info;

    public CustomBukkitDecodeHandler(UserConnection info, ByteToMessageDecoder minecraftDecoder, ChannelHandler oldBukkitDecodeHandler) {
        this.info = info;
        this.minecraftDecoder = minecraftDecoder;
        this.oldBukkitDecodeHandler = oldBukkitDecodeHandler;
    }

    public void addCustomDecoder(Object customDecoder) {
        customDecoders.add(customDecoder);
    }

    public Object getCustomDecoder(String handlerName) {
        for (Object customDecoder : customDecoders) {
            ReflectionObject reflectionObject = new ReflectionObject(customDecoder);
            try {
                String customDecoderHandlerName = reflectionObject.readString(0);
                if (customDecoderHandlerName.equals(handlerName)) {
                    return customDecoder;
                }
            }
            catch (Exception ignored) {
            }
        }
        return null;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf bytebuf, List<Object> list) throws Exception {
        if (!info.checkServerboundPacket()) {
            bytebuf.clear(); // Don't accumulate
            throw CancelDecoderException.generate(null);
        }
        ByteBuf transformedBuf = null;
        try {
            if (info.shouldTransformPacket()) {
                transformedBuf = ctx.alloc().buffer().writeBytes(bytebuf);
                info.transformServerbound(transformedBuf, CancelDecoderException::generate);
            }

            try {
                Object result = transformedBuf == null ? bytebuf : transformedBuf;
                for (Object customDecoder : customDecoders) {
                    //We only support one output
                    if (customDecoder instanceof ByteToMessageDecoder) {
                        result = PipelineUtil.callDecode((ByteToMessageDecoder) customDecoder, ctx, result).get(0);
                    }
                    else if (customDecoder instanceof MessageToMessageDecoder) {
                        result = PipelineUtil.callDecode((MessageToMessageDecoder) customDecoder, ctx, result).get(0);
                    }
                }
                list.addAll(PipelineUtil.callDecode(minecraftDecoder, ctx, result));
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof Exception) {
                    throw (Exception) e.getCause();
                } else if (e.getCause() instanceof Error) {
                    throw (Error) e.getCause();
                }
            }
        } finally {
            if (transformedBuf != null) {
                transformedBuf.release();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (PipelineUtil.containsCause(cause, CancelCodecException.class)) return; // ProtocolLib compat

        super.exceptionCaught(ctx, cause);
        if (!NMSUtil.isDebugPropertySet() && PipelineUtil.containsCause(cause, InformativeException.class)
                && (info.getProtocolInfo().getState() != State.HANDSHAKE || Via.getManager().isDebug())) {
            cause.printStackTrace(); // Print if CB doesn't already do it
        }
    }
}