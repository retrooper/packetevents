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
import io.github.retrooper.packetevents.util.FabricInjectionUtil;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.protocol.PacketFlow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = net.minecraft.network.Connection.class, priority = 1500) // priority to inject after Via
public class ConnectionMixin {

    @Inject(
            method = "configureSerialization*",
            at = @At("TAIL"),
            require = 1
    )
    private static void configureSerialization(
            CallbackInfo ci,
            @Local(ordinal = 0, argsOnly = true) ChannelPipeline pipeline,
            @Local(ordinal = 0, argsOnly = true) PacketFlow flow
    ) {
        FabricInjectionUtil.injectAtPipelineBuilder(pipeline, flow);
    }
}
