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

import com.github.retrooper.packetevents.protocol.color.DyeColor;
import com.github.retrooper.packetevents.protocol.item.banner.BannerPattern;
import com.github.retrooper.packetevents.protocol.item.banner.BannerPatterns;
import com.github.retrooper.packetevents.protocol.nbt.*;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class BannerLayers {

    private List<Layer> layers;

    public BannerLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public static BannerLayers read(PacketWrapper<?> wrapper) {
        List<Layer> layers = wrapper.readList(Layer::read);
        return new BannerLayers(layers);
    }

    public static void write(PacketWrapper<?> wrapper, BannerLayers patterns) {
        wrapper.writeList(patterns.layers, Layer::write);
    }

    public static BannerLayers decode(NBT nbt, ClientVersion version) {
        NBTList<NBTCompound> list = (NBTList<NBTCompound>) nbt;
        List<Layer> layers = new ArrayList<>();
        for (NBTCompound compound : list.getTags()) {
            layers.add(Layer.decode(compound, version));
        }
        return new BannerLayers(layers);
    }

    public static NBT encode(BannerLayers layers, ClientVersion version) {
        NBTList<NBTCompound> list = new NBTList<>(NBTType.COMPOUND);
        for (Layer layer : layers.layers) {
            list.addTag(Layer.encode(layer, version));
        }
        return list;
    }

    public void addLayer(Layer layer) {
        this.layers.add(layer);
    }

    public List<Layer> getLayers() {
        return this.layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BannerLayers)) return false;
        BannerLayers that = (BannerLayers) obj;
        return this.layers.equals(that.layers);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.layers);
    }

    public static class Layer {

        private BannerPattern pattern;
        private DyeColor color;

        public Layer(BannerPattern pattern, DyeColor color) {
            this.pattern = pattern;
            this.color = color;
        }

        public static Layer read(PacketWrapper<?> wrapper) {
            BannerPattern pattern = wrapper.readMappedEntityOrDirect(
                    BannerPatterns.getRegistry(), BannerPattern::readDirect);
            DyeColor color = DyeColor.read(wrapper);
            return new Layer(pattern, color);
        }

        public static void write(PacketWrapper<?> wrapper, Layer layer) {
            wrapper.writeMappedEntityOrDirect(layer.pattern, BannerPattern::writeDirect);
            DyeColor.write(wrapper, layer.color);
        }

        public static Layer decode(NBTCompound compound, ClientVersion version) {
            DyeColor color = DyeColor.valueOf(compound.getStringTagValueOrThrow("color").toUpperCase(Locale.ROOT));
            BannerPattern pattern;
            NBT patternNbt = compound.getTagOrThrow("pattern");
            if (patternNbt instanceof NBTCompound) {
                pattern = BannerPattern.decode(patternNbt, version, null);
            } else {
                pattern = BannerPatterns.getByName(((NBTString) patternNbt).getValue());
            }
            return new Layer(pattern, color);
        }

        public static NBTCompound encode(Layer layer, ClientVersion version) {
            NBTCompound compound = new NBTCompound();
            compound.setTag("color", new NBTString(layer.color.name().toLowerCase(Locale.ROOT)));
            NBT patternNbt;
            if (layer.pattern.isRegistered()) {
                patternNbt = new NBTString(layer.pattern.getName().toString());
            } else {
                patternNbt = BannerPattern.encode(layer.pattern, version);
            }
            compound.setTag("pattern", patternNbt);
            return compound;
        }

        public BannerPattern getPattern() {
            return this.pattern;
        }

        public void setPattern(BannerPattern pattern) {
            this.pattern = pattern;
        }

        public DyeColor getColor() {
            return this.color;
        }

        public void setColor(DyeColor color) {
            this.color = color;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Layer)) return false;
            Layer layer = (Layer) obj;
            if (!this.pattern.equals(layer.pattern)) return false;
            return this.color == layer.color;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.pattern, this.color);
        }
    }
}
