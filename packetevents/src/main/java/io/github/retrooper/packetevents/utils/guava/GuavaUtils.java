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

package io.github.retrooper.packetevents.utils.guava;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.util.concurrent.ConcurrentMap;

public class GuavaUtils {
    public static <T, K>ConcurrentMap<T, K> makeMap() {
        if (PacketEvents.get().getServerUtils().getVersion().isNewerThan(ServerVersion.v_1_7_10)) {
            return GuavaUtils_8.makeMap();
        }
        else {
            return GuavaUtils_7.makeMap();
        }
    }
}
