package io.github.retrooper.packetevents.mixin;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(MessageToMessageDecoder.class)
public interface MessageToMessageDecoderInvoker<I> {
    @Invoker("decode")
    void invokeDecode(ChannelHandlerContext ctx, I msg, List<Object> out);
}
