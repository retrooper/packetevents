package io.github.retrooper.packetevents.injector;

import io.github.retrooper.packetevents.mixin.CompressionDecoderInvoker;
import io.github.retrooper.packetevents.mixin.CompressionEncoderInvoker;
import io.github.retrooper.packetevents.mixin.MessageToMessageDecoderInvoker;
import io.github.retrooper.packetevents.mixin.MessageToMessageEncoderInvoker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class CustomPipelineUtil {

    public static List<Object> callDecode(Object decoder, ChannelHandlerContext ctx, ByteBuf input) {
        List<Object> output = new ArrayList<>();
        ((CompressionDecoderInvoker) decoder).invokeDecode(ctx, input, output);
        return output;
    }

    public static void callEncode(Object encoder, ChannelHandlerContext ctx, ByteBuf msg, ByteBuf output) {
        ((CompressionEncoderInvoker) encoder).invokeEncode(ctx, msg, output);
    }

    public static List<Object> callMTMEncode(Object encoder, ChannelHandlerContext ctx, Object msg) {
        List<Object> output = new ArrayList<>();
        ((MessageToMessageEncoderInvoker) encoder).invokeEncode(ctx, msg, output);
        return output;
    }

    public static List<Object> callMTMDecode(Object decoder, ChannelHandlerContext ctx, Object msg) {
        List<Object> output = new ArrayList<>();
        ((MessageToMessageDecoderInvoker) decoder).invokeDecode(ctx, msg, output);
        return output;
    }
}
