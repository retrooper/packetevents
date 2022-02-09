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

package io.github.retrooper.packetevents.utils;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.protocol.world.Location;

public class SpigotDataHelper {
    public static Location fromBukkitLocation(org.bukkit.Location location) {
        return new Location(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public static org.bukkit.Location toBukkitLocation(org.bukkit.World world, Location location) {
        return new org.bukkit.Location(world, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public static PotionType fromBukkitPotionEffectType(org.bukkit.potion.PotionEffectType potionEffectType) {
        return PotionTypes.getById(potionEffectType.getId());
    }

    public static org.bukkit.potion.PotionEffectType toBukkitPotionEffectType(PotionType potionType) {
        return org.bukkit.potion.PotionEffectType.getById(potionType.getId());
    }

    public static GameMode fromBukkitGameMode(org.bukkit.GameMode gameMode) {
        return GameMode.getById(gameMode.getValue());
    }

    public static org.bukkit.GameMode toBukkitGameMode(GameMode gameMode) {
        return org.bukkit.GameMode.getByValue(gameMode.getId());
    }

    //TODO Material conversions

    public static ItemStack fromBukkitItemStack(org.bukkit.inventory.ItemStack itemStack) {
        return SpigotReflectionUtil.decodeBukkitItemStack(itemStack);
    }

    public static org.bukkit.inventory.ItemStack toBukkitItemStack(ItemStack itemStack) {
        return SpigotReflectionUtil.encodeBukkitItemStack(itemStack);
    }
}
