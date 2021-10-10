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

import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.util.reflection.ClassUtil;
import com.viaversion.viaversion.exception.CancelCodecException;
import com.viaversion.viaversion.exception.InformativeException;
import com.viaversion.viaversion.handlers.ChannelHandlerContextWrapper;
import com.viaversion.viaversion.handlers.ViaCodecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CustomBukkitEncodeHandlerModern extends MessageToByteEncoder<Object> implements ViaCodecHandler {
    private static Field versionField;

    static {
        try {
            // Attempt to get any version info from the handler
            versionField = ViaNMSUtil.nms(
                    "PacketEncoder",
                    "net.minecraft.network.PacketEncoder"
            ).getDeclaredField("version");

            versionField.setAccessible(true);
        } catch (Exception e) {
            // Not compat version
        }
    }

    public final ChannelHandler oldBukkitEncodeHandler;
    public final List<Object> customEncoders = new ArrayList<>();
    public final MessageToByteEncoder<?> minecraftEncoder;
    private final Object userInfo;

    public CustomBukkitEncodeHandlerModern(Object userInfo, MessageToByteEncoder<?> minecraftEncoder, ChannelHandler oldBukkitEncodeHandler) {
        this.userInfo = userInfo;
        this.minecraftEncoder = minecraftEncoder;
        this.oldBukkitEncodeHandler = oldBukkitEncodeHandler;
    }

    public void addCustomEncoder(Object customEncoder) {
        customEncoders.add(customEncoder);
    }

    public <T> T getCustomEncoder(Class<T> clazz) {
        for (Object customEncoder : customEncoders) {
            if (customEncoder.getClass().equals(clazz)) {
                return (T) customEncoder;
            }
        }
        return null;
    }

    public Object getCustomEncoderByName(String simpleName) {
        for (Object customEncoder : customEncoders) {
            if (ClassUtil.getClassSimpleName(customEncoder.getClass()).equals(simpleName)) {
                return customEncoder;
            }
        }
        return null;
    }

    @Override
    public void transform(ByteBuf bytebuf) throws Exception {
        if (!ViaVersionUtil.checkClientboundPacketUserConnection(userInfo))
            throw ViaVersionUtil.throwCancelEncoderException(null);
        if (!ViaVersionUtil.isUserConnectionActive(userInfo)) return;
        ViaVersionUtil.transformPacket(userInfo, bytebuf, false);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, ByteBuf byteBuf) throws Exception {
        if (versionField != null) {
            versionField.set(minecraftEncoder, versionField.get(this));
        }

        if (!(o instanceof ByteBuf)) {
            try {
                CustomPipelineUtil.callEncode(minecraftEncoder, new ChannelHandlerContextWrapper(ctx, this), o, byteBuf);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof Exception) {
                    throw (Exception) e.getCause();
                } else if (e.getCause() instanceof Error) {
                    throw (Error) e.getCause();
                }
            }
        } else {
            byteBuf.writeBytes((ByteBuf) o);
        }
        ByteBuf transformed = ctx.alloc().buffer().writeBytes(byteBuf);
        //Call our custom encoders before ViaVersion translates the packet.
        for (Object customEncoder : customEncoders) {
            if (customEncoder instanceof MessageToByteEncoder) {
                CustomPipelineUtil.callEncode((MessageToByteEncoder<?>) customEncoder, new ChannelHandlerContextWrapper(ctx, this), transformed, byteBuf);
                transformed.clear().writeBytes(byteBuf);
            } else if (customEncoder instanceof MessageToMessageEncoder) {
                ByteBuf bb = (ByteBuf) CustomPipelineUtil.callEncode((MessageToMessageEncoder<?>) customEncoder, new ChannelHandlerContextWrapper(ctx, this), transformed).get(0);
                transformed.clear().writeBytes(bb);
            }
        }
        //Let ViaVersion translate our potentially modified version of the packet.
        transform(transformed);
        byteBuf.clear().writeBytes(transformed);
        transformed.release();
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
