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

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.ConnectionState;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.wrapper.SendablePacketWrapper;

/**
 * This packet is used by the server to disconnect the client while in the {@link ConnectionState#LOGIN} connection state.
 */
public class WrapperLoginServerDisconnect extends SendablePacketWrapper {
    private static final int MODERN_REASON_LENGTH = 262144;
    private static final int LEGACY_REASON_LENGTH = 32767;
    private String reason;

    public WrapperLoginServerDisconnect(PacketSendEvent event) {
        super(event);
        int reasonLength = getServerVersion().isNewerThanOrEquals(ServerVersion.v_1_14) ? MODERN_REASON_LENGTH : LEGACY_REASON_LENGTH;
        this.reason = readString(reasonLength);
    }

    public WrapperLoginServerDisconnect(String reason) {
        super(PacketType.Login.Server.DISCONNECT.getID());
        this.reason = reason;
    }


    /**
     * The reason the server disconnected the client. (Specified by the server)
     *
     * @return Disconnection reason
     */
    public String getReason() {
        return reason;
    }

    @Override
    public void createPacket() {
        int maxLen = protocolVersion >= 477 ? MODERN_REASON_LENGTH : LEGACY_REASON_LENGTH;
        if (reason.length() > maxLen) {
            reason = reason.substring(0, maxLen);
        }
        writeString(reason, maxLen);
    }
}
