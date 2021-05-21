/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
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

package io.github.retrooper.packetevents.utils.npc;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.play.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.play.out.entitydestroy.WrappedPacketOutEntityDestroy;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityheadrotation.WrappedPacketOutEntityHeadRotation;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityteleport.WrappedPacketOutEntityTeleport;
import io.github.retrooper.packetevents.packetwrappers.play.out.namedentityspawn.WrappedPacketOutNamedEntitySpawn;
import io.github.retrooper.packetevents.packetwrappers.play.out.playerinfo.WrappedPacketOutPlayerInfo;
import io.github.retrooper.packetevents.utils.gameprofile.WrappedGameProfile;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.GameMode;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class NPC {
    private final String name;
    private final int entityID;
    private final UUID uuid;
    private final WrappedGameProfile gameProfile;
    private final Map<UUID, Boolean> spawnedForPlayerMap = new ConcurrentHashMap<>();
    private Vector3d position;
    private float yaw, pitch;
    private boolean onGround;

    public NPC(String name) {
        this.name = name;
        this.entityID = NMSUtils.generateEntityId();
        this.uuid = NMSUtils.generateUUID();
        this.gameProfile = new WrappedGameProfile(uuid, name);
        this.position = new Vector3d(0, 0, 0);
        this.yaw = 0;
        this.pitch = 0;
    }

    public NPC(String name, Vector3d position, float yaw, float pitch) {
        this.name = name;
        this.entityID = NMSUtils.generateEntityId();
        this.uuid = NMSUtils.generateUUID();
        this.gameProfile = new WrappedGameProfile(uuid, name);
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public NPC(String name, Location location) {
        this(name, new Vector3d(location.getX(), location.getY(), location.getZ()), location.getYaw(), location.getPitch());
    }

    public void despawn(Player player) {
        boolean spawned = spawnedForPlayerMap.getOrDefault(player.getUniqueId(), false);
        spawnedForPlayerMap.remove(player.getUniqueId());
        if (spawned) {
            try {
                CompletableFuture.runAsync(new Runnable() {
                    @Override
                    public void run() {
                        WrappedPacketOutPlayerInfo playerInfo = new WrappedPacketOutPlayerInfo(WrappedPacketOutPlayerInfo.PlayerInfoAction.REMOVE_PLAYER, new WrappedPacketOutPlayerInfo.PlayerInfo(name, gameProfile, GameMode.SURVIVAL, 0));
                        PacketEvents.get().getPlayerUtils().sendPacket(player, playerInfo);
                    }
                }).thenRunAsync(new Runnable() {
                    @Override
                    public void run() {
                        WrappedPacketOutEntityDestroy wrappedPacketOutEntityDestroy = new WrappedPacketOutEntityDestroy(entityID);
                        PacketEvents.get().getPlayerUtils().sendPacket(player, wrappedPacketOutEntityDestroy);
                    }
                })

                        .get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void spawn(Player player) {
        try {
            boolean spawned = spawnedForPlayerMap.getOrDefault(player.getUniqueId(), false);
            if (!spawned) {
                CompletableFuture.runAsync(new Runnable() {
                    @Override
                    public void run() {
                        WrappedPacketOutPlayerInfo playerInfo = new WrappedPacketOutPlayerInfo(WrappedPacketOutPlayerInfo.PlayerInfoAction.ADD_PLAYER, new WrappedPacketOutPlayerInfo.PlayerInfo(name, gameProfile, GameMode.SURVIVAL, 0));
                        PacketEvents.get().getPlayerUtils().sendPacket(player, playerInfo);
                    }
                }).thenRunAsync(new Runnable() {
                    @Override
                    public void run() {
                        WrappedPacketOutNamedEntitySpawn wrappedPacketOutNamedEntitySpawn = new WrappedPacketOutNamedEntitySpawn(entityID, uuid, position, yaw, pitch);
                        PacketEvents.get().getPlayerUtils().sendPacket(player, wrappedPacketOutNamedEntitySpawn);
                        spawnedForPlayerMap.put(player.getUniqueId(), true);
                    }
                }).get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public int getEntityId() {
        return entityID;
    }

    public UUID getUUID() {
        return uuid;
    }

    public WrappedGameProfile getGameProfile() {
        return gameProfile;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public Vector3d getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void moveDelta(List<Player> players, Vector3d deltaPosition) {
        SendableWrapper sentPacket = getAppropriatePacket(getPosition().add(deltaPosition));
        this.position = this.position.add(deltaPosition);
        for (Player player : players) {
            boolean spawned = spawnedForPlayerMap.getOrDefault(player.getUniqueId(), false);
            if (spawned) {
                if (sentPacket != null) {
                    PacketEvents.get().getPlayerUtils().sendPacket(player, sentPacket);
                }
            }
        }
    }

    public void move(List<Player> players, Vector3d targetPosition) {
        SendableWrapper sentPacket = getAppropriatePacket(targetPosition);
        this.position = targetPosition;
        for (Player player : players) {
            boolean spawned = spawnedForPlayerMap.getOrDefault(player.getUniqueId(), false);
            if (spawned) {
                if (sentPacket != null) {
                    PacketEvents.get().getPlayerUtils().sendPacket(player, sentPacket);
                }
            }
        }
    }

    public void moveDeltaAndRotate(List<Player> players, Vector3d deltaPosition, byte yaw, byte pitch) {
        SendableWrapper[] sentPackets = getAppropriatePacket(getPosition().add(deltaPosition), yaw, pitch);
        this.position = this.position.add(deltaPosition);
        this.yaw = yaw;
        this.pitch = pitch;
        for (Player player : players) {
            boolean spawned = spawnedForPlayerMap.getOrDefault(player.getUniqueId(), false);
            if (spawned) {
                for (SendableWrapper sentPacket : sentPackets) {
                    PacketEvents.get().getPlayerUtils().sendPacket(player, sentPacket);
                }
            }
        }
    }

    public void moveAndRotate(List<Player> players, Vector3d targetPosition, float yaw, float pitch) {
        SendableWrapper[] sentPackets = getAppropriatePacket(targetPosition, yaw, pitch);
        this.position = targetPosition;
        this.yaw = yaw;
        this.pitch = pitch;
        for (Player player : players) {
            boolean spawned = spawnedForPlayerMap.getOrDefault(player.getUniqueId(), false);
            if (spawned) {
                for (SendableWrapper sentPacket : sentPackets) {
                    PacketEvents.get().getPlayerUtils().sendPacket(player, sentPacket);
                }
            }
        }
    }

    public void rotate(List<Player> players, float yaw, float pitch) {
        SendableWrapper[] sentPackets = getAppropriatePacket(yaw, pitch);
        this.yaw = yaw;
        this.pitch = pitch;
        for (Player player : players) {
            boolean spawned = spawnedForPlayerMap.getOrDefault(player.getUniqueId(), false);
            if (spawned) {
                for (SendableWrapper sentPacket : sentPackets) {
                    PacketEvents.get().getPlayerUtils().sendPacket(player, sentPacket);
                }
            }
        }
    }

    private SendableWrapper getAppropriatePacket(Vector3d targetPos) {
        double dist = getPosition().distance(targetPos);
        if (dist == 0.0) {
            return null;
        }
        return new WrappedPacketOutEntityTeleport(entityID, targetPos, yaw, pitch, onGround);
    }

    private SendableWrapper[] getAppropriatePacket(Vector3d targetPos, float yaw, float pitch) {
        double dist = getPosition().distance(targetPos);
        if (dist == 0.0 && this.yaw == yaw && this.pitch == pitch) {
            return new SendableWrapper[]{};
        }
        return new SendableWrapper[]{new WrappedPacketOutEntityTeleport(entityID, targetPos, yaw, pitch, onGround)};
    }

    private SendableWrapper[] getAppropriatePacket(float yaw, float pitch) {
        if (this.yaw == yaw && this.pitch == pitch) {
            return new SendableWrapper[]{};
        }
        return new SendableWrapper[]{
                new WrappedPacketOutEntity.WrappedPacketOutEntityLook(entityID, (byte) (yaw * 256 / 360), (byte) (pitch * 256 / 360), onGround)
                , new WrappedPacketOutEntityHeadRotation(entityID, (byte) (yaw * 256 / 360))
        };
    }
}
