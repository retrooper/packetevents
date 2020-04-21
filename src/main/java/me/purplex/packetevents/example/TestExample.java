package me.purplex.packetevents.example;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.PlayerAction;
import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.events.packetevent.ServerTickEvent;
import me.purplex.packetevents.events.handler.PacketHandler;
import me.purplex.packetevents.events.listener.PacketListener;
import me.purplex.packetevents.events.packetevent.PacketReceiveEvent;
import me.purplex.packetevents.events.packetevent.PacketSendEvent;
import me.purplex.packetevents.packets.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TestExample implements PacketListener {
    /**
     * How to register a packet listener?
     * <p>
     * PacketEvents.getPacketManager().registerPacketListener(new TestExample());
     */

    private int tick;


    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        //ONLY CLIENT PACKETS ALLOWED HERE!
        if (e.getPacketName().equals(Packet.Client.USE_ENTITY)) {
            Entity entity = e.getInteractedEntity();
            EntityUseAction entityUseAction = e.getEntityUseAction();
            e.getPlayer().sendMessage("Dist: " + entity.getLocation().distanceSquared(e.getPlayer().getLocation()));
        } else if (e.getPacketName().equals(Packet.Client.ARM_ANIMATION)) {
            e.getPlayer().sendMessage("You swung your arm.");
        } else if (e.getPacketName().equals(Packet.Client.BLOCK_DIG)) {
            PlayerDigType type = e.getPlayerDigType();
        } else if (e.getPacketName().equals(Packet.Client.CHAT)) {
            String message = e.getChatPacketMessage();
        }
    }

    @PacketHandler
    public void onPacketSend(PacketSendEvent e) {
        //ONLY SERVER PACKETS ALLOWED HERE!
        if (e.getPacketName().equals(Packet.Server.ENTITY_VELOCITY)) {
            System.out.println("preee");
            Entity entity = e.getVelocityEntity();
            System.out.println(entity.getName());
            System.out.println(e.getVelocityX());
        }
    }

    @PacketHandler
    public void onServerTick(ServerTickEvent e) {
        //CALLED EVERY TICK
        tick++;
        if (tick % 20 == 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage("One second has passed!");
            }
        }
    }
}
