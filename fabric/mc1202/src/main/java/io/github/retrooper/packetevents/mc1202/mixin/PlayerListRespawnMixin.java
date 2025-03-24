package io.github.retrooper.packetevents.mc1202.mixin;

import com.github.retrooper.packetevents.PacketEvents;
import io.netty.channel.Channel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListRespawnMixin {
    /**
     * @reason Minecraft creates a new player instance on respawn
     */
    // Intermediary name for respawnPlayer() is method_14556
    // We use this and disable remapping because the method signature changes in 1.21
    // This allows the same code to inject into (in theory) very version and
    @Dynamic
    @Inject(
            method = "method_14556*",
            at = @At("RETURN"),
            remap = false
    )
    private void postRespawn(CallbackInfoReturnable<ServerPlayer> cir) {
        ServerPlayer player = cir.getReturnValue();
        Channel channel = player.connection.connection.channel;
        PacketEvents.getAPI().getInjector().setPlayer(channel, player);
    }
}


