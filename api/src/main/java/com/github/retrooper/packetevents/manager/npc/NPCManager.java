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

package com.github.retrooper.packetevents.manager.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.protocol.chat.component.impl.TextComponent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.server.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NPCManager {
    public static int ENTITY_TELEPORT_THRESHOLD = 8;
    //A map that has an NPC as key with all channels that can see this NPC as values
    private static final Map<NPC, Set<ChannelAbstract>> TARGET_CHANNELS = new ConcurrentHashMap<>();

    public void spawn(ChannelAbstract channel, NPC npc) {
        Set<ChannelAbstract> targetChannels = TARGET_CHANNELS.computeIfAbsent(npc, k -> new HashSet<>());
        //Spawn with packets
        WrapperPlayServerPlayerInfo.PlayerData tabData = new WrapperPlayServerPlayerInfo.PlayerData(TextComponent.builder().text(npc.getDisplayName()).build(), npc.getProfile(),
                GameMode.SURVIVAL, npc.getDisplayPing());
        WrapperPlayServerPlayerInfo playerInfoPacket = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.ADD_PLAYER, npc.getProfile().getId(), tabData);
        PacketEvents.getAPI().getPlayerManager().sendPacket(channel, playerInfoPacket);

        List<EntityData> entityMetadata = new ArrayList<>();
        WrapperPlayServerSpawnPlayer spawnPlayer = new WrapperPlayServerSpawnPlayer(npc.getId(), npc.getProfile().getId(), npc.getLocation(), entityMetadata);
        PacketEvents.getAPI().getPlayerManager().sendPacket(channel, spawnPlayer);
        targetChannels.add(channel);
    }



    public void despawn(ChannelAbstract channel, NPC npc) {
        Set<ChannelAbstract> targetChannels = TARGET_CHANNELS.get(npc);
        if (targetChannels != null) {
            WrapperPlayServerDestroyEntities destroyEntities = new WrapperPlayServerDestroyEntities(npc.getId());
            PacketEvents.getAPI().getPlayerManager().sendPacket(channel, destroyEntities);
            targetChannels.remove(channel);
            if (targetChannels.isEmpty()) {
                TARGET_CHANNELS.remove(npc);
            }
        }
    }

    public boolean isUsed(NPC npc) {
        return TARGET_CHANNELS.containsKey(npc);
    }

    public boolean isUsedFor(ChannelAbstract channel, NPC npc) {
        Set<ChannelAbstract> targetChannels = TARGET_CHANNELS.get(npc);
        return targetChannels != null && targetChannels.contains(channel);
    }

    public void teleportNPC(NPC npc, Location to) {
        npc.setLocation(to);
        Set<ChannelAbstract> targetChannels = TARGET_CHANNELS.get(npc);
        if (targetChannels != null) {
            for (ChannelAbstract channel : targetChannels) {
                WrapperPlayServerEntityTeleport entityTeleport = new WrapperPlayServerEntityTeleport(npc.getId(), to, true);
                PacketEvents.getAPI().getPlayerManager().sendPacket(channel, entityTeleport);
            }
        }
    }

    public void updateNPCLocation(NPC npc, Location to) {
        Location from = npc.getLocation();
        npc.setLocation(to);
        Set<ChannelAbstract> targetChannels = TARGET_CHANNELS.get(npc);
        if (targetChannels != null && !targetChannels.isEmpty()) {
            double distXAbs = Math.abs(to.getPosition().getX() - from.getPosition().getX());
            double distYAbs = Math.abs(to.getPosition().getY() - from.getPosition().getY());
            double distZAbs = Math.abs(to.getPosition().getZ() - from.getPosition().getZ());
            boolean shouldUseEntityTeleport = distXAbs > ENTITY_TELEPORT_THRESHOLD ||
                    distYAbs > ENTITY_TELEPORT_THRESHOLD ||
                    distZAbs > ENTITY_TELEPORT_THRESHOLD;
            for (ChannelAbstract channel : targetChannels) {
                if (shouldUseEntityTeleport) {
                    WrapperPlayServerEntityTeleport entityTeleport = new WrapperPlayServerEntityTeleport(npc.getId(), to, true);
                    PacketEvents.getAPI().getPlayerManager().sendPacket(channel, entityTeleport);
                }
                else {
                    boolean rotationChanged = to.getYaw() != from.getYaw() || to.getPitch() != from.getPitch();
                    boolean positionChanged = to.getPosition().getX() != from.getPosition().getX() ||
                            to.getPosition().getY() != from.getPosition().getY() ||
                            to.getPosition().getZ() != from.getPosition().getZ();
                    double deltaX = positionChanged ? (to.getPosition().getX() - from.getPosition().getX()) : 0;
                    double deltaY = positionChanged ? (to.getPosition().getY() - from.getPosition().getY()) : 0;
                    double deltaZ = positionChanged ? (to.getPosition().getZ() - from.getPosition().getZ()) : 0;
                    if (positionChanged && rotationChanged) {
                        WrapperPlayServerEntityRelativeMoveAndRotation entityRelativeMoveAndRotation = new WrapperPlayServerEntityRelativeMoveAndRotation(npc.getId(), deltaX, deltaY, deltaZ, (byte)to.getYaw(), (byte)to.getPitch(), true);
                        PacketEvents.getAPI().getPlayerManager().sendPacket(channel, entityRelativeMoveAndRotation);

                        WrapperPlayServerEntityHeadLook headYaw = new WrapperPlayServerEntityHeadLook(npc.getId(), (byte) to.getYaw());
                        PacketEvents.getAPI().getPlayerManager().sendPacket(channel, headYaw);
                    }
                    else if (positionChanged) {
                        WrapperPlayServerEntityRelativeMove entityRelativeMove = new WrapperPlayServerEntityRelativeMove(npc.getId(), deltaX, deltaY, deltaZ, true);
                        PacketEvents.getAPI().getPlayerManager().sendPacket(channel, entityRelativeMove);
                    }
                    else if (rotationChanged) {
                        WrapperPlayServerEntityRotation entityRotation = new WrapperPlayServerEntityRotation(npc.getId(), (byte) to.getYaw(), (byte)to.getPitch(), true);
                        PacketEvents.getAPI().getPlayerManager().sendPacket(channel, entityRotation);

                        WrapperPlayServerEntityHeadLook headYaw = new WrapperPlayServerEntityHeadLook(npc.getId(), (byte) to.getYaw());
                        PacketEvents.getAPI().getPlayerManager().sendPacket(channel, headYaw);
                    }
                }
            }
        }
    }

    public void updateNPCRotation(NPC npc, byte yaw, byte pitch) {
        npc.getLocation().setYaw(yaw);
        npc.getLocation().setPitch(pitch);
        Set<ChannelAbstract> targetChannels = TARGET_CHANNELS.get(npc);
        if (targetChannels != null && !targetChannels.isEmpty()) {
            for (ChannelAbstract channel : targetChannels) {
                WrapperPlayServerEntityRotation entityRotation = new WrapperPlayServerEntityRotation(npc.getId(), yaw, pitch, true);
                PacketEvents.getAPI().getPlayerManager().sendPacket(channel, entityRotation);

                WrapperPlayServerEntityHeadLook headYaw = new WrapperPlayServerEntityHeadLook(npc.getId(), yaw);
                PacketEvents.getAPI().getPlayerManager().sendPacket(channel, headYaw);
            }
        }
    }
}
