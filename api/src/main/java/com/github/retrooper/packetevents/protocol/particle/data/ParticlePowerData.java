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
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.9+
 */
@NullMarked
public class ParticlePowerData extends ParticleData {

    private float power;

    public ParticlePowerData(float power) {
        this.power = power;
    }

    public static ParticlePowerData read(PacketWrapper<?> wrapper) {
        float power = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_9) ? 1f : wrapper.readFloat();
        return new ParticlePowerData(power);
    }

    public static void write(PacketWrapper<?> wrapper, ParticlePowerData data) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
            wrapper.writeFloat(data.power);
        }
    }

    @ApiStatus.Internal
    public static ParticlePowerData decode(NBTCompound tag, ClientVersion version) {
        float power = tag.getNumberTagValueOrDefault("power", 1f).floatValue();
        return new ParticlePowerData(power);
    }

    @ApiStatus.Internal
    public static void encode(ParticlePowerData data, ClientVersion version, NBTCompound tag) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_21_9) && data.power != 1f) {
            tag.setTag("power", new NBTFloat(data.power));
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public float getPower() {
        return this.power;
    }

    public void setPower(float power) {
        this.power = power;
    }
}
