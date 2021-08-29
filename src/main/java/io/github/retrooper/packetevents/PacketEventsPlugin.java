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
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.dependencies.viaversion.ViaVersionLookupUtils;
import io.github.retrooper.packetevents.wrapper.game.client.WrapperGameClientInteractEntity;
import io.github.retrooper.packetevents.wrapper.game.server.WrapperGameServerHeldItemChange;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class PacketEventsPlugin extends JavaPlugin {
    //TODO Complete the legacy handlers.
    @Override
    public void onLoad() {
        PacketEvents.get().load(this);
        //You can do something here as it is loading
    }

    @Override
    public void onEnable() {
        PacketEvents.get().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                if (event.getPacketType() == PacketType.Game.Client.INTERACT_ENTITY) {
                    WrapperGameClientInteractEntity interactEntity = new WrapperGameClientInteractEntity(event);
                    int entityID = interactEntity.getEntityID();
                    Entity entity = PacketEvents.get().getServerManager().getEntityByID(entityID);
                    event.getPlayer().sendMessage("entity name: " + entity.getName());
                    event.getPlayer().sendMessage("action: " + interactEntity.getType().name());
                    WrapperGameServerHeldItemChange heldItemChange = new WrapperGameServerHeldItemChange((byte) 7);
                    PacketEvents.get().getPlayerManager().sendPacket(event.getChannel(), heldItemChange);
                }
            }
        });
        PacketEvents.get().init();
    }

    @Override
    public void onDisable() {
        PacketEvents.get().terminate();
    }
}