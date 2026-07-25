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
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.PacketRegistry;
import net.minestom.server.network.packet.PacketVanilla;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.network.player.PlayerSocketConnection;
import org.jetbrains.annotations.Nullable;

import java.nio.channels.SocketChannel;

/**
 * Feeds Minestom's {@link PlayerPacketEvent} (inbound, client -&gt; server) and
 * {@link PlayerPacketOutEvent} (outbound, server -&gt; client) into
 * {@link PacketEventsImplHelper#handlePacket}, the same entry point every other
 * platform's Netty encoder/decoder uses.
 * <p>
 * This is a Phase 0 scaffold: {@link #register()} wires the listeners, and the
 * packet-side/reserialization logic below is a real, compiling best-effort
 * implementation, but it has <strong>not</strong> been exercised against a live client.
 * See the {@code PHASE-0-GATE} javadoc on {@link #reserializeClientPacket} and
 * {@link #reserializeServerPacket} for exactly what still needs live verification.
 */
public final class MinestomPacketFeeder {

    private MinestomPacketFeeder() {
    }

    /**
     * Registers the Minestom listeners that feed packets into PacketEvents. Must be
     * called after {@link PacketEvents#setAPI} and {@code PacketEventsAPI#init()}.
     */
    public static void register() {
        MinecraftServer.getGlobalEventHandler()
                .addListener(PlayerPacketEvent.class, MinestomPacketFeeder::onInbound);
        MinecraftServer.getGlobalEventHandler()
                .addListener(PlayerPacketOutEvent.class, MinestomPacketFeeder::onOutbound);
    }

    private static void onInbound(PlayerPacketEvent event) {
        Player player = event.getPlayer();
        SocketChannel channel = socketChannelOf(player);
        if (channel == null) {
            return;
        }
        User user = PacketEvents.getAPI().getProtocolManager().getUser(channel);
        if (user == null) {
            // Channel not (yet) tracked by PacketEvents (e.g. connect-time hookup,
            // a later phase's responsibility) - nothing to feed.
            return;
        }

        ConnectionState state = player.getPlayerConnection().getClientState();
        byte[] payload = reserializeClientPacket(state, event.getPacket());

        // PacketSide.SERVER is "this platform's" side (see MinestomChannelInjector);
        // inbound packets are fed with the opposite side, mirroring how
        // fabric-common's PacketDecoder passes side.getOpposite() into handlePacket.
        PacketSide side = PacketEvents.getAPI().getInjector().getPacketSide().getOpposite();
        feed(channel, user, player, payload, side);
    }

    private static void onOutbound(PlayerPacketOutEvent event) {
        Player player = event.getPlayer();
        SocketChannel channel = socketChannelOf(player);
        if (channel == null) {
            return;
        }
        User user = PacketEvents.getAPI().getProtocolManager().getUser(channel);
        if (user == null) {
            return;
        }

        ConnectionState state = player.getPlayerConnection().getServerState();
        byte[] payload = reserializeServerPacket(state, event.getPacket());

        // Outbound packets are fed with this platform's own side unchanged, mirroring
        // fabric-common's PacketEncoder, which passes its side through as-is.
        PacketSide side = PacketEvents.getAPI().getInjector().getPacketSide();
        feed(channel, user, player, payload, side);
    }

    private static void feed(SocketChannel channel, User user, Player player, byte[] payload, PacketSide side) {
        ByteBuf buf = Unpooled.wrappedBuffer(payload);
        try {
            PacketEventsImplHelper.handlePacket(channel, user, player, buf, false, side);
        } catch (Exception e) {
            PacketEvents.getAPI().getLogManager().warn("Failed to process a Minestom packet through PacketEvents", e);
        } finally {
            buf.release();
        }
    }

    private static @Nullable SocketChannel socketChannelOf(Player player) {
        PlayerConnection connection = player.getPlayerConnection();
        if (connection instanceof PlayerSocketConnection socketConnection) {
            return socketConnection.getChannel();
        }
        return null;
    }

    /**
     * PHASE-0-GATE: reserializes a decoded {@link ClientPacket} back into raw bytes
     * (packet id varint immediately followed by the payload - uncompressed, with no
     * outer length-prefix frame, matching what a Netty decoder would hand to the next
     * pipeline stage on other platforms) using Minestom's own
     * {@link NetworkBuffer}/{@link PacketVanilla} registries - the exact types Minestom
     * itself uses internally (see {@code PlayerSocketConnection#read}). This compiles
     * against Minestom 26.2 and mirrors Minestom's internal parse path, but round-trip
     * byte fidelity against a real client has NOT been verified live and must be
     * checked in the live-wire phase - in particular for packets whose
     * {@code NetworkBuffer.Type} depends on dynamic registries (e.g. synced registry
     * ids), where {@link MinecraftServer#getRegistries()} may not be the exact
     * registry view that produced the original packet.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static byte[] reserializeClientPacket(ConnectionState state, ClientPacket packet) {
        PacketRegistry registry = PacketVanilla.CLIENT_PACKET_PARSER.stateRegistry(state);
        PacketRegistry.PacketInfo info = registry.packetInfo(packet.getClass());
        NetworkBuffer.Type type = info.serializer();
        int id = info.id();
        return NetworkBuffer.makeArray(buffer -> {
            buffer.write(NetworkBuffer.VAR_INT, id);
            buffer.write(type, packet);
        }, MinecraftServer.getRegistries());
    }

    /**
     * PHASE-0-GATE: outbound counterpart of {@link #reserializeClientPacket}; the same
     * caveats apply (see {@code PlayerSocketConnection#writePacketSync} for Minestom's
     * own equivalent write path).
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static byte[] reserializeServerPacket(ConnectionState state, ServerPacket packet) {
        PacketRegistry registry = PacketVanilla.SERVER_PACKET_PARSER.stateRegistry(state);
        PacketRegistry.PacketInfo info = registry.packetInfo(packet.getClass());
        NetworkBuffer.Type type = info.serializer();
        int id = info.id();
        return NetworkBuffer.makeArray(buffer -> {
            buffer.write(NetworkBuffer.VAR_INT, id);
            buffer.write(type, packet);
        }, MinecraftServer.getRegistries());
    }
}
