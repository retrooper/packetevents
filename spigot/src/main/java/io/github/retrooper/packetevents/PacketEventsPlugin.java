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
import com.github.retrooper.packetevents.protocol.chat.component.BaseComponent;
import com.github.retrooper.packetevents.protocol.chat.component.impl.ScoreComponent;
import com.github.retrooper.packetevents.protocol.chat.component.impl.TextComponent;
import com.github.retrooper.packetevents.protocol.chat.component.impl.TranslatableComponent;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnLivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

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

        PacketEvents.getAPI().getSettings().debug(true).bStats(false);

        PacketListenerAbstract debugListener = new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                Player player = event.getPlayer() == null ? null : (Player) event.getPlayer();
                if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
                    WrapperPlayClientInteractEntity interactEntity = new WrapperPlayClientInteractEntity(event);
                    int entityID = interactEntity.getEntityId();
                    WrapperPlayClientInteractEntity.InteractAction action = interactEntity.getAction();
                    InteractionHand hand = interactEntity.getHand();
                    player.sendMessage("Received packet: " + event.getPacketType().getName() + " from " + player.getName() + " with entityID: " + entityID + " and action: " + action.name() + " and hand: " + hand.name());
                } else if (event.getPacketType() == PacketType.Play.Client.UPDATE_SIGN) {
                    WrapperPlayClientUpdateSign updateSign = new WrapperPlayClientUpdateSign(event);
                    Vector3i pos = updateSign.getBlockPosition();
                    String[] textLines = updateSign.getTextLines();
                    player.sendMessage("Received packet: " + event.getPacketType().getName() + " from " + player.getName() + " with pos: " + pos.toString() + " and textLines: " + Arrays.toString(textLines));
                }
            }

            @Override
            public void onPacketSend(PacketSendEvent event) {
                Player player = event.getPlayer() == null ? null : (Player) event.getPlayer();
                //TODO Fix translatable components, and check others for safety

                if (event.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE) {
                    WrapperPlayServerChatMessage cm = new WrapperPlayServerChatMessage(event);
                    BaseComponent component = cm.getChatComponent();
                    if (component instanceof TextComponent) {
                        PacketEvents.getAPI().getLogManager().debug("text received: " + ((TextComponent) component).getText());
                    }
                    else if (component instanceof TranslatableComponent) {
                        TranslatableComponent tc = (TranslatableComponent) component;
                        String translate = tc.getTranslate();
                        List<Object> with = tc.getWith();
                        PacketEvents.getAPI().getLogManager().debug("received translate: " + tc.getColor().getName() + ":" + translate);
                        for (Object child : with) {
                            String content = child instanceof String ? child.toString() : child instanceof TranslatableComponent ? ((TranslatableComponent) child).getTranslate() : "";
                            String colorName = child instanceof String ? "none" : child instanceof TranslatableComponent ? ((TranslatableComponent) child).getColor().getName() : "";
                            PacketEvents.getAPI().getLogManager().debug("translate child: " + colorName + ":" + content);
                        }
                    }
                }
                else if (event.getPacketType() == PacketType.Play.Server.SPAWN_LIVING_ENTITY) {
                    WrapperPlayServerSpawnLivingEntity spawnMob = new WrapperPlayServerSpawnLivingEntity(event);
                    int entityID = spawnMob.getEntityId();
                    UUID uuid = spawnMob.getEntityUUID();
                    Vector3d position = spawnMob.getPosition();
                    if (player != null) {
                        //player.sendMessage("Spawned entity with id: " + entityID + ", with uuid: " + uuid + ", at position: " + position);
                    }
                    //TODO Complete spawn living entity for outdated versions
                } else if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
                    WrapperPlayServerSetSlot setSlot = new WrapperPlayServerSetSlot(event);
                    int windowID = setSlot.getWindowId();
                    int slot = setSlot.getSlot();
                    ItemStack item = setSlot.getItem();
                    player.sendMessage("Set slot with window ID: " + windowID + ", slot: " + slot + ", item: " + (item.getType() != null ? item.toString() : "null item"));
                }//TODO Complete chunk data packet
            }
        };
        //PacketEvents.getAPI().getEventManager().registerListener(debugListener);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}