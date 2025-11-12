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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

public class WrapperPlayServerDisconnect extends PacketWrapper<WrapperPlayServerDisconnect> {
    private Component reason;

    public WrapperPlayServerDisconnect(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDisconnect(Component reason) {
        super(PacketType.Play.Server.DISCONNECT);
        this.reason = reason;
    }

    @Override
    public void read() {
        reason = readComponent();
    }

    @Override
    public void write() {
        writeComponent(reason);
    }

    @Override
    public void copy(WrapperPlayServerDisconnect wrapper) {
        this.reason = wrapper.reason;
    }

    public Component getReason() {
        return reason;
    }

    public void setReason(Component reason) {
        this.reason = reason;
    }
}
