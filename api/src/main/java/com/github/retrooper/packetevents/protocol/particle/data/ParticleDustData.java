/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.particle.data;

import com.github.retrooper.packetevents.protocol.color.Color;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleDustData extends ParticleData {
    //0.01 - 4
    private float scale;

    //0-1
    private float red;
    private float green;
    private float blue;

    public ParticleDustData(float scale, float red, float green, float blue) {
        this.scale = scale;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public ParticleDustData(float scale, Vector3f rgb) {
        this(scale, rgb.getX(), rgb.getY(), rgb.getZ());
    }

    public ParticleDustData(float scale, Color color) {
        this(scale, color.red() / 255f, color.green() / 255f, color.blue() / 255f);
    }

    public float getRed() {
        return red;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public float getGreen() {
        return green;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public float getBlue() {
        return blue;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public static ParticleDustData read(PacketWrapper<?> wrapper) {
        float red = wrapper.readFloat();
        float green = wrapper.readFloat();
        float blue = wrapper.readFloat();
        float scale = wrapper.readFloat();
        return new ParticleDustData(scale, red, green, blue);
    }

    public static void write(PacketWrapper<?> wrapper, ParticleDustData data) {
        wrapper.writeFloat(data.getRed());
        wrapper.writeFloat(data.getGreen());
        wrapper.writeFloat(data.getBlue());
        wrapper.writeFloat(data.getScale());
    }


    @Override
    public boolean isEmpty() {
        return false;
    }
}
