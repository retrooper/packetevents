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

package com.github.retrooper.packetevents.protocol.particle.data;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleColorData extends ParticleData {

    private int color;

    public ParticleColorData(int color) {
        this.color = color;
    }

    public static ParticleColorData read(PacketWrapper<?> wrapper) {
        int color = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5) ? wrapper.readInt() : 0;
        return new ParticleColorData(color);
    }

    public static void write(PacketWrapper<?> wrapper, ParticleColorData data) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            wrapper.writeInt(data.color);
        }
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
