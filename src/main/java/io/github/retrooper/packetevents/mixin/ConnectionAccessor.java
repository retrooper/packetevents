package io.github.retrooper.packetevents.mixin;

import io.netty.channel.Channel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(net.minecraft.network.Connection.class)
public interface ConnectionAccessor {
    @Accessor
    Channel getChannel();
}
