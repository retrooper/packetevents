package me.purplex.packetevents.packetwrappers.in.use_entity;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.packetwrappers.api.WrappedPacket;
import me.purplex.packetevents.packetwrappers.in.use_entity.impl.*;
import org.bukkit.entity.Entity;

public class WrappedPacketInUseEntity extends WrappedPacket {

    public Entity entity;
    public EntityUseAction action;

    public WrappedPacketInUseEntity(Object packet) {
        super(packet);
    }



    @Override
    protected void setup() {
        if(version == ServerVersion.v_1_7_10) {
            WrappedPacketInUseEntity_1_7_10 p = new WrappedPacketInUseEntity_1_7_10(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        }
        else if (version == ServerVersion.v_1_8) {
            WrappedPacketInUseEntity_1_8 p = new WrappedPacketInUseEntity_1_8(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_8_3) {
            WrappedPacketUseEntity_1_8_3 p = new WrappedPacketUseEntity_1_8_3(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_8_8) {
            WrappedPacketInUseEntity_1_8_8 p = new WrappedPacketInUseEntity_1_8_8(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_9) {
            WrappedPacketInUseEntity_1_9 p = new WrappedPacketInUseEntity_1_9(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_9_4) {
            WrappedPacketInUseEntity_1_9_4 p = new WrappedPacketInUseEntity_1_9_4(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_10) {
            WrappedPacketInUseEntity_1_10 p = new WrappedPacketInUseEntity_1_10(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_11) {
            WrappedPacketInUseEntity_1_11 p = new WrappedPacketInUseEntity_1_11(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_12) {
            WrappedPacketInUseEntity_1_12 p = new WrappedPacketInUseEntity_1_12(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_13) {
            WrappedPacketInUseEntity_1_13 p = new WrappedPacketInUseEntity_1_13(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_13_2) {
            WrappedPacketInUseEntity_1_13_2 p = new WrappedPacketInUseEntity_1_13_2(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_14) {
            WrappedPacketInUseEntity_1_14 p = new WrappedPacketInUseEntity_1_14(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        } else if (version == ServerVersion.v_1_15) {
            WrappedPacketInUseEntity_1_15 p = new WrappedPacketInUseEntity_1_15(packet);
            this.entity = p.getEntity();
            this.action = p.getEntityUseAction();
        }
    }
}
