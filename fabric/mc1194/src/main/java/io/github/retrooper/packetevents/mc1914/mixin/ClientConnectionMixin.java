package io.github.retrooper.packetevents.mc1914.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.retrooper.packetevents.util.FabricInjectionUtil;
import io.netty.channel.ChannelPipeline;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientConnection.class, priority = 1500) // priority to inject after Via
@Restriction(
        require = {
                @Condition(value = "minecraft", versionPredicates = {">1.19.3"}),
        }
)
public class ClientConnectionMixin {
    @Inject(
            method = "addHandlers*",
            at = @At("TAIL"),
            require = 1
    )
    private static void addHandlers(
            CallbackInfo ci,
            @Local(ordinal = 0, argsOnly = true) ChannelPipeline pipeline,
            @Local(ordinal = 0, argsOnly = true) NetworkSide flow
    ) {
        FabricInjectionUtil.injectAtPipelineBuilder(pipeline, flow);
    }
}