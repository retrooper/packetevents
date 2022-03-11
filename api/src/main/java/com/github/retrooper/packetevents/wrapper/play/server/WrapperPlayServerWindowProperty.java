package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerWindowProperty extends PacketWrapper<WrapperPlayServerWindowProperty> {

    private byte containerId;
    private int id;
    private int value;

    public WrapperPlayServerWindowProperty(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerWindowProperty(byte containerId, int id, int value) {
        super(PacketType.Play.Server.WINDOW_PROPERTY);
        this.containerId = containerId;
        this.id = id;
        this.value = value;
    }

    @Override
    public void read() {
        this.containerId = (byte) readUnsignedByte();
        this.id = readShort();
        this.value = readShort();
    }

    @Override
    public void copy(WrapperPlayServerWindowProperty wrapper) {
        this.containerId = wrapper.containerId;
        this.id = wrapper.id;
        this.value = wrapper.value;
    }

    @Override
    public void write() {
        writeByte(this.containerId);
        writeShort(this.id);
        writeShort(this.value);
    }

    public byte getContainerId() {
        return containerId;
    }

    public void setContainerId(byte containerId) {
        this.containerId = containerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
