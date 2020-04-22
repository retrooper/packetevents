package me.purplex.packetevents;

import com.comphenix.tinyprotocol.Reflection.*;
import com.comphenix.tinyprotocol.Reflection;
import io.netty.channel.Channel;
import me.purplex.packetevents.events.manager.PacketManager;
import me.purplex.packetevents.packets.Packet;
import org.bukkit.entity.Player;

public class PacketEvents {
    private static PacketManager packetManager;


    public static PacketManager getPacketManager() {
        if (packetManager == null) {
            packetManager = new PacketManager();
        }
        return packetManager;
    }


    public void sendPacket(Player player, Object packet) {
        Main.getInstance().protocol.sendPacket(player, packet);
    }

    public void sendPacket(Channel channel, Object packet) {
        Main.getInstance().protocol.sendPacket(channel, packet);
    }

    public static Class<?> getPacketCass(String packet) {
        return Reflection.getClass("{nms}." + packet);
    }


    public static void sendVelocity(Player player, double x, double y, double z) {
        Class<?> velocityClass = getPacketCass(Packet.Server.ENTITY_VELOCITY);
        FieldAccessor<Integer> entityIdField = Reflection.getField(velocityClass, int.class, 0);
        FieldAccessor<Integer> fieldX = Reflection.getField(velocityClass, int.class, 1);
        FieldAccessor<Integer> fieldY = Reflection.getField(velocityClass, int.class, 2);
        FieldAccessor<Integer> fieldZ = Reflection.getField(velocityClass, int.class, 3);

        Object velocityObject = null;
        try {
            velocityObject = velocityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        fieldX.set(velocityObject, (int) (x * 8000));
        fieldY.set(velocityObject, (int) (y * 8000));
        fieldZ.set(velocityObject, (int) (z * 8000));

        entityIdField.set(velocityObject, player.getEntityId());
        Main.getInstance().protocol.sendPacket(player, velocityObject);
    }
}
