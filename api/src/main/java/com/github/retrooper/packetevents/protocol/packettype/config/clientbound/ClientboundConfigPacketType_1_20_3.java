/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2023 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.packettype.config.clientbound;

public enum ClientboundConfigPacketType_1_20_3 {

    PLUGIN_MESSAGE,
    DISCONNECT,
    CONFIGURATION_END,
    KEEP_ALIVE,
    PING,
    REGISTRY_DATA,
    RESOURCE_PACK_REMOVE, // New packet
    RESOURCE_PACK_SEND,
    UPDATE_ENABLED_FEATURES,
    UPDATE_TAGS,
}
