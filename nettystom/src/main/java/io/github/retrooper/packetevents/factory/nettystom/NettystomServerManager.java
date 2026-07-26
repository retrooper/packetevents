/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package io.github.retrooper.packetevents.factory.nettystom;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import io.github.retrooper.packetevents.impl.netty.manager.server.ServerManagerAbstract;

public class NettystomServerManager extends ServerManagerAbstract {

    private final ServerVersion version;

    public NettystomServerManager() {
        this(ServerVersion.V_1_21_11);
    }

    public NettystomServerManager(ServerVersion version) {
        this.version = version;
    }

    /**
     * Resolves a packetevents {@link ServerVersion} from a Minecraft release name
     * (e.g. {@code "1.21.11"}).
     */
    public static ServerVersion resolveVersion(String mcVersion) {
        for (ServerVersion version : ServerVersion.reversedValues()) {
            if (mcVersion.contains(version.getReleaseName())) {
                return version;
            }
        }
        throw new IllegalStateException("PacketEvents doesn't support Minecraft version: " + mcVersion);
    }

    @Override
    public ServerVersion getVersion() {
        return this.version;
    }

    @Override
    public Object getRegistryCacheKey(User user, ClientVersion version) {
        return GlobalRegistryHolder.getGlobalRegistryCacheKey(user, version);
    }
}
