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

public class ParticleDustColorTransitionData extends ParticleData {
    //0.01 - 4
    private float scale;
    private Color start;
    private Color end;

    public ParticleDustColorTransitionData(
            float scale,
            float startRed, float startGreen, float startBlue,
            float endRed, float endGreen, float endBlue
    ) {
        this(scale, new Color(startRed, startGreen, startBlue),
                new Color(endRed, endGreen, endBlue));
    }

    public ParticleDustColorTransitionData(float scale, float[] startRGB, float[] endRGB) {
        this(scale, startRGB[0], startRGB[1], startRGB[2],
                endRGB[0], endRGB[1], endRGB[2]);
    }

    public ParticleDustColorTransitionData(float scale, Vector3f startRGB, Vector3f endRGB) {
        this(scale, startRGB.getX(), startRGB.getY(), startRGB.getZ(),
                endRGB.getX(), endRGB.getY(), endRGB.getZ());
    }

    public ParticleDustColorTransitionData(float scale, Color start, Color end) {
        this.scale = scale;
        this.start = start;
        this.end = end;
    }

    public static ParticleDustColorTransitionData read(PacketWrapper<?> wrapper) {
        Color start;
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            start = new Color(wrapper.readInt());
        } else {
            float startRed = wrapper.readFloat();
            float startGreen = wrapper.readFloat();
            float startBlue = wrapper.readFloat();
            start = new Color(startRed, startGreen, startBlue);
        }
        float scale = 0f;
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_20_5)) {
            scale = wrapper.readFloat();
        }
        Color end;
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            end = new Color(wrapper.readInt());
        } else {
            float endRed = wrapper.readFloat();
            float endGreen = wrapper.readFloat();
            float endBlue = wrapper.readFloat();
            end = new Color(endRed, endGreen, endBlue);
        }
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            scale = wrapper.readFloat();
        }
        return new ParticleDustColorTransitionData(scale, start, end);
    }

    public static void write(PacketWrapper<?> wrapper, ParticleDustColorTransitionData data) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            wrapper.writeInt(data.getStart().asRGB());
        } else {
            wrapper.writeFloat(data.getStartRed());
            wrapper.writeFloat(data.getStartGreen());
            wrapper.writeFloat(data.getStartBlue());
        }
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_20_5)) {
            wrapper.writeFloat(data.getScale());
        }
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            wrapper.writeInt(data.getEnd().asRGB());
        } else {
            wrapper.writeFloat(data.getEndRed());
            wrapper.writeFloat(data.getEndGreen());
            wrapper.writeFloat(data.getEndBlue());
        }
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
        Color fromColor = Color.decode(compound.getTagOrThrow(fromColorKey), version);
        Color toColor = Color.decode(compound.getTagOrThrow(toColorKey), version);
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
        compound.setTag(fromColorKey, Color.encode(data.start, version));
        compound.setTag(toColorKey, Color.encode(data.end, version));
        compound.setTag("scale", new NBTFloat(data.scale));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public float getStartRed() {
        return this.start.red() / 255f;
    }

    public void setStartRed(float startRed) {
        this.start = new Color(startRed, this.getStartGreen(), this.getStartBlue());
    }

    public float getStartGreen() {
        return this.start.green() / 255f;
    }

    public void setStartGreen(float startGreen) {
        this.start = new Color(this.getStartRed(), startGreen, this.getStartBlue());
    }

    public float getStartBlue() {
        return this.start.blue() / 255f;
    }

    public void setStartBlue(float startBlue) {
        this.start = new Color(this.getStartRed(), this.getStartGreen(), startBlue);
    }

    public float getEndRed() {
        return this.end.red() / 255f;
    }

    public void setEndRed(float endRed) {
        this.end = new Color(endRed, this.getEndGreen(), this.getEndBlue());
    }

    public float getEndGreen() {
        return this.end.green() / 255f;
    }

    public void setEndGreen(float endGreen) {
        this.end = new Color(this.getEndRed(), endGreen, this.getEndBlue());
    }

    public float getEndBlue() {
        return this.end.blue() / 255f;
    }

    public void setEndBlue(float endBlue) {
        this.end = new Color(this.getEndRed(), this.getEndGreen(), endBlue);
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Color getStart() {
        return this.start;
    }

    public void setStart(Color start) {
        this.start = start;
    }

    public Color getEnd() {
        return this.end;
    }

    public void setEnd(Color end) {
        this.end = end;
    }
}
