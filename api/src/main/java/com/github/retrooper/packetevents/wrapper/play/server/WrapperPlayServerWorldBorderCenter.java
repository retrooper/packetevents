package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerWorldBorderCenter extends PacketWrapper<WrapperPlayServerWorldBorderCenter> {
    double x;
    double z;

    public WrapperPlayServerWorldBorderCenter(double x, double z) {
        super(PacketType.Play.Server.WORLD_BORDER_CENTER);
        this.x = x;
        this.z = z;
    }

    public WrapperPlayServerWorldBorderCenter(PacketSendEvent event) {
        super(event);
    }

    @Override
    public void readData() {
        this.x = readDouble();
        this.z = readDouble();
    }

    @Override
    public void readData(WrapperPlayServerWorldBorderCenter wrapper) {
        this.x = wrapper.x;
        this.z = wrapper.z;
    }

    @Override
    public void writeData() {
        writeDouble(this.x);
        writeDouble(this.z);
    }

    public double getX() {
        return x;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
