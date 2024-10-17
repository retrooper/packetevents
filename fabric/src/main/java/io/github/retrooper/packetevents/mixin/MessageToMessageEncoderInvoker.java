package io.github.retrooper.packetevents.mixin;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(MessageToMessageEncoder.class)
public interface MessageToMessageEncoderInvoker<I> {
    @Invoker("encode")
    void invokeEncode(ChannelHandlerContext ctx, I msg, List<Object> out);
}
