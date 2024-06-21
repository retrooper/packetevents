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
package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.mapdecoration.MapDecorationType;
import com.github.retrooper.packetevents.protocol.item.mapdecoration.MapDecorationTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WrapperPlayServerMapData extends PacketWrapper<WrapperPlayServerMapData> {

    private int mapId;
    private byte scale;
    private boolean trackingPosition;
    private boolean locked;
    private @Nullable List<MapDecoration> decorations;
    private int columns;
    private int rows;
    private int x;
    private int z;
    private byte @Nullable [] data;

    public WrapperPlayServerMapData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerMapData(int mapId, byte scale, @Nullable List<MapDecoration> decorations) {
        this(mapId, scale, false, decorations);
    }

    public WrapperPlayServerMapData(int mapId, byte scale, boolean locked, @Nullable List<MapDecoration> decorations) {
        this(mapId, scale, false, locked, decorations, 0, 0, 0, 0, null);
    }

    public WrapperPlayServerMapData(
            int mapId, byte scale, boolean trackingPosition,
            boolean locked, @Nullable List<MapDecoration> decorations,
            int columns, int rows, int x, int z, byte @Nullable [] data
    ) {
        super(PacketType.Play.Server.MAP_DATA);
        this.mapId = mapId;
        this.scale = scale;
        this.trackingPosition = trackingPosition;
        this.locked = locked;
        this.decorations = decorations;
        this.columns = columns;
        this.rows = rows;
        this.x = x;
        this.z = z;
        this.data = data;
    }

    @Override
    public void read() {
        this.mapId = this.readVarInt();
        this.scale = this.readByte();
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)
                && this.serverVersion.isOlderThan(ServerVersion.V_1_17)) {
            this.trackingPosition = this.readBoolean();
        }
        this.locked = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14) && this.readBoolean();

        if (this.serverVersion.isOlderThan(ServerVersion.V_1_17) || this.readBoolean()) {
            this.decorations = this.readList(MapDecoration::read);
        }

        this.columns = this.readUnsignedByte();
        if (this.columns > 0) {
            this.rows = this.readUnsignedByte();
            this.x = this.readUnsignedByte();
            this.z = this.readUnsignedByte();
            this.data = this.readByteArray();
        }
    }

    @Override
    public void write() {
        this.writeVarInt(this.mapId);
        this.writeByte(this.scale);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)
                && this.serverVersion.isOlderThan(ServerVersion.V_1_17)) {
            this.writeBoolean(this.trackingPosition);
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            this.writeBoolean(this.locked);
        }

        if (this.decorations != null) {
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
                this.writeBoolean(true); // decorations are present
            }
            this.writeList(this.decorations, MapDecoration::write);
        } else if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            this.writeBoolean(false); // not present
        } else {
            // write empty list size
            this.writeVarInt(0);
        }

        if (this.data != null) {
            this.writeByte(this.columns);
            if (this.columns > 0) {
                this.writeByte(this.rows);
                this.writeByte(this.x);
                this.writeByte(this.z);
                this.writeByteArray(this.data);
            }
        } else {
            this.writeByte(0); // 0 columns mean no data
        }
    }

    public static class MapDecoration {

        private MapDecorationType type;
        private byte x;
        private byte y;
        private byte direction; // 0 to 15
        private @Nullable Component displayName;

        public MapDecoration(MapDecorationType type, byte x, byte y, byte direction, @Nullable Component displayName) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.displayName = displayName;
        }

        public static MapDecoration read(PacketWrapper<?> wrapper) {
            // first 4 bits determine decoration type, last 4 bits determine rotation below 1.13
            boolean v113 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13);
            byte flags = v113 ? 0 : wrapper.readByte();
            MapDecorationType type = !v113
                    ? MapDecorationTypes.getById(wrapper.getServerVersion().toClientVersion(), (flags >> 4) & 0xF)
                    : wrapper.readMappedEntity(MapDecorationTypes::getById);

            byte x = wrapper.readByte();
            byte y = wrapper.readByte();
            byte direction = (byte) ((v113 ? wrapper.readByte() : flags) & 0xF);
            Component displayName = v113 ? wrapper.readOptional(PacketWrapper::readComponent) : null;
            return new MapDecoration(type, x, y, direction, displayName);
        }

        public static void write(PacketWrapper<?> wrapper, MapDecoration decoration) {
            boolean v113 = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13);
            if (v113) {
                wrapper.writeMappedEntity(decoration.getType());
            } else {
                int typeId = decoration.getType().getId(wrapper.getServerVersion().toClientVersion());
                wrapper.writeByte((typeId & 0xF) << 4 | (decoration.getDirection() & 0xF));
            }

            wrapper.writeByte(decoration.getX());
            wrapper.writeByte(decoration.getY());
            if (v113) {
                wrapper.writeByte(decoration.getDirection());
                wrapper.writeOptional(decoration.getDisplayName(), PacketWrapper::writeComponent);
            }
        }

        public MapDecorationType getType() {
            return this.type;
        }

        public void setType(MapDecorationType type) {
            this.type = type;
        }

        public byte getX() {
            return this.x;
        }

        public void setX(byte x) {
            this.x = x;
        }

        public byte getY() {
            return this.y;
        }

        public void setY(byte y) {
            this.y = y;
        }

        public byte getDirection() {
            return this.direction;
        }

        public void setDirection(byte direction) {
            this.direction = direction;
        }

        public @Nullable Component getDisplayName() {
            return this.displayName;
        }

        public void setDisplayName(@Nullable Component displayName) {
            this.displayName = displayName;
        }
    }

    public int getMapId() {
        return this.mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public byte getScale() {
        return this.scale;
    }

    public void setScale(byte scale) {
        this.scale = scale;
    }

    public boolean isTrackingPosition() {
        return this.trackingPosition;
    }

    public void setTrackingPosition(boolean trackingPosition) {
        this.trackingPosition = trackingPosition;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public @Nullable List<MapDecoration> getDecorations() {
        return this.decorations;
    }

    public void setDecorations(@Nullable List<MapDecoration> decorations) {
        this.decorations = decorations;
    }

    public int getColumns() {
        return this.columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public byte @Nullable [] getData() {
        return this.data;
    }

    public void setData(byte @Nullable [] data) {
        this.data = data;
    }
}
