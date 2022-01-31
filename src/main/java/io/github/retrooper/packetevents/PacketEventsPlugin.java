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
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityeffect.WrappedPacketOutEntityEffect;
import io.github.retrooper.packetevents.packetwrappers.play.out.explosion.WrappedPacketOutExplosion;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.netty.channel.Channel;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class PacketEventsPlugin extends JavaPlugin {
    @Override
    public void onLoad() {
        PacketEventsSettings settings = PacketEvents.create(this).getSettings();
        settings
                .fallbackServerVersion(ServerVersion.getLatest())
                .compatInjector(false)
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
                    WrappedPacketInUseEntity ue = new WrappedPacketInUseEntity(event.getNMSPacket());
                    event.getPlayer().sendMessage("You interacted with entity " + ue.getEntity().getName());
                    System.out.println("Handlers: " + Arrays.toString(((Channel)event.getChannel()).pipeline().names().toArray(new String[0])));
                }
            }

            @Override
            public void onPacketPlaySend(PacketPlaySendEvent event) {
                if (event.getPacketId() == PacketType.Play.Server.EXPLOSION) {
                    WrappedPacketOutExplosion explosion = new WrappedPacketOutExplosion(event.getNMSPacket());
                    event.getPlayer().sendMessage("explosion: " + explosion.getPosition() + ", velocity: " + explosion.getPlayerVelocity());
                }
                else if (event.getPacketId() == PacketType.Play.Server.ENTITY_EFFECT) {
                    WrappedPacketOutEntityEffect effect = new WrappedPacketOutEntityEffect(event.getNMSPacket());
                    int id = effect.getEffectId();
                    int amplifier = effect.getAmplifier();
                    int duration = effect.getDuration();
                    event.getPlayer().sendMessage("id: " + id + ", amplifier: " + amplifier + ", duration: " + duration);
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