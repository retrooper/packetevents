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

import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.User;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link ChannelInjector} implementation for a Minestom server.
 * <p>
 * Minestom's connections are backed by plain {@link SocketChannel}s driven by its own
 * read/write threads, not a Netty {@code ChannelPipeline}. There is therefore nothing to
 * "inject" or "uninject" into, mirroring {@code FabricChannelInjector}'s no-op approach
 * (which itself relies on a Netty pipeline present for other reasons Minestom lacks
 * entirely). We just keep our own bookkeeping of which {@link User}/player is associated
 * with which channel.
 */
public class MinestomChannelInjector implements ChannelInjector {

    private final Map<SocketChannel, User> users = new ConcurrentHashMap<>();
    private final Map<SocketChannel, Object> players = new ConcurrentHashMap<>();

    @Override
    public void inject() {
        // NO-OP: no pipeline to inject into.
    }

    @Override
    public void uninject() {
        // NO-OP: no pipeline to uninject from.
    }

    @Override
    public void updateUser(Object channel, User user) {
        this.users.put((SocketChannel) channel, user);
    }

    @Override
    public void setPlayer(Object channel, Object player) {
        this.players.put((SocketChannel) channel, player);
    }

    @Override
    public boolean isPlayerSet(Object channel) {
        return this.players.containsKey((SocketChannel) channel);
    }

    @Override
    public boolean isProxy() {
        return false;
    }

    @Override
    public PacketSide getPacketSide() {
        return PacketSide.SERVER;
    }

    /**
     * Removes any bookkeeping kept for the given channel. Should be called once a
     * connection is fully closed/disconnected.
     */
    public void removeChannel(SocketChannel channel) {
        this.users.remove(channel);
        this.players.remove(channel);
    }
}
