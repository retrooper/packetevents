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
import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ViaVersionUtil {
    private static ViaState available = ViaState.UNKNOWN;
    private static ViaVersionAccessor viaVersionAccessor;

    private ViaVersionUtil() {
    }

    private static void load() {
        if (viaVersionAccessor == null) {
            try {
                Class.forName("com.viaversion.viaversion.api.Via");
                viaVersionAccessor = new ViaVersionAccessorImpl();
            } catch (Exception e) {
                try {
                    Class.forName("us.myles.ViaVersion.api.Via");
                    viaVersionAccessor = new ViaVersionAccessorImplLegacy();
                } catch (ClassNotFoundException ex) {
                    viaVersionAccessor = null;
                }
            }
        }
    }

    public static void checkIfViaIsPresent() {
        boolean present = Bukkit.getPluginManager().isPluginEnabled("ViaVersion");
        available = present ? ViaState.ENABLED : ViaState.DISABLED;
    }

    public static boolean isAvailable() {
        if (available == ViaState.UNKNOWN) { // Plugins haven't loaded... let's refer to whether we have a class
            return getViaVersionAccessor() != null;
        }
        return available == ViaState.ENABLED;
    }

    public static ViaVersionAccessor getViaVersionAccessor() {
        load();
        return viaVersionAccessor;
    }

    public static int getProtocolVersion(User user) {
        try {
            if (user.getUUID() != null) {
                Player player = Bukkit.getPlayer(user.getUUID());
                if (player != null) {
                    int version = getProtocolVersion(player);
                    // -1 means via hasn't gotten join event yet
                    if (version != -1) return version;
                }
            }
            Object viaEncoder = ((Channel) user.getChannel()).pipeline().get("via-encoder");
            Object connection = Reflection.getField(viaEncoder.getClass(), "connection").get(viaEncoder);
            Object protocolInfo = Reflection.getField(connection.getClass(), "protocolInfo").get(connection);
            return (int) Reflection.getField(protocolInfo.getClass(), "protocolVersion").get(protocolInfo);
        } catch (Exception e) {
            PacketEvents.getAPI().getLogManager().warn("Unable to grab ViaVersion client version for player!");
            return -1;
        }
    }

    public static int getProtocolVersion(Player player) {
        return getViaVersionAccessor().getProtocolVersion(player);
    }

    public static Class<?> getUserConnectionClass() {
        return getViaVersionAccessor().getUserConnectionClass();
    }

    public static Class<?> getBukkitDecodeHandlerClass() {
        return getViaVersionAccessor().getBukkitDecodeHandlerClass();
    }

    public static Class<?> getBukkitEncodeHandlerClass() {
        return getViaVersionAccessor().getBukkitEncodeHandlerClass();
    }
}

enum ViaState {
    UNKNOWN,
    DISABLED,
    ENABLED
}
