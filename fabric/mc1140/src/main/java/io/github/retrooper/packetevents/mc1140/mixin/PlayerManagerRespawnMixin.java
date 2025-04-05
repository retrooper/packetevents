package io.github.retrooper.packetevents.mc1140.mixin;

import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsAPI;
import io.netty.channel.Channel;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(
        require = {
                @Condition(value = "minecraft", versionPredicates = {"<1.15.2"}),
        }
)
@Mixin(PlayerManager.class)
public class PlayerManagerRespawnMixin {
    /** Handles grabbing new player object on respawns for 1.20.1-, a separate mixin is required
     * because the location of the field player.connection.connection changes
     * from inheritance ServerGamePacketListenerImpl -> ServerCommonPacketListenerImpl thus breaking intermediary compatability
     *
     * @reason Minecraft creates a new player instance on respawn
     */
    @Inject(
            method = "respawnPlayer*",
            at = @At("RETURN"),
            require = 1
    )
    private void postRespawn(CallbackInfoReturnable<ServerPlayerEntity> cir) {
        ServerPlayerEntity player = cir.getReturnValue();
        Channel channel = player.networkHandler.client.channel;
        FabricPacketEventsAPI.getServerAPI().getInjector().setPlayer(channel, player);
    }
}
