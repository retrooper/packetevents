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
import com.github.retrooper.packetevents.protocol.ProtocolVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.impl.netty.manager.protocol.ProtocolManagerAbstract;
import org.jetbrains.annotations.Nullable;

/**
 * {@link com.github.retrooper.packetevents.manager.protocol.ProtocolManager} implementation
 * for a Minestom server.
 * <p>
 * PHASE-0-GATE: {@link ProtocolManagerAbstract}'s default {@code getUser}/{@code setUser}/
 * {@code removeUser} key the shared {@code USERS} map off {@code ChannelHelper.getPipeline(channel)},
 * which assumes {@code channel} is a real {@code io.netty.channel.Channel} with a pipeline.
 * Minestom's "channel" is a plain {@link java.nio.channels.SocketChannel}, which has no
 * pipeline, so those defaults would throw a {@link ClassCastException} at runtime here.
 * We override them below to key directly off the channel object instead. This is a real
 * fix (not a stub), but the underlying {@code sendPacket}/{@code writePacket}/etc. methods
 * inherited from {@link ProtocolManagerAbstract} still go through {@code ChannelHelper},
 * i.e. {@code NettyManagerImpl}'s {@code ChannelOperatorImpl}, which likewise assumes a
 * real Netty {@code Channel}. Those will still misbehave against a raw
 * {@link java.nio.channels.SocketChannel} at runtime and need a Minestom-aware
 * {@code ChannelOperator}/{@code NettyManager} pair in a later phase; this is out of scope
 * for the Phase 0 SPI wiring done here and must be verified live.
 */
public class MinestomProtocolManager extends ProtocolManagerAbstract {

    @Override
    public ProtocolVersion getPlatformVersion() {
        // TODO implement platform version, same as FabricProtocolManager
        return ProtocolVersion.UNKNOWN;
    }

    @Override
    public @Nullable User getUser(Object channel) {
        return USERS.get(channel);
    }

    @Override
    public void setUser(Object channel, User user) {
        synchronized (channel) {
            USERS.put(channel, user);
        }
        PacketEvents.getAPI().getInjector().updateUser(channel, user);
    }

    @Override
    public @Nullable User removeUser(Object channel) {
        return USERS.remove(channel);
    }
}
