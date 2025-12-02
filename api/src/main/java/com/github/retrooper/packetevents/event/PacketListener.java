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

public interface PacketListener {
    default PacketListenerAbstract asAbstract(PacketListenerPriority priority) {
        return new PacketListenerAbstract(priority) {
            @Override
            public void onUserConnect(UserConnectEvent event) {
                PacketListener.this.onUserConnect(event);
            }

            @Override
            public void onUserLogin(UserLoginEvent event) {
                PacketListener.this.onUserLogin(event);
            }

            @Override
            public void onUserDisconnect(UserDisconnectEvent event) {
                PacketListener.this.onUserDisconnect(event);
            }

            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                PacketListener.this.onPacketReceive(event);
            }

            @Override
            public void onPacketSend(PacketSendEvent event) {
                PacketListener.this.onPacketSend(event);
            }

            @Override
            public void onPacketEventExternal(PacketEvent event) {
                PacketListener.this.onPacketEventExternal(event);
            }
        };
    }

    default void onUserConnect(UserConnectEvent event) {
    }

    default void onUserLogin(UserLoginEvent event) {
    }

    default void onUserDisconnect(UserDisconnectEvent event) {
    }

    default void onPacketReceive(PacketReceiveEvent event) {
    }

    default void onPacketSend(PacketSendEvent event) {
    }

    default void onPacketEventExternal(PacketEvent event) {
    }
}
