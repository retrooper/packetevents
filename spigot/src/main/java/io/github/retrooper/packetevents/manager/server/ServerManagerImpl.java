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
import io.github.retrooper.packetevents.utils.dependencies.protocolsupport.ProtocolSupportUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ServerManagerImpl implements ServerManager {
    private ServerVersion serverVersion;

    private ServerVersion resolveVersionNoCache() {
        for (final ServerVersion val : ServerVersion.reversedValues()) {
            //For example "V_1_18" -> "1.18"
            if (Bukkit.getBukkitVersion().contains(val.getReleaseName())) {
                return val;
            }
        }

        ServerVersion fallbackVersion = PacketEvents.getAPI().getSettings().getFallbackServerVersion();
        if (fallbackVersion != null) {
            if (fallbackVersion == ServerVersion.V_1_7_10) {
                try {
                    Class.forName("net.minecraft.util.io.netty.buffer.ByteBuf");
                } catch (Exception ex) {
                    //We will assume its 1.8.8
                    fallbackVersion = ServerVersion.V_1_8_8;
                }
            }
            Plugin plugin = (Plugin) PacketEvents.getAPI().getPlugin();
            plugin.getLogger().warning("[packetevents] Your server software is preventing us from checking the server version. This is what we found: " + Bukkit.getBukkitVersion() + ". We will assume the server version is " + fallbackVersion.name() + "...");
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
    public void receivePacketSilently(ChannelAbstract channel, ByteBufAbstract byteBuf) {
        //Receive the packet for all handlers after our decoder
        channel.pipeline().context(PacketEvents.DECODER_NAME).fireChannelRead(byteBuf);
    }

    @Override
    public void receivePacket(ChannelAbstract channel, ByteBufAbstract byteBuf) {
        if (channel.isOpen()) {
            if (ProtocolSupportUtil.isAvailable()) {
                //We want to skip ProtocolSupport's translation handlers,
                //because the buffer is fit for the current server-version
            }
            List<String> handlerNames = channel.pipeline().names();

            if (handlerNames.contains("ps_decoder_transformer")) {
                //We want to skip ProtocolSupport's translation handlers,
                //because the buffer is fit for the current server-version
                channel.pipeline().context("ps_decoder_transformer").fireChannelRead(byteBuf);
            } else if (handlerNames.contains("decompress")) {
                //We will have to just skip through the minecraft server's decompression handler
                channel.pipeline().context("decompress").fireChannelRead(byteBuf);
            } else {
                if (handlerNames.contains("decrypt")) {
                    //We will have to just skip through the minecraft server's decryption handler
                    //We don't have to deal with decompressing, as that handler isn't currently in the pipeline
                    channel.pipeline().context("decrypt").fireChannelRead(byteBuf);
                } else {
                    //No decompressing nor decrypting handlers are present
                    //You cannot fill this buffer up with chunks of packets,
                    //since we skip the packet-splitter handler.
                    channel.pipeline().context("splitter").fireChannelRead(byteBuf);
                }
            }
        }
    }
}
