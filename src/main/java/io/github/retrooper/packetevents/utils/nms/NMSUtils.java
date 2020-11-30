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

package io.github.retrooper.packetevents.utils.nms;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.entityfinder.EntityFinderUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

public final class NMSUtils {
    public static final HashMap<UUID, Object> channelCache = new HashMap<>();
    private static final String nmsDir = ServerVersion.getNMSDirectory();
    private static final String obcDir = ServerVersion.getOBCDirectory();
    public static ServerVersion version;
    public static String nettyPrefix = "io.netty";
    public static Class<?> nmsEntityClass, minecraftServerClass, craftWorldClass, playerInteractManagerClass, entityPlayerClass, playerConnectionClass, craftServerClass,
            craftPlayerClass, serverConnectionClass, craftEntityClass,
            craftItemStack, nmsItemStackClass, networkManagerClass, nettyChannelClass, gameProfileClass, iChatBaseComponentClass,
            blockPosClass, enumDirectionClass;
    private static Method craftWorldGetHandle;
    private static Method getCraftPlayerHandle;
    private static Method getCraftEntityHandle;
    private static Method asBukkitCopy;
    private static Method getBukkitEntity;
    private static Field entityPlayerPingField, playerConnectionField;

    public static void load() {
        try {
            Class.forName(nettyPrefix + ".channel.Channel");
        } catch (ClassNotFoundException e) {
            nettyPrefix = "net.minecraft.util.io.netty";
            try {
                Class.forName(nettyPrefix + ".channel.Channel");
            } catch (ClassNotFoundException e2) {
                throw new IllegalStateException("PacketEvents failed to locate Netty's location.");
            }
        }
        try {
            nettyChannelClass = getNettyClass("channel.Channel");
            nmsEntityClass = getNMSClass("Entity");
            minecraftServerClass = getNMSClass("MinecraftServer");
            craftWorldClass = getOBCClass("CraftWorld");
            craftPlayerClass = getOBCClass("entity.CraftPlayer");
            craftServerClass = getOBCClass("CraftServer");
            entityPlayerClass = getNMSClass("EntityPlayer");
            playerConnectionClass = getNMSClass("PlayerConnection");
            serverConnectionClass = getNMSClass("ServerConnection");
            craftEntityClass = getOBCClass("entity.CraftEntity");
            craftItemStack = getOBCClass("inventory.CraftItemStack");
            nmsItemStackClass = getNMSClass("ItemStack");
            networkManagerClass = getNMSClass("NetworkManager");
            playerInteractManagerClass = getNMSClass("PlayerInteractManager");
            try {
                gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
            } catch (ClassNotFoundException e) {
                gameProfileClass = Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
            }
            iChatBaseComponentClass = NMSUtils.getNMSClass("IChatBaseComponent");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        blockPosClass = NMSUtils.getNMSClassWithoutException("BlockPosition");
        enumDirectionClass = NMSUtils.getNMSClassWithoutException("EnumDirection");
        //METHODS
        try {
            getCraftPlayerHandle = craftPlayerClass.getMethod("getHandle");
            getCraftEntityHandle = craftEntityClass.getMethod("getHandle");
            asBukkitCopy = craftItemStack.getMethod("asBukkitCopy", nmsItemStackClass);
            craftWorldGetHandle = craftWorldClass.getMethod("getHandle");
            getBukkitEntity = nmsEntityClass.getMethod("getBukkitEntity");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        try {
            entityPlayerPingField = entityPlayerClass.getField("ping");
            playerConnectionField = entityPlayerClass.getField("playerConnection");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static Object getMinecraftServerInstance() throws InvocationTargetException, IllegalAccessException {
        return Reflection.getMethod(minecraftServerClass, "getServer", 0).invoke(null);
    }

    public static Object getMinecraftServerConnection() {
        WrappedPacket wrapper = null;
        try {
            wrapper = new WrappedPacket(getMinecraftServerInstance());
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (wrapper == null) {
            return null;
        }
        return wrapper.readObject(0, serverConnectionClass);
    }

    public static double[] recentTPS() throws IllegalAccessException, InvocationTargetException {
        final Object minecraftServerObj = getMinecraftServerInstance();
        return (double[]) Reflection.getField(minecraftServerClass, double[].class, 0).get(minecraftServerObj);
    }

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName(nmsDir + "." + name);
    }

    public static Class<?> getNMSClassWithoutException(String name) {
        try {
            return getNMSClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }

    }

    public static Class<?> getOBCClass(String name) throws ClassNotFoundException {
        return Class.forName(obcDir + "." + name);
    }

    public static Class<?> getNettyClass(String name) throws ClassNotFoundException {
        return Class.forName(nettyPrefix + "." + name);
    }

    public static String getChannelFutureListFieldName() {
        if (version.isLowerThan(ServerVersion.v_1_8)) {
            return "e";
        }
        if (version.isLowerThan(ServerVersion.v_1_13)) {
            return "g";
        }
        return "f";
    }

    @Nullable
    public static Entity getEntityById(final int id) {
        return EntityFinderUtils.getEntityById(id);
    }

    @Nullable
    public static Entity getEntityByIdWithWorld(final World world, final int id) {
        return EntityFinderUtils.getEntityByIdWithWorld(world, id);
    }

    public static Object getNMSEntity(final Entity entity) {
        final Object craftEntity = craftEntityClass.cast(entity);
        try {
            return getCraftEntityHandle.invoke(craftEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Entity getBukkitEntity(final Object nmsEntity) {
        try {
            return (Entity) getBukkitEntity.invoke(nmsEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getCraftPlayer(final Player player) {
        return craftPlayerClass.cast(player);
    }

    public static Object getEntityPlayer(final Player player) {
        try {
            return getCraftPlayerHandle.invoke(getCraftPlayer(player));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getPlayerConnection(final Player player) {
        try {
            return playerConnectionField.get(getEntityPlayer(player));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getNetworkManager(final Player player) {
        WrappedPacket wrapper = new WrappedPacket(getPlayerConnection(player));
        return wrapper.readObject(0, networkManagerClass);
    }

    public static Object getChannel(final Player player) {
        if (PacketEvents.get().packetManager.tinyProtocol == null) {
            UUID uuid = player.getUniqueId();
            Object channel = channelCache.get(uuid);
            if (channel == null) {
                Object newChannel = getChannelNoCache(player);
                channelCache.put(uuid, newChannel);
                return newChannel;
            }
            return channel;
        } else {
            return PacketEvents.get().packetManager.tinyProtocol.getChannel(player);
        }
    }

    public static Object getChannelNoCache(final Player player) {
        if (PacketEvents.get().packetManager.tinyProtocol == null) {
            WrappedPacket wrapper = new WrappedPacket(getNetworkManager(player));
            return wrapper.readObject(0, nettyChannelClass);
        } else {
            return PacketEvents.get().packetManager.tinyProtocol.getChannel(player);
        }
    }

    public static int getPlayerPing(final Player player) {
        Object entityPlayer = getEntityPlayer(player);
        try {
            return entityPlayerPingField.getInt(entityPlayer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static ItemStack toBukkitItemStack(final Object nmsItemstack) {
        try {
            return (ItemStack) asBukkitCopy.invoke(null, nmsItemstack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object convertBukkitServerToNMSServer(Server server) {
        Object craftServer = craftServerClass.cast(server);
        WrappedPacket wrapper = new WrappedPacket(craftServer);
        return wrapper.readObject(0, minecraftServerClass);
    }

    public static Object convertBukkitWorldToNMSWorld(World world) {
        Object craftWorld = craftWorldClass.cast(world);

        try {
            return craftWorldGetHandle.invoke(craftWorld);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object createNewEntityPlayer(Server server, World world, Object gameProfile) {
        Object nmsServer = convertBukkitServerToNMSServer(server);
        Object nmsWorld = convertBukkitWorldToNMSWorld(world);
        return privateCreateNewEntityPlayer(nmsServer, nmsWorld, gameProfile);
    }

    private static Object privateCreateNewEntityPlayer(Object nmsServer, Object nmsWorld, Object gameProfile) {
        Object playerInteractManager = null;
        try {
            playerInteractManager = playerInteractManagerClass.getConstructor(nmsWorld.getClass()).newInstance(nmsWorld);
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            return entityPlayerClass.getConstructor(nmsServer.getClass(), nmsWorld.getClass(),
                    gameProfile.getClass(), playerConnectionClass).
                    newInstance(nmsServer, nmsWorld, gameProfile, playerInteractManager);
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
