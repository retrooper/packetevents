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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Enforcement seam (Task 1.4): when a PacketEvents listener cancels a fed packet, the
 * {@code feed} path reports it so {@code MinestomPacketFeeder} can cancel the original
 * Minestom event. Loopback-tested via {@link MinestomPacketFeeder#feedInboundForTest};
 * the two-line mirror onto {@code PlayerPacketEvent#setCancelled} is verified at the
 * Phase 1 live-client checkpoint.
 */
class CancelMirroringTest {

    /** Toggled per-test so the shared listener does not affect other test classes. */
    private static volatile boolean cancelNext;

    @BeforeAll
    static void boot() {
        PeMinestomTestBootstrap.ensureBooted();
        PacketEvents.getAPI().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(PacketReceiveEvent e) {
                if (cancelNext && e.getPacketType() == PacketType.Play.Client.PLAYER_POSITION) {
                    e.setCancelled(true);
                }
            }
        });
    }

    private static byte[] positionBytes() {
        return MinestomPacketFeeder.reserializeClientPacket(
                ConnectionState.PLAY, new ClientPlayerPositionPacket(new Pos(1, 2, 3), true, false));
    }

    @Test
    void peCancelIsReportedToCaller() throws Exception {
        cancelNext = true;
        try {
            assertTrue(MinestomPacketFeeder.feedInboundForTest(positionBytes()),
                    "feed() must report cancellation when a PE listener cancels the packet");
        } finally {
            cancelNext = false;
        }
    }

    @Test
    void uncancelledPacketIsNotReportedAsCancelled() throws Exception {
        cancelNext = false;
        assertFalse(MinestomPacketFeeder.feedInboundForTest(positionBytes()),
                "feed() must not report cancellation when no listener cancels");
    }
}
