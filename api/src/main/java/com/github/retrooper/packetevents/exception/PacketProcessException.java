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

package com.github.retrooper.packetevents.exception;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import org.jetbrains.annotations.Nullable;

public class PacketProcessException extends Exception {
    public enum PacketProcessExceptionReason {
        PACKET_ID,
        PACKET_CONTENT
    }
    private final PacketProcessExceptionReason reason;
    @Nullable
    private final PacketSide side;
    @Nullable
    private final PacketTypeCommon type;
    private final int packetSize;

    public PacketProcessException(Throwable throwable, PacketProcessExceptionReason reason,
                                  @Nullable PacketSide side,
                                  @Nullable Integer packetSize,
                                  @Nullable PacketTypeCommon type) {
        super(throwable);
        this.side = side;
        this.reason = reason;
        this.type = type;
        this.packetSize = packetSize != null ? packetSize : -1;
    }

    public PacketProcessException(PacketProcessExceptionReason reason,
                                  @Nullable PacketSide side, @Nullable Integer packetSize,
                                  @Nullable PacketTypeCommon type) {
        this.side = side;
        this.reason = reason;
        this.type = type;
        this.packetSize = packetSize != null ? packetSize : -1;
    }

    public PacketSide getSide() {
        return side;
    }

    public PacketProcessExceptionReason getReason() {
        return reason;
    }

    @Nullable
    public PacketTypeCommon getType() {
        return type;
    }

    @Override
    public String getMessage() {
        String message = "PacketEvents failed to process a" + (side != null ? side.name().toLowerCase() : "") + " packet.\n";
        switch (reason) {
            case PACKET_ID:
                message += "The packet ID could not be found in the buffer.\n";
                break;
            case PACKET_CONTENT:
                //TODO More clear
                message += "The packet content was not read successfully.\n";
                break;
        }
        if (type != null) {
            message += "Packet type: " + type.getName() + ", Packet ID: " + type.getId() + "\n";
        }
        if (packetSize != -1) {
            message += "Packet size: " + packetSize + "\n";
        }
        message += "Platform version: " + PacketEvents.getAPI().getServerManager().getVersion().getReleaseName() + "\n";
        return message;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
