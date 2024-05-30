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
import io.github.retrooper.packetevents.event.impl.PacketConfigReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.play.in.custompayload.WrappedPacketInCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityeffect.WrappedPacketOutEntityEffect;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.play.out.setslot.WrappedPacketOutSetSlot;
import io.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.charset.StandardCharsets;

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
                    WrappedPacketInUseEntity ue = new WrappedPacketInUseEntity(event.getNMSPacket());
                    event.getPlayer().sendMessage("action: " + ue.getAction());
                    event.getPlayer().sendMessage("target: " + ue.getTarget());
                    ItemStack stack = new ItemStack(Material.STICK);
                    WrappedPacketOutSetSlot setSlot = new WrappedPacketOutSetSlot(0, 37, stack);
                    PacketEvents.get().getPlayerUtils().sendPacket(event.getPlayer(), setSlot);
                } else if (event.getPacketId() == PacketType.Play.Client.CUSTOM_PAYLOAD) {
                    WrappedPacketInCustomPayload cp = new WrappedPacketInCustomPayload(event.getNMSPacket());
                    String name = cp.getChannelName();
                    System.out.println("name play: " + name);
                    System.out.println("Data: " + ((new String(cp.getData(), StandardCharsets.UTF_8))));
                }
                else if (event.getPacketId() == PacketType.Play.Client.ENTITY_ACTION) {
                    WrappedPacketInEntityAction ea = new WrappedPacketInEntityAction(event.getNMSPacket());
                    event.getPlayer().sendMessage("EA: " + ea.getAction());
                } else if (PacketType.Play.Client.BLOCK_DIG == event.getPacketId()) {
                    WrappedPacketInBlockDig bd = new WrappedPacketInBlockDig(event.getNMSPacket());
                    event.getPlayer().sendMessage("bd: " + bd.getDigType());
                }
                else if (PacketType.Play.Client.Util.isInstanceOfFlying(event.getPacketId())) {
                    WrappedPacketInFlying f = new WrappedPacketInFlying(event.getNMSPacket());
                    System.out.println("flying: " + f.getPosition().toString() + ", pitch and yaw: " + f.getPitch() + ", " + f.getYaw());
                }
            }

            @Override
            public void onPacketConfigReceive(PacketConfigReceiveEvent event) {
                if (event.getPacketId() == PacketType.Play.Client.CUSTOM_PAYLOAD) {
                    WrappedPacketInCustomPayload cp = new WrappedPacketInCustomPayload(event.getNMSPacket());
                    String name = cp.getChannelName();
                    System.out.println("name: " + name);
                    System.out.println("Data: " + ((new String(cp.getData(), StandardCharsets.UTF_8))));
                }
            }

            @Override
            public void onPacketPlaySend(PacketPlaySendEvent event) {
                if (PacketType.Play.Server.Util.isInstanceOfEntity(event.getPacketId())) {
                    WrappedPacketOutEntity entity = new WrappedPacketOutEntity(event.getNMSPacket());
                } else if (event.getPacketId() == PacketType.Play.Server.ENTITY_EFFECT) {
                    WrappedPacketOutEntityEffect eff = new WrappedPacketOutEntityEffect(event.getNMSPacket());
                    //TODO Broken on 1.20.5 eff.setEffectId(eff.getEffectId());
                    System.out.println("eff: " + eff.getEffectId() + ", ampl: " + eff.getAmplifier() + ", dur:" + eff.getDuration());
                } else if (event.getPacketId() == PacketType.Play.Server.ENTITY_VELOCITY) {
                    WrappedPacketOutEntityVelocity ev = new WrappedPacketOutEntityVelocity(event.getNMSPacket());
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