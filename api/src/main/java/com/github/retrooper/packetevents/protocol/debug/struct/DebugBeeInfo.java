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

package com.github.retrooper.packetevents.protocol.debug.struct;

import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

/**
 * @versions 1.21.9+
 */
@NullMarked
public final class DebugBeeInfo {

    private final @Nullable Vector3i hivePos;
    private final @Nullable Vector3i flowerPos;
    private final int travelTicks;
    private final List<Vector3i> blacklistedHives;

    public DebugBeeInfo(
            @Nullable Vector3i hivePos, @Nullable Vector3i flowerPos,
            int travelTicks, List<Vector3i> blacklistedHives
    ) {
        this.hivePos = hivePos;
        this.flowerPos = flowerPos;
        this.travelTicks = travelTicks;
        this.blacklistedHives = blacklistedHives;
    }

    public static DebugBeeInfo read(PacketWrapper<?> wrapper) {
        Vector3i hivePos = wrapper.readOptional(PacketWrapper::readBlockPosition);
        Vector3i flowerPos = wrapper.readOptional(PacketWrapper::readBlockPosition);
        int travelTicks = wrapper.readVarInt();
        List<Vector3i> blacklistedHives = wrapper.readList(PacketWrapper::readBlockPosition);
        return new DebugBeeInfo(hivePos, flowerPos, travelTicks, blacklistedHives);
    }

    public static void write(PacketWrapper<?> wrapper, DebugBeeInfo info) {
        wrapper.writeOptional(info.hivePos, PacketWrapper::writeBlockPosition);
        wrapper.writeOptional(info.flowerPos, PacketWrapper::writeBlockPosition);
        wrapper.writeVarInt(info.travelTicks);
        wrapper.writeList(info.blacklistedHives, PacketWrapper::writeBlockPosition);
    }

    public @Nullable Vector3i getHivePos() {
        return this.hivePos;
    }

    public @Nullable Vector3i getFlowerPos() {
        return this.flowerPos;
    }

    public int getTravelTicks() {
        return this.travelTicks;
    }

    public List<Vector3i> getBlacklistedHives() {
        return this.blacklistedHives;
    }
}
