package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerRemoveEntityEffect extends PacketWrapper<WrapperPlayServerRemoveEntityEffect> {
    private int entityId;
    private PotionType potionType;

    public WrapperPlayServerRemoveEntityEffect(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerRemoveEntityEffect(int entityId, PotionType potionType) {
        super(PacketType.Play.Server.REMOVE_ENTITY_EFFECT);
        this.entityId = entityId;
        this.potionType = potionType;
    }

    @Override
    public void read() {
        this.entityId = readVarInt();
        int effectId;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18_2)) {
            effectId = readVarInt();
        }
        else {
            effectId = readByte();
        }
        this.potionType = PotionTypes.getById(effectId);
    }

    @Override
    public void write() {
        writeVarInt(entityId);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18_2)) {
            writeVarInt(potionType.getId());
        }
        else {
            writeByte(potionType.getId());
        }
    }

    @Override
    public void copy(WrapperPlayServerRemoveEntityEffect wrapper) {
        entityId = wrapper.entityId;
        potionType = wrapper.potionType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public PotionType getPotionType() {
        return potionType;
    }

    public void setPotionType(PotionType potionType) {
        this.potionType = potionType;
    }
}
