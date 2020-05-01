package me.purplex.packetevents.example;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.events.packetevent.ServerTickEvent;
import me.purplex.packetevents.events.handler.PacketHandler;
import me.purplex.packetevents.events.listener.PacketListener;
import me.purplex.packetevents.events.packetevent.*;
import me.purplex.packetevents.packetwrappers.in.*;
import me.purplex.packetevents.packets.Packet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TestExample implements PacketListener {
    /**
     * How to register a packet listener?
     * <p>
     * PacketEvents.getPacketManager().registerPacketListener(new TestExample());
     */

    private int tick;

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        final Player p = e.getPlayer();
        final long timestamp = e.getTimestamp();
        //ONLY CLIENT PACKETS ALLOWED HERE!
        switch (e.getPacketName()) {
            case Packet.Client.USE_ENTITY:
                final WrappedPacketPlayInUseEntity useEntity = new WrappedPacketPlayInUseEntity(e.getPacket());
                final Entity entity = useEntity.getEntity();
                if (useEntity.getEntityUseAction() == EntityUseAction.ATTACK) {
                    final double dist = entity.getLocation().distanceSquared(p.getLocation());
                    //p.sendMessage("dist: " + dist);
                }
                break;
            case Packet.Client.ABILITIES:
                final WrappedPacketPlayInAbilities abilities = new WrappedPacketPlayInAbilities(e.getPacket());
                final boolean a = abilities.a;
                final boolean b = abilities.b;
                final boolean c = abilities.c;
                final boolean d = abilities.d;
                final float eFloat = abilities.e;
                final float fFloat = abilities.f;
                //just like the 1.8 NMS classes.
                break;
            case Packet.Client.BLOCK_DIG:
                final WrappedPacketPlayInBlockDig blockDig = new WrappedPacketPlayInBlockDig(e.getPacket());
                final PlayerDigType type = blockDig.getPlayerDigType();
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
