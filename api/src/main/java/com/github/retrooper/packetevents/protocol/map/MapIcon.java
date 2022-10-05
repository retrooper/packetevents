package com.github.retrooper.packetevents.protocol.map;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMapData;
import net.kyori.adventure.text.Component;

import java.util.Objects;

public class MapIcon implements Cloneable {

    private final Type type;
    private final int x;
    private final int y;
    private final int rotation;
    private final Component name;

    public MapIcon(Type type, int x, int y, int rotation, Component name) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.name = name;
    }

    public MapIcon(PacketWrapper<WrapperPlayServerMapData> packetWrapper) {
        byte rotation = 0;
        if (packetWrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.type = Type.values()[packetWrapper.readVarInt()];
        } else {
            short s = packetWrapper.readByte();
            this.type = MapIcon.Type.getById((byte) (s >> 4));
            rotation = (byte) (s & 15);
        }
        this.x = packetWrapper.readByte();
        this.y = packetWrapper.readByte();
        if (packetWrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
            rotation = packetWrapper.readByte();
            if (packetWrapper.readBoolean()) {
                this.name = packetWrapper.readComponent();
            } else {
                this.name = null;
            }
        } else {
            this.name = null;
        }
        this.rotation = rotation;
    }

    public MapIcon clone() {
        try {
            return (MapIcon) super.clone();
        } catch (CloneNotSupportedException var2) {
            throw new AssertionError();
        }
    }

    public Type getType() {
        return this.type;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getRotation() {
        return this.rotation;
    }

    public Component getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapIcon mapIcon = (MapIcon) o;
        return x == mapIcon.x && y == mapIcon.y && rotation == mapIcon.rotation && type == mapIcon.type && Objects.equals(name, mapIcon.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, x, y, rotation, name);
    }

    public static enum Type {
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

        private static final Type[] VALUES = values();
        private final byte id;

        private Type(int value) {
            this.id = (byte) value;
        }

        public byte getId() {
            return this.id;
        }

        public static Type getById(byte value) {
            for (Type t : VALUES) {
                if (t.id == value) {
                    return t;
                }
            }

            return null;
        }
    }
}
