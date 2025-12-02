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

package com.github.retrooper.packetevents.util.crypto;

import java.security.PublicKey;
import java.time.Instant;

public class SignatureData {
    private final Instant timestamp;
    private final PublicKey publicKey;
    private final byte[] signature;

    public SignatureData(Instant timestamp, PublicKey publicKey, byte[] signature) {
        this.timestamp = timestamp;
        this.publicKey = publicKey;
        this.signature = signature;
    }

    public SignatureData(Instant timestamp, byte[] encodedPublicKey, byte[] signature) {
        this.timestamp = timestamp;
        this.publicKey = MinecraftEncryptionUtil.publicKey(encodedPublicKey);
        this.signature = signature;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

   public PublicKey getPublicKey() {
        return publicKey;
    }

    public byte[] getSignature() {
        return signature;
    }
}
