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

package io.github.retrooper.packetevents.sponge.util.viaversion;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.sponge.handlers.SpongeDecodeHandler;
import com.viaversion.viaversion.sponge.handlers.SpongeEncodeHandler;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class ViaVersionAccessorImpl implements ViaVersionAccessor {

    @Override
    public int getProtocolVersion(ServerPlayer player) {
        return Via.getAPI().getPlayerVersion(player);
    }

    @Override
    public Class<?> getUserConnectionClass() {
        return UserConnection.class;
    }

    @Override
    public Class<?> getSpongeDecodeHandlerClass() {
        return SpongeDecodeHandler.class;
    }

    @Override
    public Class<?> getSpongeEncodeHandlerClass() {
        return SpongeEncodeHandler.class;
    }
}
