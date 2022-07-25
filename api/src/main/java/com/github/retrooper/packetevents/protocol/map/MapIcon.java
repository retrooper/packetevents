package com.github.retrooper.packetevents.protocol.map;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMapData;

public class MapIcon implements Cloneable {

    private final Type type;
    private final int x;
    private final int y;
    private final int rotation;

    public MapIcon(Type type, int x, int y, int rotation) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public MapIcon(PacketWrapper<WrapperPlayServerMapData> packetWrapper) {
        short s = packetWrapper.readByte();
        this.type = Type.getById((byte) (s >> 4 & 15));
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

    public Type getType() {
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

    public enum Type {
        WHITE_POINTER(0),
        GREEN_POINTER(1),
        RED_POINTER(2),
        BLUE_POINTER(3),
        WHITE_CROSS(4),
        RED_MARKER(5),
        WHITE_CIRCLE(6),
        SMALL_WHITE_CIRCLE(7),
        MANSION(8),
        TEMPLE(9),
        BANNER_WHITE(10),
        BANNER_ORANGE(11),
        BANNER_MAGENTA(12),
        BANNER_LIGHT_BLUE(13),
        BANNER_YELLOW(14),
        BANNER_LIME(15),
        BANNER_PINK(16),
        BANNER_GRAY(17),
        BANNER_LIGHT_GRAY(18),
        BANNER_CYAN(19),
        BANNER_PURPLE(20),
        BANNER_BLUE(21),
        BANNER_BROWN(22),
        BANNER_GREEN(23),
        BANNER_RED(24),
        BANNER_BLACK(25),
        RED_X(26);

        private final byte id;

        Type(int value) {
            this.id = (byte) value;
        }

        public byte getId() {
            return id;
        }

        public static Type getById(byte value) {
            for (Type t : values()) {
                if (t.id == value) return t;
            }
            return null;
        }
    }
}