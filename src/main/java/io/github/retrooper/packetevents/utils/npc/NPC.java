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

    public NPC(String name, int entityID, UUID uuid, WrappedGameProfile gameProfile) {
        this.name = name;
        this.entityID = entityID;
        this.uuid = uuid;
        this.gameProfile = gameProfile;
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
                CompletableFuture.runAsync(() -> {
                    WrappedPacketOutPlayerInfo playerInfo = new WrappedPacketOutPlayerInfo(WrappedPacketOutPlayerInfo.PlayerInfoAction.REMOVE_PLAYER, new WrappedPacketOutPlayerInfo.PlayerInfo(name, gameProfile, GameMode.SURVIVAL, 0));
                    PacketEvents.get().getPlayerUtils().sendPacket(player, playerInfo);
                    WrappedPacketOutEntityDestroy wrappedPacketOutEntityDestroy = new WrappedPacketOutEntityDestroy(entityID);
                    PacketEvents.get().getPlayerUtils().sendPacket(player, wrappedPacketOutEntityDestroy);
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasSpawned(Player player) {
        return spawnedForPlayerMap.getOrDefault(player.getUniqueId(), false);
    }

    public void spawn(Player player) {
        try {
            if (!hasSpawned(player)) {
                CompletableFuture.runAsync(() -> {
                    WrappedPacketOutPlayerInfo playerInfo = new WrappedPacketOutPlayerInfo(WrappedPacketOutPlayerInfo.PlayerInfoAction.ADD_PLAYER, new WrappedPacketOutPlayerInfo.PlayerInfo(name, gameProfile, GameMode.SURVIVAL, 0));
                    PacketEvents.get().getPlayerUtils().sendPacket(player, playerInfo);
                    WrappedPacketOutNamedEntitySpawn wrappedPacketOutNamedEntitySpawn = new WrappedPacketOutNamedEntitySpawn(entityID, uuid, position, yaw, pitch);
                    PacketEvents.get().getPlayerUtils().sendPacket(player, wrappedPacketOutNamedEntitySpawn);
                    spawnedForPlayerMap.put(player.getUniqueId(), true);
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

    public void teleport(Player player, Vector3d targetPosition, float yaw, float pitch) {
        this.position = targetPosition;
        this.yaw = yaw;
        this.pitch = pitch;
        if (hasSpawned(player)) {
            PacketEvents.get().getPlayerUtils().sendPacket(player, new WrappedPacketOutEntityTeleport(entityID, position, yaw, pitch, onGround));
        }
    }

    public void move(Player player, Vector3d targetPosition) {
        this.position = targetPosition;
        double distX = targetPosition.x - position.x;
        double distY = targetPosition.y - position.y;
        double distZ = targetPosition.z - position.z;
        double dist = distX + distY + distZ;
        SendableWrapper sentPacket;
        if (dist > 8) {
            sentPacket = new WrappedPacketOutEntityTeleport(entityID, position, yaw, pitch, onGround);
        } else {

            sentPacket = new WrappedPacketOutEntity.WrappedPacketOutRelEntityMove(entityID, distX, distY, distZ, onGround);
        }
        if (hasSpawned(player)) {
            PacketEvents.get().getPlayerUtils().sendPacket(player, sentPacket);
        }
    }

    public void moveAndRotate(Player player, Vector3d targetPosition, float yaw, float pitch) {
        this.position = targetPosition;
        this.yaw = yaw;
        this.pitch = pitch;
        double distX = targetPosition.x - position.x;
        double distY = targetPosition.y - position.y;
        double distZ = targetPosition.z - position.z;
        double dist = distX + distY + distZ;
        SendableWrapper sentPacket;
        if (dist > 8) {
            sentPacket = new WrappedPacketOutEntityTeleport(entityID, position, yaw, pitch, onGround);
        } else {
            sentPacket = new WrappedPacketOutEntity.WrappedPacketOutRelEntityMoveLook(entityID, distX, distY, distZ, yaw, pitch, onGround);
        }
        if (hasSpawned(player)) {
            PacketEvents.get().getPlayerUtils().sendPacket(player, sentPacket);
        }
    }

    public void rotate(Player player, float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        WrappedPacketOutEntity.WrappedPacketOutEntityLook lookPacket = new WrappedPacketOutEntity.WrappedPacketOutEntityLook(entityID, (byte) (yaw * 256 / 360), (byte) (pitch * 256 / 360), onGround);
        WrappedPacketOutEntityHeadRotation headRotationPacket = new WrappedPacketOutEntityHeadRotation(entityID, (byte) (yaw * 256 / 360));
        if (hasSpawned(player)) {
            PacketEvents.get().getPlayerUtils().sendPacket(player, lookPacket);
            PacketEvents.get().getPlayerUtils().sendPacket(player, headRotationPacket);
        }
    }

    public void teleport(List<Player> players, Vector3d targetPosition, float yaw, float pitch) {
        this.position = targetPosition;
        this.yaw = yaw;
        this.pitch = pitch;
        for (Player player : players) {
            if (hasSpawned(player)) {
                PacketEvents.get().getPlayerUtils().sendPacket(player, new WrappedPacketOutEntityTeleport(entityID, position, yaw, pitch, onGround));
            }
        }
    }

    public void move(List<Player> players, Vector3d targetPosition) {
        double distX = targetPosition.x - position.x;
        double distY = targetPosition.y - position.y;
        double distZ = targetPosition.z - position.z;
        double dist = distX + distY + distZ;
        this.position = targetPosition;
        SendableWrapper sentPacket;
        if (dist > 8) {
            sentPacket = new WrappedPacketOutEntityTeleport(entityID, position, yaw, pitch, onGround);
        } else {
            sentPacket = new WrappedPacketOutEntity.WrappedPacketOutRelEntityMove(entityID, distX, distY, distZ, onGround);
        }
        for (Player player : players) {
            if (hasSpawned(player)) {
                PacketEvents.get().getPlayerUtils().sendPacket(player, sentPacket);
            }
        }
    }

    public void moveAndRotate(List<Player> players, Vector3d targetPosition, float yaw, float pitch) {
        double distX = targetPosition.x - position.x;
        double distY = targetPosition.y - position.y;
        double distZ = targetPosition.z - position.z;
        double dist = distX + distY + distZ;
        this.position = targetPosition;
        this.yaw = yaw;
        this.pitch = pitch;
        SendableWrapper sentPacket;
        if (dist > 8) {
            sentPacket = new WrappedPacketOutEntityTeleport(entityID, position, yaw, pitch, onGround);
        } else {
            sentPacket = new WrappedPacketOutEntity.WrappedPacketOutRelEntityMoveLook(entityID, distX, distY, distZ, yaw, pitch, onGround);
        }
        for (Player player : players) {
            if (hasSpawned(player)) {
                PacketEvents.get().getPlayerUtils().sendPacket(player, sentPacket);
            }
        }
    }

    public void rotate(List<Player> players, float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        WrappedPacketOutEntity.WrappedPacketOutEntityLook lookPacket = new WrappedPacketOutEntity.WrappedPacketOutEntityLook(entityID, (byte) (yaw * 256 / 360), (byte) (pitch * 256 / 360), onGround);
        WrappedPacketOutEntityHeadRotation headRotationPacket = new WrappedPacketOutEntityHeadRotation(entityID, (byte) (yaw * 256 / 360));
        for (Player player : players) {
            if (hasSpawned(player)) {
                PacketEvents.get().getPlayerUtils().sendPacket(player, lookPacket);
                PacketEvents.get().getPlayerUtils().sendPacket(player, headRotationPacket);
            }
        }
    }

    @Deprecated
    public void moveDelta(List<Player> players, Vector3d deltaPosition) {
        this.position = this.position.add(deltaPosition);
        WrappedPacketOutEntityTeleport teleportPacket = new WrappedPacketOutEntityTeleport(entityID, position, yaw, pitch, onGround);
        for (Player player : players) {
            if (hasSpawned(player)) {
                PacketEvents.get().getPlayerUtils().sendPacket(player, teleportPacket);
            }
        }
    }

    @Deprecated
    public void moveDeltaAndRotate(List<Player> players, Vector3d deltaPosition, float yaw, float pitch) {
        this.position = this.position.add(deltaPosition);
        this.yaw = yaw;
        this.pitch = pitch;
        WrappedPacketOutEntityTeleport teleportPacket = new WrappedPacketOutEntityTeleport(entityID, position, yaw, pitch, onGround);
        for (Player player : players) {
            if (hasSpawned(player)) {
                PacketEvents.get().getPlayerUtils().sendPacket(player, teleportPacket);
            }
        }
    }
}
