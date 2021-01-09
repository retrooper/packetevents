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

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.entityfinder.EntityFinderUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public final class NMSUtils {
    public static boolean legacyNettyImportMode;
 
    private static final String NMS_DIR = ServerVersion.getNMSDirectory() + ".";
    private static final String OBC_DIR = ServerVersion.getOBCDirectory() + ".";
    public static ServerVersion version;
    private static String nettyPrefix = "net.minecraft.util.io.netty.";
    public static Constructor<?> blockPosConstructor;
    public static Class<?> nmsEntityClass, minecraftServerClass, craftWorldClass, playerInteractManagerClass, entityPlayerClass, playerConnectionClass, craftServerClass,
            craftPlayerClass, serverConnectionClass, craftEntityClass, nmsItemStackClass, networkManagerClass, nettyChannelClass, gameProfileClass, iChatBaseComponentClass,
            blockPosClass, enumDirectionClass, vec3DClass, channelFutureClass, blockClass, iBlockDataClass, watchableObjectClass, nmsWorldClass, craftItemStackClass;
    private static Method getCraftPlayerHandle;
    private static Method getCraftEntityHandle;
    private static Method getCraftWorldHandle;
    private static Method asBukkitCopy;
    private static Method asNMSCopy;
    private static Field entityPlayerPingField, playerConnectionField;
    private static Object minecraftServer;
    private static Object minecraftServerConnection;

    public static void load() {
        try {
            Class.forName("net.minecraft.util.io.netty.channel.Channel");
            legacyNettyImportMode = true;
        } catch (ClassNotFoundException e) {
            legacyNettyImportMode = false;
        }
        try {
            Class.forName(nettyPrefix + "channel.Channel");
        } catch (ClassNotFoundException e) {
            nettyPrefix = "io.netty.";
            try {
                Class.forName(nettyPrefix + "channel.Channel");
            } catch (ClassNotFoundException e2) {
                throw new IllegalStateException("PacketEvents failed to locate Netty's location.");
            }
        }
        try {
            nettyChannelClass = getNettyClass("channel.Channel");
            channelFutureClass = getNettyClass("channel.ChannelFuture");
            nmsEntityClass = getNMSClass("Entity");
            minecraftServerClass = getNMSClass("MinecraftServer");
            craftWorldClass = getOBCClass("CraftWorld");
            craftPlayerClass = getOBCClass("entity.CraftPlayer");
            craftServerClass = getOBCClass("CraftServer");
            entityPlayerClass = getNMSClass("EntityPlayer");
            playerConnectionClass = getNMSClass("PlayerConnection");
            serverConnectionClass = getNMSClass("ServerConnection");
            craftEntityClass = getOBCClass("entity.CraftEntity");
            craftItemStackClass = getOBCClass("inventory.CraftItemStack");
            nmsItemStackClass = getNMSClass("ItemStack");
            networkManagerClass = getNMSClass("NetworkManager");
            playerInteractManagerClass = getNMSClass("PlayerInteractManager");
            blockClass = getNMSClass("Block");
            //IBlockData doesn't exist on 1.7.10
            iBlockDataClass = getNMSClassWithoutException("IBlockData");
            nmsWorldClass = getNMSClass("World");
            try {
                watchableObjectClass = getNMSClass("WatchableObject");
            } catch (Exception ex) {
                try {
                    Class<?> dataWatcher = getNMSClass("DataWatcher");
                    watchableObjectClass = SubclassUtil.getSubClass(dataWatcher, 0);
                } catch (Exception ex2) {
                    //1.9+
                    //ignore
                }
            }
            try {
                gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
            } catch (ClassNotFoundException e) {
                gameProfileClass = Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
            }
            iChatBaseComponentClass = NMSUtils.getNMSClass("IChatBaseComponent");
            vec3DClass = NMSUtils.getNMSClass("Vec3D");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        blockPosClass = NMSUtils.getNMSClassWithoutException("BlockPosition");
        try {
            //If null, it is 1.7.10
            if (blockPosClass != null) {
                blockPosConstructor = blockPosClass.getConstructor(double.class, double.class, double.class);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        enumDirectionClass = NMSUtils.getNMSClassWithoutException("EnumDirection");
        //METHODS
        try {
            getCraftPlayerHandle = craftPlayerClass.getMethod("getHandle");
            getCraftEntityHandle = craftEntityClass.getMethod("getHandle");
            getCraftWorldHandle = craftWorldClass.getMethod("getHandle");
            asBukkitCopy = craftItemStackClass.getMethod("asBukkitCopy", nmsItemStackClass);
            asNMSCopy = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
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

    public static Object getMinecraftServerInstance() {
        if (minecraftServer == null) {
            try {
                minecraftServer = Reflection.getField(craftServerClass, minecraftServerClass, 0)
                        .get(Bukkit.getServer());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return minecraftServer;
    }

    public static Object getMinecraftServerConnection() {
        if (minecraftServerConnection == null) {
            try {
                minecraftServerConnection = Reflection.getField(minecraftServerClass, serverConnectionClass, 0).get(getMinecraftServerInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return minecraftServerConnection;
    }

    public static double[] recentTPS() {
        return new WrappedPacket(new NMSPacket(getMinecraftServerInstance()), minecraftServerClass).readDoubleArray(0);
    }

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName(NMS_DIR + name);
    }

    public static Class<?> getNMSClassWithoutException(String name) {
        try {
            return getNMSClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }

    }

    public static Class<?> getOBCClass(String name) throws ClassNotFoundException {
        return Class.forName(OBC_DIR + name);
    }

    public static Class<?> getNettyClass(String name) throws ClassNotFoundException {
        return Class.forName(nettyPrefix + name);
    }

    public static Entity getEntityById(final int id) {
        Entity entity = EntityFinderUtils.getEntityById(id);
        if (entity == null) {
            for (World world : Bukkit.getWorlds()) {
                for (Entity e : world.getEntities()) {
                    if (e.getEntityId() == id) {
                        entity = e;
                        break;
                    }
                }
            }
        }
        return entity;
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
        WrappedPacket wrapper = new WrappedPacket(new NMSPacket(getPlayerConnection(player)));
        return wrapper.readObject(0, networkManagerClass);
    }

    public static Object getChannel(final Player player) {
        WrappedPacket wrapper = new WrappedPacket(new NMSPacket(getNetworkManager(player)));
        return wrapper.readObject(0, nettyChannelClass);
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

    public static List<Object> getNetworkMarkers() {
        Method method = Reflection.getMethod(serverConnectionClass, List.class, 0, serverConnectionClass);
        if (method != null) {
            try {
                return (List<Object>) method.invoke(null, getMinecraftServerConnection());
            } catch (Exception ignored) {

            }
        }
        WrappedPacket serverConnectionWrapper = new WrappedPacket(new NMSPacket(getMinecraftServerConnection()));
        for (int i = 0; true; i++) {
            try {
                List<Object> list = (List<Object>) serverConnectionWrapper.readObject(i, List.class);
                for (Object obj : list) {
                    if (!obj.getClass().isAssignableFrom(channelFutureClass)) {
                        return list;
                    }
                    break;
                }
            } catch (Exception e) {
                break;
            }
        }
        throw new IllegalStateException("Failed to locate the network managers!");
    }

    public static ItemStack toBukkitItemStack(final Object nmsItemStack) {
        try {
            return (ItemStack) asBukkitCopy.invoke(null, nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object toNMSItemStack(final ItemStack stack) {
        try {
            return asNMSCopy.invoke(null, stack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object convertBukkitServerToNMSServer(Server server) {
        Object craftServer = craftServerClass.cast(server);
        WrappedPacket wrapper = new WrappedPacket(new NMSPacket(craftServer));
        return wrapper.readObject(0, minecraftServerClass);
    }

    public static Object convertBukkitWorldToNMSWorld(World world) {
        Object craftWorld = craftWorldClass.cast(world);
        try {
            return getCraftWorldHandle.invoke(craftWorld);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object generateNMSBlockPos(double x, double y, double z) {
        try {
            return blockPosConstructor.newInstance(x, y, z);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
