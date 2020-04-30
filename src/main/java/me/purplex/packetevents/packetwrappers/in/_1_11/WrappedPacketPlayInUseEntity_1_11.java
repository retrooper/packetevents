package me.purplex.packetevents.packetwrappers.in._1_11;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrappedPacketPlayInUseEntity_1_11 {
    private final ServerVersion version;
    private Entity entity;
    private EntityUseAction action;
    private final Object packet;

    public WrappedPacketPlayInUseEntity_1_11(Object packet) {
        this.version = ServerVersion.getVersion();
        this.packet = packet;
        setupEntity();
        setupAction();
    }

    private Object getRawPacket() {
        return this.packet;
    }

    public Entity getEntity() {
        return entity;
    }

    public EntityUseAction getEntityUseAction() {
        return action;
    }

    private void setupEntity() {
        net.minecraft.server.v1_11_R1.PacketPlayInUseEntity packet = (net.minecraft.server.v1_11_R1.PacketPlayInUseEntity) getRawPacket();
        net.minecraft.server.v1_11_R1.WorldServer worldServer = null;
        for (World bukkitWorld : Bukkit.getWorlds()) {
            org.bukkit.craftbukkit.v1_11_R1.CraftWorld craftWorld = (org.bukkit.craftbukkit.v1_11_R1.CraftWorld) bukkitWorld;
            worldServer = craftWorld.getHandle();
            if (packet.a(worldServer) != null) {
                break;
            }
        }
        net.minecraft.server.v1_11_R1.Entity entity = packet.a(worldServer);
        this.entity = entity.getBukkitEntity();

    }

    private void setupAction() {
        net.minecraft.server.v1_11_R1.PacketPlayInUseEntity packet = (net.minecraft.server.v1_11_R1.PacketPlayInUseEntity) getRawPacket();
        String name = packet.a().name();
        this.action = EntityUseAction.valueOf(name);
    }
}
