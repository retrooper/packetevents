package me.purplex.packetevents.example;

import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.Direction;
import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.Hand;
import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.event.PacketEvent;
import me.purplex.packetevents.event.impl.*;
import me.purplex.packetevents.event.handler.PacketHandler;
import me.purplex.packetevents.event.listener.PacketListener;
import me.purplex.packetevents.packet.Packet;
import me.purplex.packetevents.packetwrappers.in.abilities.WrappedPacketInAbilities;
import me.purplex.packetevents.packetwrappers.in.blockdig.WrappedPacketInBlockDig;
import me.purplex.packetevents.packetwrappers.in.blockplace.WrappedPacketInBlockPlace;
import me.purplex.packetevents.packetwrappers.in.chat.WrappedPacketInChat;
import me.purplex.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.purplex.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import me.purplex.packetevents.utils.math.Vector3i;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Implementing PacketListener to listen to packets and packetevents' custom events,
 * Implementing org.bukkit.event.Listener to listen to bukkit's events
 */
public class RegisteredListener implements PacketListener, Listener {


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
                final WrappedPacketInUseEntity useEntity = new WrappedPacketInUseEntity(e.getPlayer(), e.getPacket());
                final Entity entity = useEntity.getEntity();
                final double distanceSquared = entity.getLocation().distanceSquared(p.getLocation());
                final Hand hand = useEntity.getHand();
                final EntityUseAction action = useEntity.getAction();
                break;
            case Packet.Client.ABILITIES:
                final WrappedPacketInAbilities abilities = new WrappedPacketInAbilities(e.getPacket());
                final boolean isVulnerable = abilities.isVulnerable();
                final boolean isFlying = abilities.isFlying();
                final boolean canFly = abilities.canFly();
                final boolean canInstantlyBuild = abilities.canInstantlyBuild();
                final float flySpeed = abilities.getFlySpeed();
                final float walkSpeed = abilities.getWalkSpeed();
                // e.getPlayer().sendMessage("walkspeed: " + walkSpeed + ", flyspeed: " + flySpeed + ", canFly: " + canFly);
                break;
            case Packet.Client.BLOCK_DIG:
                //Custom PlayerDigType enum
                final WrappedPacketInBlockDig blockDig = new WrappedPacketInBlockDig(e.getPacket());
                final PlayerDigType type = blockDig.getDigType();
                final Direction direction = blockDig.getDirection();
                break;
            case Packet.Client.FLYING:
                final WrappedPacketInFlying flying = new WrappedPacketInFlying(e.getPacket());
                final float pitch = flying.getPitch();
                final float yaw = flying.getYaw();
                break;
            case Packet.Client.POSITION:
                final WrappedPacketInFlying.WrappedPacketInPosition position =
                        new WrappedPacketInFlying.WrappedPacketInPosition(e.getPacket());
                final boolean isPos = position.isPosition(); //true
                break;
            case Packet.Client.POSITION_LOOK:
                final WrappedPacketInFlying.WrappedPacketInPosition_Look position_look =
                        new WrappedPacketInFlying.WrappedPacketInPosition_Look(e.getPacket());
                final boolean isLook = position_look.isLook(); //true
                break;
            case Packet.Client.CHAT:
                final WrappedPacketInChat chat = new WrappedPacketInChat(e.getPacket());
                final String message = chat.getMessage();
                //e.getPlayer().sendMessage(message);
                break;
            case Packet.Client.BLOCK_PLACE:
                final WrappedPacketInBlockPlace blockPlace = new WrappedPacketInBlockPlace(e.getPlayer(), e.getPacket());
                final Vector3i blockPosition = blockPlace.getBlockPosition();
                final Hand h = blockPlace.getHand();
                final ItemStack stack = blockPlace.getItemStack();
                break;

            //System.out.println("YOU SAID: " + message);
        }

    }

    @PacketHandler
    public void onPacketSend(PacketSendEvent e) {

    }

    @PacketHandler
    public void onCustomMove(CustomMoveEvent e) {
        double distance = e.getTo().distanceSquared(e.getFrom());
    }

    @PacketHandler
    public void onInject(PlayerInjectEvent event) {
        //event.getPlayer().sendMessage("injected");


        //event.setCancelled(true) is possible
    }

    @PacketHandler
    public void onUninject(PlayerUninjectEvent e) {
        //e.setCancelled(true); will
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
            // System.out.println(PacketEvents.getCurrentServerTPS() + " is tps");
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
        PacketEvents.getEventManager().callEvent(customMoveEvent);

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
