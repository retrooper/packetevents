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

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;
import java.util.Objects;

public class FireworkExplosion {

    private Shape shape;
    private List<Integer> colors;
    private List<Integer> fadeColors;
    private boolean hasTrail;
    private boolean hasTwinkle;

    public FireworkExplosion(
            Shape shape, List<Integer> colors, List<Integer> fadeColors,
            boolean hasTrail, boolean hasTwinkle
    ) {
        this.shape = shape;
        this.colors = colors;
        this.fadeColors = fadeColors;
        this.hasTrail = hasTrail;
        this.hasTwinkle = hasTwinkle;
    }

    public static FireworkExplosion read(PacketWrapper<?> wrapper) {
        Shape shape = wrapper.readEnum(Shape.values());
        List<Integer> colors = wrapper.readList(PacketWrapper::readInt);
        List<Integer> fadeColors = wrapper.readList(PacketWrapper::readInt);
        boolean hasTrail = wrapper.readBoolean();
        boolean hasTwinkle = wrapper.readBoolean();
        return new FireworkExplosion(shape, colors, fadeColors, hasTrail, hasTwinkle);
    }

    public static void write(PacketWrapper<?> wrapper, FireworkExplosion explosion) {
        wrapper.writeEnum(explosion.shape);
        wrapper.writeList(explosion.colors, PacketWrapper::writeInt);
        wrapper.writeList(explosion.fadeColors, PacketWrapper::writeInt);
        wrapper.writeBoolean(explosion.hasTrail);
        wrapper.writeBoolean(explosion.hasTwinkle);
    }

    public Shape getShape() {
        return this.shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void addColor(int color) {
        this.colors.add(color);
    }

    public List<Integer> getColors() {
        return this.colors;
    }

    public void setColors(List<Integer> colors) {
        this.colors = colors;
    }

    public void addFadeColor(int color) {
        this.fadeColors.add(color);
    }

    public List<Integer> getFadeColors() {
        return this.fadeColors;
    }

    public void setFadeColors(List<Integer> fadeColors) {
        this.fadeColors = fadeColors;
    }

    public boolean isHasTrail() {
        return this.hasTrail;
    }

    public void setHasTrail(boolean hasTrail) {
        this.hasTrail = hasTrail;
    }

    public boolean isHasTwinkle() {
        return this.hasTwinkle;
    }

    public void setHasTwinkle(boolean hasTwinkle) {
        this.hasTwinkle = hasTwinkle;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FireworkExplosion)) return false;
        FireworkExplosion that = (FireworkExplosion) obj;
        if (this.hasTrail != that.hasTrail) return false;
        if (this.hasTwinkle != that.hasTwinkle) return false;
        if (this.shape != that.shape) return false;
        if (!this.colors.equals(that.colors)) return false;
        return this.fadeColors.equals(that.fadeColors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.shape, this.colors, this.fadeColors, this.hasTrail, this.hasTwinkle);
    }

    public enum Shape {
        SMALL_BALL,
        LARGE_BALL,
        STAR,
        CREEPER,
        BURST,
    }
}
