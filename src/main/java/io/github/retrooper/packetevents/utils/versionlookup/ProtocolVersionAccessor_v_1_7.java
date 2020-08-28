/*
 * Copyright (c) 2020 retrooper
 */

package io.github.retrooper.packetevents.utils.versionlookup;

import org.bukkit.entity.Player;

public class ProtocolVersionAccessor_v_1_7 {
    public static int getProtocolVersion(Player player) {
        net.minecraft.server.v1_7_R4.EntityPlayer entityPlayer = ((org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer) player).getHandle();
        return entityPlayer.playerConnection.networkManager.getVersion();
    }
}
