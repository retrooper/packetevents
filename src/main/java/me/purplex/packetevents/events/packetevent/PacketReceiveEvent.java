package me.purplex.packetevents.events.packetevent;

import com.comphenix.tinyprotocol.Reflection;
import com.comphenix.tinyprotocol.Reflection.*;
import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.PlayerAction;
import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.events.PacketEvent;
import me.purplex.packetevents.packets.Packet;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class PacketReceiveEvent extends PacketEvent {

    private Player player;
    private String name;
    private Object packet;
    private long timestamp;
    private boolean cancelled;

    public PacketReceiveEvent(Player player, String packetName, Object packet) {
        this.player = player;
        this.name = packetName;
        this.packet = packet;
        this.timestamp = (System.nanoTime() / 1000000);
        this.cancelled = false;
    }


    /**
     * Get the player sending the packet
     * @return
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the packet object's class name, aka. Packet Name
     * @return
     */
    public String getPacketName() {
        return this.name;
    }

    /**
     * Get the raw packet object
     * @return
     */
    public Object getPacket() {
        return this.packet;
    }

    /**
     * The time the packet was received(ms)
     * @return
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Cancel the packet
     * @param cancelled
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Returns if the packet has been cancelled
     * @return
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Get the type of dig
     * PacketPlayInBlockDig
     * @return
     */
    public PlayerDigType getPlayerDigType() {
        String c = String.valueOf(Reflection.getMethod(packet.getClass(), "c").invoke(packet));
        PlayerDigType type;
        try {
            type = PlayerDigType.valueOf(c);
        } catch (IllegalArgumentException e) {
            type = PlayerDigType.WRONG_PACKET;
        }
        return type;
    }

    /**
     * Get the entity interact action
     * PacketPlayInUseEntity
     * @return
     */
    public EntityUseAction getEntityUseAction() {
        Object action = null;
        try {
            action = FieldUtils.readDeclaredField(packet, "action", true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (EntityUseAction a : EntityUseAction.values()) {
            if (a.toString().equalsIgnoreCase(action.toString())) {
                return a;
            }
        }
        return EntityUseAction.WRONG_PACKET;
    }

    /**
     * Get the PlayerAction
     * PacketPlayInEntityAction
     * @return
     */
    public PlayerAction getPlayerAction() {
        Object animation = null;
        try {
            animation = FieldUtils.readDeclaredField(packet, "animation", true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (PlayerAction a : PlayerAction.values()) {
            if (a.toString().equalsIgnoreCase(animation.toString())) {
                return a;
            }
        }
        return PlayerAction.WRONG_PACKET;
    }

    /**
     * Entity you interacted with
     * PacketPlayInUseEntity
     * @return
     */
    public Entity getInteractedEntity() {
        try {
            FieldAccessor<Integer> field = Reflection.getField(packet.getClass(), int.class, 0);
            int id = field.get(packet);
            for (Entity e : player.getWorld().getEntities()) {
                if (e.getEntityId() == id) {
                    return e;
                }
            }
            for(World w : Bukkit.getWorlds()) {
                for(Entity e  : w.getEntities()) {
                    if(e.getEntityId() == id) {
                        return e;
                    }
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * Get the string message
     * PacketPlayInChat
     * @return
     */
    public String getChatPacketMessage() {
        FieldAccessor<String> field = Reflection.getField(packet.getClass(), String.class, 0);
        String message = field.get(packet);
        return message;
    }


    public int getPing() {
        int ping = 0;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return ping;
    }

    /**
     * Get the motX variable in the player's CraftPlayer class
     * using Reflection
     * @return
     */
    public double getMotionX() {
        Reflection.MethodInvoker getPlayerHandle = Reflection.getMethod("{obc}.entity.CraftPlayer", "getHandle");
        Object entityplayer = getPlayerHandle.invoke(player);
        Field field = null;
        try {
            field = entityplayer.getClass().getField("motX");
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }
        double mot = 0.0D;
        try {
            mot = field.getDouble(entityplayer);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return mot;
    }

    /**
     * Get the motY variable in the player's CraftPlayer class
     * using Reflection
     * @return
     */
    public double getMotionY() {
        Reflection.MethodInvoker getPlayerHandle = Reflection.getMethod("{obc}.entity.CraftPlayer", "getHandle");
        Object entityplayer = getPlayerHandle.invoke(player);
        Field field = null;
        try {
            field = entityplayer.getClass().getField("motY");
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }
        double mot = 0.0D;
        try {
            mot = field.getDouble(entityplayer);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return mot;
    }

    /**
     * Get the motZ variable in the player's CraftPlayer class
     * using Reflection
     * @return
     */
    public double getMotionZ() {
        Reflection.MethodInvoker getPlayerHandle = Reflection.getMethod("{obc}.entity.CraftPlayer", "getHandle");
        Object entityplayer = getPlayerHandle.invoke(player);
        Field field = null;
        try {
            field = entityplayer.getClass().getField("motZ");
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        }
        double mot = 0.0D;
        try {
            mot = field.getDouble(entityplayer);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return mot;
    }
}
