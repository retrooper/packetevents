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

package com.github.retrooper.packetevents.protocol.particle.data;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.color.Color;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @version 1.21.9+
 */
@NullMarked
public class ParticleSpellData extends ParticleData {

    private Color color;
    private float power;

    public ParticleSpellData(Color color, float power) {
        this.color = color;
        this.power = power;
    }

    public static ParticleSpellData read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            Color color = Color.read(wrapper);
            float power = wrapper.readFloat();
            return new ParticleSpellData(color, power);
        }
        return new ParticleSpellData(Color.WHITE, 1f);
    }

    public static void write(PacketWrapper<?> wrapper, ParticleSpellData data) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            Color.write(wrapper, data.color);
            wrapper.writeFloat(data.power);
        }
    }

    @ApiStatus.Internal
    public static ParticleSpellData decode(NBTCompound compound, ClientVersion version) {
        Color color = compound.getOr("color", Color::decode, Color.WHITE, null);
        float power = compound.getNumberTagValueOrDefault("power", 1f).floatValue();
        return new ParticleSpellData(color, power);
    }

    @ApiStatus.Internal
    public static void encode(ParticleSpellData data, ClientVersion version, NBTCompound compound) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_21_9)) {
            if (!Color.WHITE.equals(data.color)) {
                compound.setTag("color", Color.encode(data.color, version));
            }
            if (data.power != 1f) {
                compound.setTag("power", new NBTFloat(data.power));
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getPower() {
        return this.power;
    }

    public void setPower(float power) {
        this.power = power;
    }
}
