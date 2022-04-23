package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerWorldBorderSize extends PacketWrapper<WrapperPlayServerWorldBorderSize> {
    double diameter;

    public WrapperPlayServerWorldBorderSize(double diameter) {
        super(PacketType.Play.Server.WORLD_BORDER_SIZE);
        this.diameter = diameter;
    }

    public WrapperPlayServerWorldBorderSize(PacketSendEvent event) {
        super(event);
    }

    @Override
    public void read() {
        diameter = readDouble();
    }

    @Override
    public void copy(WrapperPlayServerWorldBorderSize packet) {
        diameter = packet.diameter;
    }

    @Override
    public void write() {
        writeDouble(diameter);
    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }
}
