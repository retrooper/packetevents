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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PlayerInjectEvent;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import io.github.retrooper.packetevents.injector.latest.SpigotChannelInjectorLatest;
import io.github.retrooper.packetevents.injector.legacy.SpigotChannelInjectorLegacy;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;

public class SpigotChannelInjector implements ChannelInjector {
    private final ChannelInjector injector;

    public SpigotChannelInjector() {
        this.injector = SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE
                ? new SpigotChannelInjectorLatest() //1.8+ injector
                : new SpigotChannelInjectorLegacy(); //1.7 injector
    }

    @Override
    public boolean isServerBound() {
        return injector.isServerBound();
    }

    @Override
    public void inject() {
        injector.inject();
    }

    @Override
    public void uninject() {
        injector.uninject();
    }

    @Override
    public void updateUser(Object channel, User user) {
        injector.updateUser(channel, user);
    }

    public void updatePlayer(Object player) {
        PlayerInjectEvent injectEvent = new PlayerInjectEvent(player);
        PacketEvents.getAPI().getEventManager().callEvent(injectEvent);
        if (!injectEvent.isCancelled()) {
            Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
            setPlayer(channel, player);
        }
    }

    @Override
    public boolean hasPlayer(Object player) {
        return injector.hasPlayer(player);
    }

    @Override
    public User getUser(Object channel) {
        return injector.getUser(channel);
    }

    @Override
    public void setPlayer(Object channel, Object player) {
        injector.setPlayer(channel, player);
    }

    @Override
    public void changeConnectionState(Object channel, ConnectionState connectionState) {
        injector.changeConnectionState(channel, connectionState);
    }
}
