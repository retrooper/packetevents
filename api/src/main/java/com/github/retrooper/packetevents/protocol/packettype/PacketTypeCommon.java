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

package com.github.retrooper.packetevents.protocol.packettype;

public interface PacketTypeCommon {
    default String getName() {
        return ((Enum<?>) this).name();
    }

    default int getId() {
        if (this instanceof PacketType.Handshaking.Client) {
            PacketType.Handshaking.Client client = (PacketType.Handshaking.Client) this;
            return client.getId();
        }
        else if (this instanceof PacketType.Status.Client) {
            PacketType.Status.Client client = (PacketType.Status.Client) this;
            return client.getId();
        }
        else if (this instanceof PacketType.Status.Server) {
            PacketType.Status.Server server = (PacketType.Status.Server) this;
            return server.getId();
        }
        else if (this instanceof PacketType.Login.Client) {
            PacketType.Login.Client client = (PacketType.Login.Client) this;
            return client.getId();
        }
        else if (this instanceof PacketType.Login.Server) {
            PacketType.Login.Server server = (PacketType.Login.Server) this;
            return server.getId();
        }
        else if (this instanceof PacketType.Play.Client) {
            PacketType.Play.Client client = (PacketType.Play.Client) this;
            return client.getId();
        }
        else if (this instanceof PacketType.Play.Server) {
            PacketType.Play.Server server = (PacketType.Play.Server) this;
            return server.getId();
        }
        else {
            return -1;
        }
    }
}
