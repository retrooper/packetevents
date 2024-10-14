package io.github.retrooper.packetevents.injector;

import io.github.retrooper.packetevents.mixin.CompressionDecoderMixin;
import io.github.retrooper.packetevents.mixin.CompressionEncoderMixin;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class CustomPipelineUtil {

    public static List<Object> callDecode(CompressionDecoderMixin decoder, ChannelHandlerContext ctx, ByteBuf input) {
        List<Object> output = new ArrayList<>();
        decoder.packetevents_decode(ctx, input, output);
        return output;
    }


    public static void callEncode(CompressionEncoderMixin encoder, ChannelHandlerContext ctx, ByteBuf msg, ByteBuf output) {
        encoder.packetevents_encode(ctx, msg, output);
    }
}