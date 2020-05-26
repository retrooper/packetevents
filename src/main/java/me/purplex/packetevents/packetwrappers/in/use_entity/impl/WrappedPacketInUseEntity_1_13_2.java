package me.purplex.packetevents.packetwrappers.in.use_entity.impl;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_13_R2.Entity;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import net.minecraft.server.v1_13_R2.PacketPlayInUseEntity;
import net.minecraft.server.v1_13_R2.WorldServer;
class WrappedPacketInUseEntity_1_13_2 extends WrappedVersionPacket {

    private org.bukkit.entity.Entity entity;
    private EntityUseAction action;

    WrappedPacketInUseEntity_1_13_2(Object packet) {
        super(packet);
    }

    public org.bukkit.entity.Entity getEntity() {
        return entity;
    }

    public EntityUseAction getEntityUseAction() {
        return action;
    }

    @Override
    protected void setup() {
        setupEntity();
        setupAction();
    }

    private void setupEntity() {
        PacketPlayInUseEntity p = (PacketPlayInUseEntity) packet;
        WorldServer worldServer = null;
        for (World bukkitWorld : Bukkit.getWorlds()) {
            CraftWorld craftWorld = (CraftWorld) bukkitWorld;
            worldServer = craftWorld.getHandle();
            if (p.a(worldServer) != null) {
                break;
            }
        }
        Entity entity = p.a(worldServer);
        this.entity = entity.getBukkitEntity();

    }

    private void setupAction() {
        PacketPlayInUseEntity p = (PacketPlayInUseEntity) packet;
        String name = p.b().name();
        this.action = EntityUseAction.valueOf(name);
    }
}
