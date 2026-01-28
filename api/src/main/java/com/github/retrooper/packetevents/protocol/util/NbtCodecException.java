/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.util;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class NbtCodecException extends RuntimeException {

    private static final boolean DEBUG_TRACES = Boolean.getBoolean("packetevents.debug.nbt-codec-trace");

    public NbtCodecException(String message) {
        super(message);
    }

    public NbtCodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public NbtCodecException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        // don't fill in stack traces by default
        return DEBUG_TRACES ? super.fillInStackTrace() : this;
    }
}
