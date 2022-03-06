package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerOpenHorseWindow extends PacketWrapper<WrapperPlayServerOpenHorseWindow> {
    int windowId;
    int slotCount;
    int entityId;

    public WrapperPlayServerOpenHorseWindow(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerOpenHorseWindow(int windowId, int slotCount, int entityId) {
        super(PacketType.Play.Server.OPEN_HORSE_WINDOW);
        this.windowId = windowId;
        this.slotCount = slotCount;
        this.entityId = entityId;
    }

    @Override
    public void readData() {
        this.windowId = readUnsignedByte();
        this.slotCount = readVarInt();
        this.entityId = readInt();
    }

    @Override
    public void readData(WrapperPlayServerOpenHorseWindow other) {
        this.windowId = other.windowId;
        this.slotCount = other.slotCount;
        this.entityId = other.entityId;
    }

    @Override
    public void writeData() {
        writeByte(windowId);
        writeVarInt(slotCount);
        writeInt(entityId);
    }

    public int getWindowId() {
        return windowId;
    }

    public void setWindowId(int windowId) {
        this.windowId = windowId;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        this.slotCount = slotCount;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }
}
