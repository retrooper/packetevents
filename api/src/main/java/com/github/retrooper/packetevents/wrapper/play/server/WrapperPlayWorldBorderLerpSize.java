package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayWorldBorderLerpSize extends PacketWrapper<WrapperPlayWorldBorderLerpSize> {
    double oldDiameter;
    double newDiameter;
    long speed;

    public WrapperPlayWorldBorderLerpSize(double oldDiameter, double newDiameter, long speed) {
        super(PacketType.Play.Server.WORLD_BORDER_LERP_SIZE);
        this.oldDiameter = oldDiameter;
        this.newDiameter = newDiameter;
        this.speed = speed;
    }

    public WrapperPlayWorldBorderLerpSize(PacketSendEvent event) {
        super(event);
    }

    @Override
    public void readData() {
        oldDiameter = readDouble();
        newDiameter = readDouble();
        speed = readVarLong();
    }

    @Override
    public void readData(WrapperPlayWorldBorderLerpSize packet) {
        this.oldDiameter = packet.oldDiameter;
        this.newDiameter = packet.newDiameter;
        this.speed = packet.speed;
    }

    @Override
    public void writeData() {
        writeDouble(oldDiameter);
        writeDouble(newDiameter);
        writeVarLong(speed);
    }

    public double getOldDiameter() {
        return oldDiameter;
    }

    public double getNewDiameter() {
        return newDiameter;
    }

    public long getSpeed() {
        return speed;
    }

    public void setOldDiameter(double oldDiameter) {
        this.oldDiameter = oldDiameter;
    }

    public void setNewDiameter(double newDiameter) {
        this.newDiameter = newDiameter;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }
}
