package io.github.retrooper.packetevents.mc1202.mixin;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsAPI;
import io.netty.channel.Channel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListRespawnMixin {
    /** Handles grabbing new player object on respawns for 1.20.2+, a separate mixin is required
     * because the location of the field player.connection.connection changes
     * from inheritance ServerGamePacketListenerImpl <- ServerCommonPacketListenerImpl thus breaking intermediary compatability
     *
     * @reason Minecraft creates a new player instance on respawn
     */
    @Inject(
            method = "respawn*",
            at = @At("RETURN"),
            require = 1
    )
    private void postRespawn(CallbackInfoReturnable<ServerPlayer> cir) {
        ServerPlayer player = cir.getReturnValue();
        Channel channel = player.connection.connection.channel;
        FabricPacketEventsAPI.getServerAPI().getInjector().setPlayer(channel, player);
    }
}


