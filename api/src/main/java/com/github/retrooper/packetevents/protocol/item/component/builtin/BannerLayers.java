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

package com.github.retrooper.packetevents.protocol.item.component.builtin;

import com.github.retrooper.packetevents.protocol.color.DyeColor;
import com.github.retrooper.packetevents.protocol.item.banner.BannerPattern;
import com.github.retrooper.packetevents.protocol.item.banner.BannerPatterns;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;

public class BannerLayers {

    private final List<Layer> layers;

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

    public static class Layer {

        private final BannerPattern pattern;
        private final DyeColor color;

        public Layer(BannerPattern pattern, DyeColor color) {
            this.pattern = pattern;
            this.color = color;
        }

        public static Layer read(PacketWrapper<?> wrapper) {
            BannerPattern pattern = wrapper.readMappedEntityOrDirect(
                    BannerPatterns::getById, BannerPattern::readDirect);
            DyeColor color = wrapper.readEnum(DyeColor.values());
            return new Layer(pattern, color);
        }

        public static void write(PacketWrapper<?> wrapper, Layer layer) {
            wrapper.writeMappedEntityOrDirect(layer.pattern, BannerPattern::writeDirect);
            wrapper.writeEnum(layer.color);
        }
    }
}
