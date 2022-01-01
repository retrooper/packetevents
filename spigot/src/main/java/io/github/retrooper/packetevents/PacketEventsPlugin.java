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
import com.github.retrooper.packetevents.manager.npc.NPC;
import com.github.retrooper.packetevents.protocol.chat.component.BaseComponent;
import com.github.retrooper.packetevents.protocol.chat.component.impl.ScoreComponent;
import com.github.retrooper.packetevents.protocol.chat.component.impl.TextComponent;
import com.github.retrooper.packetevents.protocol.chat.component.impl.TranslatableComponent;
import com.github.retrooper.packetevents.protocol.gameprofile.GameProfile;
import com.github.retrooper.packetevents.protocol.gameprofile.TextureProperty;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.MojangAPIUtil;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
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
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
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
                else if (event.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE) {
                    WrapperPlayClientChatMessage chatMessage = new WrapperPlayClientChatMessage(event);
                    String msg = chatMessage.getMessage();
                    String[] sp = msg.split(" ");
                    if (sp[0].equalsIgnoreCase("plzspawn")) {
                        String name = sp[1];
                        UUID uuid = MojangAPIUtil.requestPlayerUUID(name);
                        player.sendMessage("Spawning " + name + " with UUID " + uuid.toString());
                        List<TextureProperty> textureProperties = MojangAPIUtil.requestPlayerTextureProperties(uuid);
                        GameProfile profile = new GameProfile(uuid, name, textureProperties);
                        NPC npc = new NPC(name + "_clone", SpigotReflectionUtil.generateEntityId(), profile);
                        npc.setLocation(new Location(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                        PacketEvents.getAPI().getNPCManager().spawn(event.getChannel(), npc);
                        player.sendMessage("Successfully spawned " + name);
                    }
                }
            }

            @Override
            public void onPacketSend(PacketSendEvent event) {
                Player player = event.getPlayer() == null ? null : (Player) event.getPlayer();
                //TODO Fix hoverEvent contents/value when its a component i believe
                //TODO Work on copying styling properly from children components

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
                            String content = child instanceof String ? child.toString() : child instanceof BaseComponent ? ((BaseComponent) child).buildJson().toString() : "";
                            String colorName = child instanceof String ? "none" : child instanceof BaseComponent ? ((BaseComponent) child).getColor().getName() : "";
                            PacketEvents.getAPI().getLogManager().debug("translate child: " + colorName + ":" + content);
                        }
                        PacketEvents.getAPI().getLogManager().debug("output: " + tc.buildJson().toString());
                        /*
                        og json msg: {"color":"yellow","translate":"multiplayer.player.joined",
                        "with":[{"insertion":"retrooper",
                        "clickEvent":{"action":"suggest_command","value":"/tell retrooper "},
                        "hoverEvent":{"action":"show_entity","contents":{"type":"minecraft:player",
                        "id":"7fe54c65-80ba-45c8-8450-b45796aa0eb9","name":{"text":"retrooper"}}},
                        "text":"retrooper"}]}
                         */
                    }
                }
                else if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
                    WrapperPlayServerSetSlot setSlot = new WrapperPlayServerSetSlot(event);
                    int windowID = setSlot.getWindowId();
                    int slot = setSlot.getSlot();
                    ItemStack item = setSlot.getItem();
                    player.sendMessage("Set slot with window ID: " + windowID + ", slot: " + slot + ", item: " + (item.getType() != null ? item.toString() : "null item"));
                }
            }
        };
        PacketEvents.getAPI().getEventManager().registerListener(debugListener);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}