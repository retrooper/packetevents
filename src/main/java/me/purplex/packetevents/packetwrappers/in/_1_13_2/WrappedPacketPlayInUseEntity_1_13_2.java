package me.purplex.packetevents.packetwrappers.in._1_13_2;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.Entity;
import net.minecraft.server.v1_13_R2.PacketPlayInUseEntity;
import net.minecraft.server.v1_13_R2.WorldServer;
public class WrappedPacketPlayInUseEntity_1_13_2 {
    private final ServerVersion version;
    private Entity entity;
    private EntityUseAction action;
    private final Object packet;

    public WrappedPacketPlayInUseEntity_1_13_2(Object packet) {
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
        PacketPlayInUseEntity packet = (net.minecraft.server.v1_13_R2.PacketPlayInUseEntity) getRawPacket();
        WorldServer worldServer = null;
        for (World bukkitWorld : Bukkit.getWorlds()) {
            CraftWorld craftWorld = (CraftWorld) bukkitWorld;
            worldServer = craftWorld.getHandle();
            if (packet.a(worldServer) != null) {
                break;
            }
        }
        net.minecraft.server.v1_13_R2.Entity entity = packet.a(worldServer);
        this.entity = entity.getBukkitEntity();

    }

    private void setupAction() {
        PacketPlayInUseEntity packet = (PacketPlayInUseEntity) getRawPacket();
        String name = packet.b().name();
        this.action = EntityUseAction.valueOf(name);
    }
}
