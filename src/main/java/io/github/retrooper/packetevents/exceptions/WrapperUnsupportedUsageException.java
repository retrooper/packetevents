/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
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

package io.github.retrooper.packetevents.exceptions;

import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;

/**
 * An exception thrown by PacketEvents when a wrapper is used although it isn't supported on the local server version.
 * Make sure to decompile a wrapper before using it, if you are unsure if it is supported through out all the server versions.
 * We override a {@link WrappedPacket#isSupported()} method if it isn't supported on all server versions.
 *
 * @author retrooper
 * @see WrappedPacket#isSupported()
 * @see WrappedPacket.SupportedVersions
 * @since 1.7
 */
public class WrapperUnsupportedUsageException extends RuntimeException {
    public WrapperUnsupportedUsageException(String message) {
        super(message);
    }

    public WrapperUnsupportedUsageException(Class<? extends WrappedPacket> wrapperClass) {
        this("You are using a packet wrapper which happens to be unsupported on the local server version. Packet wrapper you attempted to use: " + ClassUtil.getClassSimpleName(wrapperClass));
    }
}
