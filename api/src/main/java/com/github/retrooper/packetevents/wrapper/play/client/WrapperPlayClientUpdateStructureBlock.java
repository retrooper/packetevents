/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.states.enums.Mirror;
import com.github.retrooper.packetevents.protocol.world.states.enums.Mode;
import com.github.retrooper.packetevents.protocol.world.states.enums.Rotation;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Range;

import java.util.Optional;

public class WrapperPlayClientUpdateStructureBlock extends PacketWrapper<WrapperPlayClientUpdateStructureBlock> {
    private static final int IGNORE_ENTITIES = 0x01;
    private static final int SHOW_AIR = 0x02;
    private static final int SHOW_BOUNDING_BOX = 0x04;

    private Vector3i position;
    private Action action;
    private Mode mode;
    private String name;
    private byte offsetX;
    private byte offsetY;
    private byte offsetZ;
    private byte sizeX;
    private byte sizeY;
    private byte sizeZ;
    private Mirror mirror;
    private Rotation rotation;
    private String metadata;
    private float integrity;
    private float seed;
    private boolean ignoreEntities;
    private boolean showAir;
    private boolean showBoundingBox;
    private short flags;

    public WrapperPlayClientUpdateStructureBlock(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientUpdateStructureBlock(Vector3i position, Action action, Mode mode, String name,
                                                 @Range(from = -32, to = 32) byte offsetX, @Range(from = -32, to = 32) byte offsetY,
                                                 @Range(from = -32, to = 32) byte offsetZ, @Range(from = -32, to = 32) byte sizeX,
                                                 @Range(from = -32, to = 32) byte sizeY, @Range(from = -32, to = 32) byte sizeZ,
                                                 Mirror mirror, Rotation rotation, String metadata, boolean ignoreEntities,
                                                 boolean showAir, boolean showBoundingBox, short flags) {
        super(PacketType.Play.Client.UPDATE_STRUCTURE_BLOCK);
        this.position = position;
        this.action = action;
        this.mode = mode;
        this.name = name;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.mirror = mirror;
        this.rotation = rotation;
        this.metadata = metadata;
        this.ignoreEntities = ignoreEntities;
        this.showAir = showAir;
        this.showBoundingBox = showBoundingBox;
        this.flags = flags;
    }

    public WrapperPlayClientUpdateStructureBlock(Vector3i position, Action action, Mode mode, String name,
                                                 @Range(from = -32, to = 32) byte offsetX, @Range(from = -32, to = 32) byte offsetY,
                                                 @Range(from = -32, to = 32) byte offsetZ, @Range(from = -32, to = 32) byte sizeX,
                                                 @Range(from = -32, to = 32) byte sizeY, @Range(from = -32, to = 32) byte sizeZ,
                                                 Mirror mirror, Rotation rotation, String metadata, float integrity,
                                                 float seed, boolean ignoreEntities, boolean showAir, boolean showBoundingBox, short flags) {
        super(PacketType.Play.Client.UPDATE_STRUCTURE_BLOCK);
        this.position = position;
        this.action = action;
        this.mode = mode;
        this.name = name;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.mirror = mirror;
        this.rotation = rotation;
        this.metadata = metadata;
        this.integrity = integrity;
        this.seed = seed;
        this.ignoreEntities = ignoreEntities;
        this.showAir = showAir;
        this.showBoundingBox = showBoundingBox;
        this.flags = flags;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.position = new Vector3i(readLong());
        } else {
            int x = readInt();
            int y = readShort();
            int z = readInt();
            this.position = new Vector3i(x, y, z);
        }
        this.action = Action.getById(readVarInt());
        this.mode = Mode.getById(readVarInt());
        this.name = readString();
        this.offsetX = readByte();
        this.offsetY = readByte();
        this.offsetZ = readByte();
        this.sizeX = readByte();
        this.sizeY = readByte();
        this.sizeZ = readByte();
        this.mirror = Mirror.getById(readVarInt());
        this.rotation = Rotation.getById(readVarInt());
        this.metadata = readString();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10)) {
            this.integrity = readFloat();
            this.seed = readVarLong();
        }
        this.flags = readUnsignedByte();
        this.ignoreEntities = (flags & IGNORE_ENTITIES) != 0;
        this.showAir = (flags & SHOW_AIR) != 0;
        this.showBoundingBox = (flags & SHOW_BOUNDING_BOX) != 0;
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            long positionVector = this.position.getSerializedPosition();
            writeLong(positionVector);
        } else {
            writeInt(this.position.x);
            writeShort(this.position.y);
            writeInt(this.position.z);
        }
        writeVarInt(this.action.ordinal());
        writeVarInt(this.mode.ordinal());
        writeString(this.name);
        writeByte(this.offsetX);
        writeByte(this.offsetY);
        writeByte(this.offsetZ);
        writeByte(this.sizeX);
        writeByte(this.sizeY);
        writeByte(this.sizeZ);
        writeVarInt(this.mirror.ordinal());
        writeVarInt(this.rotation.ordinal());
        writeString(this.metadata);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10)) {
            writeFloat(this.integrity);
            writeVarLong((long) this.seed);
        }

        if (this.ignoreEntities) {
            this.flags |= IGNORE_ENTITIES;
        }

        if (this.showAir) {
            this.flags |= SHOW_AIR;
        }

        if (this.showBoundingBox) {
            this.flags |= SHOW_BOUNDING_BOX;
        }

        writeByte(this.flags);
    }

    @Override
    public void copy(WrapperPlayClientUpdateStructureBlock wrapper) {
        this.position = wrapper.position;
        this.action = wrapper.action;
        this.mode = wrapper.mode;
        this.name = wrapper.name;
        this.offsetX = wrapper.offsetX;
        this.offsetY = wrapper.offsetY;
        this.offsetZ = wrapper.offsetZ;
        this.sizeX = wrapper.sizeX;
        this.sizeY = wrapper.sizeY;
        this.sizeZ = wrapper.sizeZ;
        this.mirror = wrapper.mirror;
        this.rotation = wrapper.rotation;
        this.metadata = wrapper.metadata;
        this.integrity = wrapper.integrity;
        this.seed = wrapper.seed;
        this.ignoreEntities = wrapper.ignoreEntities;
        this.showAir = wrapper.showAir;
        this.showBoundingBox = wrapper.showBoundingBox;
        this.flags = wrapper.flags;
    }

    public Vector3i getPosition() {
        return position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @Range(from = -32, to = 32) byte getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(@Range(from = -32, to = 32) byte offsetX) {
        this.offsetX = offsetX;
    }

    public @Range(from = -32, to = 32) byte getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(@Range(from = -32, to = 32) byte offsetY) {
        this.offsetY = offsetY;
    }

    public @Range(from = -32, to = 32) byte getOffsetZ() {
        return offsetZ;
    }

    public void setOffsetZ(@Range(from = -32, to = 32) byte offsetZ) {
        this.offsetZ = offsetZ;
    }

    public @Range(from = -32, to = 32) byte getSizeX() {
        return sizeX;
    }

    public void setSizeX(@Range(from = -32, to = 32) byte sizeX) {
        this.sizeX = sizeX;
    }

    public @Range(from = -32, to = 32) byte getSizeY() {
        return sizeY;
    }

    public void setSizeY(@Range(from = -32, to = 32) byte sizeY) {
        this.sizeY = sizeY;
    }

    public @Range(from = -32, to = 32) byte getSizeZ() {
        return sizeZ;
    }

    public void setSizeZ(@Range(from = -32, to = 32) byte sizeZ) {
        this.sizeZ = sizeZ;
    }

    public Mirror getMirror() {
        return mirror;
    }

    public void setMirror(Mirror mirror) {
        this.mirror = mirror;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Optional<Float> getIntegrity() {
        return Optional.of(integrity);
    }

    public void setIntegrity(float integrity) {
        this.integrity = integrity;
    }

    public Optional<Float> getSeed() {
        return Optional.of(seed);
    }

    public void setSeed(float seed) {
        this.seed = seed;
    }

    public boolean isIgnoreEntities() {
        return ignoreEntities;
    }

    public void setIgnoreEntities(boolean ignoreEntities) {
        this.ignoreEntities = ignoreEntities;
    }

    public boolean isShowAir() {
        return showAir;
    }

    public void setShowAir(boolean showAir) {
        this.showAir = showAir;
    }

    public boolean isShowBoundingBox() {
        return showBoundingBox;
    }

    public void setShowBoundingBox(boolean showBoundingBox) {
        this.showBoundingBox = showBoundingBox;
    }

    public short getFlags() {
        return flags;
    }

    public void setFlags(short flags) {
        this.flags = flags;
    }

    public enum Action {
        UPDATE_DATA,
        SAVE_THE_STRUCTURE,
        LOAD_THE_STRUCTURE,
        DETECT_SIZE;

        private static final Action[] VALUES = values();

        public static Action getById(int index) {
            return VALUES[index];
        }
    }
}
