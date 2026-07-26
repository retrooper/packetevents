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

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.common.PingPacket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Enforcement seam, outbound half (Task 1.5): a PacketEvents server ping wrapper
 * translates into a real Minestom {@link PingPacket} carrying the same id — proving the
 * PE-encode → Minestom-decode round trip Grim needs for its self-sent timer packets.
 * The {@code player.sendPacket(...)} leg is verified at the Phase 1 live-client checkpoint.
 */
class PacketSenderTest {

    @BeforeAll
    static void boot() {
        PeMinestomTestBootstrap.ensureBooted();
    }

    @Test
    void pingRoundTripsToMinestomPacket() {
        int id = 0x0BADF00D;
        WrapperPlayServerPing wrapper = new WrapperPlayServerPing(id);

        ServerPacket packet = MinestomPacketSender.toMinestomServerPacket(ConnectionState.PLAY, wrapper);

        PingPacket ping = assertInstanceOf(PingPacket.class, packet);
        assertEquals(id, ping.id());
    }
}
