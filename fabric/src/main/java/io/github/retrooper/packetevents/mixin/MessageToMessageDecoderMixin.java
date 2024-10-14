package io.github.retrooper.packetevents.mixin;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MessageToMessageDecoder.class)
public interface MessageToMessageDecoderMixin {
  @Invoker(value = "decode") void packetevents_decode(ChannelHandlerContext ctx, Object msg, List<Object> out);
}
