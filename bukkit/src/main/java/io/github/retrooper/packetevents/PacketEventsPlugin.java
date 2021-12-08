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
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.factory.bukkit.BukkitPacketEventsBuilder;
import com.github.retrooper.packetevents.protocol.chat.Color;
import com.github.retrooper.packetevents.protocol.inventory.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.Hand;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnLivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PacketEventsPlugin extends JavaPlugin {
    @Override
    public void onLoad() {
        PacketEvents.setAPI(BukkitPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        //Register your listeners
        PacketEvents.getAPI().init();

        PacketListenerAbstract debugListener = new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                Player player = event.getPlayer() == null ? null : (Player) event.getPlayer();
                if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
                    WrapperPlayClientInteractEntity interactEntity = new WrapperPlayClientInteractEntity(event);
                    int entityID = interactEntity.getEntityId();
                    WrapperPlayClientInteractEntity.InteractAction action = interactEntity.getAction();
                    Hand hand = interactEntity.getHand();
                }
            }

            @Override
            public void onPacketSend(PacketSendEvent event) {
                Player player = event.getPlayer() == null ? null : (Player) event.getPlayer();
                if (event.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE) {
                    /*WrapperPlayServerChatMessage chatMessage = new WrapperPlayServerChatMessage(event);
                    BaseComponent chatComponent = chatMessage.getChatComponent();
                    if (chatComponent instanceof TextComponent) {
                        TextComponent textComponent = (TextComponent) chatComponent;
                        StringBuilder text = new StringBuilder(textComponent.getColor().getFullCode() + textComponent.getText());
                        for (BaseComponent child : textComponent.getChildren()) {
                            if (child instanceof TextComponent) {
                                TextComponent textChild = (TextComponent) child;
                                text.append(textChild.getColor().getFullCode()).append(textChild.getText());
                            }
                        }
                        System.out.println("Text: " + text);
                    }*/
                    //TODO Fix translatable chat components
                } else if (event.getPacketType() == PacketType.Play.Server.SPAWN_LIVING_ENTITY) {
                    WrapperPlayServerSpawnLivingEntity spawnMob = new WrapperPlayServerSpawnLivingEntity(event);
                    int entityID = spawnMob.getEntityId();
                    UUID uuid = spawnMob.getEntityUUID();
                    Vector3d position = spawnMob.getPosition();
                    if (player != null) {
                        player.sendMessage("Spawned entity with id: " + entityID + ", with uuid: " + uuid + ", at position: " + position);
                    }
                    //TODO Complete spawn living entity for outdated versions
                    event.setLastUsedWrapper(null);
                } else if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
                    WrapperPlayServerSetSlot setSlot = new WrapperPlayServerSetSlot(event);
                    int windowID = setSlot.getWindowId();
                    int slot = setSlot.getSlot();
                    //TODO Fix ItemTypes on all versions
                    ItemStack item = setSlot.getItem();
                    System.out.println("Set slot with window ID: " + windowID + ", slot: " + slot + ", item: " + (item.getType() != null ? item.toString() : "null item"));
                }
                //TODO Complete chunk data packet
            }
        };
        PacketEvents.getAPI().getEventManager().registerListener(debugListener);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}