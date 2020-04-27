package me.purplex.packetevents.events.packetevent;

import com.comphenix.tinyprotocol.Reflection;
import com.comphenix.tinyprotocol.Reflection.*;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.events.PacketEvent;
import me.purplex.packetevents.exceptions.VersionNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PacketSendEvent extends PacketEvent {
    private final Player player;
    private final String name;
    private final Object packet;
    private final long timestamp;
    private boolean cancelled;
    private final ServerVersion version = ServerVersion.getVersion();
    private static FieldAccessor<Integer> velId;
    private static FieldAccessor<Integer> velX;
    private static FieldAccessor<Integer> velY;
    private static FieldAccessor<Integer> velZ;

    public PacketSendEvent(Player player, String packetName, Object packet) {
        this.player = player;
        this.name = packetName;
        this.packet = packet;
        this.timestamp = (System.nanoTime() / 1000000);
        this.cancelled = false;
    }


    public Player getPlayer() {
        return this.player;
    }

    public String getPacketName() {
        return this.name;
    }

    public Object getPacket() {
        return this.packet;
    }

    public long getTimestamp() {
        return timestamp;
    }


    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Get Velocity entity (partly reflection)
     * PacketPlayOutEntityVelocity
     *
     * @return
     */
    public Entity getVelocityEntity() throws VersionNotFoundException {
        int entityId;
        Object velPacket;
        Entity entity = null;
        if (version == ServerVersion.v_1_8) {
            net.minecraft.server.v1_8_R1.PacketPlayOutEntityVelocity p = (net.minecraft.server.v1_8_R1.PacketPlayOutEntityVelocity) packet;
            velPacket = p;
            if (velId == null) {
                velId = Reflection.getField(velPacket.getClass(), int.class, 0);
            }
            int id = velId.get(velPacket);
            for (World bukkitWorld : Bukkit.getWorlds()) {
                net.minecraft.server.v1_8_R1.World world = ((org.bukkit.craftbukkit.v1_8_R1.CraftWorld) bukkitWorld).getHandle();
                net.minecraft.server.v1_8_R1.Entity nmsEntity = world.a(id);
                if (nmsEntity != null) {
                    entity = nmsEntity.getBukkitEntity();
                    break;
                }
            }
        } else if (version == ServerVersion.v_1_8_3) {
            net.minecraft.server.v1_8_R2.PacketPlayOutEntityVelocity p = (net.minecraft.server.v1_8_R2.PacketPlayOutEntityVelocity) packet;
            velPacket = p;
            if (velId == null) {
                velId = Reflection.getField(velPacket.getClass(), int.class, 0);
            }
            int id = velId.get(velPacket);
            for (World bukkitWorld : Bukkit.getWorlds()) {
                net.minecraft.server.v1_8_R2.World world = ((org.bukkit.craftbukkit.v1_8_R2.CraftWorld) bukkitWorld).getHandle();
                net.minecraft.server.v1_8_R2.Entity nmsEntity = world.a(id);
                if (nmsEntity != null) {
                    entity = nmsEntity.getBukkitEntity();
                    break;
                }
            }
        } else if (version == ServerVersion.v_1_8_8) {
            net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity p = (net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity) packet;
            velPacket = p;
            if (velId == null) {
                velId = Reflection.getField(velPacket.getClass(), int.class, 0);
            }
            int id = velId.get(velPacket);
            for (World bukkitWorld : Bukkit.getWorlds()) {
                net.minecraft.server.v1_8_R3.World world = ((org.bukkit.craftbukkit.v1_8_R3.CraftWorld) bukkitWorld).getHandle();
                net.minecraft.server.v1_8_R3.Entity nmsEntity = world.a(id);
                if (nmsEntity != null) {
                    entity = nmsEntity.getBukkitEntity();
                    break;
                }
            }
        } else if (version == ServerVersion.v_1_9) {
            net.minecraft.server.v1_9_R1.PacketPlayOutEntityVelocity p = (net.minecraft.server.v1_9_R1.PacketPlayOutEntityVelocity) packet;
            velPacket = p;
            if (velId == null) {
                velId = Reflection.getField(velPacket.getClass(), int.class, 0);
            }
            int id = velId.get(velPacket);
            for (World bukkitWorld : Bukkit.getWorlds()) {
                net.minecraft.server.v1_9_R1.World world = ((org.bukkit.craftbukkit.v1_9_R1.CraftWorld) bukkitWorld).getHandle();
                net.minecraft.server.v1_9_R1.Entity nmsEntity = world.getEntity(id);
                if (nmsEntity != null) {
                    entity = nmsEntity.getBukkitEntity();
                    break;
                }
            }
        } else if (version == ServerVersion.v_1_9_4) {
            net.minecraft.server.v1_9_R2.PacketPlayOutEntityVelocity p = (net.minecraft.server.v1_9_R2.PacketPlayOutEntityVelocity) packet;
            velPacket = p;
            if (velId == null) {
                velId = Reflection.getField(velPacket.getClass(), int.class, 0);
            }
            int id = velId.get(velPacket);
            for (World bukkitWorld : Bukkit.getWorlds()) {
                net.minecraft.server.v1_9_R2.World world = ((org.bukkit.craftbukkit.v1_9_R2.CraftWorld) bukkitWorld).getHandle();
                net.minecraft.server.v1_9_R2.Entity nmsEntity = world.getEntity(id);
                if (nmsEntity != null) {
                    entity = nmsEntity.getBukkitEntity();
                    break;
                }
            }
        } else if (version == ServerVersion.v_1_10_2) {
            net.minecraft.server.v1_10_R1.PacketPlayOutEntityVelocity p = (net.minecraft.server.v1_10_R1.PacketPlayOutEntityVelocity) packet;
            velPacket = p;
            if (velId == null) {
                velId = Reflection.getField(velPacket.getClass(), int.class, 0);
            }
            int id = velId.get(velPacket);
            for (World bukkitWorld : Bukkit.getWorlds()) {
                net.minecraft.server.v1_10_R1.World world = ((org.bukkit.craftbukkit.v1_10_R1.CraftWorld) bukkitWorld).getHandle();
                net.minecraft.server.v1_10_R1.Entity nmsEntity = world.getEntity(id);
                if (nmsEntity != null) {
                    entity = nmsEntity.getBukkitEntity();
                    break;
                }
            }
        } else if (version == ServerVersion.v_1_11_1 || version == ServerVersion.v_1_11_2) {
            net.minecraft.server.v1_11_R1.PacketPlayOutEntityVelocity p = (net.minecraft.server.v1_11_R1.PacketPlayOutEntityVelocity) packet;
            velPacket = p;
            if (velId == null) {
                velId = Reflection.getField(velPacket.getClass(), int.class, 0);
            }
            int id = velId.get(velPacket);
            for (World bukkitWorld : Bukkit.getWorlds()) {
                net.minecraft.server.v1_11_R1.World world = ((org.bukkit.craftbukkit.v1_11_R1.CraftWorld) bukkitWorld).getHandle();
                net.minecraft.server.v1_11_R1.Entity nmsEntity = world.getEntity(id);
                if (nmsEntity != null) {
                    entity = nmsEntity.getBukkitEntity();
                    break;
                }
            }
        } else if (version == ServerVersion.v_1_12_1 || version == ServerVersion.v_1_12_2) {
            net.minecraft.server.v1_12_R1.PacketPlayOutEntityVelocity p = (net.minecraft.server.v1_12_R1.PacketPlayOutEntityVelocity) packet;
            velPacket = p;
            if (velId == null) {
                velId = Reflection.getField(velPacket.getClass(), int.class, 0);
            }
            int id = velId.get(velPacket);
            for (World bukkitWorld : Bukkit.getWorlds()) {
                net.minecraft.server.v1_12_R1.World world = ((org.bukkit.craftbukkit.v1_12_R1.CraftWorld) bukkitWorld).getHandle();
                net.minecraft.server.v1_12_R1.Entity nmsEntity = world.getEntity(id);
                if (nmsEntity != null) {
                    entity = nmsEntity.getBukkitEntity();
                    break;
                }
            }
        } else if (version == ServerVersion.v_1_13) {
            net.minecraft.server.v1_13_R1.PacketPlayOutEntityVelocity p = (net.minecraft.server.v1_13_R1.PacketPlayOutEntityVelocity) packet;
            velPacket = p;
            if (velId == null) {
                velId = Reflection.getField(velPacket.getClass(), int.class, 0);
            }
            int id = velId.get(velPacket);
            for (World bukkitWorld : Bukkit.getWorlds()) {
                net.minecraft.server.v1_13_R1.World world = ((org.bukkit.craftbukkit.v1_13_R1.CraftWorld) bukkitWorld).getHandle();
                net.minecraft.server.v1_13_R1.Entity nmsEntity = world.getEntity(id);
                if (nmsEntity != null) {
                    entity = nmsEntity.getBukkitEntity();
                    break;
                }
            }
        } else if (version == ServerVersion.v_1_13_2) {
            net.minecraft.server.v1_13_R2.PacketPlayOutEntityVelocity p = (net.minecraft.server.v1_13_R2.PacketPlayOutEntityVelocity) packet;
            velPacket = p;
            if (velId == null) {
                velId = Reflection.getField(velPacket.getClass(), int.class, 0);
            }
            int id = velId.get(velPacket);
            for (World bukkitWorld : Bukkit.getWorlds()) {
                net.minecraft.server.v1_13_R2.World world = ((org.bukkit.craftbukkit.v1_13_R2.CraftWorld) bukkitWorld).getHandle();
                net.minecraft.server.v1_13_R2.Entity nmsEntity = world.getEntity(id);
                if (nmsEntity != null) {
                    entity = nmsEntity.getBukkitEntity();
                    break;
                }

            }
        } else if (version == ServerVersion.v_1_14 || version == ServerVersion.v_1_14_1) {
            net.minecraft.server.v1_14_R1.PacketPlayOutEntityVelocity p = (net.minecraft.server.v1_14_R1.PacketPlayOutEntityVelocity) packet;
            velPacket = p;
            if (velId == null) {
                velId = Reflection.getField(velPacket.getClass(), int.class, 0);
            }
            int id = velId.get(velPacket);
            for (World bukkitWorld : Bukkit.getWorlds()) {
                net.minecraft.server.v1_14_R1.World world = ((org.bukkit.craftbukkit.v1_14_R1.CraftWorld) bukkitWorld).getHandle();
                net.minecraft.server.v1_14_R1.Entity nmsEntity = world.getEntity(id);
                if (nmsEntity != null) {
                    entity = nmsEntity.getBukkitEntity();
                    break;
                }
            }
        } else if (version == ServerVersion.v_1_15_1) {
            net.minecraft.server.v1_15_R1.PacketPlayOutEntityVelocity p = (net.minecraft.server.v1_15_R1.PacketPlayOutEntityVelocity) packet;
            velPacket = p;
            if (velId == null) {
                velId = Reflection.getField(velPacket.getClass(), int.class, 0);
            }
            int id = velId.get(velPacket);
            for (World bukkitWorld : Bukkit.getWorlds()) {
                net.minecraft.server.v1_15_R1.World world = ((org.bukkit.craftbukkit.v1_15_R1.CraftWorld) bukkitWorld).getHandle();
                net.minecraft.server.v1_15_R1.Entity nmsEntity = world.getEntity(id);
                if (nmsEntity != null) {
                    entity = nmsEntity.getBukkitEntity();
                    break;
                }
            }
        } else {
            throw new VersionNotFoundException();
        }
        return entity;
    }


    /**
     * Get X velocity (with reflection)
     * PacketPlayOutEntityVelocity
     *
     * @return x
     */
    public double getVelocityX() {
        FieldAccessor<Integer> field = Reflection.getField(packet.getClass(), int.class, 1);
        int i = field.get(packet);
        double x = i / 8000.0;
        return x;
    }

    /**
     * Get Y velocity (with reflection)
     * PacketPlayOutEntityVelocity
     *
     * @return y
     */
    public double getVelocityY() {
        FieldAccessor<Integer> field = Reflection.getField(packet.getClass(), int.class, 2);
        int i = field.get(packet);
        double y = i / 8000.0;
        return y;
    }

    /**
     * Get Z velocity (with reflection)
     * PacketPlayOutEntityVelocity
     *
     * @return z
     */
    public double getVelocityZ() {
        FieldAccessor<Integer> field = Reflection.getField(packet.getClass(), int.class, 3);
        int i = field.get(packet);
        double z = i / 8000.0;
        return z;
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

