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

package io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SteelPhoenix, retrooper
 * @since 1.8
 * TODO Make sendable, allow modifying watchable objects, test on 1.7.10
 */
public class WrappedPacketOutEntityMetadata extends WrappedPacketEntityAbstraction {
    public WrappedPacketOutEntityMetadata(NMSPacket packet) {
        super(packet);
    }


    public List<WrappedWatchableObject> getWatchableObjects() {
        List<Object> nmsWatchableObjectList = readList(0);
        //It's annotated as nullable on 1.17 NMS, so lets just handle it being null
        if (nmsWatchableObjectList == null) {
            return new ArrayList<>();
        }
        List<WrappedWatchableObject> wrappedWatchableObjects = new ArrayList<>(nmsWatchableObjectList.size());
        for (Object watchableObject : nmsWatchableObjectList) {
            wrappedWatchableObjects.add(new WrappedWatchableObject(new NMSPacket(watchableObject)));
        }
        return wrappedWatchableObjects;
    }
}
