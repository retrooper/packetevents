/*
 * This file is part of ViaVersion - https://github.com/ViaVersion/ViaVersion
 * Copyright (C) 2016-2021 ViaVersion and contributors
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

import com.viaversion.viaversion.exception.CancelCodecException;
import com.viaversion.viaversion.exception.InformativeException;
import com.viaversion.viaversion.util.PipelineUtil;
import io.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CustomBukkitDecodeHandler extends ByteToMessageDecoder {
    public final ChannelHandler oldBukkitDecodeHandler;
    public final List<Object> customDecoders = new ArrayList<>();
    public final ByteToMessageDecoder minecraftDecoder;
    private final Object userInfo;

    public CustomBukkitDecodeHandler(Object userInfo, ByteToMessageDecoder minecraftDecoder, ChannelHandler oldBukkitDecodeHandler) {
        this.userInfo = userInfo;
        this.minecraftDecoder = minecraftDecoder;
        this.oldBukkitDecodeHandler = oldBukkitDecodeHandler;
    }

    public void addCustomDecoder(Object customDecoder) {
        customDecoders.add(customDecoder);
    }

    public <T> T getCustomDecoder(Class<T> clazz) {
        for (Object customDecoder : customDecoders) {
            if (customDecoder.getClass().equals(clazz)) {
                return (T) customDecoder;
            }
        }
        return null;
    }

    public Object getCustomDecoderBySimpleName(String simpleName) {
        for (Object customDecoder : customDecoders) {
            if (ClassUtil.getClassSimpleName(customDecoder.getClass()).equals(simpleName)) {
                return customDecoder;
            }
        }
        return null;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (!ViaVersionUtil.checkServerboundPacketUserConnection(userInfo)) {
            byteBuf.clear(); //Don't accumulate
            throw ViaVersionUtil.throwCancelEncoderException(null);
        }
        
        ByteBuf transformedBuf = null;
        try {
            if (ViaVersionUtil.isUserConnectionActive(userInfo)) {
                transformedBuf = ctx.alloc().buffer().writeBytes(byteBuf);
                ViaVersionUtil.transformPacket(userInfo, transformedBuf, true);
            }

            try {
                Object result = transformedBuf == null ? byteBuf : transformedBuf;
                for (Object customDecoder : customDecoders) {
                    //We only support one output (except for ProtocolLib)
                    if (customDecoder instanceof ByteToMessageDecoder) {
                        result = PipelineUtil.callDecode((ByteToMessageDecoder) customDecoder, ctx, result).get(0);
                    } else if (customDecoder instanceof MessageToMessageDecoder) {
                        result = PipelineUtil.callDecode((MessageToMessageDecoder<?>) customDecoder, ctx, result).get(0);
                    }
                }
                if (result instanceof ByteBuf) {
                    //We will utilize the vanilla decoder to convert the ByteBuf to an NMS packet
                    List<Object> nmsObjects = PipelineUtil.callDecode(minecraftDecoder, ctx, result);
                    list.addAll(nmsObjects);
                } else {
                    //Some previous decoder likely already converted the ByteBuf to an NMS packet
                    if (result instanceof List) {
                        list.addAll((List<?>) result);
                    } else {
                        list.add(result);
                    }
                }
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

    private boolean containsCause(Throwable t, Class<?> c) {
        while (t != null) {
            if (c.isAssignableFrom(t.getClass())) {
                return true;
            }

            t = t.getCause();
        }
        return false;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (containsCause(cause, CancelCodecException.class)) return; // ProtocolLib compat

        super.exceptionCaught(ctx, cause);
        if (!ViaNMSUtil.isDebugPropertySet() && containsCause(cause, InformativeException.class)
                && (ViaVersionUtil.getUserConnectionProtocolState(userInfo) != ConnectionState.HANDSHAKING || ViaVersionUtil.isDebug())) {
            cause.printStackTrace(); // Print if CB doesn't already do it
        }
    }
}