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

package io.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import org.jetbrains.annotations.Nullable;

public class VelocityPipelineInjector implements ChannelInjector {
    @Override
    public boolean isBound() {
        return true;
    }

    @Override
    public @Nullable ConnectionState getConnectionState(Object channel) {
        return null;
    }

    @Override
    public void changeConnectionState(Object channel, @Nullable ConnectionState packetState) {

    }

    @Override
    public void inject() {

    }

    @Override
    public void eject() {

    }

    @Override
    public void injectPlayer(Object player, @Nullable ConnectionState connectionState) {

    }

    @Override
    public void updateUser(Object channel, User user) {

    }

    @Override
    public void ejectPlayer(Object player) {

    }

    @Override
    public boolean hasInjected(Object player) {
        return false;
    }
}
