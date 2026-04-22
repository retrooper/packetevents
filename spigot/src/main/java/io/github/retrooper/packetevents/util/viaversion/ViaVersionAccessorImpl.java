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

import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.ViaChannelHandler;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.bukkit.handlers.BukkitDecodeHandler;
import com.viaversion.viaversion.bukkit.handlers.BukkitEncodeHandler;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;

import java.lang.reflect.Field;

@NullMarked
public class ViaVersionAccessorImpl implements ViaVersionAccessor {

    // effectively checks for https://github.com/ViaVersion/ViaVersion/commit/22bd350e35880b345203ce4e93078a5ee43e5e24#diff-7f601b0b63bd577a3833b606b5004d9ef134efd5a0ae36f4bb44995b72b0121cR112
    private static final boolean HAS_INFO_PROTOCOL_VERSION = Reflection.getClassByNameWithoutException("com.viaversion.viaversion.api.protocol.version.VersionType") != null;
    // checks for https://github.com/ViaVersion/ViaVersion/commit/16a2a3a671b0b5f13746c704c0a26385cecb86f4
    private static final boolean HAS_VIA_CHANNEL_HANDLER = Reflection.getClassByNameWithoutException("com.viaversion.viaversion.api.platform.ViaChannelHandler") != null;
    // cache field lookup for legacy connection getter
    private static @MonotonicNonNull Field CONNECTION_FIELD = null;

    @Override
    public int getProtocolVersion(Player player) {
        @SuppressWarnings("unchecked")
        ViaAPI<Player> api = Via.getAPI();
        return api.getPlayerVersion(player);
    }

    @Override
    public int getProtocolVersion(User user) {
        ViaInjector viaInjector = Via.getManager().getInjector();
        Channel channel = (Channel) user.getChannel();
        Object viaEncoder = channel.pipeline().get(viaInjector.getEncoderName());

        UserConnection connection = null;
        if (HAS_VIA_CHANNEL_HANDLER) {
            // modern via exposes the user connection object in api
            if (viaEncoder instanceof ViaChannelHandler) {
                connection = ((ViaChannelHandler) viaEncoder).connection();
            }
        } else {
            // read user connection object using reflection from netty handler
            if (CONNECTION_FIELD == null) {
                CONNECTION_FIELD = Reflection.getField(viaEncoder.getClass(), UserConnection.class, 0);
            }
            if (CONNECTION_FIELD != null) {
                try {
                    connection = (UserConnection) CONNECTION_FIELD.get(viaEncoder);
                } catch (IllegalAccessException ignored) {
                }
            }
        }

        return connection != null ? this.getVersion(connection) : UNKNOWN_PROTOCOL_VERSION;
    }

    private int getVersion(UserConnection connection) {
        if (HAS_INFO_PROTOCOL_VERSION) {
            ProtocolVersion version = connection.getProtocolInfo().protocolVersion();
            if (version.isKnown()) {
                return version.getVersion();
            }
            return UNKNOWN_PROTOCOL_VERSION;
        } else {
            @SuppressWarnings("deprecation")
            int proto = connection.getProtocolInfo().getProtocolVersion();
            return proto;
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
