package io.github.retrooper.packetevents.mixin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.CompressionDecoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(CompressionDecoder.class)
public interface CompressionDecoderInvoker {
    @Invoker("decode")
    void invokeDecode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list);
}
