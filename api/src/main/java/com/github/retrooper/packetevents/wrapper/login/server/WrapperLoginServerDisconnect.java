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

package com.github.retrooper.packetevents.wrapper.login.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

/**
 * This packet is used by the server to disconnect the client while in the {@link ConnectionState#LOGIN} connection state.
 */
public class WrapperLoginServerDisconnect extends PacketWrapper<WrapperLoginServerDisconnect> {
    private Component reason;

    public WrapperLoginServerDisconnect(PacketSendEvent event) {
        super(event);
    }

    public WrapperLoginServerDisconnect(Component reason) {
        super(PacketType.Login.Server.DISCONNECT);
        this.reason = reason;
    }

    @Override
    public void read() {
        this.reason = this.readComponentAsJSON();
    }

    @Override
    public void write() {
        this.writeComponentAsJSON(this.reason);
    }

    @Override
    public void copy(WrapperLoginServerDisconnect wrapper) {
        this.reason = wrapper.reason;
    }

    /**
     * The reason the server disconnected the client. (Specified by the server)
     *
     * @return Disconnection reason
     */
    public Component getReason() {
        return reason;
    }

    public void setReason(Component reason) {
        this.reason = reason;
    }
}
