package io.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.util.reflection.Reflection;
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

    public static void init() {
        try {
            Class<?> compressionDecoderClass = Reflection.getClassByNameWithoutException("net.minecraft.network.CompressionDecoder");
            DECODE_METHOD = compressionDecoderClass.getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class);
            DECODE_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            Class<?> compressionDecoderClass = Reflection.getClassByNameWithoutException("net.minecraft.network.CompressionEncoder");
            ENCODE_METHOD = compressionDecoderClass.getDeclaredMethod("encode", ChannelHandlerContext.class, ByteBuf.class, ByteBuf.class);
            ENCODE_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            Class<?> messageToMessageDecoderClass = Reflection.getClassByNameWithoutException("io.netty.handler.codec.MessageToMessageDecoder");
            MTM_DECODE = messageToMessageDecoderClass.getDeclaredMethod("decode", ChannelHandlerContext.class, Object.class, List.class);
            MTM_DECODE.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            Class<?> messageToMessageDecoderClass = Reflection.getClassByNameWithoutException("io.netty.handler.codec.MessageToMessageEncoder");
            MTM_ENCODE = messageToMessageDecoderClass.getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, List.class);
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
