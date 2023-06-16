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

package com.github.retrooper.packetevents.protocol.chat;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MessageSignature {
    private byte[] bytes;

    public MessageSignature(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public static class Packed {
        private int id;
        private @Nullable MessageSignature fullSignature;

        public Packed(@Nullable MessageSignature fullSignature) {
            this.id = -1;
            this.fullSignature = fullSignature;
        }

        public Packed(int id) {
            this.id = id;
            this.fullSignature = null;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Optional<MessageSignature> getFullSignature() {
            return Optional.ofNullable(fullSignature);
        }

        public void setFullSignature(@Nullable MessageSignature fullSignature) {
            this.fullSignature = fullSignature;
        }
    }
}
