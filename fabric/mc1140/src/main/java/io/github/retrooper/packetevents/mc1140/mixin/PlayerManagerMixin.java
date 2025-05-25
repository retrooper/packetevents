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

package io.github.retrooper.packetevents.mc1140.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsAPI;
import io.github.retrooper.packetevents.util.FabricInjectionUtil;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    /**
     * @reason Associate connection instance with player instance
     */
    @Inject(
            method = "onPlayerConnect*",
            at = @At("HEAD")
    )
    private void onPlayerConnect(
            CallbackInfo ci,
            @Local(ordinal = 0, argsOnly = true) ClientConnection connection,
            @Local(ordinal = 0, argsOnly = true) ServerPlayerEntity player
    ) {
        FabricPacketEventsAPI.getServerAPI().getInjector().setPlayer(connection.channel, player);
    }

    /**
     * @reason Associate connection instance with player instance and handle login event
     */
    @Inject(
            method = "onPlayerConnect*",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;sendToAll(Lnet/minecraft/network/Packet;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onPlayerLogin(
        CallbackInfo ci,
        @Local(ordinal = 0, argsOnly = true) ServerPlayerEntity player
    ) {
        FabricInjectionUtil.fireUserLoginEvent(player);
    }
}
