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

package io.github.retrooper.packetevents.wrapper.login.server;

import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginServerDisconnect extends PacketWrapper {
    public static int REASON_LENGTH = -1;
    private final String reason;

    public WrapperLoginServerDisconnect(ClientVersion version, ByteBufAbstract byteBuf) {
        super(version, byteBuf);
        if (REASON_LENGTH == -1) {
            REASON_LENGTH = version.isNewerThanOrEquals(ClientVersion.v_1_14) ? 262144 : 32767;
        }
        this.reason = readString(REASON_LENGTH);
    }

    public String getReason() {
        return reason;
    }
}
