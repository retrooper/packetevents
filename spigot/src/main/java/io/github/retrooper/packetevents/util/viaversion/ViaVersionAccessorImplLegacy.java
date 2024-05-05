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
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ViaVersionAccessorImplLegacy implements ViaVersionAccessor {
    private Class<?> viaClass;
    private Class<?> bukkitDecodeHandlerClass;
    private Class<?> bukkitEncodeHandlerClass;
    private Field viaManagerField;
    private Method apiAccessor;
    private Method getPlayerVersionMethod;
    private Class<?> userConnectionClass;

    private void load() {
        if (viaClass == null) {
            try {
                viaClass = Class.forName("us.myles.ViaVersion.api.Via");
                viaManagerField = viaClass.getDeclaredField("manager");
                bukkitDecodeHandlerClass = Class.forName("us.myles.ViaVersion.bukkit.handlers.BukkitDecodeHandler");
                bukkitEncodeHandlerClass = Class.forName("us.myles.ViaVersion.bukkit.handlers.BukkitEncodeHandler");
                Class<?> viaAPIClass = Class.forName("us.myles.ViaVersion.api.ViaAPI");
                apiAccessor = viaClass.getMethod("getAPI");
                getPlayerVersionMethod = viaAPIClass.getMethod("getPlayerVersion", Object.class);
            } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        if (userConnectionClass == null) {
            try {
                userConnectionClass = Class.forName("us.myles.ViaVersion.api.data.UserConnection");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getProtocolVersion(Player player) {
        load();
        try {
            Object viaAPI = apiAccessor.invoke(null);
            return (int) getPlayerVersionMethod.invoke(viaAPI, player);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int getProtocolVersion(User user) {
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
            Object protocolVersion = Reflection.getField(protocolInfo.getClass(), "protocolVersion").get(protocolInfo);
            return protocolVersion instanceof Integer ? (int) protocolVersion : ((ProtocolVersion) protocolVersion).getVersion();
        } catch (Exception e) {
            PacketEvents.getAPI().getLogManager().warn("Unable to grab ViaVersion client version for player!");
            return -1;
        }
    }

    @Override
    public Class<?> getUserConnectionClass() {
        load();
        return userConnectionClass;
    }

    @Override
    public Class<?> getBukkitDecodeHandlerClass() {
        load();
        return bukkitDecodeHandlerClass;
    }

    @Override
    public Class<?> getBukkitEncodeHandlerClass() {
        load();
        return bukkitEncodeHandlerClass;
    }
}
