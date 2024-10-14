package io.github.retrooper.packetevents.mixin;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MessageToMessageEncoder.class)
public interface MessageToMessageEncoderMixin {
  @Invoker(value = "encode") void packetevents_encode(ChannelHandlerContext ctx, Object msg, List<Object> out);
}
