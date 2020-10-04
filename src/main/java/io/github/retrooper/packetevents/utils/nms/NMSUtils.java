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

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.nettyhandler.NettyPacketHandler;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.entityfinder.EntityFinderUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
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
    public static ServerVersion version;
    private static final String nmsDir = ServerVersion.getNMSDirectory();
    private static final String obcDir = ServerVersion.getOBCDirectory();
    public static String nettyPrefix = "io.netty";
    public static Class<?> nmsEntityClass, minecraftServerClass, craftWorldClass, playerInteractManagerClass, entityPlayerClass, playerConnectionClass, craftServerClass,
            craftPlayerClass, serverConnectionClass, craftEntityClass,
            craftItemStack, nmsItemStackClass, networkManagerClass, nettyChannelClass;
    private static Method craftWorldGetHandle,getCraftWorldHandleMethod, getServerConnection, getCraftPlayerHandle, getCraftEntityHandle, asBukkitCopy;
    private static Field entityPlayerPingField, playerConnectionField;

    public static final HashMap<UUID, Object> channelCache = new HashMap<>();

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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //METHODS
        try {
            getCraftWorldHandleMethod = craftWorldClass.getMethod("getHandle");
            getCraftPlayerHandle = craftPlayerClass.getMethod("getHandle");
            getCraftEntityHandle = craftEntityClass.getMethod("getHandle");
            getServerConnection = minecraftServerClass.getMethod("getServerConnection");
            asBukkitCopy = craftItemStack.getMethod("asBukkitCopy", nmsItemStackClass);
            craftWorldGetHandle = craftWorldClass.getMethod("getHandle");
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

    public static Object getNMSEntityById(final int id) {
        return EntityFinderUtils.getNMSEntityById(id);
    }

    public static Object getNMSEntityByIdWithWorld(final World world, final int id) {
        return EntityFinderUtils.getNMSEntityByIdWithWorld(world, id);
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
        try {
            return Reflection.getField(playerConnectionClass, networkManagerClass, 0).get(getPlayerConnection(player));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getChannel(final Player player) {
        UUID uuid = player.getUniqueId();
        if (!channelCache.containsKey(uuid)) {
            channelCache.put(uuid, getChannelNoCache(player));
        }
        return channelCache.get(uuid);
    }

    public static Object getChannelNoCache(final Player player) {
        try {
            return Reflection.getField(networkManagerClass, nettyChannelClass, 0).get(getNetworkManager(player));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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

        Object worldServer = null;
        try {
            worldServer = craftWorldGetHandle.invoke(craftWorld);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return worldServer;
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

    public static void sendSendableWrapper(final Player player, final SendableWrapper sendableWrapper) {
        NMSUtils.sendNMSPacket(player, sendableWrapper.asNMSPacket());
    }

    public static void sendNMSPacket(final Player player, final Object nmsPacket) {
        NettyPacketHandler.sendPacket(getChannel(player), nmsPacket);
    }
}
