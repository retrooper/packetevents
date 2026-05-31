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

package io.github.retrooper.packetevents.util.protocolsupport;

import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Method;
import java.net.SocketAddress;

/**
 * @deprecated ProtocolSupport has not received an update since 2022.
 */
@NullMarked
@Deprecated
public final class ProtocolSupportUtil {

    private static ProtocolSupportState available = ProtocolSupportState.UNKNOWN;
    // access protocolsupport methods via reflection because jitpack sucks
    private static @Nullable Method getProtocolVersionByAddress, getProtocolVersionByPlayer, getId;

    private ProtocolSupportUtil() {
    }

    public static boolean isAvailable() {
        if (available != ProtocolSupportState.UNKNOWN) {
            return available == ProtocolSupportState.ENABLED;
        }
        try {
            ClassLoader classLoader = PacketEvents.getAPI().getPlugin().getClass().getClassLoader();
            Class<?> apiClass = classLoader.loadClass("protocolsupport.api.ProtocolSupportAPI");
            getProtocolVersionByAddress = apiClass.getMethod("getProtocolVersion", SocketAddress.class);
            getProtocolVersionByPlayer = apiClass.getMethod("getProtocolVersion", Player.class);
            Class<?> versionClass = classLoader.loadClass("protocolsupport.api.ProtocolVersion");
            getId = versionClass.getMethod("getId");

            available = ProtocolSupportState.ENABLED;
            return true;
        } catch (Exception ignored) {
            available = ProtocolSupportState.DISABLED;
            return false;
        }
    }

    @ApiStatus.Internal
    @Deprecated
    public static void checkIfProtocolSupportIsPresent() {
        isAvailable();
    }

    public static int getProtocolVersion(SocketAddress address) {
        if (getId == null || getProtocolVersionByAddress == null) {
            throw new IllegalStateException("ProtocolSupport is not loaded");
        }
        try {
            return (int) getId.invoke(getProtocolVersionByAddress.invoke(null, address));
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Error while trying to access ProtocolSupport", exception);
        }
    }

    public static int getProtocolVersion(Player player) {
        if (getId == null || getProtocolVersionByPlayer == null) {
            throw new IllegalStateException("ProtocolSupport is not loaded");
        }
        try {
            return (int) getId.invoke(getProtocolVersionByPlayer.invoke(null, player));
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Error while trying to access ProtocolSupport", exception);
        }
    }
}

enum ProtocolSupportState {
    UNKNOWN,
    DISABLED,
    ENABLED
}
