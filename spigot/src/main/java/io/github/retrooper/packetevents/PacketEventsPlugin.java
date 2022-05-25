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
import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.npc.NPC;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.util.*;
import com.github.retrooper.packetevents.wrapper.play.client.*;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import io.github.retrooper.packetevents.util.SpigotDataHelper;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.netty.buffer.ByteBuf;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.charset.StandardCharsets;
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
        PacketEvents.getAPI().getSettings().debug(false).bStats(true)
                .checkForUpdates(true).timeStampMode(TimeStampMode.MILLIS).readOnlyListeners(false);
        PacketEvents.getAPI().init();
        SimplePacketListenerAbstract listener = new SimplePacketListenerAbstract(PacketListenerPriority.HIGH) {
            @Override
            public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
                User user = event.getUser();
                Player player = (Player) event.getPlayer();
                switch (event.getPacketType()) {
                    case CHAT_MESSAGE:
                        System.out.println("Running 10 seconds later");
                        WrapperPlayClientChatMessage chatMessage = new WrapperPlayClientChatMessage(event);
                        if (chatMessage.getMessage().equalsIgnoreCase("keyword")) {
                            System.out.println("pipe: " + ChannelHelper.pipelineHandlerNamesAsString(event.getChannel()));
                            chatMessage.setMessage(chatMessage.getMessage() + " (edited) - client version: " + user.getClientVersion());
                            Particle particle = new Particle(ParticleTypes.ANGRY_VILLAGER);
                            Vector3d position = SpigotDataHelper
                                    .fromBukkitLocation(((Player) event.getPlayer()).getLocation())
                                    .getPosition().add(0, 2, 0);
                            WrapperPlayServerParticle particlePacket
                                    = new WrapperPlayServerParticle(particle, true, position,
                                    new Vector3f(0.4f, 0.4f, 0.4f), 0, 25);
                            user.writePacket(particlePacket);

                            Component title = Component.text("Hello, you must be " + user.getProfile().getName() + "!")
                                    .color(NamedTextColor.DARK_GREEN);
                            Component subtitle = Component.text("Welcome...")
                                    .color(NamedTextColor.GREEN);
                            user.sendTitle(title, subtitle, 40, 20, 40);

                            Vector3i bp = SpigotDataHelper.fromBukkitLocation(((Player) event.getPlayer()).getLocation())
                                    .getPosition().toVector3i();
                            bp = bp.subtract(0, 1, 0);
                            StateType type = StateTypes.GOLD_BLOCK;
                            WrappedBlockState blockState = type.createBlockState(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
                            WrapperPlayServerBlockChange blockChange = new WrapperPlayServerBlockChange(bp,
                                    blockState.getGlobalId());
                            user.writePacket(blockChange);
                            String npcName = "retrooper";
                            UUID npcUUID = MojangAPIUtil.requestPlayerUUID(npcName);
                            UserProfile up = new UserProfile(npcUUID, npcName);
                            Component prefixName = Component.text("[Admin] ").color(NamedTextColor.DARK_RED);
                            Component tabName = Component.text(npcName).color(NamedTextColor.DARK_RED);
                            NPC npc = new NPC(up, SpigotReflectionUtil.generateEntityId(), tabName,
                                    NamedTextColor.BLUE, prefixName, null);
                            Location playerLocation = SpigotDataHelper.fromBukkitLocation(player.getLocation());
                            npc.setLocation(playerLocation);
                            npc.spawn(user.getChannel());
                            user.sendMessage("Spawned npc!");
                        } else if (chatMessage.getMessage().equalsIgnoreCase("test3")) {
                            Material ironDoor = Material.IRON_DOOR;
                            WrappedBlockState state = SpigotDataHelper.fromBukkitBlockData(new MaterialData(ironDoor, (byte) 0));
                            StateType type = state.getType();
                            user.sendMessage("Bukkit block type: " + ironDoor.name() + ", packetevents type: " + type.getName());
                            MaterialData backToDoorData = SpigotDataHelper.toBukkitBlockData(state.clone());
                            if (backToDoorData != null) {
                                user.sendMessage("Back to Bukkit block type: " + backToDoorData.getItemType().name() + ", type: " + backToDoorData.getClass().getSimpleName());
                            } else {
                                user.sendMessage("No back way");
                            }
                            org.bukkit.inventory.ItemStack bukkitStack = new org.bukkit.inventory.ItemStack(Material.EMERALD, 10);
                            ItemStack stack = SpigotDataHelper.fromBukkitItemStack(bukkitStack);
                            user.sendMessage("Bukkit itemstack type: " + bukkitStack.getType().name()
                                    + ", packetevents type: " + stack.getType().getName());
                            org.bukkit.inventory.ItemStack backToBukkitStack = SpigotDataHelper.toBukkitItemStack(stack);
                            user.sendMessage("Back to Bukkit itemstack type: " + backToBukkitStack.getType().name() + ", type: " + backToBukkitStack.getClass().getSimpleName());

                        } else if (chatMessage.getMessage().equalsIgnoreCase("test0")) {
                            for (org.bukkit.entity.EntityType type : org.bukkit.entity.EntityType.values()) {
                                EntityType entityType = SpigotDataHelper.fromBukkitEntityType(type);
                                if (entityType != null) {
                                    System.out.println("EntityType: " + entityType.getName() + ", Bukkit type: " + type.getName());
                                }
                            }
                        } else if (chatMessage.getMessage().equalsIgnoreCase("test1")) {
                            for (org.bukkit.entity.EntityType type : org.bukkit.entity.EntityType.values()) {
                                if (type.getTypeId() != -1) {
                                    EntityType entityType = SpigotDataHelper.fromBukkitEntityType(type);
                                    if (entityType == null) {
                                        System.out.println("Bukkit type not found in packetevents: " + type.getName() + ", id: " + type.getTypeId());
                                    }
                                }
                            }
                        }
                        break;
                    case PLAYER_FLYING:
                    case PLAYER_POSITION:
                    case PLAYER_POSITION_AND_ROTATION:
                    case PLAYER_ROTATION:
                        WrapperPlayClientPlayerFlying flying = new WrapperPlayClientPlayerFlying(event);
                        Location location = flying.getLocation();
                        break;
                    case PLAYER_DIGGING:
                        WrapperPlayClientPlayerDigging digging = new WrapperPlayClientPlayerDigging(event);
                        DiggingAction action = digging.getAction();
                        break;
                    case PLAYER_BLOCK_PLACEMENT:
                        WrapperPlayClientPlayerBlockPlacement blockPlacement = new WrapperPlayClientPlayerBlockPlacement(event);
                        BlockFace face = blockPlacement.getFace();
                        Vector3i bp = blockPlacement.getBlockPosition();
                        user.sendMessage(ChatColor.GOLD + "Face: " + face + ", bp: " + bp);
                        break;
                    case PLUGIN_MESSAGE:
                        WrapperPlayClientPluginMessage pluginMessage = new WrapperPlayClientPluginMessage(event);
                        String channel = pluginMessage.getChannelName();
                        byte[] data = pluginMessage.getData();
                        String brandData = new String(data, StandardCharsets.UTF_8);
                        Component component = Component.text("The channel name: " + channel)
                                .color(NamedTextColor.RED)
                                .append(Component.text(" Data: " + brandData)
                                        .color(NamedTextColor.GOLD));
                        break;
                    case STEER_VEHICLE:
                        WrapperPlayClientSteerVehicle steerVehicle = new WrapperPlayClientSteerVehicle(event);
                        float sideways = steerVehicle.getSideways();
                        float forward = steerVehicle.getForward();
                        user.sendMessage(ChatColor.GOLD + "Sideways: " + sideways + ", forward: " + forward);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPacketPlaySend(PacketPlaySendEvent event) {
                Player player = (Player) event.getPlayer();
                User user = event.getUser();
                if (event.getPacketType() == PacketType.Play.Server.JOIN_GAME) {
                    if (player != null) {
                        player.sendMessage("Hii " + player.getName());
                        user.sendMessage(ChatColor.GREEN + "Hi pt TWOOO");
                    } else {
                        user.sendMessage(ChatColor.RED + "player null, but hi dude!!!");
                    }
                    ByteBuf b;
                    System.out.println("Pipeline: " + ChannelHelper.pipelineHandlerNamesAsString(event.getChannel()));
                } else if (event.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE) {
                    WrapperPlayServerChatMessage chatMessage = new WrapperPlayServerChatMessage(event);
                    /*event.setCancelled(true);
                    Object buffer = chatMessage.getBuffer();
                    Object copy = ByteBufHelper.duplicate(buffer);
                    ByteBufHelper.retain(copy);
                    ByteBufHelper.readerIndex(copy, 0);
                    PacketEvents.getAPI().getProtocolManager().sendPacketSilently(event.getChannel(), copy);
                    System.out.println("Delayed " + chatMessage.getChatComponentJson());*/
                    event.getPostTasks().add(() -> {
                        user.sendTitle("Post chat message", "Pretty much", 10, 10, 10);
                    });
                } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_EFFECT) {
                    WrapperPlayServerEntityEffect effect = new WrapperPlayServerEntityEffect(event);
                    System.out.println("type: " + effect.getPotionType().getName() + ", type id: " + effect.getPotionType().getId());
                } else if (event.getPacketType() == PacketType.Play.Server.SPAWN_LIVING_ENTITY) {
                    WrapperPlayServerSpawnLivingEntity spawnLivingEntity = new WrapperPlayServerSpawnLivingEntity(event);
                    EntityType type = spawnLivingEntity.getEntityType();
                } else if (event.getPacketType() == PacketType.Play.Server.SPAWN_PAINTING) {
                    WrapperPlayServerSpawnPainting spawnPainting = new WrapperPlayServerSpawnPainting(event);
                    //System.out.println("Painting: " + spawnPainting.getEntityId() + ", " + spawnPainting.getType().name() + ", " + spawnPainting.getPosition().toString() + ", " + spawnPainting.getDirection().name() + ", " + spawnPainting.getUUID().toString());
                }
            }

            @Override
            public void onUserConnect(UserConnectEvent event) {
                System.out.println("User: (host-name) " + event.getUser().getAddress().getHostString() + " connected...");
            }

            @Override
            public void onUserLogin(UserLoginEvent event) {
                Player player = (Player) event.getPlayer();
                player.sendMessage("You logged in! User name: " + event.getUser().getProfile().getName());
            }

            @Override
            public void onUserDisconnect(UserDisconnectEvent event) {
                System.out.println("User: (host-name) " + event.getUser().getAddress().getHostString() + " disconnected...");
            }
        };
        //PacketEvents.getAPI().getEventManager().registerListener(listener);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}