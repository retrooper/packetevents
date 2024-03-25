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

package com.github.retrooper.packetevents.wrapper.configuration.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.wrapper.common.server.WrapperServerResourcePackSend;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Deprecated
public class WrapperConfigServerResourcePackSend extends WrapperServerResourcePackSend {

    public static final int MAX_HASH_LENGTH = 40;

    public WrapperConfigServerResourcePackSend(PacketSendEvent event) {
        super(event);
    }

    public WrapperConfigServerResourcePackSend(String url, String hash, boolean required, @Nullable Component prompt) {
        super(ConnectionState.CONFIGURATION, url, hash, required, prompt);
    }

    public WrapperConfigServerResourcePackSend(UUID packId, String url, String hash, boolean required, @Nullable Component prompt) {
        super(ConnectionState.CONFIGURATION, packId, url, hash, required, prompt);
    }
}
