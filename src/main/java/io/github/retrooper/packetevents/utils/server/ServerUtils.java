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

package io.github.retrooper.packetevents.utils.server;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.entityfinder.EntityFinderUtils;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.npc.NPCManager;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spigotmc.SpigotConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ServerUtils {
    private static Method getLevelEntityGetterIterable;
    private static Class<?> persistentEntitySectionManagerClass, levelEntityGetterClass;
    private static byte v_1_17 = -1;
    private static Class<?> geyserClass;
    private boolean geyserClassChecked;
    private final NPCManager npcManager = new NPCManager();

    //Initialized in PacketEvents#load
    public Map<Integer, Entity> entityCache;

    /**
     * Get the server version.
     *
     * @return Get Server Version
     */
    public ServerVersion getVersion() {
        return ServerVersion.getVersion();
    }

    /**
     * Get recent TPS array from NMS.
     *
     * @return Get Recent TPS
     */
    public double[] getRecentTPS() {
        return NMSUtils.recentTPS();
    }

    /**
     * Get the current TPS.
     *
     * @return Get Current TPS
     */
    public double getTPS() {
        return getRecentTPS()[0];
    }

    /**
     * Get the operating system of the local machine
     *
     * @return Get Operating System
     */
    public SystemOS getOS() {
        return SystemOS.getOS();
    }

    /**
     * Get the NPC Manager.
     *
     * @return NPC Manager
     */
    public NPCManager getNPCManager() {
        return npcManager;
    }

    public boolean isBungeeCordEnabled() {
        return SpigotConfig.bungee;
    }

    @Nullable
    public Entity getEntityById(@Nullable World world, int entityID) {
        Entity e = entityCache.get(entityID);
        if (e != null) {
            return e;
        }

        if (v_1_17 == -1) {
            v_1_17 = (byte) (getVersion().isNewerThanOrEquals(ServerVersion.v_1_17) ? 1 : 0);
        }

        if (v_1_17 == 1) {
            if (world != null) {
                for (Entity entity : PacketEvents.get().getServerUtils().getEntityList(world)) {
                    if (entity.getEntityId() == entityID) {
                        return entity;
                    }
                }
            }
            for (World w : Bukkit.getWorlds()) {
                for (Entity entity : PacketEvents.get().getServerUtils().getEntityList(w)) {
                    if (entity.getEntityId() == entityID) {
                        return entity;
                    }
                }
            }
        } else {
            return EntityFinderUtils.getEntityByIdUnsafe(world, entityID);
        }
        return null;
    }

    @Nullable
    public Entity getEntityById(int entityID) {
        return getEntityById(null, entityID);
    }

    public List<Entity> getEntityList(World world) {
        if (v_1_17 == -1) {
            v_1_17 = (byte) (getVersion().isNewerThanOrEquals(ServerVersion.v_1_17) ? 1 : 0);
        }

        if (v_1_17 == 1) {
            if (persistentEntitySectionManagerClass == null) {
                persistentEntitySectionManagerClass = NMSUtils.getNMClassWithoutException("world.level.entity.PersistentEntitySectionManager");
            }
            if (levelEntityGetterClass == null) {
                levelEntityGetterClass = NMSUtils.getNMClassWithoutException("world.level.entity.LevelEntityGetter");
            }
            if (getLevelEntityGetterIterable == null) {
                getLevelEntityGetterIterable = Reflection.getMethod(levelEntityGetterClass, Iterable.class, 0);
            }
            Object worldServer = NMSUtils.convertBukkitWorldToWorldServer(world);
            WrappedPacket wrappedWorldServer = new WrappedPacket(new NMSPacket(worldServer));
            Object persistentEntitySectionManager = wrappedWorldServer.readObject(0, persistentEntitySectionManagerClass);
            WrappedPacket wrappedPersistentEntitySectionManager = new WrappedPacket(new NMSPacket(persistentEntitySectionManager));
            Object levelEntityGetter = wrappedPersistentEntitySectionManager.readObject(0, levelEntityGetterClass);
            Iterable<Object> nmsEntitiesIterable = null;
            try {
                nmsEntitiesIterable = (Iterable<Object>) getLevelEntityGetterIterable.invoke(levelEntityGetter);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            List<Entity> entityList = new ArrayList<>();
            nmsEntitiesIterable.forEach(nmsEntity -> {
                Entity bukkitEntity = NMSUtils.getBukkitEntity(nmsEntity);
                entityList.add(bukkitEntity);
            });
            return entityList;
        } else {
            return world.getEntities();
        }
    }


    public boolean isGeyserAvailable() {
        if (!geyserClassChecked) {
            geyserClassChecked = true;
            try {
                geyserClass = Class.forName("org.geysermc.connector.GeyserConnector");
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        }
        return geyserClass != null;
    }
}
