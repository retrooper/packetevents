/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.utils.nms_entityfinder;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.utils.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class EntityFinderUtils {
    @Nullable
    public static ServerVersion version;
    private static Class<?> worldServerClass;
    private static Class<?> craftWorldClass;
    private static Class<?> entityClass;
    private static Method getEntityByIdMethod;
    private static Method craftWorldGetHandle;
    private static Method getBukkitEntity;

    private static boolean isServerVersion_v_1_8_x;

    public static void load() {
        isServerVersion_v_1_8_x = version.isHigherThan(ServerVersion.v_1_7_10) && version.isLowerThan(ServerVersion.v_1_9);
        try {
            worldServerClass = NMSUtils.getNMSClass("WorldServer");
            craftWorldClass = NMSUtils.getOBCClass("CraftWorld");
            entityClass = NMSUtils.getNMSClass("Entity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            getEntityByIdMethod = worldServerClass.getMethod(getEntityByIDMethodName(), int.class);
            craftWorldGetHandle = craftWorldClass.getMethod("getHandle");
            getBukkitEntity = entityClass.getMethod("getBukkitEntity");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static Entity getEntityById(final int id) {
        for (final World world : Bukkit.getWorlds()) {
            final Entity entity = getEntityByIdWithWorld(world, id);
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }

    @Nullable
    public static Entity getEntityByIdWithWorld(final World world, final int id) {
        if (world == null) {
            return null;
        }
        Object craftWorld = craftWorldClass.cast(world);

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

    /**
     * This is the name of the method that returns the NMS entity when you pass in its ID.
     * On 1.8 -&gt; 1.8.8 the name is called 'a', on 1.7.10 &amp; 1.9+ the name is called 'getEntity'.
     *
     * @return entity by ID method name
     */
    public static String getEntityByIDMethodName() {
        if (isServerVersion_v_1_8_x) {
            return "a";
        }
        return "getEntity";
    }
}
