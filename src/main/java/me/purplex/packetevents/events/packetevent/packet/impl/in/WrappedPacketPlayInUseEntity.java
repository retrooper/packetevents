package me.purplex.packetevents.events.packetevent.packet.impl.in;

import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.enums.ServerVersion;
import me.purplex.packetevents.events.packetevent.packet.impl.in._1_10.WrappedPacketPlayInUseEntity_1_10;
import me.purplex.packetevents.events.packetevent.packet.impl.in._1_11.WrappedPacketPlayInUseEntity_1_11;
import me.purplex.packetevents.events.packetevent.packet.impl.in._1_12.WrappedPacketPlayInUseEntity_1_12;
import me.purplex.packetevents.events.packetevent.packet.impl.in._1_13.WrappedPacketPlayInUseEntity_1_13;
import me.purplex.packetevents.events.packetevent.packet.impl.in._1_13_2.WrappedPacketPlayInUseEntity_1_13_2;
import me.purplex.packetevents.events.packetevent.packet.impl.in._1_14.WrappedPacketPlayInUseEntity_1_14;
import me.purplex.packetevents.events.packetevent.packet.impl.in._1_15.WrappedPacketPlayInUseEntity_1_15;
import me.purplex.packetevents.events.packetevent.packet.impl.in._1_8.WrappedPacketPlayInUseEntity_1_8;
import me.purplex.packetevents.events.packetevent.packet.impl.in._1_8_3.WrappedPacketPlayInUseEntity_1_8_3;
import me.purplex.packetevents.events.packetevent.packet.impl.in._1_8_8.WrappedPacketPlayInUseEntity_1_8_8;
import me.purplex.packetevents.events.packetevent.packet.impl.in._1_9.WrappedPacketPlayInUseEntity_1_9;
import me.purplex.packetevents.events.packetevent.packet.impl.in._1_9_4.WrappedPacketPlayInUseEntity_1_9_4;
import org.bukkit.entity.Entity;

public class WrappedPacketPlayInUseEntity {
    private final ServerVersion version;
    private Entity entity;
    private EntityUseAction action;
    private final Object packet;

    public WrappedPacketPlayInUseEntity(Object packet) {
        this.version = ServerVersion.getVersion();
        this.packet = packet;
        setupEntity();
        setupAction();
    }

    private Object getRawPacket() {
        return this.packet;
    }

    public Entity getEntity() {
        return entity;
    }

    public EntityUseAction getEntityUseAction() {
        return action;
    }

    private void setupEntity() {
        if (version == ServerVersion.v_1_8) {
            this.entity = new WrappedPacketPlayInUseEntity_1_8(packet).getEntity();
        } else if (version == ServerVersion.v_1_8_3) {
            this.entity = new WrappedPacketPlayInUseEntity_1_8_3(packet).getEntity();
        } else if (version == ServerVersion.v_1_8_8) {
            this.entity = new WrappedPacketPlayInUseEntity_1_8_8(packet).getEntity();
        } else if (version == ServerVersion.v_1_9) {
            this.entity = new WrappedPacketPlayInUseEntity_1_9(packet).getEntity();
        } else if (version == ServerVersion.v_1_9_4) {
            this.entity = new WrappedPacketPlayInUseEntity_1_9_4(packet).getEntity();
        } else if (version == ServerVersion.v_1_10) {
            this.entity = new WrappedPacketPlayInUseEntity_1_10(packet).getEntity();
        } else if (version == ServerVersion.v_1_11) {
            this.entity = new WrappedPacketPlayInUseEntity_1_11(packet).getEntity();
        } else if (version == ServerVersion.v_1_12) {
            this.entity = new WrappedPacketPlayInUseEntity_1_12(packet).getEntity();
        } else if (version == ServerVersion.v_1_13) {
            this.entity = new WrappedPacketPlayInUseEntity_1_13(packet).getEntity();
        } else if (version == ServerVersion.v_1_13_2) {
            this.entity = new WrappedPacketPlayInUseEntity_1_13_2(packet).getEntity();
        } else if (version == ServerVersion.v_1_14) {
            this.entity = new WrappedPacketPlayInUseEntity_1_14(packet).getEntity();
        } else if (version == ServerVersion.v_1_15) {
            this.entity = new WrappedPacketPlayInUseEntity_1_15(packet).getEntity();
        }
    }

    private void setupAction() {
        if (version == ServerVersion.v_1_8) {
            this.action = new WrappedPacketPlayInUseEntity_1_8(packet).getEntityUseAction();
        } else if (version == ServerVersion.v_1_8_3) {
            this.action = new WrappedPacketPlayInUseEntity_1_8_3(packet).getEntityUseAction();
        } else if (version == ServerVersion.v_1_8_8) {
            this.action = new WrappedPacketPlayInUseEntity_1_8_8(packet).getEntityUseAction();
        } else if (version == ServerVersion.v_1_9) {
            this.action = new WrappedPacketPlayInUseEntity_1_9(packet).getEntityUseAction();
        } else if (version == ServerVersion.v_1_9_4) {
            this.action = new WrappedPacketPlayInUseEntity_1_9_4(packet).getEntityUseAction();
        } else if (version == ServerVersion.v_1_10) {
            this.action = new WrappedPacketPlayInUseEntity_1_10(packet).getEntityUseAction();
        } else if (version == ServerVersion.v_1_11) {
            this.action = new WrappedPacketPlayInUseEntity_1_11(packet).getEntityUseAction();
        } else if (version == ServerVersion.v_1_12) {
            this.action = new WrappedPacketPlayInUseEntity_1_12(packet).getEntityUseAction();
        } else if (version == ServerVersion.v_1_13) {
            this.action = new WrappedPacketPlayInUseEntity_1_13(packet).getEntityUseAction();
        } else if (version == ServerVersion.v_1_13_2) {
            this.action = new WrappedPacketPlayInUseEntity_1_13_2(packet).getEntityUseAction();
        } else if (version == ServerVersion.v_1_14) {
            this.action = new WrappedPacketPlayInUseEntity_1_14(packet).getEntityUseAction();
        } else if (version == ServerVersion.v_1_15) {
            this.action = new WrappedPacketPlayInUseEntity_1_15(packet).getEntityUseAction();
        }
    }
}
