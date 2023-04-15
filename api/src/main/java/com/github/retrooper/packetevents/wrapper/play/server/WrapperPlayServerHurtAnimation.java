package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerHurtAnimation extends PacketWrapper<WrapperPlayServerHurtAnimation> {
    int entityId;
    float hurtDir;

    public WrapperPlayServerHurtAnimation() {
        super(PacketType.Play.Server.HURT_ANIMATION);
    }

    @Override
    public void read() {
        entityId = readVarInt();
        hurtDir = readFloat();
    }

    @Override
    public void write() {
        writeVarInt(entityId);
        writeFloat(hurtDir);
    }

    @Override
    public void copy(WrapperPlayServerHurtAnimation wrapper) {
        this.entityId = wrapper.entityId;
        this.hurtDir = wrapper.hurtDir;
    }
}
