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

package io.github.retrooper.packetevents.handlers.modern;

import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.lang.reflect.Method;
import java.util.List;

public class WrappedVanillaDecoderModern extends ByteToMessageDecoder {
    private final ByteToMessageDecoder vanillaDecoder;
    private static Method DECODE_METHOD;

    public WrappedVanillaDecoderModern(ByteToMessageDecoder vanillaDecoder) {
        this.vanillaDecoder = vanillaDecoder;
        if (DECODE_METHOD == null) {
            DECODE_METHOD = Reflection.getMethod(vanillaDecoder.getClass(), "decode", 0);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> output) throws Exception {
        DECODE_METHOD.invoke(vanillaDecoder, ctx, byteBuf, output);
    }
}
