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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * Mojang name: ClientboundLowDiskSpaceWarningPacket
 *
 * @versions 26.1+
 */
@NullMarked
public class WrapperPlayServerLowDiskSpaceWarning extends PacketWrapper<WrapperPlayServerLowDiskSpaceWarning> {

    public WrapperPlayServerLowDiskSpaceWarning(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerLowDiskSpaceWarning() {
        super(PacketType.Play.Server.LOW_DISK_SPACE_WARNING);
    }
}
