package io.github.retrooper.packetevents.example;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.Direction;
import io.github.retrooper.packetevents.enums.EntityUseAction;
import io.github.retrooper.packetevents.enums.PlayerAction;
import io.github.retrooper.packetevents.enums.PlayerDigType;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketHandler;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.impl.*;
import io.github.retrooper.packetevents.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.in.abilities.WrappedPacketInAbilities;
import io.github.retrooper.packetevents.packetwrappers.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.in.blockplace.WrappedPacketInBlockPlace;
import io.github.retrooper.packetevents.packetwrappers.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.packetwrappers.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.in.keepalive.WrappedPacketInKeepAlive;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.login.WrappedPacketLoginHandshake;
import io.github.retrooper.packetevents.packetwrappers.out.chat.WrappedPacketOutChat;
import io.github.retrooper.packetevents.packetwrappers.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.out.kickdisconnect.WrappedPacketOutKickDisconnect;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Implementing PacketListener to listen to packetevents' custom events,
 */
public class RegisteredListener implements PacketListener {
    /**
     * Listen to PacketReceiveEvent
     *
     * @param e PacketReceiveEvent
     */
    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        final Player p = e.getPlayer();
        final long timestamp = e.getTimestamp();

        //The block place name field is not set at compilation time, so we cannot use it in the switch statement
        //On 1.7->1.8.8 servers it is "PacketPlayInBlockPlace" and on 1.9+ servers it is "PacketPlayInUseItem"
        if(e.getPacketName().equals(Packet.Client.BLOCK_PLACE)) {
            final WrappedPacketInBlockPlace blockPlace = new WrappedPacketInBlockPlace(e.getPlayer(), e.getPacket());
            final Vector3i blockPosition = blockPlace.getBlockPosition();
            final ItemStack stack = blockPlace.getItemStack();
           // e.getPlayer().sendMessage("pos: " + blockPosition.toString() + ", type: " + stack.getType());
        }
        switch (e.getPacketName()) {
            case Packet.Client.USE_ENTITY:
                final WrappedPacketInUseEntity useEntity = new WrappedPacketInUseEntity(e.getPacket());
                final Entity entity = useEntity.getEntity();
                final double distanceSquared = entity.getLocation().distanceSquared(p.getLocation());
                final EntityUseAction useAction = useEntity.getAction();
                break;
            case Packet.Client.ABILITIES:
                final WrappedPacketInAbilities abilities = new WrappedPacketInAbilities(e.getPacket());
                final boolean isVulnerable = abilities.isVulnerable();
                final boolean isFlying = abilities.isFlying();
                final boolean canFly = abilities.canFly();
                final boolean canInstantlyBuild = abilities.canInstantlyBuild();
                final float flySpeed = abilities.getFlySpeed();
                final float walkSpeed = abilities.getWalkSpeed();
               //  e.getPlayer().sendMessage("walkspeed: " + walkSpeed + ", flyspeed: " + flySpeed + ", canFly: " + canFly);
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
                break;
            case Packet.Client.KEEP_ALIVE:
                final WrappedPacketInKeepAlive keepAlive = new WrappedPacketInKeepAlive(e.getPacket());
                final long responseID = keepAlive.getId();
                break;
            case Packet.Client.ENTITY_ACTION:
                final WrappedPacketInEntityAction entityAction = new WrappedPacketInEntityAction(e.getPacket());
                final PlayerAction action = entityAction.getAction();
                final int jumpBoost = entityAction.getJumpBoost();
                break;
        }

    }


    @PacketHandler
    public void onInject(PlayerInjectEvent event) {

    }

    /**
     * In this event you can safely register your players, unlike the PlayerInjectEvent.
     * In the PlayerInjectEvent some fields in the player object might be null.
     * So use this event!
     * @param event
     */
    @PacketHandler
    public void onPostInject(PostPlayerInjectEvent event) {

    }

    /**
     * You can use this event to unregister your players,
     * This event is called when a player is uninjected
     * @param event
     */
    @PacketHandler
    public void onUninject(PlayerUninjectEvent event) {

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
            if (p.getPacketName().equals(Packet.Server.ENTITY_VELOCITY)) {
                final WrappedPacketOutEntityVelocity vel = new WrappedPacketOutEntityVelocity(p.getPacket());
                final double velocityX = vel.getVelocityX();
                final double velocityY = vel.getVelocityY();
                final double velocityZ = vel.getVelocityZ();
            } else if (p.getPacketName().equals(Packet.Server.CHAT)) {
                final WrappedPacketOutChat chat = new WrappedPacketOutChat(p.getPacket());
            }
        } else if (e instanceof CustomMoveEvent) {
            final CustomMoveEvent event = (CustomMoveEvent) e;
            final Player player = event.getPlayer();
            final Location to = event.getTo();
            final Location from = event.getFrom();
        } else if (e instanceof ServerTickEvent) {
            final ServerTickEvent event = (ServerTickEvent) e;
            final int currentTick = event.getCurrentTick();
            final double tps = PacketEvents.getCurrentServerTPS();
        }
        else if(e instanceof PacketLoginEvent) {
            final PacketLoginEvent event = (PacketLoginEvent)e;
            if(event.getPacketName().equals(Packet.Login.HANDSHAKE)) {
                final WrappedPacketLoginHandshake handshake = new WrappedPacketLoginHandshake(event.getPacket());
                final int protocolVersion = handshake.getProtocolVersion();
                final String hostname = handshake.getHostname();
                final int port = handshake.getPort();
            }
        }
    }
}
