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

import com.github.retrooper.packetevents.protocol.debug.poi.PoiType;
import com.github.retrooper.packetevents.protocol.debug.poi.PoiTypes;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.9+
 */
@NullMarked
public final class DebugPoiInfo {

    private final Vector3i pos;
    private final PoiType poiType;
    private final int freeTicketCount;

    public DebugPoiInfo(Vector3i pos, PoiType poiType, int freeTicketCount) {
        this.pos = pos;
        this.poiType = poiType;
        this.freeTicketCount = freeTicketCount;
    }

    public static DebugPoiInfo read(PacketWrapper<?> wrapper) {
        Vector3i pos = wrapper.readBlockPosition();
        PoiType poiType = wrapper.readMappedEntity(PoiTypes.getRegistry());
        int freeTicketCount = wrapper.readVarInt();
        return new DebugPoiInfo(pos,poiType,freeTicketCount);
    }

    public static void write(PacketWrapper<?> wrapper, DebugPoiInfo info) {
        wrapper.writeBlockPosition(info.pos);
        wrapper.writeMappedEntity(info.poiType);
        wrapper.writeVarInt(info.freeTicketCount);
    }

    public Vector3i getPos() {
        return this.pos;
    }

    public PoiType getPoiType() {
        return this.poiType;
    }

    public int getFreeTicketCount() {
        return this.freeTicketCount;
    }
}
