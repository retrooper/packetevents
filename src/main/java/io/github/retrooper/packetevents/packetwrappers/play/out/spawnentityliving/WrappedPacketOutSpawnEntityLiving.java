/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.packetwrappers.play.out.spawnentityliving;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

//TODO finish wrapper
class WrappedPacketOutSpawnEntityLiving extends WrappedPacketEntityAbstraction {
    public WrappedPacketOutSpawnEntityLiving(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
    }
    /*
    public Vector3d getPosition() {
        if (packet != null) {
            //2,3,4
            int x = readInt(2);
            int y = readInt(3);
            int z = readInt(4);
            return new Vector3d(x, y, z);
        }
        else {
            return null;
        }
    }*/
}
