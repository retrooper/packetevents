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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.color.Color;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import static com.github.retrooper.packetevents.protocol.particle.data.ParticleDustData.decodeColor;
import static com.github.retrooper.packetevents.protocol.particle.data.ParticleDustData.encodeColor;

public class ParticleDustColorTransitionData extends ParticleData {
    //0.01 - 4
    private float scale;

    //0-1
    private float startRed;
    private float startGreen;
    private float startBlue;

    //0-1
    private float endRed;
    private float endGreen;
    private float endBlue;

    public ParticleDustColorTransitionData(float scale, float startRed, float startGreen,
                                           float startBlue, float endRed, float endGreen,
                                           float endBlue) {
        this.scale = scale;
        this.startRed = startRed;
        this.startGreen = startGreen;
        this.startBlue = startBlue;
        this.endRed = endRed;
        this.endGreen = endGreen;
        this.endBlue = endBlue;
    }

    public ParticleDustColorTransitionData(float scale, float[] startRGB, float[] endRGB) {
        this(scale, startRGB[0], startRGB[1], startRGB[2], endRGB[0], endRGB[1], endRGB[2]);
    }

    public ParticleDustColorTransitionData(float scale, Vector3f startRGB, Vector3f endRGB) {
        this(scale, startRGB.getX(), startRGB.getY(), startRGB.getZ(), endRGB.getX(), endRGB.getY(), endRGB.getZ());
    }

    public ParticleDustColorTransitionData(float scale, Color start, Color end) {
        this(scale, start.red() / 255f, start.green() / 255f, start.blue() / 255f, end.red() / 255f, end.green() / 255f, end.blue() / 255f);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getStartRed() {
        return startRed;
    }

    public void setStartRed(float startRed) {
        this.startRed = startRed;
    }

    public float getStartGreen() {
        return startGreen;
    }

    public void setStartGreen(float startGreen) {
        this.startGreen = startGreen;
    }

    public float getStartBlue() {
        return startBlue;
    }

    public void setStartBlue(float startBlue) {
        this.startBlue = startBlue;
    }

    public float getEndRed() {
        return endRed;
    }

    public void setEndRed(float endRed) {
        this.endRed = endRed;
    }

    public float getEndGreen() {
        return endGreen;
    }

    public void setEndGreen(float endGreen) {
        this.endGreen = endGreen;
    }

    public float getEndBlue() {
        return endBlue;
    }

    public void setEndBlue(float endBlue) {
        this.endBlue = endBlue;
    }

    public static ParticleDustColorTransitionData read(PacketWrapper<?> wrapper) {
        float startRed = wrapper.readFloat();
        float startGreen = wrapper.readFloat();
        float startBlue = wrapper.readFloat();
        float scale = 0f;
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_20_5)) {
            scale = wrapper.readFloat();
        }
        float endRed = wrapper.readFloat();
        float endGreen = wrapper.readFloat();
        float endBlue = wrapper.readFloat();
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            scale = wrapper.readFloat();
        }
        return new ParticleDustColorTransitionData(scale, startRed, startGreen, startBlue, endRed, endGreen, endBlue);
    }

    public static void write(PacketWrapper<?> wrapper, ParticleDustColorTransitionData data) {
        wrapper.writeFloat(data.getStartRed());
        wrapper.writeFloat(data.getStartGreen());
        wrapper.writeFloat(data.getStartBlue());
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_20_5)) {
            wrapper.writeFloat(data.getScale());
        }
        wrapper.writeFloat(data.getEndRed());
        wrapper.writeFloat(data.getEndGreen());
        wrapper.writeFloat(data.getEndBlue());
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            wrapper.writeFloat(data.getScale());
        }
    }

    public static ParticleDustColorTransitionData decode(NBTCompound compound, ClientVersion version) {
        String fromColorKey = "from_color";
        String toColorKey = "to_color";
        if (version.isOlderThan(ClientVersion.V_1_20_5)) {
            fromColorKey = "fromColor";
            toColorKey = "toColor";
        }
        float[] fromColor = decodeColor(compound.getTagOrThrow(fromColorKey));
        float[] toColor = decodeColor(compound.getTagOrThrow(toColorKey));
        float scale = compound.getNumberTagOrThrow("scale").getAsFloat();
        return new ParticleDustColorTransitionData(scale, fromColor, toColor);
    }

    public static void encode(ParticleDustColorTransitionData data, ClientVersion version, NBTCompound compound) {
        String fromColorKey = "from_color";
        String toColorKey = "to_color";
        if (version.isOlderThan(ClientVersion.V_1_20_5)) {
            fromColorKey = "fromColor";
            toColorKey = "toColor";
        }
        compound.setTag(fromColorKey, encodeColor(null, data.startRed, data.startGreen, data.startBlue));
        compound.setTag(toColorKey, encodeColor(null, data.endRed, data.endGreen, data.endBlue));
        compound.setTag("scale", new NBTFloat(data.scale));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
