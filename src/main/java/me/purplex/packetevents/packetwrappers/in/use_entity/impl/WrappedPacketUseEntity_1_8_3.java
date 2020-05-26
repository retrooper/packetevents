package me.purplex.packetevents.packetwrappers.in.use_entity.impl;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.packetwrappers.api.version.WrappedVersionPacket;
import net.minecraft.server.v1_8_R2.Entity;
import net.minecraft.server.v1_8_R2.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R2.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;

public class WrappedPacketUseEntity_1_8_3 extends WrappedVersionPacket {
    private org.bukkit.entity.Entity entity;
    private EntityUseAction action;

    public WrappedPacketUseEntity_1_8_3(Object packet) {
        super(packet);
    }

    private Object getRawPacket() {
        return this.packet;
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
        PacketPlayInUseEntity packet = (PacketPlayInUseEntity) getRawPacket();
        WorldServer worldServer = null;
        for (World bukkitWorld : Bukkit.getWorlds()) {
            CraftWorld craftWorld = (CraftWorld) bukkitWorld;
            worldServer = craftWorld.getHandle();
            if (packet.a(worldServer) != null) {
                break;
            }
        }
        //nms entity
        Entity entity = packet.a(worldServer);

        //bukkit entity
        this.entity = entity.getBukkitEntity();
    }

    private void setupAction() {
        net.minecraft.server.v1_8_R2.PacketPlayInUseEntity packet = (net.minecraft.server.v1_8_R2.PacketPlayInUseEntity) getRawPacket();
        String name = packet.a().name();
        this.action = EntityUseAction.valueOf(name);
    }
}


