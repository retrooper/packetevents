/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package io.github.retrooper.packetevents.factory.minestom;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.impl.netty.manager.server.ServerManagerAbstract;

/**
 * {@link com.github.retrooper.packetevents.manager.server.ServerManager} implementation
 * for a Minestom server.
 * <p>
 * Unlike {@code FabricServerManager}, this does not throw when the running Minecraft
 * version has no matching {@link ServerVersion} entry. Minestom's own versioning scheme
 * (e.g. {@code "26.2"}) does not line up with {@link ServerVersion#getReleaseName()}
 * (e.g. {@code "1.21.11"}), so unmatched versions fall back to the latest known one
 * instead of hard-failing the whole platform.
 */
public class MinestomServerManager extends ServerManagerAbstract {

    private final ServerVersion version;

    public MinestomServerManager(String mcVersion) {
        this.version = resolveVersion(mcVersion);
    }

    private static ServerVersion resolveVersion(String mcVersion) {
        for (ServerVersion version : ServerVersion.reversedValues()) {
            if (mcVersion.contains(version.getReleaseName())) {
                return version;
            }
        }
        // PHASE-0-GATE: falling back to the latest known ServerVersion rather than
        // throwing. Minestom 26.2 doesn't correspond to a dotted Minecraft release name
        // yet known to PacketEvents; revisit once ServerVersion gains a matching entry.
        return ServerVersion.getLatest();
    }

    @Override
    public ServerVersion getVersion() {
        return this.version;
    }
}
