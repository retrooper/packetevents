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

import com.github.retrooper.packetevents.protocol.item.mapdecoration.MapDecorationType;
import com.github.retrooper.packetevents.protocol.item.mapdecoration.MapDecorationTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ItemMapDecorations {

    private Map<String, Decoration> decorations;

    public ItemMapDecorations(Map<String, Decoration> decorations) {
        this.decorations = decorations;
    }

    public static ItemMapDecorations read(PacketWrapper<?> wrapper) {
        NBTCompound compound = wrapper.readNBT();
        Map<String, Decoration> decorations = new HashMap<>(compound.size());
        for (Map.Entry<String, NBT> tag : compound.getTags().entrySet()) {
            Decoration decoration = Decoration.readCompound((NBTCompound) tag.getValue());
            decorations.put(tag.getKey(), decoration);
        }
        return new ItemMapDecorations(decorations);
    }

    public static void write(PacketWrapper<?> wrapper, ItemMapDecorations decorations) {
        NBTCompound compound = new NBTCompound();
        for (Map.Entry<String, Decoration> decoration : decorations.decorations.entrySet()) {
            NBTCompound entry = new NBTCompound();
            Decoration.writeCompound(entry, decoration.getValue());
            compound.setTag(decoration.getKey(), entry);
        }
        wrapper.writeNBT(compound);
    }

    public @Nullable Decoration getDecoration(String key) {
        return this.decorations.get(key);
    }

    public void setDecoration(String key, @Nullable Decoration decoration) {
        if (decoration != null) {
            this.decorations.put(key, decoration);
        } else {
            this.decorations.remove(key);
        }
    }

    public Map<String, Decoration> getDecorations() {
        return this.decorations;
    }

    public void setDecorations(Map<String, Decoration> decorations) {
        this.decorations = decorations;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemMapDecorations)) return false;
        ItemMapDecorations that = (ItemMapDecorations) obj;
        return this.decorations.equals(that.decorations);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.decorations);
    }

    public static final class Decoration {

        private MapDecorationType type;
        private double x;
        private double z;
        private float rotation;

        public Decoration(MapDecorationType type, double x, double z, float rotation) {
            this.type = type;
            this.x = x;
            this.z = z;
            this.rotation = rotation;
        }

        @ApiStatus.Internal
        public static Decoration readCompound(NBTCompound compound) {
            MapDecorationType type = MapDecorationTypes.getByName(compound.getStringTagValueOrThrow("type"));
            double x = compound.getNumberTagOrThrow("x").getAsDouble();
            double z = compound.getNumberTagOrThrow("z").getAsDouble();
            float rotation = compound.getNumberTagOrThrow("rotation").getAsFloat();
            return new Decoration(type, x, z, rotation);
        }

        @ApiStatus.Internal
        public static void writeCompound(NBTCompound compound, Decoration decoration) {
            compound.setTag("type", new NBTString(decoration.type.getName().toString()));
            compound.setTag("x", new NBTDouble(decoration.x));
            compound.setTag("z", new NBTDouble(decoration.z));
            compound.setTag("rotation", new NBTFloat(decoration.rotation));
        }

        public MapDecorationType getType() {
            return this.type;
        }

        public void setType(MapDecorationType type) {
            this.type = type;
        }

        public double getX() {
            return this.x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getZ() {
            return this.z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public float getRotation() {
            return this.rotation;
        }

        public void setRotation(float rotation) {
            this.rotation = rotation;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Decoration)) return false;
            Decoration that = (Decoration) obj;
            if (Double.compare(that.x, this.x) != 0) return false;
            if (Double.compare(that.z, this.z) != 0) return false;
            if (Float.compare(that.rotation, this.rotation) != 0) return false;
            return this.type.equals(that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.type, this.x, this.z, this.rotation);
        }
    }
}
