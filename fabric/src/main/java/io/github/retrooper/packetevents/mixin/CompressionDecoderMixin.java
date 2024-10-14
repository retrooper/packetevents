package io.github.retrooper.packetevents.mixin;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import net.minecraft.network.CompressionDecoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CompressionDecoder.class)
public interface CompressionDecoderMixin {
  @Invoker(value = "decode") void packetevents_decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out);
}
