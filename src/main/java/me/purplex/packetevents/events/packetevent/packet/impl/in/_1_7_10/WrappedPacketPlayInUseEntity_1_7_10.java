package me.purplex.packetevents.events.packetevent.packet.impl.in._1_7_10;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.ServerVersion;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import net.minecraft.server.v1_7_R4.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

public class WrappedPacketPlayInUseEntity_1_7_10 {
    private final ServerVersion version;
    private org.bukkit.entity.Entity entity;
    private EntityUseAction action;
    private final Object packet;

    public WrappedPacketPlayInUseEntity_1_7_10(Object packet) {
        this.version = ServerVersion.getVersion();
        this.packet = packet;
        setupEntity();
        setupAction();
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

    private void setupEntity() {
        if (version == ServerVersion.v_1_8) {
            PacketPlayInUseEntity packet = (PacketPlayInUseEntity) getRawPacket();
            WorldServer worldServer = null;
            for (World bukkitWorld : Bukkit.getWorlds()) {
                CraftWorld craftWorld = (CraftWorld) bukkitWorld;
                worldServer = craftWorld.getHandle();
                if (packet.a(worldServer) != null) {
                    break;
                }
            }
            Entity entity = packet.a(worldServer);
            this.entity = entity.getBukkitEntity();
        }
    }

    private void setupAction() {
        PacketPlayInUseEntity packet = (PacketPlayInUseEntity) getRawPacket();
        String name = packet.c().name();
        this.action = EntityUseAction.valueOf(name);
    }
}
