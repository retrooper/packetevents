/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package io.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.PacketSide;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
public final class FabricUtil {

    private FabricUtil() {
    }

    /**
     * Tests whether this connection should be handled by this packetevents api instance.
     */
    public static boolean isOurConnection(Connection connection) {
        return isOurConnection(connection.getReceiving());
    }

    /**
     * Tests whether this connection should be handled by this packetevents api instance.
     */
    public static boolean isOurConnection(PacketFlow flow) {
        PacketSide connectionSide = switch (flow) {
            case CLIENTBOUND -> PacketSide.CLIENT;
            case SERVERBOUND -> PacketSide.SERVER;
        };
        PacketEventsAPI<?> api = PacketEvents.getAPI();
        return api != null && api.getInjector().getPacketSide() == connectionSide;
    }
}
