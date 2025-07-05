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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.color.Color;
import com.github.retrooper.packetevents.protocol.nbt.*;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom model data was completely rewritten with 1.21.4.
 * <p>
 * Before 1.21.4, this used to be a single integer.<br/>
 * With 1.21.4, this is now a holder of many elements.
 * <p>
 * If possible, use {@link com.github.retrooper.packetevents.protocol.component.ComponentTypes#ITEM_MODEL}
 * instead for specifying which model an item should have.
 * <p>
 * When working with legacy versions, the first element in the {@link #floats}
 * will represent the legacy single integer, just like Mojang is handling this.
 */
public class ItemCustomModelData {

    private List<Float> floats;
    private List<Boolean> flags;
    private List<String> strings;
    private List<Color> colors;

    public ItemCustomModelData(List<Float> floats, List<Boolean> flags, List<String> strings, List<Color> colors) {
        this.floats = floats;
        this.flags = flags;
        this.strings = strings;
        this.colors = colors;
    }

    public ItemCustomModelData(int legacyId) {
        this.floats = new ArrayList<>(1);
        this.flags = new ArrayList<>(0);
        this.strings = new ArrayList<>(0);
        this.colors = new ArrayList<>(0);
        this.setLegacyId(legacyId);
    }

    public static ItemCustomModelData read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
            return new ItemCustomModelData(
                    wrapper.readList(PacketWrapper::readFloat),
                    wrapper.readList(PacketWrapper::readBoolean),
                    wrapper.readList(PacketWrapper::readString),
                    wrapper.readList(Color::read));
        } else {
            return new ItemCustomModelData(wrapper.readVarInt());
        }
    }

    public static void write(PacketWrapper<?> wrapper, ItemCustomModelData data) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
            wrapper.writeList(data.floats, PacketWrapper::writeFloat);
            wrapper.writeList(data.flags, PacketWrapper::writeBoolean);
            wrapper.writeList(data.strings, PacketWrapper::writeString);
            wrapper.writeList(data.colors, Color::write);
        } else {
            wrapper.writeVarInt(data.getLegacyId());
        }
    }

    public static ItemCustomModelData decode(NBT nbt, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_21_4)) {
            NBTCompound compound = (NBTCompound) nbt;
            PacketWrapper<?> wrapper = PacketWrapper.createDummyWrapper(version);
            List<Float> floats = compound.getListOrEmpty("floats", (subNbt, subWrapper) -> ((NBTNumber) subNbt).getAsFloat(), wrapper);
            List<Boolean> flags = compound.getListOrEmpty("flags", (subNbt, subWrapper) -> ((NBTByte) subNbt).getAsBool(), wrapper);
            List<String> strings = compound.getListOrEmpty("strings", (subNbt, subWrapper) -> ((NBTString) subNbt).getValue(), wrapper);
            List<Color> colors = compound.getListOrEmpty("colors", (subNbt, subWrapper) -> Color.decode(subNbt, version), wrapper);
            return new ItemCustomModelData(floats, flags, strings, colors);
        } else {
            return new ItemCustomModelData(((NBTNumber) nbt).getAsInt());
        }
    }

    public static NBT encode(ItemCustomModelData data, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_21_4)) {
            NBTCompound compound = new NBTCompound();
            PacketWrapper<?> wrapper = PacketWrapper.createDummyWrapper(version);
            compound.setList("floats", data.floats, (subWrapper, value) -> new NBTFloat(value), wrapper);
            compound.setList("flags", data.flags, (subWrapper, value) -> new NBTByte(value), wrapper);
            compound.setList("strings", data.strings, (subWrapper, value) -> new NBTString(value), wrapper);
            compound.setList("colors", data.colors, (subWrapper, value) -> Color.encode(value, version), wrapper);
            return compound;
        } else {
            return new NBTInt(data.getLegacyId());
        }
    }

    public List<Float> getFloats() {
        return this.floats;
    }

    public void setFloats(List<Float> floats) {
        this.floats = floats;
    }

    public List<Boolean> getFlags() {
        return this.flags;
    }

    public void setFlags(List<Boolean> flags) {
        this.flags = flags;
    }

    public List<String> getStrings() {
        return this.strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
    }

    public List<Color> getColors() {
        return this.colors;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    @ApiStatus.Obsolete
    public int getLegacyId() {
        if (!this.floats.isEmpty()) {
            return this.floats.get(0).intValue();
        }
        return 0;
    }

    @ApiStatus.Obsolete
    public void setLegacyId(int legacyId) {
        if (this.flags.isEmpty()) {
            this.floats.add((float) legacyId);
        } else {
            this.floats.set(0, (float) legacyId);
        }
    }
}
