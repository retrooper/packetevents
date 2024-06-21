package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.chunk.LightData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUpdateLight extends PacketWrapper<WrapperPlayServerUpdateLight> {

    private int x;
    private int z;
    private LightData lightData;

    public WrapperPlayServerUpdateLight(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUpdateLight(int x, int z, LightData lightData) {
        super(PacketType.Play.Server.UPDATE_LIGHT);
        this.x = x;
        this.z = z;
        this.lightData = lightData;
    }

    @Override
    public void read() {
        x = readVarInt();
        z = readVarInt();
        lightData = LightData.read(this);
    }

    @Override
    public void write() {
        writeVarInt(x);
        writeVarInt(z);
        LightData.write(this, lightData);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public LightData getLightData() {
        return lightData;
    }

    public void setLightData(LightData lightData) {
        this.lightData = lightData;
    }

    @Override
    public void copy(WrapperPlayServerUpdateLight wrapper) {
        x = wrapper.x;
        z = wrapper.z;
        lightData = wrapper.lightData.clone();
    }
}