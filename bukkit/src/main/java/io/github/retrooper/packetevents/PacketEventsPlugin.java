/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.factory.bukkit.PacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEventsPlugin extends JavaPlugin {
    @Override
    public void onLoad() {
        PacketEvents.setAPI(PacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        //Register your listeners
        PacketEvents.getAPI().init();
        net.minecraft.server.v1_16_R2.PacketPlayOutNamedEntitySpawn a1;
        net.minecraft.server.v1_15_R1.PacketPlayOutNamedEntitySpawn a2;
        net.minecraft.server.v1_14_R1.PacketPlayOutNamedEntitySpawn a3;
        net.minecraft.server.v1_13_R2.PacketPlayOutNamedEntitySpawn a4;
        net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn a5;
        net.minecraft.server.v1_11_R1.PacketPlayOutNamedEntitySpawn a6;
        net.minecraft.server.v1_10_R1.PacketPlayOutNamedEntitySpawn a7;
        net.minecraft.server.v1_9_R1.PacketPlayOutNamedEntitySpawn a8;
        net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn a9;
        net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn a10;
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}