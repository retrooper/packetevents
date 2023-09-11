package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.event.PacketSendEvent;

public class WrapperPlayServerHurtAnimation extends PacketWrapper<WrapperPlayServerHurtAnimation> {
    private int entityId;
    private float yaw;

    public WrapperPlayServerHurtAnimation(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerHurtAnimation(int entityId, float yaw) {
        super(PacketType.Play.Server.HURT_ANIMATION);
        this.entityId = entityId;
        this.yaw = yaw;
    }

    @Override
    public void read() {
        entityId = readVarInt();
        yaw = readFloat();
    }

    @Override
    public void write() {
        writeVarInt(entityId);
        writeFloat(yaw);
    }

    @Override
    public void copy(WrapperPlayServerHurtAnimation wrapper) {
        this.entityId = wrapper.entityId;
        this.yaw = wrapper.yaw;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
