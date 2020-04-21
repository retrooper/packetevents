package me.purplex.packetevents.events.packetevent;

import com.comphenix.tinyprotocol.Reflection;
import com.comphenix.tinyprotocol.Reflection.*;
import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.PlayerAction;
import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.events.PacketEvent;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;

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

}
