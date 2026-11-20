/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.User;

public interface ChannelInjector {

    /**
     * @param channel the player's channel
     * @return true when both packetevents handlers are present in the channel's pipeline
     */
    default boolean isInjected(Object channel) {
        return ChannelHelper.getPipelineHandler(channel, PacketEvents.DECODER_NAME) != null
                && ChannelHelper.getPipelineHandler(channel, PacketEvents.ENCODER_NAME) != null;
    }

    /**
     * Adds the packetevents handlers back to an open channel that lost them, reusing the
     * {@link User} still registered for it so its client version, connection state and profile
     * carry over. Does nothing when the channel is already injected or closed.
     * <p>
     * The pipeline is modified on the channel's event loop, so {@link #isInjected(Object)} may
     * still return false directly after this call.
     *
     * @param channel the player's channel
     * @throws UnsupportedOperationException when the platform does not support re-injection
     */
    default void reinject(Object channel) {
        throw new UnsupportedOperationException("Re-injection is not supported on this platform");
    }

    default boolean isServerBound() {
        return true;
    }

    void inject();

    void uninject();

    void updateUser(Object channel, User user);

    void setPlayer(Object channel, Object player);

    boolean isPlayerSet(Object channel);

    boolean isProxy();

    default PacketSide getPacketSide() {
        return PacketSide.SERVER;
    }
}
