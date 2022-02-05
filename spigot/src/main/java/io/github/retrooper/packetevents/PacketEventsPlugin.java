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

package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEventsPlugin extends JavaPlugin {
    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        //Register your listeners
        PacketEvents.getAPI().init();
        PacketEvents.getAPI().getSettings().debug(false).bStats(true);
        SimplePacketListenerAbstract listener = new SimplePacketListenerAbstract() {
            @Override
            public void onPacketPlayReceive(PacketPlayReceiveEvent event) {

            }

            @Override
            public void onPacketPlaySend(PacketPlaySendEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.CHUNK_DATA) {
                    WrapperPlayServerChunkData chunkData = new WrapperPlayServerChunkData(event);
                    event.getUser().sendMessage("Chunk " + chunkData.getColumn().getX() + " " + chunkData.getColumn().getZ() + " : " +chunkData.getColumn().getChunks()[0].get(0,0,1));
                }
            }
        };
        //net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles w1;
        PacketEvents.getAPI().getEventManager().registerListener(listener);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}