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

import com.github.retrooper.packetevents.netty.channel.ChannelHandlerContextAbstract;
import com.github.retrooper.packetevents.netty.channel.pipeline.ChannelPipelineAbstract;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CustomPipelineUtil {
    private static Method DECODE_METHOD;
    private static Method ENCODE_METHOD;
    private static Method MTM_DECODE;
    private static Method MTM_ENCODE;

    static {
        try {
            Class<?> byteToMessageDecoderClass =
                    SpigotReflectionUtil.getNettyClass("handler.codec.ByteToMessageDecoder");
            DECODE_METHOD = byteToMessageDecoderClass.getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class);
            DECODE_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            Class<?> messageToByteEncoderClass =
                    SpigotReflectionUtil.getNettyClass("handler.codec.MessageToByteEncoder");
            ENCODE_METHOD = messageToByteEncoderClass.getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, ByteBuf.class);
            ENCODE_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            Class<?> messageToMessageDecoderClass =
                    SpigotReflectionUtil.getNettyClass("handler.codec.MessageToMessageDecoder");
            MTM_DECODE = messageToMessageDecoderClass.getDeclaredMethod("decode", ChannelHandlerContext.class, Object.class, List.class);
            MTM_DECODE.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            Class<?> messageToMessageEncoderClass =
                    SpigotReflectionUtil.getNettyClass("handler.codec.MessageToMessageEncoder");
            MTM_ENCODE = messageToMessageEncoderClass.getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, List.class);
            MTM_ENCODE.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call the decode method on a netty ByteToMessageDecoder
     *
     * @param decoder The decoder
     * @param ctx     The current context
     * @param input   The packet to decode
     * @return A list of the decoders output
     * @throws InvocationTargetException If an exception happens while executing
     */
    public static List<Object> callDecode(Object decoder, Object ctx, Object input) throws InvocationTargetException {
        List<Object> output = new ArrayList<>();
        try {
            CustomPipelineUtil.DECODE_METHOD.invoke(decoder, ctx, input, output);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Call the encode method on a netty MessageToByteEncoder
     *
     * @param encoder The encoder
     * @param ctx     The current context
     * @param msg     The packet to encode
     * @param output  The bytebuf to write the output to
     * @throws InvocationTargetException If an exception happens while executing
     */
    public static void callEncode(Object encoder, Object ctx, Object msg, Object output) throws InvocationTargetException {
        try {
            CustomPipelineUtil.ENCODE_METHOD.invoke(encoder, ctx, msg, output);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static List<Object> callMTMEncode(Object encoder, Object ctx, Object msg) {
        List<Object> output = new ArrayList<>();
        try {
            MTM_ENCODE.invoke(encoder, ctx, msg, output);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static List<Object> callMTMDecode(Object decoder, Object ctx, Object msg) throws InvocationTargetException {
        List<Object> output = new ArrayList<>();
        try {
            MTM_DECODE.invoke(decoder, ctx, msg, output);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Check if a stack trace contains a certain exception
     *
     * @param t The throwable
     * @param c The exception to look for
     * @return True if the stack trace contained it as its cause or if t is an instance of c.
     */
    public static boolean containsCause(Throwable t, Class<?> c) {
        while (t != null) {
            if (c.isAssignableFrom(t.getClass())) {
                return true;
            }

            t = t.getCause();
        }
        return false;
    }


    public static ChannelHandlerContextAbstract getNextContext(String name, ChannelPipelineAbstract pipeline) {
        boolean mark = false;
        for (String s : pipeline.names()) {
            if (mark) {
                return pipeline.context(pipeline.get(s));
            }
            if (s.equals(name))
                mark = true;
        }
        return null;
    }

    public static ChannelHandlerContextAbstract getPreviousContext(String name, ChannelPipelineAbstract pipeline) {
        String previous = null;
        for (String entry : pipeline.toMap().keySet()) {
            if (entry.equals(name)) {
                return pipeline.context(previous);
            }
            previous = entry;
        }
        return null;
    }
}

