/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package shadow.utils.holders.packet.custom;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WrapperPlayServerMapData extends PacketWrapper<WrapperPlayServerMapData> {

    private int mapId;
    private byte scale;
    private boolean locked;
    private MapIcon[] icons;
    private int columns, rows, x, z;
    private byte[] bytes;

    public WrapperPlayServerMapData(PacketSendEvent event) {
        super(event);
    }

    //The only change occurred here, where the array's length became optional
    //https://wiki.vg/index.php?title=Protocol&oldid=17753#Map_Data 1.19
    //https://wiki.vg/index.php?title=Protocol&oldid=18067#Map_Data 1.19.2

    //hasIcons can also be called trackingPositions, but both ultimately do the same

    public WrapperPlayServerMapData(int mapId, byte scale, boolean locked, @Nullable MapIcon[] icons) {
        super(PacketType.Play.Server.MAP_DATA);
        this.mapId = mapId;
        this.scale = scale;
        this.locked = locked;
        this.icons = icons;
    }

    public WrapperPlayServerMapData(int mapId, byte scale, boolean locked, @Nullable MapIcon[] icons, int columns, int rows, int x, int z, @NotNull byte[] bytes) {
        super(PacketType.Play.Server.MAP_DATA);
        this.mapId = mapId;
        this.scale = scale;
        this.locked = locked;
        this.icons = icons;
        this.columns = columns;
        this.rows = rows;
        this.x = x;
        this.z = z;
        this.bytes = bytes;
    }

    //No version changes fixes necessary here
    @Override
    public void read() {
        this.mapId = readVarInt();
        this.scale = readByte();
        this.locked = readBoolean();

        boolean hasIcons = this.readBoolean();
        if (hasIcons) {
            int length = this.readVarInt();
            MapIcon[] icons = new MapIcon[length];
            for (int i = 0; i < length; i++)
                icons[i] = new MapIcon(MapIconType.ofId(this.readVarInt()), this.readByte(), this.readByte(), this.readByte(), this.readBoolean() ? this.readComponent() : null);
            this.icons = icons;
        }
        this.columns = this.readByte();
        if (columns > 0) {
            this.rows = this.readByte();
            this.x = this.readByte();
            this.z = this.readByte();
            this.bytes = this.readByteArrayOfSize(this.readVarInt());
        }
    }

    @Override
    public void write() {
        ServerVersion version = this.getServerVersion();

        writeVarInt(this.mapId);
        writeByte(scale);//0-4
        writeBoolean(locked);
        boolean hasIcons = icons != null;

        writeBoolean(hasIcons);//hasIcons/trackingPosition, whatever you call it
        if (hasIcons) {
            writeVarInt(icons.length);
            for (MapIcon icon : icons) {
                writeVarInt(icon.getType().getId());
                writeByte(icon.getX());
                writeByte(icon.getZ());
                writeByte(icon.getDirection());
                boolean hasDisplayName = icon.getDisplayName() != null;
                writeBoolean(hasDisplayName);
                if (hasDisplayName) writeComponent(icon.getDisplayName());
            }
        } else if (version.isOlderThanOrEquals(ServerVersion.V_1_19))
            writeVarInt(0);//The array is not optional on 1.19 and below, so if it's missing, we have to at least write that it's length is 0

        writeByte(columns);
        if (columns > 0) {
            writeByte(rows);
            writeByte(x);
            writeByte(z);
            writeVarInt(bytes.length);
            writeBytes(bytes);
        }
    }

    public static class MapIcon {

        private MapIconType type;
        private byte x, z, direction;
        private Component displayName;

        public MapIcon(MapIconType type, byte x, byte z, byte direction, @Nullable Component displayName) {
            this.type = type;
            this.x = x;
            this.z = z;
            this.direction = direction;//0-15
            this.displayName = displayName;
        }

        public MapIconType getType() {
            return type;
        }

        public void setType(MapIconType type) {
            this.type = type;
        }

        public Component getDisplayName() {
            return displayName;
        }

        public void setDisplayName(Component displayName) {
            this.displayName = displayName;
        }

        public byte getDirection() {
            return direction;
        }

        public void setDirection(byte direction) {
            this.direction = direction;
        }

        public byte getZ() {
            return z;
        }

        public void setZ(byte z) {
            this.z = z;
        }

        public byte getX() {
            return x;
        }

        public void setX(byte x) {
            this.x = x;
        }
    }

    //the id is equivalent to their ordinal
    public enum MapIconType {
        WHITE_ARROW,
        GREEN_ARROW,
        RED_ARROW,
        BLUE_ARROW,
        WHITE_CROSS,
        RED_POINTER,
        WHITE_CIRCLE,
        SMALL_WHITE_CIRCLE,
        MANSION,
        TEMPLE,
        WHITE_BANNER,
        ORANGE_BANNER,
        MAGENTA_BANNER,
        LIGHT_BLUE_BANNER,
        YELLOW_BANNER,
        LIME_BANNER,
        PINK_BANNER,
        GRAY_BANNER,
        LIGHT_GRAY_BANNER,
        CYAN_BANNER,
        PURPLE_BANNER,
        BLUE_BANNER,
        BROWN_BANNER,
        GREEN_BANNER,
        RED_BANNER,
        BLACK_BANNER,
        TREASURE_MARKER;

        public int getId() {
            return this.ordinal();
        }

        public static MapIconType ofId(int id) {
            return id < MapIconType.values().length && id >= 0 ? MapIconType.values()[id] : null;
        }
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(@NotNull byte[] bytes) {
        this.bytes = bytes;
    }

    public void setColumns(byte columns) {
        this.columns = columns;
    }

    public MapIcon[] getIcons() {
        return icons;
    }

    public void setIcons(@Nullable MapIcon[] icons) {
        this.icons = icons;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public byte getScale() {
        return scale;
    }

    public void setScale(byte scale) {
        this.scale = scale;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
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