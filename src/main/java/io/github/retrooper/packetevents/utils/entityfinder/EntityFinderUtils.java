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

package io.github.retrooper.packetevents.utils.entityfinder;

import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Internal utility class to find entities by their Entity ID.
 *
 * @author retrooper
 * @since 1.6.8
 */
public final class EntityFinderUtils {
    public static ServerVersion version;
    private static Class<?> worldServerClass;
    private static Method getEntityByIdMethod;
    private static Method craftWorldGetHandle;
    private static Method getBukkitEntity;

    public static void load() {
        try {
            worldServerClass = NMSUtils.getNMSClass("WorldServer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            getEntityByIdMethod = worldServerClass.getMethod((version.getProtocolVersion() == (short) 47)
                    ? "a" : "getEntity", int.class);

            craftWorldGetHandle = NMSUtils.craftWorldClass.getMethod("getHandle");
            getBukkitEntity = NMSUtils.nmsEntityClass.getMethod("getBukkitEntity");
        } catch (NoSuchMethodException e) {
            try {
                getEntityByIdMethod = worldServerClass.getMethod("getEntity", int.class);

                craftWorldGetHandle = NMSUtils.craftWorldClass.getMethod("getHandle");
                getBukkitEntity = NMSUtils.nmsEntityClass.getMethod("getBukkitEntity");
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Get an entity by their ID.
     *
     * @param id Entity ID
     * @return Bukkit Entity.
     */
    @Nullable
    public static Entity getEntityById(World origin, int id) {
        Entity e = getEntityByIdWithWorld(origin, id);
        if (e == null) {
            for (World world : Bukkit.getWorlds()) {
                Entity entity = getEntityByIdWithWorld(world, id);
                if (entity != null) {
                    return entity;
                }
            }
        }
        return null;
    }

    /**
     * Get an entity by their ID, guaranteed to be in the specified world.
     *
     * @param world Bukkit world.
     * @param id    Entity ID.
     * @return Bukkit Entity.
     */
    public static Entity getEntityByIdWithWorld(World world, int id) {
        if (world == null) {
            return null;
        }
        if (NMSUtils.craftWorldClass == null) {
            throw new IllegalStateException("PacketEvents failed to locate the CraftWorld class.");
        }
        Object craftWorld = NMSUtils.craftWorldClass.cast(world);

        try {
            Object worldServer = craftWorldGetHandle.invoke(craftWorld);
            Object nmsEntity = getEntityByIdMethod.invoke(worldServer, id);
            if (nmsEntity == null) {
                return null;
            }
            return (Entity) getBukkitEntity.invoke(nmsEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
