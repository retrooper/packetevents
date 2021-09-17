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
import com.viaversion.viaversion.bukkit.handlers.BukkitDecodeHandler;
import com.viaversion.viaversion.bukkit.platform.BukkitViaInjector;
import com.viaversion.viaversion.exception.CancelDecoderException;
import com.viaversion.viaversion.exception.CancelEncoderException;
import com.viaversion.viaversion.util.PipelineUtil;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.ReflectionObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ViaVersionAccessorImpl implements ViaVersionAccessor {
    @Override
    public int getProtocolVersion(Player player) {
        return Via.getAPI().getPlayerVersion(player);
    }

    public List<Object> callDecoder(Object decoder, ChannelHandlerContext ctx, Object input) {
        try {
            if (decoder instanceof ByteToMessageDecoder) {
                return PipelineUtil.callDecode((ByteToMessageDecoder) decoder, ctx, input);
            } else if (decoder instanceof MessageToMessageDecoder) {
                return PipelineUtil.callDecode((MessageToMessageDecoder) decoder, ctx, input);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Exception throwCancelDecoderException(Throwable throwable) {
        return CancelDecoderException.generate(throwable);
    }

    @Override
    public Exception throwCancelEncoderException(Throwable throwable) {
        return CancelEncoderException.generate(throwable);
    }

    @Override
    public void transformPacket(Object userConnectionObj, Object byteBufObj, boolean clientSide) {
        UserConnection userConnection = (UserConnection) userConnectionObj;
        ByteBuf byteBuf = (ByteBuf) byteBufObj;

        try {
            if (clientSide) {
                userConnection.transformServerbound(byteBuf, CancelDecoderException::generate);
            } else {
                userConnection.transformClientbound(byteBuf, CancelEncoderException::generate);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setUserConnectionActive(Object userConnectionObj, boolean active) {
        ((UserConnection)userConnectionObj).setActive(active);
    }

    @Override
    public boolean isUserConnectionActive(Object userConnectionObj) {
        return ((UserConnection)userConnectionObj).isActive();
    }

    @Override
    public boolean checkServerboundPacketUserConnection(Object userConnectionObj) {
        return ((UserConnection)userConnectionObj).checkServerboundPacket();
    }

    @Override
    public Class<?> getUserConnectionClass() {
        return UserConnection.class;
    }
}
