/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.manager.server;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.data.world.BoundingBox;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.github.retrooper.packetevents.utils.MinecraftReflectionUtil;
import org.bukkit.Bukkit;

public class ServerManagerImpl implements ServerManager {
    private ServerVersion serverVersion;

    @Override
    public ServerVersion resolveVersionNoCache() {
        if (ServerVersion.reversedValues[0] == null) {
            ServerVersion.reversedValues = ServerVersion.reverse();
        }

        for (final ServerVersion val : ServerVersion.reversedValues) {
            String valName = val.name().substring(2).replace("_", ".");
            if (Bukkit.getBukkitVersion().contains(valName)) {
                return val;
            }
        }

        ServerVersion fallbackVersion = PacketEvents.getAPI().getSettings().getFallbackServerVersion();
        if (fallbackVersion != null) {
            if (fallbackVersion == ServerVersion.v_1_7_10) {
                try {
                    Class.forName("net.minecraft.util.io.netty.buffer.ByteBuf");
                } catch (Exception ex) {
                    //We will assume its 1.8.8
                    fallbackVersion = ServerVersion.v_1_8_8;
                }
            }
            Bukkit.getLogger().warning("[packetevents] Your server software is preventing us from checking the server version. We will assume the server version is " + fallbackVersion.name() + "...");
            return fallbackVersion;
        }
        return ServerVersion.ERROR;
    }

    @Override
    public ServerVersion getVersion() {
        if (serverVersion == null) {
            serverVersion = resolveVersionNoCache();
        }
        return serverVersion;
    }

    @Override
    public void receivePacket(ChannelAbstract channel, ByteBufAbstract byteBuf) {

    }

    @Override
    public void receivePacket(Object player, ByteBufAbstract byteBuf) {

    }

    @Override
    public void receivePacket(Object player, PacketWrapper<?> wrapper) {

    }

    @Override
    public void receivePacket(ChannelAbstract channel, PacketWrapper<?> wrapper) {

    }

    @Override
    public boolean isGeyserAvailable() {
        return MinecraftReflectionUtil.GEYSER_CLASS != null;
    }
}
