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

package com.github.retrooper.packetevents.manager.server;

import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.channel.ChannelAbstract;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public interface ServerManager {
    ServerVersion resolveVersionNoCache();

    /**
     * Get the server version.
     *
     * @return Get Server Version
     */
    ServerVersion getVersion();

    /**
     * Get the operating system of the local machine
     *
     * @return Get Operating System
     */
    default SystemOS getOS() {
        return SystemOS.getOS();
    }

    void receivePacket(ChannelAbstract channel, ByteBufAbstract byteBuf);

    void receivePacket(Object player, ByteBufAbstract byteBuf);

    void receivePacket(Object player, PacketWrapper<?> wrapper);

    void receivePacket(ChannelAbstract channel, PacketWrapper<?> wrapper);

   /* private static Entity getEntityByIDWithWorldUnsafe(World world, int id) {
        if (world == null) {
            return null;
        }

        Object craftWorld = MinecraftReflectionUtil.CRAFT_WORLD_CLASS.cast(world);

        try {
            Object worldServer = MinecraftReflectionUtil.GET_CRAFT_WORLD_HANDLE_METHOD.invoke(craftWorld);
            Object nmsEntity = MinecraftReflectionUtil.GET_ENTITY_BY_ID_METHOD.invoke(worldServer, id);
            if (nmsEntity == null) {
                return null;
            }
            return MinecraftReflectionUtil.getBukkitEntity(nmsEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private static Entity getEntityByIDUnsafe(World origin, int id) {
        Entity e = getEntityByIDWithWorldUnsafe(origin, id);
        if (e != null) {
            return e;
        }
        for (World world : Bukkit.getWorlds()) {
            Entity entity = getEntityByIDWithWorldUnsafe(world, id);
            if (entity != null) {
                return entity;
            }
        }
        for (World world : Bukkit.getWorlds()) {
            try {
                for (Entity entity : world.getEntities()) {
                    if (entity.getEntityId() == id) {
                        return entity;
                    }
                }
            } catch (ConcurrentModificationException ex) {
                return null;
            }
        }
        return null;
    }

    @Nullable
    public static Entity getEntityByID(@Nullable World world, int entityID) {
        Entity e = ENTITY_ID_CACHE.get(entityID);
        if (e != null) {
            return e;
        }

        if (MinecraftReflectionUtil.V_1_17_OR_HIGHER) {
            try {
                if (world != null) {
                    for (Entity entity : getEntityList(world)) {
                        if (entity.getEntityId() == entityID) {
                            ENTITY_ID_CACHE.putIfAbsent(entity.getEntityId(), entity);
                            return entity;
                        }
                    }
                }
            } catch (Exception ex) {
                //We are retrying below
            }
            try {
                for (World w : Bukkit.getWorlds()) {
                    for (Entity entity : getEntityList(w)) {
                        if (entity.getEntityId() == entityID) {
                            ENTITY_ID_CACHE.putIfAbsent(entity.getEntityId(), entity);
                            return entity;
                        }
                    }
                }
            } catch (Exception ex) {
                //No entity found
                return null;
            }
        } else {
            return getEntityByIDUnsafe(world, entityID);
        }
        return null;
    }

    @Nullable
    public static Entity getEntityByID(int entityID) {
        return getEntityByID(null, entityID);
    }

    public static List<Entity> getEntityList(World world) {
        if (MinecraftReflectionUtil.V_1_17_OR_HIGHER) {
            Object worldServer = MinecraftReflectionUtil.convertBukkitWorldToWorldServer(world);
            ReflectionObject wrappedWorldServer = new ReflectionObject(worldServer);
            Object persistentEntitySectionManager = wrappedWorldServer.readObject(0, MinecraftReflectionUtil.PERSISTENT_ENTITY_SECTION_MANAGER_CLASS);
            ReflectionObject wrappedPersistentEntitySectionManager = new ReflectionObject(persistentEntitySectionManager);
            Object levelEntityGetter = wrappedPersistentEntitySectionManager.readObject(0, MinecraftReflectionUtil.LEVEL_ENTITY_GETTER_CLASS);
            Iterable<Object> nmsEntitiesIterable = null;
            try {
                nmsEntitiesIterable = (Iterable<Object>) MinecraftReflectionUtil.GET_LEVEL_ENTITY_GETTER_ITERABLE_METHOD.invoke(levelEntityGetter);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            List<Entity> entityList = new ArrayList<>();
            if (nmsEntitiesIterable != null) {
                for (Object nmsEntity : nmsEntitiesIterable) {
                    Entity bukkitEntity = MinecraftReflectionUtil.getBukkitEntity(nmsEntity);
                    entityList.add(bukkitEntity);
                }
            }
            return entityList;
        } else {
            return world.getEntities();
        }
    }


    public static boolean isGeyserAvailable() {
        return MinecraftReflectionUtil.GEYSER_CLASS != null;
    }


    */
}
