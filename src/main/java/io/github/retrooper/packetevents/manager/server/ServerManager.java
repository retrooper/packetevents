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

package io.github.retrooper.packetevents.manager.server;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.BoundingBox;
import io.github.retrooper.packetevents.utils.MinecraftReflection;
import io.github.retrooper.packetevents.utils.netty.ChannelInboundHandlerUtil;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelAbstract;
import io.github.retrooper.packetevents.utils.netty.channel.ChannelHandlerContextAbstract;
import io.github.retrooper.packetevents.utils.reflection.ReflectionObject;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.spigotmc.SpigotConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;

public final class ServerManager {
    //Initialized in PacketEvents#load
    public Map<Integer, Entity> entityCache;
    private static final ServerVersion version;

    static {
        version = ServerVersion.resolve();
    }

    /**
     * Get the server version.
     *
     * @return Get Server Version
     */
    public static ServerVersion getVersion() {
        return version;
    }

    /**
     * Get recent TPS array from NMS.
     *
     * @return Get Recent TPS
     */
    public double[] getRecentTPS() {
        return MinecraftReflection.recentTPS();
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

    public boolean isBungeeCordEnabled() {
        return SpigotConfig.bungee;
    }

    public BoundingBox getEntityBoundingBox(Entity entity) {
        Object nmsEntity = MinecraftReflection.getNMSEntity(entity);
        Object aabb = MinecraftReflection.getNMSAxisAlignedBoundingBox(nmsEntity);
        ReflectionObject wrappedBoundingBox = new ReflectionObject(aabb);
        double minX = wrappedBoundingBox.readDouble(0);
        double minY = wrappedBoundingBox.readDouble(1);
        double minZ = wrappedBoundingBox.readDouble(2);
        double maxX = wrappedBoundingBox.readDouble(3);
        double maxY = wrappedBoundingBox.readDouble(4);
        double maxZ = wrappedBoundingBox.readDouble(5);
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void receivePacket(ChannelAbstract channel, ByteBufAbstract byteBuf) {
        //TODO Test with Via present
        ChannelHandlerContextAbstract ctx = channel.pipeline().context(PacketEvents.get().decoderName);
        ChannelInboundHandlerUtil.handlerChannelRead(ctx.handler().rawChannelHandler(),
                ctx.rawChannelHandlerContext(), byteBuf.rawByteBuf());
    }

    public void receivePacket(Player player, ByteBufAbstract byteBuf) {
        ChannelAbstract channel = ChannelAbstract.generate(PacketEvents.get().getPlayerManager().getChannel(player));
        receivePacket(channel, byteBuf);
    }

    public void receivePacket(Player player, PacketWrapper<?> wrapper) {
        wrapper.createPacket();
        ChannelAbstract channel = ChannelAbstract.generate(PacketEvents.get().getPlayerManager().getChannel(player));
        receivePacket(channel, wrapper.byteBuf);
    }

    public void receivePacket(ChannelAbstract channel, PacketWrapper<?> wrapper) {
        wrapper.createPacket();
        receivePacket(channel, wrapper.byteBuf);
    }

    private Entity getEntityByIDWithWorldUnsafe(World world, int id) {
        if (world == null) {
            return null;
        }

        Object craftWorld = MinecraftReflection.CRAFT_WORLD_CLASS.cast(world);

        try {
            Object worldServer = MinecraftReflection.GET_CRAFT_WORLD_HANDLE_METHOD.invoke(craftWorld);
            Object nmsEntity = MinecraftReflection.GET_ENTITY_BY_ID_METHOD.invoke(worldServer, id);
            if (nmsEntity == null) {
                return null;
            }
            return MinecraftReflection.getBukkitEntity(nmsEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private Entity getEntityByIDUnsafe(World origin, int id) {
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
    public Entity getEntityByID(@Nullable World world, int entityID) {
        Entity e = entityCache.get(entityID);
        if (e != null) {
            return e;
        }

        if (MinecraftReflection.V_1_17_OR_HIGHER) {
            try {
                if (world != null) {
                    for (Entity entity : getEntityList(world)) {
                        if (entity.getEntityId() == entityID) {
                            entityCache.putIfAbsent(entity.getEntityId(), entity);
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
                            entityCache.putIfAbsent(entity.getEntityId(), entity);
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
    public Entity getEntityByID(int entityID) {
        return getEntityByID(null, entityID);
    }

    public List<Entity> getEntityList(World world) {
        if (MinecraftReflection.V_1_17_OR_HIGHER) {
            Object worldServer = MinecraftReflection.convertBukkitWorldToWorldServer(world);
            ReflectionObject wrappedWorldServer = new ReflectionObject(worldServer);
            Object persistentEntitySectionManager = wrappedWorldServer.readObject(0, MinecraftReflection.PERSISTENT_ENTITY_SECTION_MANAGER_CLASS);
            ReflectionObject wrappedPersistentEntitySectionManager = new ReflectionObject(persistentEntitySectionManager);
            Object levelEntityGetter = wrappedPersistentEntitySectionManager.readObject(0, MinecraftReflection.LEVEL_ENTITY_GETTER_CLASS);
            Iterable<Object> nmsEntitiesIterable = null;
            try {
                nmsEntitiesIterable = (Iterable<Object>) MinecraftReflection.GET_LEVEL_ENTITY_GETTER_ITERABLE_METHOD.invoke(levelEntityGetter);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            List<Entity> entityList = new ArrayList<>();
            if (nmsEntitiesIterable != null) {
                for (Object nmsEntity : nmsEntitiesIterable) {
                    Entity bukkitEntity = MinecraftReflection.getBukkitEntity(nmsEntity);
                    entityList.add(bukkitEntity);
                }
            }
            return entityList;
        } else {
            return world.getEntities();
        }
    }


    public boolean isGeyserAvailable() {
        return MinecraftReflection.GEYSER_CLASS != null;
    }
}
