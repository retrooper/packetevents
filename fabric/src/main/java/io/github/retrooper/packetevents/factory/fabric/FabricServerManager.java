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

package io.github.retrooper.packetevents.factory.fabric;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import io.github.retrooper.packetevents.impl.netty.manager.server.ServerManagerAbstract;
import net.minecraft.SharedConstants;

public class FabricServerManager extends ServerManagerAbstract {

    private ServerVersion version;

    private ServerVersion resolveVersion() {
        String mcVersion = SharedConstants.getCurrentVersion().getId();
        for (ServerVersion version : ServerVersion.reversedValues()) {
            if (mcVersion.contains(version.getReleaseName())) {
                return version;
            }
        }
        throw new IllegalStateException("PacketEvents doesn't support Minecraft version: " + mcVersion
                + "; if you believe this is an error, please report it to it PacketEvents");
    }

    @Override
    public ServerVersion getVersion() {
        if (this.version == null) {
            this.version = this.resolveVersion();
        }
        return this.version;
    }

    @Override
    public Object getRegistryCacheKey(User user, ClientVersion version) {
        return GlobalRegistryHolder.getGlobalRegistryCacheKey(user, version);
    }
}
