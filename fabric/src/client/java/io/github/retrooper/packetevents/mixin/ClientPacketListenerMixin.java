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

package io.github.retrooper.packetevents.mixin;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.FakeChannelUtil;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.retrooper.packetevents.PacketEventsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl {

    public ClientPacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie cookie) {
        super(minecraft, connection, cookie); // dummy ctor
    }

    @Inject(
            method = "handleLogin",
            at = @At(value = "HEAD")
    )
    private void preLoginPlayerThreadSwitch(CallbackInfo ci) {
        if (PacketEventsMod.isOurConnection(this.connection)) {
            // pause reading until LocalPlayer instance has been constructed (see method below)
            this.connection.channel.config().setAutoRead(false);
        }
    }

    /**
     * @reason Associate connection instance with player instance
     */
    @Inject(
            method = "handleLogin",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/client/Minecraft;player:Lnet/minecraft/client/player/LocalPlayer;",
                    shift = At.Shift.AFTER
            )
    )
    private void postLoginPlayerConstruct(CallbackInfo ci) {
        if (PacketEventsMod.isOurConnection(this.connection)) {
            PacketEvents.getAPI().getInjector().setPlayer(this.connection.channel, this.minecraft.player);
            this.connection.channel.config().setAutoRead(true);
        }
    }

    /**
     * @reason Call login event and verify injection
     */
    @Inject(
            method = "handleLogin",
            at = @At("TAIL")
    )
    private void postLoginPacket(CallbackInfo ci) {
        if (!PacketEventsMod.isOurConnection(this.connection)) {
            return;
        }

        PacketEventsAPI<?> api = PacketEvents.getAPI();
        User user = api.getProtocolManager().getUser(this.connection.channel);
        if (user == null) {
            // Check if it's a fake connection
            if (!FakeChannelUtil.isFakeChannel(this.connection.channel) &&
                    (!api.isTerminated() || api.getSettings().isKickIfTerminated())) {
                // Kick the player if they're not a fake player
                this.connection.disconnect(Component.literal("PacketEvents failed to inject into a channel."));
            }
            return;
        }

        api.getEventManager().callEvent(new UserLoginEvent(user, this.minecraft.player));
    }

    /**
     * @reason Minecraft creates a new player instance on respawn
     */
    @Inject(
            method = "handleRespawn",
            at = {
                    @At(
                            // inject immediately after new player instance has been created (pre 1.21.9)
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;startWaitingForNewLevel(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/gui/screens/ReceivingLevelScreen$Reason;)V"
                    ),
                    @At(
                            // inject immediately after new player instance has been created (post 1.21.9)
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;startWaitingForNewLevel(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/gui/screens/LevelLoadingScreen$Reason;)V"
                    )
            },
            expect = 1
    )
    private void postRespawnPlayerConstruct(CallbackInfo ci, @Local(ordinal = 1) LocalPlayer player) {
        if (PacketEventsMod.isOurConnection(this.connection)) {
            PacketEvents.getAPI().getInjector().setPlayer(this.connection.channel, player);
        }
    }
}
