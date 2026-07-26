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

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.PacketRegistry;
import net.minestom.server.network.packet.PacketVanilla;
import net.minestom.server.network.packet.server.ServerPacket;

/**
 * Enforcement seam, outbound half (Task 1.5): translates a PacketEvents server-bound
 * wrapper into a real Minestom {@link ServerPacket} so packets that <em>Grim itself</em>
 * originates (ping/transaction for timer &amp; latency checks) reach the client.
 * <p>
 * PacketEvents' own {@code sendPacket}/{@code writePacket} route through {@code ChannelHelper}
 * → {@code NettyManagerImpl}, which assumes a Netty {@code Channel}; Minestom's channel is a
 * raw {@link java.nio.channels.SocketChannel}, so that path is unusable. Instead we serialize
 * the wrapper to wire bytes and re-decode them through Minestom's own server packet registry
 * — the exact inverse of {@link MinestomPacketFeeder#reserializeServerPacket}.
 * <p>
 * PHASE-1 SCOPE: fixed-shape, registry-independent packets (Ping is the one Grim needs).
 * Registry-dependent server packets belong to the Phase 3 live-wire work.
 */
public final class MinestomPacketSender {

    private MinestomPacketSender() {
    }

    /**
     * Serializes {@code wrapper} to wire bytes (native packet-id varint + body) and decodes
     * them into the Minestom {@link ServerPacket} for the given connection {@code state}.
     */
    public static ServerPacket toMinestomServerPacket(ConnectionState state, PacketWrapper<?> wrapper) {
        // 1. PE wrapper -> wire bytes. Pre-set an Unpooled buffer so prepareForSend does not
        //    hit ChannelHelper.pooledByteBuf (Netty-only); with proxy=false the channel arg is
        //    never dereferenced, so null is safe.
        ByteBuf peBuf = Unpooled.buffer();
        wrapper.setBuffer(peBuf);
        wrapper.prepareForSend(null, true, false);
        byte[] bytes = ByteBufUtil.getBytes(peBuf);
        peBuf.release();

        // 2. wire bytes -> Minestom ServerPacket via Minestom's own server registry.
        NetworkBuffer reader = NetworkBuffer.wrap(bytes, 0, bytes.length, MinecraftServer.getRegistries());
        int id = reader.read(NetworkBuffer.VAR_INT);
        PacketRegistry<? extends ServerPacket> registry = PacketVanilla.SERVER_PACKET_PARSER.stateRegistry(state);
        return registry.create(id, reader);
    }

    /** Translates {@code wrapper} and sends it to {@code player} on its current server state. */
    public static void send(Player player, PacketWrapper<?> wrapper) {
        ConnectionState state = player.getPlayerConnection().getServerState();
        player.sendPacket(toMinestomServerPacket(state, wrapper));
    }
}
