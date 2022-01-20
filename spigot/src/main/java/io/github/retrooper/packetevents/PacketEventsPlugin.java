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
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.npc.NPC;
import com.github.retrooper.packetevents.protocol.entity.data.provider.EntityDataProvider;
import com.github.retrooper.packetevents.protocol.entity.data.provider.PlayerDataProvider;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameProfile;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.protocol.player.SkinSection;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.MojangAPIUtil;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUpdateSign;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        PacketEvents.getAPI().getSettings().debug(true).bStats(true);

        PacketListenerAbstract debugListener = new PacketListenerAbstract(PacketListenerPriority.NORMAL, false) {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                Player player = event.getPlayer() == null ? null : (Player) event.getPlayer();
                if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
                    WrapperPlayClientInteractEntity interactEntity = new WrapperPlayClientInteractEntity(event);
                    int entityID = interactEntity.getEntityId();
                    WrapperPlayClientInteractEntity.InteractAction action = interactEntity.getAction();
                    InteractionHand hand = interactEntity.getHand();
                    player.sendMessage("Received packet: " + event.getPacketType().getName() + " from " + player.getName() + " with entityID: " + entityID + " and action: " + action.name() + " and hand: " + hand.name());

                    WrappedBlockState state = WrappedBlockState.getByString("minecraft:grass_block[snowy=true]");
                    WrapperPlayServerBlockChange bc = new WrapperPlayServerBlockChange(new Vector3i(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ()), state.getGlobalId());
                    PacketEvents.getAPI().getPlayerManager().sendPacket(event.getChannel(), bc);
                } else if (event.getPacketType() == PacketType.Play.Client.UPDATE_SIGN) {
                    WrapperPlayClientUpdateSign updateSign = new WrapperPlayClientUpdateSign(event);
                    Vector3i pos = updateSign.getBlockPosition();
                    String[] textLines = updateSign.getTextLines();
                    player.sendMessage("Received packet: " + event.getPacketType().getName() + " from " + player.getName() + " with pos: " + pos.toString() + " and textLines: " + Arrays.toString(textLines));
                } else if (event.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE) {
                    WrapperPlayClientChatMessage chatMessage = new WrapperPlayClientChatMessage(event);
                    String msg = chatMessage.getMessage();
                    String[] sp = msg.split(" ");
                    if (sp[0].equalsIgnoreCase("plzspawn")) {
                        String name = sp[1];
                        UUID uuid = MojangAPIUtil.requestPlayerUUID(name);
                        player.sendMessage("Spawning " + name + " with UUID " + uuid.toString());
                        List<TextureProperty> textureProperties = MojangAPIUtil.requestPlayerTextureProperties(uuid);
                        GameProfile profile = new GameProfile(uuid, name, textureProperties);
                        NPC npc = new NPC(Component.text(name + "_clone", NamedTextColor.RED), SpigotReflectionUtil.generateEntityId(), profile);
                        npc.setLocation(new Location(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                        PacketEvents.getAPI().getNPCManager().spawn(event.getChannel(), npc);
                        player.sendMessage("Successfully spawned " + name);

                        Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin) PacketEvents.getAPI().getPlugin(),
                                () -> {
                                    player.sendMessage("Turning the NPC into Dqgs!");
                                    UUID dogsUUID = MojangAPIUtil.requestPlayerUUID("Dqgs");
                                    List<TextureProperty> newTextureProperties = MojangAPIUtil.requestPlayerTextureProperties(dogsUUID);
                                    PacketEvents.getAPI().getNPCManager().changeNPCSkin(npc, dogsUUID, newTextureProperties);
                                    npc.setDisplayName(Component.text("Dqgs").color(NamedTextColor.RED).asComponent());
                                    WrapperPlayServerPlayerInfo pInfo = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.UPDATE_DISPLAY_NAME,
                                            npc.getPlayerInfoData());
                                    PacketEvents.getAPI().getPlayerManager().sendPacket(event.getChannel(), pInfo);


                                }, 120L);//120 ticks is 6 seconds
                    }
                }
            }

            @Override
            public void onPacketSend(PacketSendEvent event) {
                Player player = event.getPlayer() == null ? null : (Player) event.getPlayer();
                if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
                    WrapperPlayServerSetSlot setSlot = new WrapperPlayServerSetSlot(event);
                    int windowID = setSlot.getWindowId();
                    int slot = setSlot.getSlot();
                    ItemStack item = setSlot.getItem();
                    player.sendMessage("Set slot with window ID: " + windowID + ", slot: " + slot + ", item: " + (item.getType() != null ? item.toString() : "null item"));
                }
            }
        };
        //PacketEvents.getAPI().getEventManager().registerListener(debugListener);

    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}