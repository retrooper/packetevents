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

package io.github.retrooper.packetevents.factory.minestom;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Verifies {@link MinestomProtocolManager#resolveClientVersion(int)} maps a Minestom
 * per-connection protocol id to the matching PacketEvents {@link ClientVersion}.
 */
class ProtocolVersionResolutionTest {

    @Test
    void resolvesNative262() {
        // 26.2 == protocol 776 == ClientVersion.V_26_2
        assertEquals(ClientVersion.V_26_2, MinestomProtocolManager.resolveClientVersion(776));
    }

    @Test
    void aboveKnownRangeFallsBackToLatest() {
        // ClientVersion.getById already clamps out-of-range ids to getLatest(); the
        // resolver additionally guards the in-range UNKNOWN case (not leaked to callers).
        assertEquals(ClientVersion.getLatest(), MinestomProtocolManager.resolveClientVersion(Integer.MAX_VALUE));
    }
}
