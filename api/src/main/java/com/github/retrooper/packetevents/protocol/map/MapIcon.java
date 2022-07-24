package com.github.retrooper.packetevents.protocol.map;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMapData;

public class MapIcon implements Cloneable {

    private final int type;
    private final int x;
    private final int y;
    private final int rotation;

    public MapIcon(int type, int x, int y, int rotation) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public MapIcon(PacketWrapper<WrapperPlayServerMapData> packetWrapper) {
        short s = packetWrapper.readByte();
        this.type = (byte) (s >> 4 & 15);
        this.x = packetWrapper.readByte();
        this.y = packetWrapper.readByte();
        this.rotation = (byte) (s & 15);
    }

    @Override
    public MapIcon clone() {
        try {
            return (MapIcon) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public int getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRotation() {
        return rotation;
    }
}