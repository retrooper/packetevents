package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class WrapperPlayServerMapData extends PacketWrapper<WrapperPlayServerMapData> {

    private int data;
    private int scale;
    private boolean locked;
    private Collection<Icon> icons;
    private byte[] buffer;
    private int column;
    private int rows;
    private int x;
    private int z;

    public WrapperPlayServerMapData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerMapData(int data,
                                    int scale,
                                    boolean locked,
                                    @Nullable Collection<Icon> icons,
                                    byte[] buffer,
                                    int x, int z,
                                    int column, int rows) {
        super(PacketType.Play.Server.MAP_DATA);
        this.data = data;
        this.scale = scale;
        this.locked = locked;
        this.icons = icons;
        this.buffer = buffer;
        this.column = column;
        this.rows = rows;
        this.x = x;
        this.z = z;
    }

    @Override
    public void read() {
        this.data = this.readVarInt();
        this.scale = this.readByte();

        boolean readIcons = true;
        if (this.clientVersion.isNewerThanOrEquals(ClientVersion.V_1_9)) {
            if (this.clientVersion.isOlderThan(ClientVersion.V_1_17)) {
                readIcons = this.readBoolean();
                this.locked = this.readBoolean();
            } else {
                this.locked = this.readBoolean();
                readIcons = this.readBoolean();
            }
        }

        if (readIcons || this.clientVersion.isOlderThan(ClientVersion.V_1_17)) {
            int iconCount = this.readVarInt();
            this.icons = new ArrayList<>(iconCount);
            for (int i = 0; i < iconCount; i++) {
                this.icons.add(new Icon(this));
            }
        }

        this.column = this.readUnsignedByte();
        if (this.column > 0) {
            this.rows = this.readUnsignedByte();
            this.x = this.readUnsignedByte();
            this.z = this.readUnsignedByte();
            this.buffer = this.readByteArray();
        }
    }

    @Override
    public void write() {
        this.writeVarInt(this.data);
        this.writeByte(this.scale);

        boolean writeIcons = true;
        if (this.clientVersion.isNewerThanOrEquals(ClientVersion.V_1_9)) {
            writeIcons = this.icons != null;
            if (this.clientVersion.isOlderThan(ClientVersion.V_1_17)) {
                this.writeBoolean(writeIcons);
                this.writeBoolean(this.locked);
            } else {
                this.writeBoolean(this.locked);
                this.writeBoolean(writeIcons);
            }
        }

        if (writeIcons || this.clientVersion.isOlderThan(ClientVersion.V_1_17)) {
            this.writeVarInt(this.icons.size());
            this.icons.forEach(icon -> {
                this.writeByte((icon.getType() & 15) << 4 | icon.getRotation() & 15);
                this.writeByte(icon.getX());
                this.writeByte(icon.getY());
            });
        }

        this.writeByte(this.column);
        if (this.column > 0) {
            this.writeByte(this.rows);
            this.writeByte(this.x);
            this.writeByte(this.z);
            this.writeByteArray(this.buffer);
        }
    }

    @Override
    public void copy(WrapperPlayServerMapData wrapper) {
        this.data = wrapper.data;
        this.scale = wrapper.scale;
        this.icons = new ArrayList<>(wrapper.icons.size());
        wrapper.icons.forEach(icon -> this.icons.add(icon.clone()));
        this.column = wrapper.column;
        this.rows = wrapper.rows;
        this.x = wrapper.x;
        this.z = wrapper.z;
    }

    public static class Icon implements Cloneable {

        private final byte type;
        private final byte x;
        private final byte y;
        private final byte rotation;

        public Icon(byte type, byte x, byte y, byte rotation) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.rotation = rotation;
        }

        public Icon(PacketWrapper<WrapperPlayServerMapData> packetWrapper) {
            short s = packetWrapper.readByte();

            this.type = (byte)(s >> 4 & 15);
            this.x = packetWrapper.readByte();
            this.y = packetWrapper.readByte();
            this.rotation = (byte) (s & 15);
        }

        @Override
        public Icon clone() {
            try {
                return (Icon) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }

        public byte getType() {
            return type;
        }

        public byte getX() {
            return x;
        }

        public byte getY() {
            return y;
        }

        public byte getRotation() {
            return rotation;
        }
    }
}
