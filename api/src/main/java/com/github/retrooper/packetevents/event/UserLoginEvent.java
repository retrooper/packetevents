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

package com.github.retrooper.packetevents.event;

import com.github.retrooper.packetevents.protocol.player.User;

public class UserLoginEvent extends PacketEvent implements CallableEvent, UserEvent, PlayerEvent {
    private final User user;
    private final Object player;

    public UserLoginEvent(User user, Object player) {
        this.user = user;
        this.player = player;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public <T> T getPlayer() {
        return (T) player;
    }

    @Override
    public void call(PacketListenerCommon listener) {
        listener.onUserLogin(this);
    }
}
