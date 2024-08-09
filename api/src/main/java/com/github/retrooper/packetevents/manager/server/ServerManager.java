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

package com.github.retrooper.packetevents.manager.server;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import org.jetbrains.annotations.Nullable;

public interface ServerManager {
    /**
     * Get the server version.
     *
     * @return Get Server Version
     */
    ServerVersion getVersion();

    /**
     * Get the operating system of the local machine
     *
     * @return Get Operating System
     */
    default SystemOS getOS() {
        return SystemOS.getOS();
    }

    /**
     * Gets a platform-specific network-synchronized-registries cache key.
     * <p>
     * This tells packetevents, if a registry should be cached or read again. On
     * backend servers with global registries, this may be a constant value. On
     * proxy servers with per-server registries, this may be a value which depends
     * on the current server the {@link User} is on.
     *
     * @param user    the {@link User} for which the registry gets read
     * @param version the version the packet is for
     * @return some value or null for no caching at all
     */
    default @Nullable Object getRegistryCacheKey(User user, ClientVersion version) {
        return null; // no caching
    }
}
