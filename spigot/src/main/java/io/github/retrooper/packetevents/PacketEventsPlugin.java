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
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameProfile;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

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

        PacketEvents.getAPI().getSettings().debug(true).bStats(true);

        PacketListenerAbstract debugListener = new PacketListenerAbstract(PacketListenerPriority.NORMAL, false) {
            @Override
            public void onPacketReceive(PacketReceiveEvent event) {
                Player player = event.getPlayer() == null ? null : (Player) event.getPlayer();
                if (event.getPacketType() == PacketType.Play.Client.CHAT_MESSAGE) {
                    WrapperPlayClientChatMessage chatMessage = new WrapperPlayClientChatMessage(event);
                    String msg = chatMessage.getMessage();
                    String[] sp = msg.split(" ");
                   if (sp[0].equalsIgnoreCase("plzspawn")) {
                        String name = sp[1];
                        UUID uuid = MojangAPIUtil.requestPlayerUUID(name);
                        player.sendMessage("Spawning " + name + " with UUID " + uuid.toString());
                        List<TextureProperty> textureProperties = MojangAPIUtil.requestPlayerTextureProperties(uuid);
                        GameProfile profile = new GameProfile(uuid, name, textureProperties);
                        Component nameTag = Component.text("Doggy").color(NamedTextColor.GOLD).asComponent();
                        NPC npc = new NPC(profile, SpigotReflectionUtil.generateEntityId(), null,
                                NamedTextColor.GOLD,
                                Component.text("Nice prefix").color(NamedTextColor.GRAY).asComponent(),
                                Component.text("Nice suffix").color(NamedTextColor.AQUA).asComponent()
                                );
                        npc.setLocation(new Location(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                        PacketEvents.getAPI().getNPCManager().spawn(event.getChannel(), npc);
                        player.sendMessage("Successfully spawned " + name);
/*
                        Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin) PacketEvents.getAPI().getPlugin(),
                                () -> {
                                    player.sendMessage("Turning the NPC into Dqgs!");
                                    UUID dogsUUID = MojangAPIUtil.requestPlayerUUID("");
                                    List<TextureProperty> newTextureProperties = MojangAPIUtil.requestPlayerTextureProperties(dogsUUID);
                                    PacketEvents.getAPI().getNPCManager().changeNPCSkin(npc, dogsUUID, newTextureProperties);
                                    npc.setPrefixName(Component.text("New prefix").color(NamedTextColor.GOLD).asComponent());
                                    PacketEvents.getAPI().getNPCManager().updateNPCNameTag(npc);

                                }, 120L);//120 ticks is 6 seconds*/
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
        PacketEvents.getAPI().getEventManager().registerListener(debugListener);

    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}