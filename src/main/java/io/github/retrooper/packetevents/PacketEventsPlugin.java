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
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityeffect.WrappedPacketOutEntityEffect;
import io.github.retrooper.packetevents.packetwrappers.play.out.setslot.WrappedPacketOutSetSlot;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEventsPlugin extends JavaPlugin {
    @Override
    public void onLoad() {
        PacketEventsSettings settings = PacketEvents.create(this).getSettings();
        settings
                .fallbackServerVersion(ServerVersion.getLatest())
                .checkForUpdates(true)
                .bStats(true);
        PacketEvents.get().loadAsyncNewThread();
        //You can do something here as it is loading
    }

    @Override
    public void onEnable() {
        /*PacketEvents.get().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
                if (event.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
                    System.out.println("Yes");
                    ItemStack stack = new ItemStack(Material.STICK);
                    WrappedPacketOutSetSlot setSlot = new WrappedPacketOutSetSlot(0, 37, stack);
                    PacketEvents.get().getPlayerUtils().sendPacket(event.getPlayer(), setSlot);
                }
            }

            @Override
            public void onPacketPlaySend(PacketPlaySendEvent event) {
                if (PacketType.Play.Server.Util.isInstanceOfEntity(event.getPacketId())) {
                    WrappedPacketOutEntity entity = new WrappedPacketOutEntity(event.getNMSPacket());
                    System.out.println("en: " + entity.getEntityId() + ", delta x: " + entity.getDeltaX()
                            + ", delta y: " + entity.getDeltaY() + ", delta z: " + entity.getDeltaZ());
                } else if (event.getPacketId() == PacketType.Play.Server.ENTITY_EFFECT) {
                    WrappedPacketOutEntityEffect eff = new WrappedPacketOutEntityEffect(event.getNMSPacket());
                    System.out.println("eff: " + eff.getEffectId() + ", ampl: " + eff.getAmplifier() + ", dur:" + eff.getDuration());

                }
            }
        });*/
        PacketEvents.get().init();
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}