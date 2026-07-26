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
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerPositionStatusPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.channels.SocketChannel;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Phase 0 go/no-go gate: proves that an inbound Minestom movement packet, re-serialized
 * through {@link MinestomPacketFeeder#reserializeClientPacket} and fed into
 * {@link PacketEventsImplHelper#handlePacket}, decodes into a PacketEvents wrapper whose
 * field values are <em>byte-identical</em> to what the client sent.
 * <p>
 * This is a pure loopback (no live client / no socket traffic): it drives the exact
 * production code path the {@code PlayerPacketEvent} listener uses. Doubles are compared
 * with {@code delta = 0.0} (bit-exact) and floats with {@code 0.0f}; if Minestom's wire
 * layout and PacketEvents' decoder ever diverge for these packets, this test fails.
 */
class WireByteFidelityTest {

    /** Set synchronously by the capture listener during {@link #feedInbound}. */
    private static volatile Decoded captured;

    private record Decoded(PacketTypeCommon type, Double x, Double y, Double z,
                           Float yaw, Float pitch, boolean onGround, boolean horizontalCollision) {
    }

    @BeforeAll
    static void boot() {
        // Load Minestom's registries + packet parsers (needed by reserializeClientPacket);
        // init() does not bind a socket, so it is safe in a unit test.
        MinecraftServer.init();

        PacketEventsSettings settings = new PacketEventsSettings()
                .checkForUpdates(false) // no network call in tests
                .reEncodeByDefault(false);
        MinestomPacketEventsAPI api = new MinestomPacketEventsAPI("26.2", settings);
        PacketEvents.setAPI(api);
        api.load();
        api.init();

        PacketEvents.getAPI().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(PacketReceiveEvent e) {
                PacketTypeCommon t = e.getPacketType();
                if (t == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION) {
                    WrapperPlayClientPlayerPositionAndRotation w = new WrapperPlayClientPlayerPositionAndRotation(e);
                    captured = new Decoded(t, w.getPosition().getX(), w.getPosition().getY(), w.getPosition().getZ(),
                            w.getYaw(), w.getPitch(), w.isOnGround(), w.isHorizontalCollision());
                } else if (t == PacketType.Play.Client.PLAYER_POSITION) {
                    WrapperPlayClientPlayerPosition w = new WrapperPlayClientPlayerPosition(e);
                    captured = new Decoded(t, w.getPosition().getX(), w.getPosition().getY(), w.getPosition().getZ(),
                            null, null, w.isOnGround(), w.isHorizontalCollision());
                } else if (t == PacketType.Play.Client.PLAYER_ROTATION) {
                    WrapperPlayClientPlayerRotation w = new WrapperPlayClientPlayerRotation(e);
                    captured = new Decoded(t, null, null, null,
                            w.getYaw(), w.getPitch(), w.isOnGround(), w.isHorizontalCollision());
                } else if (t == PacketType.Play.Client.PLAYER_FLYING) {
                    WrapperPlayClientPlayerFlying w = new WrapperPlayClientPlayerFlying(e);
                    captured = new Decoded(t, null, null, null, null, null,
                            w.isOnGround(), w.isHorizontalCollision());
                }
            }
        });
    }

    private static Decoded feedInbound(ClientPacket packet) throws Exception {
        captured = null;
        // Minestom's own ConnectionState (not PacketEvents') drives the reserialization
        // registry lookup; PacketEvents' ConnectionState below drives the decode side.
        byte[] payload = MinestomPacketFeeder.reserializeClientPacket(
                net.minestom.server.network.ConnectionState.PLAY, packet);

        User user;
        try (SocketChannel channel = SocketChannel.open()) {
            user = new User(channel, ConnectionState.PLAY, ClientVersion.V_26_2,
                    new UserProfile(UUID.randomUUID(), "WireTestPlayer"));

            ByteBuf buf = Unpooled.wrappedBuffer(payload);
            try {
                // Inbound (serverbound) packets are fed with PacketSide.CLIENT, exactly as
                // MinestomPacketFeeder.onInbound does (getPacketSide().getOpposite()).
                PacketEventsImplHelper.handlePacket(channel, user, null, buf, false, PacketSide.CLIENT);
            } finally {
                buf.release();
            }
        }

        assertNotNull(captured, "capture listener never decoded a packet — reserialization or decode failed");
        return captured;
    }

    @Test
    void positionAndRotationRoundTripsByteIdentical() throws Exception {
        double x = 123.5;
        double y = 64.0;
        double z = -987.25;
        float yaw = 42.5f;
        float pitch = -12.25f;

        Decoded d = feedInbound(new ClientPlayerPositionAndRotationPacket(
                new Pos(x, y, z, yaw, pitch), true, false));

        assertEquals(PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION, d.type());
        assertEquals(x, d.x(), 0.0);
        assertEquals(y, d.y(), 0.0);
        assertEquals(z, d.z(), 0.0);
        assertEquals(yaw, d.yaw(), 0.0f);
        assertEquals(pitch, d.pitch(), 0.0f);
        assertTrue(d.onGround());
        assertEquals(false, d.horizontalCollision());
    }

    @Test
    void positionRoundTripsByteIdentical() throws Exception {
        double x = -0.5;
        double y = 255.0;
        double z = 1024.75;

        Decoded d = feedInbound(new ClientPlayerPositionPacket(new Pos(x, y, z), false, true));

        assertEquals(PacketType.Play.Client.PLAYER_POSITION, d.type());
        assertEquals(x, d.x(), 0.0);
        assertEquals(y, d.y(), 0.0);
        assertEquals(z, d.z(), 0.0);
        assertEquals(false, d.onGround());
        assertTrue(d.horizontalCollision());
    }

    @Test
    void rotationRoundTripsByteIdentical() throws Exception {
        float yaw = -179.75f;
        float pitch = 89.5f;

        Decoded d = feedInbound(new ClientPlayerRotationPacket(yaw, pitch, true, false));

        assertEquals(PacketType.Play.Client.PLAYER_ROTATION, d.type());
        assertEquals(yaw, d.yaw(), 0.0f);
        assertEquals(pitch, d.pitch(), 0.0f);
        assertTrue(d.onGround());
        assertEquals(false, d.horizontalCollision());
    }

    @Test
    void onGroundOnlyRoundTripsByteIdentical() throws Exception {
        Decoded d = feedInbound(new ClientPlayerPositionStatusPacket(true, true));

        assertEquals(PacketType.Play.Client.PLAYER_FLYING, d.type());
        assertTrue(d.onGround());
        assertTrue(d.horizontalCollision());
    }
}
