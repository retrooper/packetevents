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

package io.github.retrooper.packetevents.packetwrappers.play.out.multiblockchange;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

//TODO finish this wrapper
class WrappedPacketOutMultiBlockChange extends WrappedPacket {

    public WrappedPacketOutMultiBlockChange(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        net.minecraft.server.v1_7_R4.PacketPlayOutMultiBlockChange a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutMultiBlockChange a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutMultiBlockChange a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutMultiBlockChange a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutMultiBlockChange a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutMultiBlockChange a5;
        net.minecraft.server.v1_13_R2.PacketPlayOutMultiBlockChange a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutMultiBlockChange a7;

    }
}
