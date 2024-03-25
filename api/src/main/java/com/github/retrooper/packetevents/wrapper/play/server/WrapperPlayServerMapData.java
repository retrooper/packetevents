package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.map.MapIcon;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class WrapperPlayServerMapData extends PacketWrapper<WrapperPlayServerMapData> {

    private int data;
    private int scale;
    private boolean locked;
    private Collection<MapIcon> icons;
    private byte[] colors;
    private int x;
    private int z;
    private int width;
    private int height;

    public WrapperPlayServerMapData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerMapData(int data, int scale, boolean locked, @Nullable Collection<MapIcon> icons, byte[] colors, int x, int z, int width, int height) {
        super(PacketType.Play.Server.MAP_DATA);
        this.data = data;
        this.scale = scale;
        this.locked = locked;
        this.icons = icons;
        this.colors = colors;
        this.x = x;
        this.z = z;
        this.width = width;
        this.height = height;
    }

    @Override
    public void read() {
        this.data = this.readVarInt();
        this.scale = this.readByte();

        boolean readIcons = true;
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            if (this.serverVersion.isOlderThan(ServerVersion.V_1_17)) {
                readIcons = this.readBoolean();
                this.locked = this.readBoolean();
            } else {
                this.locked = this.readBoolean();
                readIcons = this.readBoolean();
            }
        }

        if (readIcons || this.serverVersion.isOlderThan(ServerVersion.V_1_17)) {
            int iconCount = this.readVarInt();
            this.icons = new ArrayList<>(iconCount);
            for (int i = 0; i < iconCount; i++) {
                this.icons.add(new MapIcon(this));
            }
        }

        this.width = this.readUnsignedByte();
        if (this.width > 0) {
            this.height = this.readUnsignedByte();
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
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            writeIcons = this.icons != null;
            if (this.serverVersion.isOlderThan(ServerVersion.V_1_17)) {
                this.writeBoolean(writeIcons);
                this.writeBoolean(this.locked);
            } else {
                this.writeBoolean(this.locked);
                this.writeBoolean(writeIcons);
            }
        }

        if (writeIcons || this.serverVersion.isOlderThan(ServerVersion.V_1_17)) {
            this.writeVarInt(this.icons.size());
            this.icons.forEach(icon -> {
                if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                    this.writeVarInt(icon.getType().getId());
                } else {
                    this.writeByte(icon.getType().getId() << 4 | icon.getRotation() & 15);
                }
                this.writeByte(icon.getX());
                this.writeByte(icon.getY());
                if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                    this.writeByte(icon.getRotation() & 15);
                    if (icon.getName() != null) {
                        this.writeBoolean(true);
                        this.writeComponent(icon.getName());
                    } else {
                        this.writeBoolean(false);
                    }
                }
            });
        }

        this.writeByte(this.width);
        if (this.width > 0) {
            this.writeByte(this.height);
            this.writeByte(this.x);
            this.writeByte(this.z);
            this.writeByteArray(this.colors);
        }
    }

    @Override
    public void copy(WrapperPlayServerMapData wrapper) {
        this.data = wrapper.data;
        this.scale = wrapper.scale;
        this.icons = new ArrayList<>(wrapper.icons.size());
        wrapper.icons.forEach(icon -> this.icons.add(icon.clone()));
        this.width = wrapper.width;
        this.height = wrapper.height;
        this.x = wrapper.x;
        this.z = wrapper.z;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Collection<MapIcon> getIcons() {
        return icons;
    }

    public void setIcons(Collection<MapIcon> icons) {
        this.icons = icons;
    }

    public byte[] getColors() {
        return colors;
    }

    public void setColors(byte[] colors) {
        this.colors = colors;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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
}
