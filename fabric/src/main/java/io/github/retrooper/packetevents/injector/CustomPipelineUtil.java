package io.github.retrooper.packetevents.injector;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import io.netty.handler.codec.DecoderException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import net.minecraft.network.CompressionDecoder;
import net.minecraft.network.CompressionEncoder;

public class CustomPipelineUtil {

    public static List<Object> callDecode(CompressionDecoder decoder, ChannelHandlerContext ctx, ByteBuf input) {
      List<Object> output = new ArrayList<>();
      try {
        decoder.decode(ctx, input, output);
      } catch (Exception e) {
        if (e instanceof DecoderException) {
            throw (DecoderException) e;
        } else if (e instanceof DataFormatException) {
            e.printStackTrace();
        // Impossible to reach state
        } else {
            throw new IllegalStateException(e);
        }
      }
      return output;
    }


    public static void callEncode(CompressionEncoder encoder, ChannelHandlerContext ctx, ByteBuf msg, ByteBuf output) {
        encoder.encode(ctx, msg, output);
    }
}