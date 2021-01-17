/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.utils.entityfinder;

import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

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
    public static Entity getEntityById(final int id) {
        for (final World world : Bukkit.getWorlds()) {
            final Entity entity = getEntityByIdWithWorld(world, id);
            if (entity != null) {
                return entity;
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
    public static Entity getEntityByIdWithWorld(final World world, final int id) {
        if (world == null) {
            return null;
        } else if (NMSUtils.craftWorldClass == null) {
            throw new IllegalStateException("PacketEvents failed to locate the CraftWorld class.");
        }
        Object craftWorld = NMSUtils.craftWorldClass.cast(world);

        Object worldServer = null;
        try {
            worldServer = craftWorldGetHandle.invoke(craftWorld);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Object nmsEntity = null;
        try {
            nmsEntity = getEntityByIdMethod.invoke(worldServer, id);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (nmsEntity == null) {
            return null;
        }
        try {
            return (Entity) getBukkitEntity.invoke(nmsEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
