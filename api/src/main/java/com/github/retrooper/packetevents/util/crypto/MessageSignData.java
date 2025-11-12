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

import java.time.Instant;

public class MessageSignData {
    private final SaltSignature saltSignature;
    private final Instant timestamp;
    private boolean signedPreview;

    public MessageSignData(SaltSignature saltSignature, Instant timestamp) {
        this.saltSignature = saltSignature;
        this.timestamp = timestamp;
    }

    public MessageSignData(SaltSignature saltSignature, Instant timestamp, boolean signedPreview) {
        this.saltSignature = saltSignature;
        this.timestamp = timestamp;
        this.signedPreview = signedPreview;
    }
    public SaltSignature getSaltSignature() {
        return saltSignature;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public boolean isSignedPreview() {
        return signedPreview;
    }

    public void setSignedPreview(boolean signedPreview) {
        this.signedPreview = signedPreview;
    }
}
