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

package io.github.retrooper.packetevents.util.viaversion;

import io.github.retrooper.packetevents.util.SpigotReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CustomPipelineUtil {
    private static Method DECODE_METHOD;
    private static Method ENCODE_METHOD;
    private static Method MTM_DECODE;
    private static Method MTM_ENCODE;

    public static void init() {
        Class<?> channelHandlerContextClass = SpigotReflectionUtil.getNettyClass("channel.ChannelHandlerContext");
        try {
            DECODE_METHOD = SpigotReflectionUtil.BYTE_TO_MESSAGE_DECODER.getDeclaredMethod("decode", channelHandlerContextClass, SpigotReflectionUtil.BYTE_BUF_CLASS, List.class);
            DECODE_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            ENCODE_METHOD = SpigotReflectionUtil.MESSAGE_TO_BYTE_ENCODER.getDeclaredMethod("encode", channelHandlerContextClass, Object.class, SpigotReflectionUtil.BYTE_BUF_CLASS);
            ENCODE_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            Class<?> messageToMessageDecoderClass =
                    SpigotReflectionUtil.getNettyClass("handler.codec.MessageToMessageDecoder");
            MTM_DECODE = messageToMessageDecoderClass.getDeclaredMethod("decode", channelHandlerContextClass, Object.class, List.class);
            MTM_DECODE.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            Class<?> messageToMessageEncoderClass =
                    SpigotReflectionUtil.getNettyClass("handler.codec.MessageToMessageEncoder");
            MTM_ENCODE = messageToMessageEncoderClass.getDeclaredMethod("encode", channelHandlerContextClass, Object.class, List.class);
            MTM_ENCODE.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static List<Object> callDecode(Object decoder, Object ctx, Object input) throws InvocationTargetException {
        List<Object> output = new ArrayList<>();
        try {
            CustomPipelineUtil.DECODE_METHOD.invoke(decoder, ctx, input, output);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return output;
    }

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
}

