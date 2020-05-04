package me.purplex.packetevents.packetwrappers.in;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;
import me.purplex.packetevents.packetwrappers.in._1_10.WrappedPacketPlayInUseEntity_1_10;
import me.purplex.packetevents.packetwrappers.in._1_11.WrappedPacketPlayInUseEntity_1_11;
import me.purplex.packetevents.packetwrappers.in._1_12.WrappedPacketPlayInUseEntity_1_12;
import me.purplex.packetevents.packetwrappers.in._1_13.WrappedPacketPlayInUseEntity_1_13;
import me.purplex.packetevents.packetwrappers.in._1_13_2.WrappedPacketPlayInUseEntity_1_13_2;
import me.purplex.packetevents.packetwrappers.in._1_14.WrappedPacketPlayInUseEntity_1_14;
import me.purplex.packetevents.packetwrappers.in._1_15.WrappedPacketPlayInUseEntity_1_15;
import me.purplex.packetevents.packetwrappers.in._1_7_10.WrappedPacketPlayInUseEntity_1_7_10;
import me.purplex.packetevents.packetwrappers.in._1_8.WrappedPacketPlayInUseEntity_1_8;
import me.purplex.packetevents.packetwrappers.in._1_8_3.WrappedPacketPlayInUseEntity_1_8_3;
import me.purplex.packetevents.packetwrappers.in._1_8_8.WrappedPacketPlayInUseEntity_1_8_8;
import me.purplex.packetevents.packetwrappers.in._1_9.WrappedPacketPlayInUseEntity_1_9;
import me.purplex.packetevents.packetwrappers.in._1_9_4.WrappedPacketPlayInUseEntity_1_9_4;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WrappedPacketPlayInUseEntity extends WrappedPacket {

    private Entity entity;
    private EntityUseAction action;

    public WrappedPacketPlayInUseEntity(Object packet) {
        super(packet);
    }

    public Entity getEntity() {
        return entity;
    }

    public EntityUseAction getEntityUseAction() {
        return action;
    }


    @Override
    protected void setup() {
        if(version == ServerVersion.v_1_7_10) {
            WrappedPacketPlayInUseEntity_1_7_10 p = new WrappedPacketPlayInUseEntity_1_7_10(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        }
        else if (version == ServerVersion.v_1_8) {
            WrappedPacketPlayInUseEntity_1_8 p = new WrappedPacketPlayInUseEntity_1_8(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_8_3) {
            WrappedPacketPlayInUseEntity_1_8_3 p = new WrappedPacketPlayInUseEntity_1_8_3(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_8_8) {
            WrappedPacketPlayInUseEntity_1_8_8 p = new WrappedPacketPlayInUseEntity_1_8_8(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_9) {
            WrappedPacketPlayInUseEntity_1_9 p = new WrappedPacketPlayInUseEntity_1_9(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_9_4) {
            WrappedPacketPlayInUseEntity_1_9_4 p = new WrappedPacketPlayInUseEntity_1_9_4(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_10) {
            WrappedPacketPlayInUseEntity_1_10 p = new WrappedPacketPlayInUseEntity_1_10(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_11) {
            WrappedPacketPlayInUseEntity_1_11 p = new WrappedPacketPlayInUseEntity_1_11(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_12) {
            WrappedPacketPlayInUseEntity_1_12 p = new WrappedPacketPlayInUseEntity_1_12(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_13) {
            WrappedPacketPlayInUseEntity_1_13 p = new WrappedPacketPlayInUseEntity_1_13(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_13_2) {
            WrappedPacketPlayInUseEntity_1_13_2 p = new WrappedPacketPlayInUseEntity_1_13_2(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_14) {
            WrappedPacketPlayInUseEntity_1_14 p = new WrappedPacketPlayInUseEntity_1_14(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_15) {
            WrappedPacketPlayInUseEntity_1_15 p = new WrappedPacketPlayInUseEntity_1_15(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        }
    }
}
