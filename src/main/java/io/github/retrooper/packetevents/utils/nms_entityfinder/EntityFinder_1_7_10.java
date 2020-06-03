package io.github.retrooper.packetevents.utils.nms_entityfinder;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.*;
import org.bukkit.entity.Entity;

class EntityFinder_1_7_10 {
    public static Entity getEntityById(final World world, final int id) {
        WorldServer worldServer = ((CraftWorld)world).getHandle();
        net.minecraft.server.v1_7_R4.Entity e = worldServer.getEntity(id);
        return e.getBukkitEntity();
    }
}
