package me.purplex.packetevents.example;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.PlayerDigType;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.events.packetevent.ServerTickEvent;
import me.purplex.packetevents.events.handler.PacketHandler;
import me.purplex.packetevents.events.listener.PacketListener;
import me.purplex.packetevents.events.packetevent.*;
import me.purplex.packetevents.events.packetevent.packet.impl.in.*;
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

    private static ServerVersion serverVersion = ServerVersion.getVersion();

    @PacketHandler
    public void onPacketReceive(PacketReceiveEvent e) {
        Player p = e.getPlayer();
        //ONLY CLIENT PACKETS ALLOWED HERE!

        switch (e.getPacketName()) {
            case Packet.Client.USE_ENTITY:
                WrappedPacketPlayInUseEntity useEntity = new WrappedPacketPlayInUseEntity(e.getPacket());
                Entity entity = useEntity.getEntity();
                if (useEntity.getEntityUseAction() == EntityUseAction.ATTACK) {
                    double dist = entity.getLocation().distanceSquared(p.getLocation());
                    //p.sendMessage("dist: " + dist);
                }
                break;
            case Packet.Client.ABILITIES:
                WrappedPacketPlayInAbilities abilities = new WrappedPacketPlayInAbilities(e.getPacket());
                boolean a = abilities.a;
                boolean b = abilities.b;
                boolean c = abilities.c;
                boolean d = abilities.d;
                float eFloat = abilities.e;
                float fFloat = abilities.f;
                //just like the 1.8 NMS classes.
                break;
            case Packet.Client.BLOCK_DIG:
                WrappedPacketPlayInBlockDig blockDig = new WrappedPacketPlayInBlockDig(e.getPacket());
                PlayerDigType type = blockDig.digType;
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
