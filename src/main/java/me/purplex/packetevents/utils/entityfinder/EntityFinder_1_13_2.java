package me.purplex.packetevents.utils.entityfinder;

import net.minecraft.server.v1_13_R2.WorldServer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.Entity;

class EntityFinder_1_13_2 {
    public static Entity getEntityById(final World world, final int id) {
        WorldServer worldServer = ((CraftWorld) world).getHandle();
        return worldServer.getEntity(id).getBukkitEntity();
    }

}