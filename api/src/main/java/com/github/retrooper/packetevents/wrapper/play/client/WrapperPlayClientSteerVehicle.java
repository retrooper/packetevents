package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * Not to be confused with {@link WrapperPlayClientSteerBoat}
 *
 * This packet is for sending player inputs to the server
 *
 * On 1.8 and older, vehicle control is server sided.  This packet includes inputs for movement.
 * On 1.9 and newer, plugins may use this packet to create vehicles out of ordinary entities.
 */
public class WrapperPlayClientSteerVehicle extends PacketWrapper<WrapperPlayClientSteerVehicle> {
    private float sideways;
    private float forward;
    private byte flags;

    public WrapperPlayClientSteerVehicle(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSteerVehicle(float sideways, float forward, byte flags) {
        super(PacketType.Play.Client.STEER_VEHICLE);
        this.sideways = sideways;
        this.forward = forward;
        this.flags = flags;
    }

    @Override
    public void readData() {
        this.sideways = readFloat();
        this.forward = readFloat();
        this.flags = readByte();
    }

    @Override
    public void readData(WrapperPlayClientSteerVehicle wrapper) {
        this.sideways = wrapper.sideways;
        this.forward = wrapper.forward;
        this.flags = wrapper.flags;
    }

    @Override
    public void writeData() {
        writeFloat(sideways);
        writeFloat(forward);
        writeByte(flags);
    }

    public float getSideways() {
        return sideways;
    }

    public void setSideways(float sideways) {
        this.sideways = sideways;
    }

    public float getForward() {
        return forward;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public byte getFlags() {
        return flags;
    }

    public void setFlags(byte flags) {
        this.flags = flags;
    }

    public boolean isJump() {
        return (flags & 0x01) != 0;
    }

    public void setJump(boolean jump) {
        if (jump) {
            flags |= 0x01;
        } else {
            flags &= ~0x01;
        }
    }

    public boolean isUnmount() {
        return (flags & 0x02) != 0;
    }

    public void setUnmount(boolean unmount) {
        if (unmount) {
            flags |= 0x02;
        } else {
            flags &= ~0x02;
        }
    }
}
