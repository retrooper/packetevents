package me.purplex.packetevents.events.packetevent;

import com.comphenix.tinyprotocol.Reflection;
import com.comphenix.tinyprotocol.Reflection.*;
import me.purplex.packetevents.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PacketSendEvent extends PacketEvent {

    private Player player;
    private String name;
    private Object packet;
    private long timestamp;
    private boolean cancelled;
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
     * Get Velocity entity
     * PacketPlayOutEntityVelocity
     * @return
     */
    public Entity getVelocityEntity() {
        //WorldServer s;
        FieldAccessor<Integer> field = Reflection.getField(packet.getClass(), int.class, 0);
        int id = field.get(packet);
        for(World w : Bukkit.getWorlds()) {
            for(Entity e : w.getEntities()) {
                if(e.getEntityId() == id) {
                    return e;
                }
            }
        }
        return null;
    }


    /**
     * Get X velocity
     * PacketPlayOutEntityVelocity
     * @return x
     */
    public double getVelocityX() {
        FieldAccessor<Integer> field = Reflection.getField(packet.getClass(), int.class, 1);
        int i = field.get(packet);
        double x = i / 8000.0;
        return x;
    }

    /**
     * Get Y velocity
     * PacketPlayOutEntityVelocity
     * @return y
     */
    public double getVelocityY() {
        FieldAccessor<Integer> field = Reflection.getField(packet.getClass(), int.class, 2);
        int i = field.get(packet);
        double y = i / 8000.0;
        return y;
    }

    /**
     * Get Z velocity
     * PacketPlayOutEntityVelocity
     * @return z
     */
    public double getVelocityZ() {
        FieldAccessor<Integer> field = Reflection.getField(packet.getClass(), int.class, 3);
        int i = field.get(packet);
        double z = i / 8000.0;
        return z;
    }

}

