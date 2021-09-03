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
import io.github.retrooper.packetevents.wrapper.game.client.WrapperGameClientChatMessage;
import io.github.retrooper.packetevents.wrapper.game.client.WrapperGameClientInteractEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEventsPlugin extends JavaPlugin {
    @Override
    public void onLoad() {
        PacketEvents.get().load(this);
        //You can do something here as it is loading
    }

    @Override
    public void onEnable() {
        //Register your listeners
        PacketEvents.get().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                if (event.getPacketType() == PacketType.Game.Client.CHAT_MESSAGE) {
                    WrapperGameClientChatMessage chatMessage = new WrapperGameClientChatMessage(event);
                }
                else if (event.getPacketType() == PacketType.Game.Client.INTERACT_ENTITY) {
                    WrapperGameClientInteractEntity interactEntity = new WrapperGameClientInteractEntity(event);
                    event.getPlayer().sendMessage("we attacked entity id: " + interactEntity.getEntityID() + ", type: " + interactEntity.getType().name());
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