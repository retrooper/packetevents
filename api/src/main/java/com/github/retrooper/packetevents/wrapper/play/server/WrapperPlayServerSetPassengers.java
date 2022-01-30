package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSetPassengers extends PacketWrapper<WrapperPlayServerSetPassengers> {
    int entityId;
    int[] passengers;

    public WrapperPlayServerSetPassengers(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSetPassengers(int entityId, int[] passengers) {
        super(PacketType.Play.Server.SET_PASSENGERS);
        this.entityId = entityId;
        this.passengers = passengers;
    }

    @Override
    public void readData() {
        this.entityId = readVarInt();
        this.passengers = readVarIntArray();
    }

    @Override
    public void writeData() {
        writeVarInt(entityId);
        writeVarIntArray(passengers);
    }

    @Override
    public void readData(WrapperPlayServerSetPassengers wrapper) {
        entityId = wrapper.entityId;
        passengers = wrapper.passengers;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int[] getPassengers() {
        return passengers;
    }

    public void setPassengers(int[] passengers) {
        this.passengers = passengers;
    }
}
