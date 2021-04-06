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
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public final class NMSUtils {
    private static final String NMS_DIR = ServerVersion.getNMSDirectory() + ".";
    private static final String OBC_DIR = ServerVersion.getOBCDirectory() + ".";
    public static boolean legacyNettyImportMode;
    public static ServerVersion version;
    public static Constructor<?> blockPosConstructor, minecraftKeyConstructor, vec3DConstructor, dataWatcherConstructor;
    public static Class<?> nmsEntityClass, minecraftServerClass, craftWorldClass, playerInteractManagerClass, entityPlayerClass, playerConnectionClass, craftServerClass,
            craftPlayerClass, serverConnectionClass, craftEntityClass, nmsItemStackClass, networkManagerClass, nettyChannelClass, gameProfileClass, iChatBaseComponentClass,
            blockPosClass, vec3DClass, channelFutureClass, blockClass, iBlockDataClass, nmsWorldClass, craftItemStackClass,
            soundEffectClass, minecraftKeyClass, chatSerializerClass, craftMagicNumbersClass, worldSettingsClass, worldServerClass, dataWatcherClass, nmsEntityHumanClass, dedicatedServerClass, entityHumanClass, packetDataSerializerClass;
    public static Class<? extends Enum<?>> enumDirectionClass, enumHandClass, enumGameModeClass;
    public static Method getBlockPosX, getBlockPosY, getBlockPosZ;
    private static String nettyPrefix;
    private static Method getCraftPlayerHandle;
    private static Method getCraftEntityHandle;
    private static Method getCraftWorldHandle;
    private static Method asBukkitCopy;
    private static Method asNMSCopy;
    private static Method getMessageMethod;
    private static Method chatFromStringMethod;
    private static Method getMaterialFromNMSBlock;
    private static Method getNMSBlockFromMaterial;
    private static Field entityPlayerPingField, playerConnectionField;
    private static Object minecraftServer;
    private static Object minecraftServerConnection;

    public static void load() {
        String legacyNettyPrefix = "net.minecraft.util.io.netty.";
        String newNettyPrefix = "io.netty.";
        if (version.isNewerThan(ServerVersion.v_1_7_10)) {
            legacyNettyImportMode = false;
            nettyPrefix = newNettyPrefix;
        } else {
            legacyNettyImportMode = true;
            nettyPrefix = legacyNettyPrefix;
        }

        try {
            //Test if the selected netty location is valid
            Object chnl = getNettyClass("channel.Channel");
        } catch (Exception ex) {
            System.err.println("[packetevents] Failed to locate the netty package location for your server version. Searching...");
            //Time to correct the netty location
            if (legacyNettyImportMode) {
                legacyNettyImportMode = false;
                nettyPrefix = newNettyPrefix;
            } else {
                legacyNettyImportMode = true;
                nettyPrefix = legacyNettyPrefix;
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
            entityHumanClass = getNMSClass("EntityHuman");
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
            soundEffectClass = getNMSClassWithoutException("SoundEffect");
            minecraftKeyClass = getNMSClassWithoutException("MinecraftKey");
            worldServerClass = getNMSClassWithoutException("WorldServer");
            dataWatcherClass = getNMSClassWithoutException("DataWatcher");
            nmsEntityHumanClass = getNMSClassWithoutException("EntityHuman");
            dedicatedServerClass = getNMSClassWithoutException("DedicatedServer");
            packetDataSerializerClass = getNMSClassWithoutException("PacketDataSerializer");
            try {
                gameProfileClass = Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
            } catch (ClassNotFoundException e) {
                gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
            }
            iChatBaseComponentClass = NMSUtils.getNMSClass("IChatBaseComponent");
            vec3DClass = NMSUtils.getNMSClass("Vec3D");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (version.isNewerThan(ServerVersion.v_1_7_10)) {
            blockPosClass = NMSUtils.getNMSClassWithoutException("BlockPosition");
        }
        try {
            //If null, it is 1.7.10
            if (blockPosClass != null) {
                blockPosConstructor = blockPosClass.getConstructor(double.class, double.class, double.class);
            }
            if (vec3DClass != null) {
                vec3DConstructor = NMSUtils.vec3DClass.getDeclaredConstructor(double.class, double.class, double.class);
                vec3DConstructor.setAccessible(true);
            }

            if (dataWatcherClass != null) {
                dataWatcherConstructor = dataWatcherClass.getConstructor(nmsEntityClass);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        enumDirectionClass = NMSUtils.getNMSEnumClassWithoutException("EnumDirection");
        //METHODS
        try {
            getCraftPlayerHandle = craftPlayerClass.getMethod("getHandle");
            getCraftEntityHandle = craftEntityClass.getMethod("getHandle");
            getCraftWorldHandle = craftWorldClass.getMethod("getHandle");
            asBukkitCopy = craftItemStackClass.getMethod("asBukkitCopy", nmsItemStackClass);
            asNMSCopy = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
            getMessageMethod = Reflection.getMethodCheckContainsString(iChatBaseComponentClass, "c", String.class);
            if (getMessageMethod == null) {
                getMessageMethod = Reflection.getMethodCheckContainsString(iChatBaseComponentClass, "Plain", String.class);
            }

            //In 1.8.3+ the ChatSerializer class is declared in the IChatBaseComponent class, so we have to handle that
            try {
                chatSerializerClass = NMSUtils.getNMSClass("ChatSerializer");
            } catch (ClassNotFoundException e) {
                //That is fine, it is probably a subclass
                chatSerializerClass = SubclassUtil.getSubClass(iChatBaseComponentClass, "ChatSerializer");
            }

            craftMagicNumbersClass = NMSUtils.getOBCClass("util.CraftMagicNumbers");

            chatFromStringMethod = Reflection.getMethod(chatSerializerClass, 0, String.class);

            getMaterialFromNMSBlock = Reflection.getMethod(craftMagicNumbersClass, "getMaterial", Material.class, NMSUtils.blockClass);
            getNMSBlockFromMaterial = Reflection.getMethod(craftMagicNumbersClass, "getBlock", NMSUtils.blockClass, Material.class);

            if (minecraftKeyClass != null) {
                minecraftKeyConstructor = minecraftKeyClass.getConstructor(String.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            entityPlayerPingField = entityPlayerClass.getField("ping");
            playerConnectionField = entityPlayerClass.getField("playerConnection");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        //Incase its null, these methods are not needed and would cause errors
        if (blockPosClass != null) {
            getBlockPosX = Reflection.getMethod(NMSUtils.blockPosClass.getSuperclass(), "getX", 0);
            getBlockPosY = Reflection.getMethod(NMSUtils.blockPosClass.getSuperclass(), "getY", 0);
            getBlockPosZ = Reflection.getMethod(NMSUtils.blockPosClass.getSuperclass(), "getZ", 0);
        }
        worldSettingsClass = NMSUtils.getNMSClassWithoutException("WorldSettings");

        enumHandClass = getNMSEnumClassWithoutException("EnumHand");
        enumGameModeClass = NMSUtils.getNMSEnumClassWithoutException("EnumGamemode");
        if (enumGameModeClass == null) {
            enumGameModeClass = SubclassUtil.getEnumSubClass(worldSettingsClass, "EnumGamemode");
        }
    }

    public static Object getMinecraftServerInstance(Server server) {
        if (minecraftServer == null) {
            try {
                minecraftServer = Reflection.getField(craftServerClass, minecraftServerClass, 0)
                        .get(server);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return minecraftServer;
    }

    public static Object getMinecraftServerConnection() {
        if (minecraftServerConnection == null) {
            try {
                minecraftServerConnection = Reflection.getField(minecraftServerClass, serverConnectionClass, 0).get(getMinecraftServerInstance(Bukkit.getServer()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return minecraftServerConnection;
    }

    public static double[] recentTPS() {
        return new WrappedPacket(new NMSPacket(getMinecraftServerInstance(Bukkit.getServer())), minecraftServerClass).readDoubleArray(0);
    }

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName(NMS_DIR + name);
    }

    public static Class<? extends Enum<?>> getNMSEnumClass(String name) throws ClassNotFoundException {
        return (Class<? extends Enum<?>>) Class.forName(NMS_DIR + name);
    }

    public static Class<? extends Enum<?>> getNMSEnumClassWithoutException(String name) {
        try {
            return (Class<? extends Enum<?>>) getNMSClass(name);
        } catch (Exception e) {
            return null;
        }

    }

    public static Class<?> getNMSClassWithoutException(String name) {
        try {
            return getNMSClass(name);
        } catch (Exception e) {
            return null;
        }

    }

    public static Class<?> getOBCClass(String name) throws ClassNotFoundException {
        return Class.forName(OBC_DIR + name);
    }

    public static Class<?> getNettyClass(String name) throws ClassNotFoundException {
        return Class.forName(nettyPrefix + name);
    }

    @Nullable
    public static Entity getEntityById(@Nullable World world, int id) {
        try {
            Entity entity = EntityFinderUtils.getEntityById(world, id);
            if (entity == null && world != null) {
                List<World> worlds = new ArrayList<>(Bukkit.getWorlds());
                for (World wrld : worlds) {
                    for (Entity e : wrld.getEntities()) {
                        if (e.getEntityId() == id) {
                            entity = e;
                            break;
                        }
                    }
                }
            }
            return entity;
        } catch (Exception ex) {
            return null;
        }
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
        Object craftPlayer = getCraftPlayer(player);
        try {
            return getCraftPlayerHandle.invoke(craftPlayer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getPlayerConnection(final Player player) {
        Object entityPlayer = getEntityPlayer(player);
        if (entityPlayer == null) {
            return null;
        }
        try {
            return playerConnectionField.get(entityPlayer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getNetworkManager(final Player player) {
        Object playerConnection = getPlayerConnection(player);
        if (playerConnection == null) {
            return null;
        }
        WrappedPacket wrapper = new WrappedPacket(new NMSPacket(playerConnection), playerConnectionClass);
        try {
            return wrapper.readObject(0, networkManagerClass);
        }
        catch (Exception ex) {
            //Support for some custom plugins.
            playerConnection = wrapper.read(0, playerConnectionClass);
            wrapper = new WrappedPacket(new NMSPacket(playerConnection), playerConnectionClass);
            return wrapper.readObject(0, networkManagerClass);
        }
    }

    public static Object getChannel(final Player player) {
        Object networkManager = getNetworkManager(player);
        if (networkManager == null) {
            return null;
        }
        WrappedPacket wrapper = new WrappedPacket(new NMSPacket(networkManager));
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

    public static List<Object> getNetworkManagers() {
        WrappedPacket serverConnectionWrapper = new WrappedPacket(new NMSPacket(getMinecraftServerConnection()));
        for (int i = 0; true; i++) {
            try {
                List<?> list = (List<?>) serverConnectionWrapper.readObject(i, List.class);
                for (Object obj : list) {
                    if (obj.getClass().isAssignableFrom(networkManagerClass)) {
                        return (List<Object>) list;
                    }
                }
            } catch (Exception ex) {
                break;
            }
        }

        return (List<Object>) serverConnectionWrapper.readObject(1, List.class);
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
        try {
            return wrapper.readObject(0, minecraftServerClass);
        } catch (Exception ex) {
            wrapper.readObject(0, dedicatedServerClass);
        }
        return null;
    }

    public static Object convertBukkitWorldToWorldServer(World world) {
        Object craftWorld = craftWorldClass.cast(world);
        try {
            return getCraftWorldHandle.invoke(craftWorld);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Object generateIChatBaseComponent(String text) {
        if (text == null) {
            return null;
        }
        try {
            return chatFromStringMethod.invoke(null, text);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Object[] generateIChatBaseComponents(String... texts) {
        Object[] components = new Object[texts.length];
        for (int i = 0; i < components.length; i++) {
            components[i] = generateIChatBaseComponent(texts[i]);
        }
        return components;
    }

    @Nullable
    public static String readIChatBaseComponent(Object iChatBaseComponent) {
        if (iChatBaseComponent == null) {
            return null;
        }
        try {
            return getMessageMethod.invoke(iChatBaseComponent).toString();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] readIChatBaseComponents(Object... components) {
        String[] texts = new String[components.length];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = readIChatBaseComponent(components[i]);
        }
        return texts;
    }

    public static Vector3i readBlockPos(Object blockPos) {
        try {
            return new Vector3i((int) getBlockPosX.invoke(blockPos), (int) getBlockPosY.invoke(blockPos), (int) getBlockPosZ.invoke(blockPos));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String fromStringToJSON(String message) {
        if (message == null) {
            return null;
        }
        return "{\"text\": \"" + message + "\"}";
    }

    public static Object generateNMSBlockPos(double x, double y, double z) {
        try {
            return blockPosConstructor.newInstance(x, y, z);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStringFromMinecraftKey(Object minecraftKey) {
        WrappedPacket minecraftKeyWrapper = new WrappedPacket(new NMSPacket(minecraftKey));
        return minecraftKeyWrapper.readString(1);
    }

    public static Object generateMinecraftKey(String text) {
        try {
            return minecraftKeyConstructor.newInstance(text);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object generateVec3D(double x, double y, double z) {
        try {
            return vec3DConstructor.newInstance(x, y, z);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Material getMaterialFromNMSBlock(Object nmsBlock) {
        try {
            return (Material) getMaterialFromNMSBlock.invoke(null, nmsBlock);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getNMSBlockFromMaterial(Material material) {
        try {
            return getNMSBlockFromMaterial.invoke(null, material);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object generateDataWatcher(Entity entity) {
        Object nmsEntity = null;
        try {
            return dataWatcherConstructor.newInstance(nmsEntity);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final ThreadLocal<Random> randomThreadLocal = ThreadLocal.withInitial(Random::new);

    public static UUID generateUUID() {
        long var1 = randomThreadLocal.get().nextLong() & -61441L | 16384L;
        long var3 = randomThreadLocal.get().nextLong() & 4611686018427387903L | -9223372036854775808L;
        return new UUID(var1, var3);
    }

    public static int generateEntityId() {
        Field field = Reflection.getField(nmsEntityClass, "entityCount");
        try {
            if (field.getType().equals(AtomicInteger.class)) {
                //Newer versions
                AtomicInteger atomicInteger = (AtomicInteger) field.get(null);
                return atomicInteger.incrementAndGet();
            } else {
                int id = field.getInt(null) + 1;
                field.set(null, id);
                return id;
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
        throw new IllegalStateException("Failed to generate a new unique entity ID!");
    }
}
