/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.mc1201.mixin;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.util.FabricInjectionUtil;
import io.netty.channel.Channel;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(
        require = {
                @Condition(value = "minecraft", versionPredicates = {"<1.20.2"}),
        }
)
@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    /**
     * @reason Associate connection instance with player instance
     */
    @Inject(
            method = "placeNewPlayer",
            at = @At("HEAD")
    )
    private void preNewPlayerPlace(
            Connection connection, ServerPlayer player,
            CallbackInfo ci
    ) {
        PacketEvents.getAPI().getInjector().setPlayer(connection.channel, player);
    }

    /**
     * @reason Associate connection instance with player instance and handle login event
     */
    @Inject(
            method = "placeNewPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onPlayerLogin(
        Connection connection, ServerPlayer player,
        CallbackInfo ci
    ) {
        FabricInjectionUtil.fireUserLoginEvent(player);
    }

    /**
     * @reason Minecraft creates a new player instance on respawn
     */
    @Inject(
            method = "respawn",
            at = @At("RETURN")
    )
    private void postRespawn(CallbackInfoReturnable<ServerPlayer> cir) {
        ServerPlayer player = cir.getReturnValue();
        Channel channel = player.connection.connection.channel;
        PacketEvents.getAPI().getInjector().setPlayer(channel, player);
    }
}
