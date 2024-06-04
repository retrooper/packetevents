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

package io.github.retrooper.packetevents.util.viaversion;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.bukkit.handlers.BukkitDecodeHandler;
import com.viaversion.viaversion.bukkit.handlers.BukkitEncodeHandler;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class ViaVersionAccessorImpl implements ViaVersionAccessor {
    private static Field CONNECTION_FIELD;
    @Override
    public int getProtocolVersion(Player player) {
        return Via.getAPI().getPlayerVersion(player);
    }

    @Override
    public int getProtocolVersion(User user) {
        try {
            Object viaEncoder = ((Channel) user.getChannel()).pipeline().get("via-encoder");
            if (CONNECTION_FIELD == null) {
                CONNECTION_FIELD = Reflection.getField(viaEncoder.getClass(), "connection");
            }
            UserConnection connection = (UserConnection) CONNECTION_FIELD.get(viaEncoder);
            return connection.getProtocolInfo().getProtocolVersion();
        }
        catch (IllegalAccessException e) {
            PacketEvents.getAPI().getLogManager().warn("Unable to grab ViaVersion client version for player!");
            return -1;
        }
    }

    @Override
    public Class<?> getUserConnectionClass() {
        return UserConnection.class;
    }

    @Override
    public Class<?> getBukkitDecodeHandlerClass() {
        return BukkitDecodeHandler.class;
    }

    @Override
    public Class<?> getBukkitEncodeHandlerClass() {
        return BukkitEncodeHandler.class;
    }
}
