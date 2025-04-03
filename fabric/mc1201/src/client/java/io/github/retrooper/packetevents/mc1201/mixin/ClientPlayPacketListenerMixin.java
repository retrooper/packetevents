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

import com.llamalad7.mixinextras.sugar.Local;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsAPI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayPacketListener.class)
public abstract class ClientPlayPacketListenerMixin implements TickablePacketListener, ClientPlayPacketListener {

    @Shadow public abstract ClientConnection getConnection();
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
        FabricPacketEventsAPI.getClientAPI().getInjector().setPlayer(this.getConnection().channel, MinecraftClient.getInstance().player);
    }

    /**
     * @reason Minecraft creates a new player instance on respawn
     */
    @Inject(
            method = "onPlayerRespawn",
            at = @At(
                    // inject immediately after new player instance has been created
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;setId(I)V"
            )
    )
    private void postRespawnPlayerConstruct(CallbackInfo ci, @Local(ordinal = 1) ClientPlayerEntity player) {
        FabricPacketEventsAPI.getClientAPI().getInjector().setPlayer(this.getConnection().channel, player);
    }
}
