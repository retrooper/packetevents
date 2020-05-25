package me.purplex.packetevents.example;

import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.event.PacketEvent;
import me.purplex.packetevents.event.impl.PacketReceiveEvent;
import me.purplex.packetevents.event.impl.PacketSendEvent;
import me.purplex.packetevents.event.impl.ServerTickEvent;
import me.purplex.packetevents.event.handler.PacketHandler;
import me.purplex.packetevents.event.listener.PacketListener;
import me.purplex.packetevents.packet.Packet;
import me.purplex.packetevents.packetwrappers.in.*;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Implementing PacketListener to listen to packets and packetevents' custom events.
 * Implementing org.bukkit.event.Listener to listen to bukkit's events
 */
public class TestExample implements PacketListener, Listener {
    /**
     * How to register a packet listener?
     * <p>
     * PacketEvents.getPacketManager().registerListener(new TestExample());
     */

    /**
     * How to unregister a packet listener?
     * PacketEvents.getPacketManager().unregisterListener(new TestExample());
     */



    /**
     * Listen to PacketReceiveEvent
     *
     * @param e PacketReceiveEvent
     */
    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        final Player p = e.getPlayer();
        final long timestamp = e.getTimestamp();
        //ONLY CLIENT PACKETS ALLOWED HERE!
        switch (e.getPacketName()) {
            case Packet.Client.USE_ENTITY:
                final WrappedPacketPlayInUseEntity useEntity = new WrappedPacketPlayInUseEntity(e.getPacket());
                final Entity entity = useEntity.entity;
                if (useEntity.action == EntityUseAction.ATTACK) {
                    final double distance = entity.getLocation().distanceSquared(p.getLocation());
                    //p.sendMessage("dist: " + dist);
                }
                break;
            case Packet.Client.ABILITIES:
                //1.8 packet style
                final WrappedPacketPlayInAbilities abilities = new WrappedPacketPlayInAbilities(e.getPacket());
                final boolean a = abilities.a;
                final boolean b = abilities.b;
                final boolean c = abilities.c;
                final boolean d = abilities.d;
                final float eFloat = abilities.e;
                final float fFloat = abilities.f;
                break;
            case Packet.Client.BLOCK_DIG:
                //Custom PlayerDigType enum
                final WrappedPacketPlayInBlockDig blockDig = new WrappedPacketPlayInBlockDig(e.getPacket());
                final PlayerDigType type = blockDig.digType;
                break;
            case Packet.Client.FLYING:
                final WrappedPacketPlayInFlying flying = new WrappedPacketPlayInFlying(e.getPacket());
                final float pitch = flying.pitch;
                final float yaw = flying.yaw;
                final boolean hasPos = flying.hasPos;
                final boolean hasLook = flying.hasLook;
                break;
            case Packet.Client.POSITION:
                final WrappedPacketPlayInFlying.WrappedPacketPlayInPosition position =
                        new WrappedPacketPlayInFlying.WrappedPacketPlayInPosition(e.getPacket());
                final boolean isPos = position.hasPos; //true
                break;
            case Packet.Client.POSITION_LOOK:
                final WrappedPacketPlayInFlying.WrappedPacketPlayInPosition_Look position_look =
                        new WrappedPacketPlayInFlying.WrappedPacketPlayInPosition_Look(e.getPacket());
                final boolean isLook = position_look.hasLook; //true
                break;
            case Packet.Client.CHAT:
                final WrappedPacketPlayInChat chat = new WrappedPacketPlayInChat(e.getPacket());
                final String message = chat.message;
                //System.out.println("YOU SAID: " + message);
        }
    }

    @PacketHandler
    public void onPacketSend(PacketSendEvent e) {
        if (e.getPacketName().equals(Packet.Server.KEEP_ALIVE)) {

        }
    }

    @PacketHandler
    public void onCustomMove(CustomMoveEvent e) {
        double distance = e.getTo().distanceSquared(e.getFrom());
    }

    /**
     * Another way to listen to packets
     *
     * @param e PacketEvent
     */
    @PacketHandler
    public void onPacket(PacketEvent e) {
        if (e instanceof PacketSendEvent) {
            final PacketSendEvent p = (PacketSendEvent) e;
            final Player targetPlayer = p.getPlayer();
            final Object rawNMSPacket = p.getPacket();
            final long timestamp = p.getTimestamp();
        } else if (e instanceof CustomMoveEvent) {
            final CustomMoveEvent event = (CustomMoveEvent) e;
            final Player player = event.getPlayer();
            final Location to = event.getTo();
            final Location from = event.getFrom();
        } else if (e instanceof ServerTickEvent) {
            final ServerTickEvent event = (ServerTickEvent) e;
            final int currentTick = event.getCurrentTick();
            //will be true once a second
            if(event.hasOneSecondPassed()) {

            }
        }
    }


    /**
     * Call the CustomMoveEvent event
     *
     * @param e PlayerMoveEvent
     */
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        CustomMoveEvent customMoveEvent = new CustomMoveEvent(e.getPlayer(), e.getTo(), e.getFrom());
        e.setCancelled(customMoveEvent.isCancelled());
        PacketEvents.getPacketManager().callEvent(customMoveEvent);

        if (e.getTo() != customMoveEvent.getTo()) {
            e.setTo(customMoveEvent.getTo());
        }
        if (e.getFrom() != customMoveEvent.getFrom()) {
            e.setFrom(customMoveEvent.getFrom());
        }
    }

    /**
     * Only listen to server tick (if enabled)
     *
     * @param e ServerTickEvent
     */
    @PacketHandler
    public void onServerTick(ServerTickEvent e) {

    }

}
