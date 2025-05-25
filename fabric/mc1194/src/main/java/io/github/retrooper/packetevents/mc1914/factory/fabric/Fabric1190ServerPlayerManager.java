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

package io.github.retrooper.packetevents.mc1914.factory.fabric;

import com.github.retrooper.packetevents.PacketEventsAPI;
import io.github.retrooper.packetevents.mc1140.factory.fabric.Fabric1140ServerPlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class Fabric1190ServerPlayerManager extends Fabric1140ServerPlayerManager {

    public Fabric1190ServerPlayerManager(PacketEventsAPI<?> packetEventsAPI) {
        super(packetEventsAPI);
    }

    // new TextComponent -> Text.literal in 1.19
    @Override
    public void disconnectPlayer(ServerPlayerEntity serverPlayerEntity, String message) {
        serverPlayerEntity.networkHandler.disconnect(Text.literal(message));
    }
}
