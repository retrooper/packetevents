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

package com.github.retrooper.packetevents.protocol.player;

import java.security.PublicKey;
import java.time.Instant;

public class PublicProfileKey {
    private final Instant expiresAt;
    private final PublicKey key;
    private final byte[] keySignature;

    public PublicProfileKey(Instant expiresAt, PublicKey key, byte[] keySignature) {
        this.expiresAt = expiresAt;
        this.key = key;
        this.keySignature = keySignature;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public PublicKey getKey() {
        return key;
    }

    public byte[] getKeySignature() {
        return keySignature;
    }

    public boolean hasExpired() {
        return expiresAt.isBefore(Instant.now());
    }
}
