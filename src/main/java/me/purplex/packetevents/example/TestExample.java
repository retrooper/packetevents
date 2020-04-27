package me.purplex.packetevents.example;

import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.events.packetevent.ServerTickEvent;
import me.purplex.packetevents.events.handler.PacketHandler;
import me.purplex.packetevents.events.listener.PacketListener;
import me.purplex.packetevents.events.packetevent.PacketReceiveEvent;
import me.purplex.packetevents.events.packetevent.PacketSendEvent;
import me.purplex.packetevents.exceptions.VersionNotFoundException;
import me.purplex.packetevents.packets.Packet;
import org.bukkit.entity.Entity;

public class TestExample implements PacketListener {
    /**
     * How to register a packet listener?
     * <p>
     * PacketEvents.getPacketManager().registerPacketListener(new TestExample());
     */

    private int tick;

    private static ServerVersion serverVersion = ServerVersion.getVersion();

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) throws VersionNotFoundException {
        //ONLY CLIENT PACKETS ALLOWED HERE!
        double motX = e.getMotX();
        double motY = e.getMotY();
        double motZ = e.getMotZ();
        long timestamp = e.getTimestamp();
        if (e.getPacketName().equals(Packet.Client.USE_ENTITY)) {
            Entity entity = e.getInteractedEntity();
            EntityUseAction entityUseAction = e.getEntityUseAction();
            if (entityUseAction == EntityUseAction.ATTACK) {
                //LEFT CLICKED
                e.getPlayer().sendMessage("Attacked an entity!");
            }
            e.getPlayer().sendMessage("Dist: " + entity.getLocation().distanceSquared(e.getPlayer().getLocation()));
        } else if (e.getPacketName().equals(Packet.Client.ARM_ANIMATION)) {

        } else if (e.getPacketName().equals(Packet.Client.BLOCK_DIG)) {
            PlayerDigType type = e.getPlayerDigType();
            if (type == PlayerDigType.ABORT_DESTROY_BLOCK) {
                //aborted destroying a block
            }
        } else if (e.getPacketName().equals(Packet.Client.CHAT)) {
            String message = e.getChatPacketMessage();
            if (message.equals("pls jump")) {
                //shoot them in the air
                PacketEvents.sendVelocity(e.getPlayer(), 0.0, 50.0, 0.0);
            }
        }
        if (serverVersion == ServerVersion.v_1_8) {
            if (e.getPacket() instanceof net.minecraft.server.v1_8_R1.PacketPlayInFlying) {
                //flying packet, position, position_look extend the flying packet
            }
        }
    }

    @PacketHandler
    public void onPacketSend(PacketSendEvent e) throws VersionNotFoundException {
        //ONLY SERVER PACKETS ALLOWED HERE!
        if (e.getPacketName().equals(Packet.Server.ENTITY_VELOCITY)) {
            int ping = e.getPing();
            Entity entity = e.getVelocityEntity();
            double velX = e.getVelocityX();
            double velY = e.getVelocityY();
            double velZ = e.getVelocityZ();
        }
    }

    @PacketHandler
    public void onServerTick(ServerTickEvent e) {
        //CALLED EVERY TICK
        tick++;
        if (tick % 20 == 0) {
            /*for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage("One second has passed!");
            }*/
        }
    }
}
