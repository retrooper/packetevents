package io.github.retrooper.packetevents.mc1140.mixin;

import io.github.retrooper.packetevents.util.FabricInjectionUtil;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.network.NetworkSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.server.ServerNetworkIo$1", priority = 1500) // Priority of 1500 to Inject after via
@Restriction(
        require = {
                @Condition(value = "minecraft", versionPredicates = {"<1.19.4"}),
        }
)
public class MixinServerNetworkIoChInit {
    @Inject(method = "initChannel", at = @At(value = "TAIL"), remap = false)
    private void onInitChannel(Channel channel, CallbackInfo ci) {
        if (channel instanceof SocketChannel) {
            FabricInjectionUtil.injectAtPipelineBuilder(channel.pipeline(), NetworkSide.SERVERBOUND);
        }
    }
}
