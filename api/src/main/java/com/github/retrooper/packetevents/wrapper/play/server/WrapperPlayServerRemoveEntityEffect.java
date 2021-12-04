package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerRemoveEntityEffect extends PacketWrapper<WrapperPlayServerRemoveEntityEffect> {
    int entityId;
    int effectId;

    public WrapperPlayServerRemoveEntityEffect(PacketSendEvent event) {
        super(event);
    }

    @Override
    public void readData() {
        this.entityId = readVarInt();
        this.effectId = readByte();
    }

    @Override
    public void writeData() {
        writeVarInt(entityId);
        writeByte(effectId);
    }

    @Override
    public void readData(WrapperPlayServerRemoveEntityEffect wrapper) {
        entityId = wrapper.entityId;
        effectId = wrapper.effectId;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEffectId() {
        return effectId;
    }

    public void setEffectId(int effectId) {
        this.effectId = effectId;
    }

    public PotionType getPotionType() {
        return PotionTypes.getById(effectId);
    }

    public void setPotionType(PotionType potionType) {
        this.effectId = potionType.getId();
    }
}
