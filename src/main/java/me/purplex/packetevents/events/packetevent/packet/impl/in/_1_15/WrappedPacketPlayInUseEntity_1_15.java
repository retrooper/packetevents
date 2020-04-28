package me.purplex.packetevents.events.packetevent.packet.impl.in._1_15;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.ServerVersion;
import net.minecraft.server.v1_15_R1.PacketPlayInUseEntity;
import net.minecraft.server.v1_15_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Entity;

public class WrappedPacketPlayInUseEntity_1_15 {
    private final ServerVersion version;
    private Entity entity;
    private EntityUseAction action;
    private final Object packet;

    public WrappedPacketPlayInUseEntity_1_15(Object packet) {
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
        PacketPlayInUseEntity packet = (PacketPlayInUseEntity) getRawPacket();
        WorldServer worldServer = null;
        for (World bukkitWorld : Bukkit.getWorlds()) {
            CraftWorld craftWorld = (CraftWorld) bukkitWorld;
            worldServer = craftWorld.getHandle();
            if (packet.a(worldServer) != null) {
                break;
            }
        }
        net.minecraft.server.v1_15_R1.Entity entity = packet.a(worldServer);
        this.entity = entity.getBukkitEntity();

    }

    private void setupAction() {
        PacketPlayInUseEntity packet = (PacketPlayInUseEntity) getRawPacket();
        String name = packet.b().name();
        this.action = EntityUseAction.valueOf(name);
    }
}
