/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.world.StructureMirror;
import com.github.retrooper.packetevents.protocol.world.StructureRotation;
import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientSetStructureBlock extends PacketWrapper<WrapperPlayClientSetStructureBlock> {

    private static final int LIMIT_PRE_1_16_2 = 32;
    private static final int LIMIT = 48;

    private Vector3i position;
    private UpdateType updateType;
    private StructureMode mode;
    private String name;
    private Vector3i offset;
    private Vector3i size;
    private StructureMirror mirror;
    private StructureRotation rotation;
    private String data;
    private boolean ignoreEntities;
    /**
     * Added with 1.21.5
     */
    private boolean strict;
    private boolean showAir;
    private boolean showBoundingBox;
    private float integrity;
    private long seed;

    public WrapperPlayClientSetStructureBlock(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientSetStructureBlock(
            Vector3i position, UpdateType updateType, StructureMode mode,
            String name, Vector3i offset, Vector3i size, StructureMirror mirror, StructureRotation rotation,
            String data, boolean ignoreEntities, boolean showAir, boolean showBoundingBox, float integrity, long seed
    ) {
        this(position, updateType, mode, name, offset, size, mirror, rotation,
                data, ignoreEntities, false, showAir, showBoundingBox, integrity, seed);
    }

    public WrapperPlayClientSetStructureBlock(
            Vector3i position, UpdateType updateType, StructureMode mode,
            String name, Vector3i offset, Vector3i size, StructureMirror mirror, StructureRotation rotation,
            String data, boolean ignoreEntities, boolean strict, boolean showAir, boolean showBoundingBox,
            float integrity, long seed
    ) {
        super(PacketType.Play.Client.UPDATE_STRUCTURE_BLOCK);
        this.position = position;
        this.updateType = updateType;
        this.mode = mode;
        this.name = name;
        this.offset = offset;
        this.size = size;
        this.mirror = mirror;
        this.rotation = rotation;
        this.data = data;
        this.ignoreEntities = ignoreEntities;
        this.strict = strict;
        this.showAir = showAir;
        this.showBoundingBox = showBoundingBox;
        this.integrity = integrity;
        this.seed = seed;
    }

    @Override
    public void read() {
        this.position = this.readBlockPosition();
        this.updateType = this.readEnum(UpdateType.class);
        this.mode = this.readEnum(StructureMode.class);
        this.name = this.readString();
        int limit = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16_2) ? LIMIT : LIMIT_PRE_1_16_2;
        this.offset = new Vector3i(
                MathUtil.clamp(this.readByte(), -limit, limit),
                MathUtil.clamp(this.readByte(), -limit, limit),
                MathUtil.clamp(this.readByte(), -limit, limit));
        this.size = new Vector3i(
                MathUtil.clamp(this.readByte(), 0, limit),
                MathUtil.clamp(this.readByte(), 0, limit),
                MathUtil.clamp(this.readByte(), 0, limit));
        this.mirror = this.readEnum(StructureMirror.class);
        this.rotation = this.readEnum(StructureRotation.class);
        this.data = this.readString(this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17) ? 128 : 12);
        this.integrity = MathUtil.clamp(this.readFloat(), 0f, 1f);
        this.seed = this.readVarLong();
        int flags = this.readByte();
        this.ignoreEntities = (flags & 0b0001) != 0;
        this.strict = (flags & 0b1000) != 0;
        this.showAir = (flags & 0b0010) != 0;
        this.showBoundingBox = (flags & 0b0100) != 0;
    }

    @Override
    public void write() {
        this.writeBlockPosition(this.position);
        this.writeEnum(this.updateType);
        this.writeEnum(this.mode);
        this.writeString(this.name);
        this.writeByte(this.offset.x);
        this.writeByte(this.offset.y);
        this.writeByte(this.offset.z);
        this.writeByte(this.size.x);
        this.writeByte(this.size.y);
        this.writeByte(this.size.z);
        this.writeEnum(this.mirror);
        this.writeEnum(this.rotation);
        this.writeString(this.data);
        this.writeFloat(this.integrity);
        this.writeVarLong(this.seed);
        this.writeByte(0
                | (this.ignoreEntities ? 0b0001 : 0)
                | (this.showAir ? 0b0010 : 0)
                | (this.showBoundingBox ? 0b0100 : 0)
                | (this.strict ? 0b1000 : 0)
        );
    }

    @Override
    public void copy(WrapperPlayClientSetStructureBlock wrapper) {
        this.position = wrapper.position;
        this.updateType = wrapper.updateType;
        this.mode = wrapper.mode;
        this.name = wrapper.name;
        this.offset = wrapper.offset;
        this.size = wrapper.size;
        this.mirror = wrapper.mirror;
        this.rotation = wrapper.rotation;
        this.data = wrapper.data;
        this.ignoreEntities = wrapper.ignoreEntities;
        this.strict = wrapper.strict;
        this.showAir = wrapper.showAir;
        this.showBoundingBox = wrapper.showBoundingBox;
        this.integrity = wrapper.integrity;
        this.seed = wrapper.seed;
    }

    public Vector3i getPosition() {
        return this.position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public UpdateType getUpdateType() {
        return this.updateType;
    }

    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    public StructureMode getMode() {
        return this.mode;
    }

    public void setMode(StructureMode mode) {
        this.mode = mode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector3i getOffset() {
        return this.offset;
    }

    public void setOffset(Vector3i offset) {
        this.offset = offset;
    }

    public Vector3i getSize() {
        return this.size;
    }

    public void setSize(Vector3i size) {
        this.size = size;
    }

    public StructureMirror getMirror() {
        return this.mirror;
    }

    public void setMirror(StructureMirror mirror) {
        this.mirror = mirror;
    }

    public StructureRotation getRotation() {
        return this.rotation;
    }

    public void setRotation(StructureRotation rotation) {
        this.rotation = rotation;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isIgnoreEntities() {
        return this.ignoreEntities;
    }

    public void setIgnoreEntities(boolean ignoreEntities) {
        this.ignoreEntities = ignoreEntities;
    }

    /**
     * Added with 1.21.5
     */
    public boolean isStrict() {
        return this.strict;
    }

    /**
     * Added with 1.21.5
     */
    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public boolean isShowAir() {
        return this.showAir;
    }

    public void setShowAir(boolean showAir) {
        this.showAir = showAir;
    }

    public boolean isShowBoundingBox() {
        return this.showBoundingBox;
    }

    public void setShowBoundingBox(boolean showBoundingBox) {
        this.showBoundingBox = showBoundingBox;
    }

    public float getIntegrity() {
        return this.integrity;
    }

    public void setIntegrity(float integrity) {
        this.integrity = integrity;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public enum UpdateType {
        UPDATE_DATA,
        SAVE_AREA,
        LOAD_AREA,
        SCAN_AREA,
    }

    public enum StructureMode {
        SAVE,
        LOAD,
        CORNER,
        DATA,
    }
}
