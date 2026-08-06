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
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
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
 * Inbound movement decode is proven: {@code WireByteFidelityTest} round-trips
 * PositionAndRotation / Position / Rotation / onGround-only packets through
 * {@link #reserializeClientPacket} into PacketEvents wrappers with bit-exact fields.
 * Still unverified against a live client: {@link #register()}'s end-to-end wiring, the
 * outbound path ({@link #reserializeServerPacket}), and any packet whose serializer
 * depends on dynamic registries — see the {@code PHASE-0-GATE} javadocs below.
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
        // Keep PE's decoder state in lockstep with Minestom so EventCreationUtil builds
        // the right event subtype (CONFIGURATION vs PLAY) for this connection phase.
        user.setDecoderState(toPeState(state));
        byte[] payload = reserializeClientPacket(state, event.getPacket());

        // PacketSide.SERVER is "this platform's" side (see MinestomChannelInjector);
        // inbound packets are fed with the opposite side, mirroring how
        // fabric-common's PacketDecoder passes side.getOpposite() into handlePacket.
        PacketSide side = PacketEvents.getAPI().getInjector().getPacketSide().getOpposite();
        if (feed(channel, user, player, payload, side)) {
            // Enforcement seam: PacketEvents (e.g. Grim) cancelled the re-serialized copy;
            // cancel the original Minestom packet so it never reaches the game.
            event.setCancelled(true);
        }
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
        user.setEncoderState(toPeState(state));
        byte[] payload = reserializeServerPacket(state, event.getPacket());

        // Outbound packets are fed with this platform's own side unchanged, mirroring
        // fabric-common's PacketEncoder, which passes its side through as-is.
        PacketSide side = PacketEvents.getAPI().getInjector().getPacketSide();

        ByteBuf buf = Unpooled.wrappedBuffer(payload);
        ProtocolPacketEvent peEvent = null;
        try {
            peEvent = PacketEventsImplHelper.handlePacket(channel, user, player, buf, false, side);
        } catch (Exception e) {
            PacketEvents.getAPI().getLogManager().warn("Failed to process a Minestom packet through PacketEvents", e);
        } finally {
            buf.release();
        }

        if (peEvent != null && peEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        // Run PacketEvents' after-send tasks. Spigot/Sponge run these from their encoder's write
        // promise (AFTER the packet hits the socket); Minestom has no such hook, so without this they
        // are silently dropped. Grim relies on them — e.g. the transaction it brackets every server
        // teleport with (PacketServerTeleport) and its player registration on LOGIN_SUCCESS.
        //
        // Crucially these must run AFTER the current packet is written, not before: Minestom fires
        // PlayerPacketOutEvent from writePacketSync *before* the packet reaches the buffer, so running
        // a task that itself sends a packet (like the post-teleport transaction) synchronously here
        // would put that packet AHEAD of the teleport on the wire — the exact ordering that makes Grim
        // think the teleport was skipped. Defer to the next tick so ordering is preserved.
        if (peEvent instanceof PacketSendEvent sendEvent && sendEvent.hasTasksAfterSend()) {
            final java.util.List<Runnable> afterSend = new java.util.ArrayList<>(sendEvent.getTasksAfterSend());
            MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
                for (Runnable task : afterSend) {
                    try {
                        task.run();
                    } catch (Throwable t) {
                        PacketEvents.getAPI().getLogManager().warn("A packet after-send task failed", t);
                    }
                }
            });
        }
    }

    /**
     * Feeds one re-serialized packet into PacketEvents.
     *
     * @return {@code true} if PacketEvents cancelled the packet (the caller must then
     * cancel the original Minestom event), {@code false} otherwise.
     */
    private static boolean feed(SocketChannel channel, User user, Player player, byte[] payload, PacketSide side) {
        ByteBuf buf = Unpooled.wrappedBuffer(payload);
        try {
            var event = PacketEventsImplHelper.handlePacket(channel, user, player, buf, false, side);
            return event != null && event.isCancelled();
        } catch (Exception e) {
            PacketEvents.getAPI().getLogManager().warn("Failed to process a Minestom packet through PacketEvents", e);
            return false;
        } finally {
            buf.release();
        }
    }

    /**
     * Test hook: re-serialized inbound bytes are fed through the exact {@link #feed} path
     * used in production, returning whether PacketEvents cancelled the packet. Uses a
     * throwaway PLAY-state {@link User} keyed on a real (unconnected) {@link SocketChannel}.
     */
    static boolean feedInboundForTest(byte[] payload) throws java.io.IOException {
        SocketChannel channel = SocketChannel.open();
        try {
            User user = new User(channel,
                    com.github.retrooper.packetevents.protocol.ConnectionState.PLAY,
                    com.github.retrooper.packetevents.protocol.player.ClientVersion.V_26_2,
                    new com.github.retrooper.packetevents.protocol.player.UserProfile(
                            java.util.UUID.randomUUID(), "CancelTestPlayer"));
            PacketSide side = PacketEvents.getAPI().getInjector().getPacketSide().getOpposite();
            return feed(channel, user, null, payload, side);
        } finally {
            channel.close();
        }
    }

    private static @Nullable SocketChannel socketChannelOf(Player player) {
        PlayerConnection connection = player.getPlayerConnection();
        if (connection instanceof PlayerSocketConnection socketConnection) {
            return socketConnection.getChannel();
        }
        return null;
    }

    /** Translates Minestom's connection state to the PacketEvents equivalent. */
    private static com.github.retrooper.packetevents.protocol.ConnectionState toPeState(ConnectionState minestom) {
        return switch (minestom) {
            case HANDSHAKE -> com.github.retrooper.packetevents.protocol.ConnectionState.HANDSHAKING;
            case STATUS -> com.github.retrooper.packetevents.protocol.ConnectionState.STATUS;
            case LOGIN -> com.github.retrooper.packetevents.protocol.ConnectionState.LOGIN;
            case CONFIGURATION -> com.github.retrooper.packetevents.protocol.ConnectionState.CONFIGURATION;
            case PLAY -> com.github.retrooper.packetevents.protocol.ConnectionState.PLAY;
        };
    }

    /**
     * PHASE-0-GATE: reserializes a decoded {@link ClientPacket} back into raw bytes
     * (packet id varint immediately followed by the payload - uncompressed, with no
     * outer length-prefix frame, matching what a Netty decoder would hand to the next
     * pipeline stage on other platforms) using Minestom's own
     * {@link NetworkBuffer}/{@link PacketVanilla} registries - the exact types Minestom
     * itself uses internally (see {@code PlayerSocketConnection#read}). Round-trip byte
     * fidelity is proven for movement packets by {@code WireByteFidelityTest} (loopback,
     * bit-exact). Still to check in the live-wire phase: packets whose
     * {@code NetworkBuffer.Type} depends on dynamic registries (e.g. synced registry
     * ids), where {@link MinecraftServer#getRegistries()} may not be the exact
     * registry view that produced the original packet.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    static byte[] reserializeClientPacket(ConnectionState state, ClientPacket packet) {
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
    static byte[] reserializeServerPacket(ConnectionState state, ServerPacket packet) {
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
