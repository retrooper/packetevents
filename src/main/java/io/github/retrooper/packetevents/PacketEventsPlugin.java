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

import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.out.blockaction.WrappedPacketOutBlockAction;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEventsPlugin extends JavaPlugin {
    //TODO Remove all deprecations including the legacy event system in 1.8.1 release
    //TODO finish remaining wrappers
    @Override
    public void onLoad() {
        //Return value of create is your PacketEvents instance.
        PacketEvents instance = PacketEvents.create(this);
        PacketEventsSettings settings = instance.getSettings();
        settings
                .fallbackServerVersion(ServerVersion.v_1_7_10)
                .compatInjector(false)
                .checkForUpdates(true)
                .bStats(true);
        PacketEvents.get().loadAsyncNewThread();
        //u can do something here as it is loading
    }

    @Override
    public void onEnable() {
        PacketEvents.get().init();

        PacketEvents.get().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketPlayReceive(final PacketPlayReceiveEvent event) {
                if (event.getPacketId() == PacketType.Play.Client.ARM_ANIMATION) {
                    final WrappedPacketOutBlockAction wrapper = new WrappedPacketOutBlockAction(
                        new Vector3i(221, 62, 167), 0, 9, Material.NOTE_BLOCK
                    );

                    PacketEvents.get().getPlayerUtils().sendPacket(event.getPlayer(), wrapper);
                }
            }

            @Override
            public void onPacketPlaySend(final PacketPlaySendEvent event) {
                if (event.getPacketId() == PacketType.Play.Server.BLOCK_ACTION) {
                    final WrappedPacketOutBlockAction wrapper = new WrappedPacketOutBlockAction(event.getNMSPacket());

                    final Vector3i vector3i = wrapper.getBlockPosition();

                    Bukkit.broadcastMessage("x: " + vector3i.getX() + " y: " + vector3i.getY() + " z: " + vector3i.getZ());
                    Bukkit.broadcastMessage("action: " + wrapper.getActionId() + " actionParam: " + wrapper.getActionParam());
                    Bukkit.broadcastMessage("type: " + wrapper.getBlockMaterial().toString());
                }
            }
        });
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}