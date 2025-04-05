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
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.ClientConnection;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
@Restriction(
        require = {
                @Condition(value = "minecraft", versionPredicates = {"<1.20.2"}),
        }
)
public abstract class ClientPlayerNetworkHandlerMixin {
    @Shadow @Final public ClientConnection connection;

    /**
     * @reason Associate connection instance with player instance
     */
    @Inject(
            method = "onGameJoin",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/client/MinecraftClient;player:Lnet/minecraft/client/network/ClientPlayerEntity;",
                    shift = At.Shift.AFTER
            )
    )
    private void postLoginPlayerConstruct(CallbackInfo ci) {
        FabricPacketEventsAPI.getClientAPI().getInjector().setPlayer(this.connection.channel, MinecraftClient.getInstance().player);
    }

    /**
     * @reason Minecraft creates a new player instance on respawn
     */
    @Inject(
            method = "onPlayerRespawn",
            at = @At(
                    // inject immediately after new player instance has been created
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayerEntity;setEntityId(I)V"
            )
    )
    private void postRespawnPlayerConstruct(CallbackInfo ci, @Local(ordinal = 1) ClientPlayerEntity player) {
        FabricPacketEventsAPI.getClientAPI().getInjector().setPlayer(this.connection.channel, player);
    }
}
