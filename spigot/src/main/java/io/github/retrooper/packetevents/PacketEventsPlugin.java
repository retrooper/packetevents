/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19_1;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleDustData;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.TimeStampMode;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.client.*;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
                    case INTERACT_ENTITY:
                        ResourceLocation identifier = new ResourceLocation("minecraft:adventure/root");
                        WrapperPlayServerSelectAdvancementsTab selectAdvancementsTab =
                                new WrapperPlayServerSelectAdvancementsTab(identifier);
                        user.sendPacket(selectAdvancementsTab);
                        user.sendMessage("sent it!");
                        break;
                    case CHAT_MESSAGE:
                        WrapperPlayClientChatMessage chatMessage = new WrapperPlayClientChatMessage(event);
                        if (chatMessage.getMessage().equalsIgnoreCase("!test")) {
                            final Particle particle = new Particle(ParticleTypes.DUST, new ParticleDustData(0.5F,
                                    new Vector3f(0, 1, 0)));
                            user.sendPacket(new WrapperPlayServerParticle(
                                    // still needs magnolia colors
                                    particle, true,
                                    new Vector3d(player.getLocation().getX(), player.getLocation().getY() + 1, player.getLocation().getZ()),
                                    new Vector3f(0.65F, 0, 0.65F), 1, 100));
                            user.sendMessage("Sent!");
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
                        BlockFace face = digging.getBlockFace();
                        event.getUser().sendMessage("action: " + action + ", face: " + face);
                        break;
                    case PLAYER_BLOCK_PLACEMENT:
                        WrapperPlayClientPlayerBlockPlacement blockPlacement = new WrapperPlayClientPlayerBlockPlacement(event);
                        BlockFace frace = blockPlacement.getFace();
                        Vector3i bp = blockPlacement.getBlockPosition();
                        user.sendMessage(ChatColor.GOLD + "Face: " + frace + ", bp: " + bp);
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
                    case UPDATE_SIGN:
                        WrapperPlayClientUpdateSign sign = new WrapperPlayClientUpdateSign(event);
                        user.sendMessage("Sign is " + sign.getBlockPosition());
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
                    System.out.println("Pipeline: " + ChannelHelper.pipelineHandlerNamesAsString(event.getChannel()));
                } else if (event.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE) {
                    WrapperPlayServerChatMessage cm = new WrapperPlayServerChatMessage(event);
                    System.out.println("He said:" + cm.getMessage().getChatContent() + ", type: " + cm.getMessage().getType().getName());
                    if (cm.getMessage() instanceof ChatMessage_v1_19_1) {
                        UUID uuid = ((ChatMessage_v1_19_1)cm.getMessage()).getSenderUUID();
                        System.out.println("uuid: " + uuid + ", type: " + cm.getMessage().getType().getName());
                    }
                }
                else if (event.getPacketType() == PacketType.Play.Server.SYSTEM_CHAT_MESSAGE) {
                    System.out.println("sent sissysystemmessage!");
                }
                else if (event.getPacketType() == PacketType.Play.Server.ENTITY_EFFECT) {
                    WrapperPlayServerEntityEffect effect = new WrapperPlayServerEntityEffect(event);
                    System.out.println("type: " + effect.getPotionType().getName() + ", type id: " + effect.getPotionType().getId());
                } else if (event.getPacketType() == PacketType.Play.Server.SPAWN_LIVING_ENTITY) {
                    WrapperPlayServerSpawnLivingEntity spawnLivingEntity = new WrapperPlayServerSpawnLivingEntity(event);
                    EntityType type = spawnLivingEntity.getEntityType();
                    System.out.println("Spawn living: " + type.getName());
                } else if (event.getPacketType() == PacketType.Play.Server.SPAWN_PAINTING) {
                    WrapperPlayServerSpawnPainting spawnPainting = new WrapperPlayServerSpawnPainting(event);
                    System.out.println("Painting: " + spawnPainting.getEntityId() + ", " + spawnPainting.getType().toString() + ", "
                            + spawnPainting.getPosition().toString() + ", " + spawnPainting.getDirection().name() + ", " + spawnPainting.getUUID().toString());

                 } else if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY) {
                   // WrapperPlayServerSpawnEntity spawnEntity = new WrapperPlayServerSpawnEntity(event);
                    //System.out.println("Spawning a new entity of type: " + spawnEntity.getEntityType());
                } else if (event.getPacketType() == PacketType.Play.Server.BLOCK_CHANGE) {
                    WrapperPlayServerBlockChange change = new WrapperPlayServerBlockChange(event);
                    Bukkit.broadcastMessage(change.getBlockState().toString());
                } else if (event.getPacketType() == PacketType.Play.Server.CHUNK_DATA) {
                    WrapperPlayServerChunkData data = new WrapperPlayServerChunkData(event);
                }
                else if (event.getPacketType() == PacketType.Play.Server.UPDATE_ATTRIBUTES) {
                    WrapperPlayServerUpdateAttributes properties = new WrapperPlayServerUpdateAttributes(event);
                    for (WrapperPlayServerUpdateAttributes.Property prop : properties.getProperties()) {
                        event.getUser().sendMessage("prop: " + prop.getKey() + ", value: " + prop.getKey() + ", mod: " + prop.getModifiers().toString());
                    }
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
