package me.purplex.packetevents.events.packetevent;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.PlayerAction;
import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.events.PacketEvent;
import me.purplex.packetevents.exceptions.VersionNotFoundException;
import org.bukkit.entity.*;

public class PacketReceiveEvent extends PacketEvent {

    private final Player player;
    private final String name;
    private final Object packet;
    private final long timestamp;
    private boolean cancelled;
    private final ServerVersion version = ServerVersion.getVersion();

    public PacketReceiveEvent(Player player, String packetName, Object packet) {
        this.player = player;
        this.name = packetName;
        this.packet = packet;
        this.timestamp = (System.nanoTime() / 1000000);
        this.cancelled = false;
    }


    /**
     * Get the player sending the packet
     *
     * @return
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the packet's name (NMS packet class simple name)
     *
     * @return
     */
    public String getPacketName() {
        return this.name;
    }

    /**
     * Get the NMS packet object
     *
     * @return
     */
    public Object getPacket() {
        return this.packet;
    }

    /**
     * The time the packet was received(ms)
     *
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Cancel the packet
     *
     * @param cancelled
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Returns if the packet has been cancelled
     *
     * @return cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Get the type of dig
     * PacketPlayInBlockDig
     *
     * @return
     */
    public PlayerDigType getPlayerDigType() throws VersionNotFoundException {
        PlayerDigType type;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.PacketPlayInBlockDig p = (net.minecraft.server.v1_8_R1.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.PacketPlayInBlockDig p = (net.minecraft.server.v1_8_R2.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.PacketPlayInBlockDig p = (net.minecraft.server.v1_8_R3.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.PacketPlayInBlockDig p = (net.minecraft.server.v1_9_R1.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.PacketPlayInBlockDig p = (net.minecraft.server.v1_9_R2.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.PacketPlayInBlockDig p = (net.minecraft.server.v1_10_R1.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        } else if (version == ServerVersion.v_1_11_1) {
            net.minecraft.server.v1_11_R1.PacketPlayInBlockDig p = (net.minecraft.server.v1_11_R1.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        } else if (version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.PacketPlayInBlockDig p = (net.minecraft.server.v1_11_R1.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        } else if (version == ServerVersion.v_1_12_1 || version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.PacketPlayInBlockDig p = (net.minecraft.server.v1_12_R1.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.PacketPlayInBlockDig p = (net.minecraft.server.v1_13_R1.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        }
        else if(version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.PacketPlayInBlockDig p = (net.minecraft.server.v1_13_R2.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        }
        else if (version == ServerVersion.v_1_14) {
            net.minecraft.server.v1_14_R1.PacketPlayInBlockDig p = (net.minecraft.server.v1_14_R1.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        } else if (version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.PacketPlayInBlockDig p = (net.minecraft.server.v1_14_R1.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.PacketPlayInBlockDig p = (net.minecraft.server.v1_15_R1.PacketPlayInBlockDig) packet;
            type = PlayerDigType.valueOf(p.c().name());
        } else {
            throw new VersionNotFoundException();
        }
        return type;
    }

    /**
     * Get the entity interact action
     * PacketPlayInUseEntity
     *
     * @return me.purplex.packetevents.enums.EntityUseAction
     */
    public EntityUseAction getEntityUseAction() throws VersionNotFoundException {
        EntityUseAction entityUseAction;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_8_R1.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.a().name());
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_8_R2.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.a().name());
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_8_R3.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.a().name());
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_9_R1.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.a().name());
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_9_R2.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.a().name());
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_10_R1.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.a().name());
        } else if (version == ServerVersion.v_1_11_1) {
            net.minecraft.server.v1_11_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_11_R1.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.a().name());
        } else if (version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_11_R1.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.a().name());
        } else if (version == ServerVersion.v_1_12_1 || version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_12_R1.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.a().name());
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_13_R1.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.b().name());
        }
        else if(version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_13_R2.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.b().name());
        }
        else if (version == ServerVersion.v_1_14) {
            net.minecraft.server.v1_14_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_14_R1.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.b().name());
        } else if (version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_14_R1.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.b().name());
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_15_R1.PacketPlayInUseEntity) packet;
            entityUseAction = EntityUseAction.valueOf(useEntity.b().name());
        } else {
            throw new VersionNotFoundException();
        }
        return entityUseAction;
    }

    /**
     * Get the PlayerAction
     * PacketPlayInEntityAction
     *
     * @return me.purplex.packetevents.enums.PlayerAction
     */
    public PlayerAction getPlayerAction() throws VersionNotFoundException {
        String animation;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.PacketPlayInEntityAction action = (net.minecraft.server.v1_8_R1.PacketPlayInEntityAction) packet;
            animation = action.b().name();
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.PacketPlayInEntityAction action = (net.minecraft.server.v1_8_R2.PacketPlayInEntityAction) packet;
            animation = action.b().name();
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.PacketPlayInEntityAction action = (net.minecraft.server.v1_8_R3.PacketPlayInEntityAction) packet;
            animation = action.b().name();
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.PacketPlayInEntityAction action = (net.minecraft.server.v1_9_R1.PacketPlayInEntityAction) packet;
            animation = action.b().name();
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.PacketPlayInEntityAction action = (net.minecraft.server.v1_9_R2.PacketPlayInEntityAction) packet;
            animation = action.b().name();
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.PacketPlayInEntityAction action = (net.minecraft.server.v1_10_R1.PacketPlayInEntityAction) packet;
            animation = action.b().name();
        } else if (version == ServerVersion.v_1_11_1) {
            net.minecraft.server.v1_11_R1.PacketPlayInEntityAction action = (net.minecraft.server.v1_11_R1.PacketPlayInEntityAction) packet;
            animation = action.b().name();
        } else if (version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.PacketPlayInEntityAction action = (net.minecraft.server.v1_11_R1.PacketPlayInEntityAction) packet;
            animation = action.b().name();
        } else if (version == ServerVersion.v_1_12_1) {
            net.minecraft.server.v1_12_R1.PacketPlayInEntityAction action = (net.minecraft.server.v1_12_R1.PacketPlayInEntityAction) packet;
            animation = action.b().name();
        } else if (version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.PacketPlayInEntityAction action = (net.minecraft.server.v1_12_R1.PacketPlayInEntityAction) packet;
            animation = action.b().name();
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.PacketPlayInEntityAction action = (net.minecraft.server.v1_13_R1.PacketPlayInEntityAction) packet;
            animation = action.c().name();
        }
        else if(version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.PacketPlayInEntityAction action = (net.minecraft.server.v1_13_R2.PacketPlayInEntityAction) packet;
            animation = action.c().name();
        }
        else if (version == ServerVersion.v_1_14) {
            net.minecraft.server.v1_14_R1.PacketPlayInEntityAction action = (net.minecraft.server.v1_14_R1.PacketPlayInEntityAction) packet;
            animation = action.c().name();
        } else if (version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.PacketPlayInEntityAction action = (net.minecraft.server.v1_14_R1.PacketPlayInEntityAction) packet;
            animation = action.c().name();
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.PacketPlayInEntityAction action = (net.minecraft.server.v1_15_R1.PacketPlayInEntityAction) packet;
            animation = action.c().name();
        } else {
            throw new VersionNotFoundException();
        }
        return PlayerAction.valueOf(animation);
    }

    /**
     * Entity you interacted with
     * PacketPlayInUseEntity
     *
     * @return org.bukkit.entity.Entity
     */
    public Entity getInteractedEntity() throws VersionNotFoundException {
        Entity entity;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_8_R1.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_8_R1.World world = ((org.bukkit.craftbukkit.v1_8_R1.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_8_R2.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_8_R2.World world = ((org.bukkit.craftbukkit.v1_8_R2.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_8_R3.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_8_R3.World world = ((org.bukkit.craftbukkit.v1_8_R3.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_9_R1.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_9_R1.World world = ((org.bukkit.craftbukkit.v1_9_R1.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_9_R2.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_9_R2.World world = ((org.bukkit.craftbukkit.v1_9_R2.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_10_R1.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_10_R1.World world = ((org.bukkit.craftbukkit.v1_10_R1.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        } else if (version == ServerVersion.v_1_11_1) {
            net.minecraft.server.v1_11_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_11_R1.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_11_R1.World world = ((org.bukkit.craftbukkit.v1_11_R1.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        } else if (version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_11_R1.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_11_R1.World world = ((org.bukkit.craftbukkit.v1_11_R1.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        } else if (version == ServerVersion.v_1_12_1 || version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_12_R1.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_12_R1.World world = ((org.bukkit.craftbukkit.v1_12_R1.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_13_R1.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_13_R1.World world = ((org.bukkit.craftbukkit.v1_13_R1.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        }
        else if(version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_13_R2.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_13_R2.World world = ((org.bukkit.craftbukkit.v1_13_R2.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        }
        else if (version == ServerVersion.v_1_14) {
            net.minecraft.server.v1_14_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_14_R1.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_14_R1.World world = ((org.bukkit.craftbukkit.v1_14_R1.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        } else if (version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_14_R1.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_14_R1.World world = ((org.bukkit.craftbukkit.v1_14_R1.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.PacketPlayInUseEntity useEntity = (net.minecraft.server.v1_15_R1.PacketPlayInUseEntity) packet;
            net.minecraft.server.v1_15_R1.World world = ((org.bukkit.craftbukkit.v1_15_R1.CraftWorld) player.getWorld()).getHandle();
            entity = useEntity.a(world).getBukkitEntity();
        } else {
            throw new VersionNotFoundException();
        }
        return entity;
    }

    /**
     * Get the string message
     * PacketPlayInChat
     *
     * @return message
     */
    public String getChatPacketMessage() throws VersionNotFoundException {
        String message;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.PacketPlayInChat p = (net.minecraft.server.v1_8_R1.PacketPlayInChat) packet;
            message = p.a();
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.PacketPlayInChat p = (net.minecraft.server.v1_8_R2.PacketPlayInChat) packet;
            message = p.a();
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.PacketPlayInChat p = (net.minecraft.server.v1_8_R3.PacketPlayInChat) packet;
            message = p.a();
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.PacketPlayInChat p = (net.minecraft.server.v1_9_R1.PacketPlayInChat) packet;
            message = p.a();
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.PacketPlayInChat p = (net.minecraft.server.v1_9_R2.PacketPlayInChat) packet;
            message = p.a();
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.PacketPlayInChat p = (net.minecraft.server.v1_10_R1.PacketPlayInChat) packet;
            message = p.a();
        } else if (version == ServerVersion.v_1_11_1) {
            net.minecraft.server.v1_11_R1.PacketPlayInChat p = (net.minecraft.server.v1_11_R1.PacketPlayInChat) packet;
            message = p.a();
        } else if (version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.PacketPlayInChat p = (net.minecraft.server.v1_11_R1.PacketPlayInChat) packet;
            message = p.a();
        } else if (version == ServerVersion.v_1_12_1 || version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.PacketPlayInChat p = (net.minecraft.server.v1_12_R1.PacketPlayInChat) packet;
            message = p.a();
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.PacketPlayInChat p = (net.minecraft.server.v1_13_R1.PacketPlayInChat) packet;
            message = p.b();
        }
        else if(version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.PacketPlayInChat p = (net.minecraft.server.v1_13_R2.PacketPlayInChat) packet;
            message = p.b();
        }
        else if (version == ServerVersion.v_1_14) {
            net.minecraft.server.v1_14_R1.PacketPlayInChat p = (net.minecraft.server.v1_14_R1.PacketPlayInChat) packet;
            message = p.b();
        } else if (version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.PacketPlayInChat p = (net.minecraft.server.v1_14_R1.PacketPlayInChat) packet;
            message = p.b();
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.PacketPlayInChat p = (net.minecraft.server.v1_15_R1.PacketPlayInChat) packet;
            message = p.b();
        } else {
            throw new VersionNotFoundException();
        }
        return message;
    }

    /**
     * Get the player's ping in the EntityPlayer's class
     * @return ping
     * @throws VersionNotFoundException
     */
    public int getPing() throws VersionNotFoundException {
        int ping;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_11_1 || version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        }  else if (version == ServerVersion.v_1_12_1 || version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        }
        else if(version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        }
        else if (version == ServerVersion.v_1_14 || version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer)player).getHandle();
            ping = ePlayer.ping;
        } else {
            throw new VersionNotFoundException();
        }
        return ping;
    }

    /**
     * Get the motX variable in the EntityPlayer class
     *
     * @return motX
     */
    public double getMotX() throws VersionNotFoundException {
        double motX;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_11_1 || version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        }  else if (version == ServerVersion.v_1_12_1 || version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        }
        else if(version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.motX;
        }
        else if (version == ServerVersion.v_1_14 || version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.getMot().getX();
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer)player).getHandle();
            motX = ePlayer.getMot().getX();
        } else {
            throw new VersionNotFoundException();
        }
        return motX;
    }



    /**
     * Get the motY variable in the EntityPlayer class
     *
     * @return motY
     */
    public double getMotY() throws VersionNotFoundException {
        double motY;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_11_1 || version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        }  else if (version == ServerVersion.v_1_12_1 || version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        }
        else if(version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.motY;
        }
        else if (version == ServerVersion.v_1_14 || version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.getMot().getY();
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer)player).getHandle();
            motY = ePlayer.getMot().getY();
        } else {
            throw new VersionNotFoundException();
        }
        return motY;
    }

    /**
     * Get the motZ variable in the EntityPlayer class
     *
     * @return motZ
     */
    public double getMotZ() throws VersionNotFoundException {
        double motZ;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_11_1 || version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        }  else if (version == ServerVersion.v_1_12_1 || version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        }
        else if(version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.motZ;
        }
        else if (version == ServerVersion.v_1_14 || version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.getMot().getZ();
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.EntityPlayer ePlayer = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer)player).getHandle();
            motZ = ePlayer.getMot().getZ();
        } else {
            throw new VersionNotFoundException();
        }
        return motZ;
    }
}
