package me.purplex.packetevents.packetwrappers.in._1_7_10;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedVersionPacket;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;
import net.minecraft.server.v1_7_R4.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

public class WrappedPacketPlayInUseEntity_1_7_10 extends WrappedVersionPacket {
    private org.bukkit.entity.Entity entity;
    private EntityUseAction action;

    public WrappedPacketPlayInUseEntity_1_7_10(Object packet) {
        super(packet);
        version = ServerVersion.getVersion();
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
        Entity entity = packet.a(worldServer);
        this.entity = entity.getBukkitEntity();
    }

    private void setupAction() {
        PacketPlayInUseEntity packet = (PacketPlayInUseEntity) getRawPacket();
        String name = packet.c().name();
        this.action = EntityUseAction.valueOf(name);
    }
}
