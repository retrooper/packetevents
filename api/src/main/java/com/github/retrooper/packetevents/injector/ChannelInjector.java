/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import org.jetbrains.annotations.Nullable;

public interface ChannelInjector {
    default boolean isBound() {
        return true;
    }

    @Nullable
    ConnectionState getConnectionState(ChannelAbstract channel);

    void changeConnectionState(ChannelAbstract channel, @Nullable ConnectionState packetState);

    void inject();

    void eject();

    void injectPlayer(Object player, @Nullable ConnectionState connectionState);

    void ejectPlayer(Object player);

    boolean hasInjected(Object player);
}
