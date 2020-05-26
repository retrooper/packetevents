package me.purplex.packetevents.example;

import me.purplex.packetevents.PacketEvents;
import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.event.PacketEvent;
import me.purplex.packetevents.event.impl.*;
import me.purplex.packetevents.event.handler.PacketHandler;
import me.purplex.packetevents.event.listener.PacketListener;
import me.purplex.packetevents.packet.Packet;
import me.purplex.packetevents.packetwrappers.in.abilities.WrappedPacketInAbilities;
import me.purplex.packetevents.packetwrappers.in.blockdig.WrappedPacketInBlockDig;
import me.purplex.packetevents.packetwrappers.in.chat.WrappedPacketInChat;
import me.purplex.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import me.purplex.packetevents.packetwrappers.in.use_entity.impl.WrappedPacketInUseEntity;
import me.purplex.packetevents.packetwrappers.login.handshake.WrappedPacketLoginHandshake;
import me.purplex.packetevents.utils.playerprotocolversion.PlayerVersionManager;
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
                final WrappedPacketInUseEntity useEntity = new WrappedPacketInUseEntity(e.getPacket());
                final Entity entity = useEntity.entity;
                if (useEntity.action == EntityUseAction.ATTACK) {
                    final double distance = entity.getLocation().distanceSquared(p.getLocation());
                    //p.sendMessage("dist: " + dist);
                }
                break;
            case Packet.Client.ABILITIES:
                //1.8 packet style
                final WrappedPacketInAbilities abilities = new WrappedPacketInAbilities(e.getPacket());
                final boolean a = abilities.a;
                final boolean b = abilities.b;
                final boolean c = abilities.c;
                final boolean d = abilities.d;
                final float eFloat = abilities.e;
                final float fFloat = abilities.f;
                break;
            case Packet.Client.BLOCK_DIG:
                //Custom PlayerDigType enum
                final WrappedPacketInBlockDig blockDig = new WrappedPacketInBlockDig(e.getPacket());
                final PlayerDigType type = blockDig.digType;
                break;
            case Packet.Client.FLYING:
                final WrappedPacketInFlying flying = new WrappedPacketInFlying(e.getPacket());
                final float pitch = flying.pitch;
                final float yaw = flying.yaw;
                final boolean hasPos = flying.hasPos;
                final boolean hasLook = flying.hasLook;
                break;
            case Packet.Client.POSITION:
                final WrappedPacketInFlying.WrappedPacketInPosition position =
                        new WrappedPacketInFlying.WrappedPacketInPosition(e.getPacket());
                final boolean isPos = position.hasPos; //true
                break;
            case Packet.Client.POSITION_LOOK:
                final WrappedPacketInFlying.WrappedPacketInPosition_Look position_look =
                        new WrappedPacketInFlying.WrappedPacketInPosition_Look(e.getPacket());
                final boolean isLook = position_look.hasLook; //true
                break;
            case Packet.Client.CHAT:
                final WrappedPacketInChat chat = new WrappedPacketInChat(e.getPacket());
                final String message = chat.message;
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
        event.setCancelled(true);
        //Kick the player synchronously as I called it asynchronously

           /* Bukkit.getScheduler().runTask(PacketEvents.getInstance().plugin, new Runnable() {
                @Override
                public void run() {
                    event.getPlayer().kickPlayer("nope");
                }
            });*/
    }

    @PacketHandler
    public void onUninject(PlayerUninjectEvent e) {

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
            System.out.println(PacketEvents.getCurrentServerTPS() + " is tps");
        } else if (e instanceof PacketLoginEvent) {
            final PacketLoginEvent event = (PacketLoginEvent) e;
            if (event.getPacketName().equals(Packet.Login.HANDSHAKE)) {
                final WrappedPacketLoginHandshake handshake = new WrappedPacketLoginHandshake(event.getPacket());
                final String hostname = handshake.hostname;
                final int port = handshake.port;
                final int protocolVersion = handshake.protocolVersion;
                PlayerVersionManager.setPlayerProtocolVersion(event.getPlayer().getUniqueId(), protocolVersion);
                System.out.println("PROTOCOL NUMBER: " + protocolVersion);
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
