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

package io.github.retrooper.packetevents.utils.nms;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.entityfinder.EntityFinderUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3d;
import io.github.retrooper.packetevents.utils.vector.Vector3f;
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
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public final class NMSUtils {
    private static boolean v_1_17;
    public static final String NMS_DIR = ServerVersion.getNMSDirectory() + ".";
    public static final String OBC_DIR = ServerVersion.getOBCDirectory() + ".";
    private static final ThreadLocal<Random> randomThreadLocal = ThreadLocal.withInitial(Random::new);
    public static boolean legacyNettyImportMode;
    public static ServerVersion version;
    public static Constructor<?> blockPosConstructor, minecraftKeyConstructor, vec3DConstructor, dataWatcherConstructor, packetDataSerializerConstructor;
    public static Class<?> mobEffectListClass, nmsEntityClass, minecraftServerClass, craftWorldClass, playerInteractManagerClass, entityPlayerClass, playerConnectionClass, craftServerClass,
            craftPlayerClass, serverConnectionClass, craftEntityClass, nmsItemStackClass, networkManagerClass, nettyChannelClass, gameProfileClass, iChatBaseComponentClass,
            blockPosClass, vec3DClass, channelFutureClass, blockClass, iBlockDataClass, nmsWorldClass, craftItemStackClass,
            soundEffectClass, minecraftKeyClass, chatSerializerClass, craftMagicNumbersClass, worldSettingsClass, worldServerClass, dataWatcherClass,
            dedicatedServerClass, entityHumanClass, packetDataSerializerClass, byteBufClass, dimensionManagerClass, nmsItemClass, movingObjectPositionBlock;
    public static Class<? extends Enum<?>> enumDirectionClass, enumHandClass, enumGameModeClass, enumDifficultyClass;
    public static Method getBlockPosX, getBlockPosY, getBlockPosZ;
    private static String nettyPrefix;
    private static Method getCraftPlayerHandle, getCraftEntityHandle, getCraftWorldHandle, asBukkitCopy,
            asNMSCopy, getMessageMethod, chatFromStringMethod, getMaterialFromNMSBlock, getNMSBlockFromMaterial,
            getMobEffectListId, getMobEffectListById, getItemId, getItemById, getBukkitEntity;
    private static Field entityPlayerPingField;
    private static Object minecraftServer;
    private static Object minecraftServerConnection;

    public static void load() {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_17)) {
            v_1_17 = true;
        }
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
            getNettyClass("channel.Channel");
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
            byteBufClass = getNettyClass("buffer.ByteBuf");
            nettyChannelClass = getNettyClass("channel.Channel");
            channelFutureClass = getNettyClass("channel.ChannelFuture");
            craftWorldClass = getOBCClass("CraftWorld");
            craftPlayerClass = getOBCClass("entity.CraftPlayer");
            craftServerClass = getOBCClass("CraftServer");
            craftEntityClass = getOBCClass("entity.CraftEntity");
            craftItemStackClass = getOBCClass("inventory.CraftItemStack");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        nmsEntityClass = getNMSClassWithoutException("Entity");
        if (nmsEntityClass == null) {
            nmsEntityClass = getNMClassWithoutException("world.entity.Entity");
        }

        if (nmsEntityClass != null) {
            getBukkitEntity = Reflection.getMethod(nmsEntityClass, craftEntityClass, 0);
        }
        minecraftServerClass = getNMSClassWithoutException("MinecraftServer");
        if (minecraftServerClass == null) {
            minecraftServerClass = getNMClassWithoutException("server.MinecraftServer");
        }
        entityPlayerClass = getNMSClassWithoutException("EntityPlayer");
        if (entityPlayerClass == null) {
            entityPlayerClass = getNMClassWithoutException("server.level.EntityPlayer");
        }
        entityHumanClass = getNMSClassWithoutException("EntityHuman");
        if (entityHumanClass == null) {
            entityHumanClass = getNMClassWithoutException("world.entity.player.EntityHuman");
        }
        playerConnectionClass = getNMSClassWithoutException("PlayerConnection");
        if (playerConnectionClass == null) {
            playerConnectionClass = getNMClassWithoutException("server.network.PlayerConnection");
        }
        serverConnectionClass = getNMSClassWithoutException("ServerConnection");
        if (serverConnectionClass == null) {
            serverConnectionClass = getNMClassWithoutException("server.network.ServerConnection");
        }
        nmsItemStackClass = getNMSClassWithoutException("ItemStack");
        if (nmsItemStackClass == null) {
            nmsItemStackClass = getNMClassWithoutException("world.item.ItemStack");
        }
        networkManagerClass = getNMSClassWithoutException("NetworkManager");
        if (networkManagerClass == null) {
            networkManagerClass = getNMClassWithoutException("network.NetworkManager");
        }
        mobEffectListClass = getNMSClassWithoutException("MobEffectList");
        if (mobEffectListClass == null) {
            mobEffectListClass = getNMClassWithoutException("world.effect.MobEffectList");
        }
        playerInteractManagerClass = getNMSClassWithoutException("PlayerInteractManager");
        if (playerInteractManagerClass == null) {
            playerInteractManagerClass = getNMClassWithoutException("server.level.PlayerInteractManager");
        }
        blockClass = getNMSClassWithoutException("Block");
        if (blockClass == null) {
            blockClass = getNMClassWithoutException("world.level.block.Block");
        }
        //IBlockData doesn't exist on 1.7.10
        iBlockDataClass = getNMSClassWithoutException("IBlockData");
        if (iBlockDataClass == null) {
            iBlockDataClass = getNMClassWithoutException("world.level.block.state.IBlockData");
        }
        nmsWorldClass = getNMSClassWithoutException("World");
        if (nmsWorldClass == null) {
            nmsWorldClass = getNMClassWithoutException("world.level.World");
        }
        soundEffectClass = getNMSClassWithoutException("SoundEffect");
        if (soundEffectClass == null) {
            soundEffectClass = getNMClassWithoutException("sounds.SoundEffect");
        }
        minecraftKeyClass = getNMSClassWithoutException("MinecraftKey");
        if (minecraftKeyClass == null) {
            minecraftKeyClass = getNMClassWithoutException("resources.MinecraftKey");
        }
        worldServerClass = getNMSClassWithoutException("WorldServer");
        if (worldServerClass == null) {
            worldServerClass = getNMClassWithoutException("server.level.WorldServer");
        }
        dataWatcherClass = getNMSClassWithoutException("DataWatcher");
        if (dataWatcherClass == null) {
            dataWatcherClass = getNMClassWithoutException("network.syncher.DataWatcher");
        }
        nmsItemClass = getNMSClassWithoutException("Item");
        if (nmsItemClass == null) {
            nmsItemClass = getNMClassWithoutException("world.item.Item");
        }
        dedicatedServerClass = getNMSClassWithoutException("DedicatedServer");
        if (dedicatedServerClass == null) {
            dedicatedServerClass = getNMClassWithoutException("server.dedicated.DedicatedServer");
        }
        packetDataSerializerClass = getNMSClassWithoutException("PacketDataSerializer");
        if (packetDataSerializerClass == null) {
            packetDataSerializerClass = getNMClassWithoutException("network.PacketDataSerializer");
        }
        if (packetDataSerializerClass != null) {
            try {
                packetDataSerializerConstructor = packetDataSerializerClass.getConstructor(NMSUtils.byteBufClass);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        dimensionManagerClass = NMSUtils.getNMSClassWithoutException("DimensionManager");
        if (dimensionManagerClass == null) {
            dimensionManagerClass = getNMClassWithoutException("world.level.dimension.DimensionManager");
        }
        try {
            gameProfileClass = Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
        } catch (ClassNotFoundException e) {
            try {
                gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            }
        }
        iChatBaseComponentClass = NMSUtils.getNMSClassWithoutException("IChatBaseComponent");
        if (iChatBaseComponentClass == null) {
            iChatBaseComponentClass = getNMClassWithoutException("network.chat.IChatBaseComponent");
        }
        vec3DClass = NMSUtils.getNMSClassWithoutException("Vec3D");
        if (vec3DClass == null) {
            vec3DClass = getNMClassWithoutException("world.phys.Vec3D");
        }

        if (version.isNewerThan(ServerVersion.v_1_7_10)) {
            blockPosClass = NMSUtils.getNMSClassWithoutException("BlockPosition");
            if (blockPosClass == null) {
                blockPosClass = getNMClassWithoutException("core.BlockPosition");
            }
        }

        if (version.isNewerThanOrEquals(ServerVersion.v_1_14)) {
            movingObjectPositionBlock = NMSUtils.getNMSClassWithoutException("MovingObjectPositionBlock");

            if (movingObjectPositionBlock == null) {
                movingObjectPositionBlock = getNMClassWithoutException("world.phys.MovingObjectPositionBlock");
            }
        }
        try {
            //If null, it is 1.7.10
            if (blockPosClass != null) {
                blockPosConstructor = blockPosClass.getConstructor(int.class, int.class, int.class);
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

        try {
            getItemId = NMSUtils.nmsItemClass.getMethod("getId", NMSUtils.nmsItemClass);
            getItemById = NMSUtils.nmsItemClass.getMethod("getById", int.class);
        } catch (Exception ex) {

        }


        enumDirectionClass = NMSUtils.getNMSEnumClassWithoutException("EnumDirection");
        if (enumDirectionClass == null) {
            enumDirectionClass = getNMEnumClassWithoutException("core.EnumDirection");
        }
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
                if (getMessageMethod == null) {
                    getMessageMethod = Reflection.getMethodCheckContainsString(iChatBaseComponentClass, "String", String.class);
                }
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
            if (mobEffectListClass != null) {
                getMobEffectListId = mobEffectListClass.getMethod("getId", mobEffectListClass);
                getMobEffectListById = mobEffectListClass.getMethod("fromId", int.class);
            }
        } catch (Exception ignored) {

        }
        try {
            entityPlayerPingField = entityPlayerClass.getField("ping");
        } catch (NoSuchFieldException e) {
            //Ignore on 1.17, we will use API (Player#getPing) to access ping
        }
        //In case its null, these methods are not needed and would cause errors
        if (blockPosClass != null) {
            getBlockPosX = Reflection.getMethod(NMSUtils.blockPosClass.getSuperclass(), "getX", 0);
            getBlockPosY = Reflection.getMethod(NMSUtils.blockPosClass.getSuperclass(), "getY", 0);
            getBlockPosZ = Reflection.getMethod(NMSUtils.blockPosClass.getSuperclass(), "getZ", 0);
        }
        worldSettingsClass = NMSUtils.getNMSClassWithoutException("WorldSettings");
        if (worldServerClass == null) {
            worldServerClass = getNMClassWithoutException("world.level.WorldSettings");
        }
        enumHandClass = getNMSEnumClassWithoutException("EnumHand");
        if (enumHandClass == null) {
            enumHandClass = getNMEnumClassWithoutException("world.EnumHand");
        }
        enumDifficultyClass = NMSUtils.getNMSEnumClassWithoutException("EnumDifficulty");
        if (enumDifficultyClass == null) {
            enumDifficultyClass = getNMEnumClassWithoutException("world.EnumDifficulty");
        }
        enumGameModeClass = NMSUtils.getNMSEnumClassWithoutException("EnumGamemode");

        if (enumGameModeClass == null) {
            enumGameModeClass = SubclassUtil.getEnumSubClass(worldSettingsClass, "EnumGamemode");
        }

        if (enumGameModeClass == null) {
            enumGameModeClass = getNMEnumClassWithoutException("world.level.EnumGamemode");
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

    public static Class<?> getNMClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft." + name);
    }

    public static Class<?> getNMClassWithoutException(String name) {
        try {
            return Class.forName("net.minecraft." + name);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public static Class<? extends Enum<?>> getNMEnumClassWithoutException(String name) {
        try {
            return (Class<? extends Enum<?>>) Class.forName("net.minecraft." + name);
        } catch (ClassNotFoundException ex) {
            return null;
        }
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
    @Deprecated
    public static Entity getEntityById(@Nullable World world, int id) {
       return PacketEvents.get().getServerUtils().getEntityById(world, id);
    }

    public static Entity getBukkitEntity(Object nmsEntity) {
        Object craftEntity = null;
        try {
            craftEntity = getBukkitEntity.invoke(nmsEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return (Entity) craftEntity;
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
        WrappedPacket wrappedEntityPlayer = new WrappedPacket(new NMSPacket(entityPlayer));
        return wrappedEntityPlayer.readObject(0, NMSUtils.playerConnectionClass);
    }

    public static Object getGameProfile(Player player) {
        Object entityPlayer = getEntityPlayer(player);
        WrappedPacket entityHumanWrapper = new WrappedPacket(new NMSPacket(entityPlayer), NMSUtils.entityHumanClass);
        return entityHumanWrapper.readObject(0, NMSUtils.gameProfileClass);
    }

    public static Object getNetworkManager(Player player) {
        Object playerConnection = getPlayerConnection(player);
        if (playerConnection == null) {
            return null;
        }
        WrappedPacket wrapper = new WrappedPacket(new NMSPacket(playerConnection), playerConnectionClass);
        try {
            return wrapper.readObject(0, networkManagerClass);
        } catch (Exception ex) {
            wrapper = new WrappedPacket(new NMSPacket(playerConnection));
            try {
                return wrapper.readObject(0, networkManagerClass);
            } catch (Exception ex2) {
                //Support for some custom plugins.
                playerConnection = wrapper.read(0, playerConnectionClass);
                wrapper = new WrappedPacket(new NMSPacket(playerConnection), playerConnectionClass);
                return wrapper.readObject(0, networkManagerClass);
            }
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
        if (entityPlayerPingField == null) {
            return PlayerAPIModern.getPing(player);
        }
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

    public static String fromStringToJSON(String message) {
        if (message == null) {
            return null;
        }
        return "{\"text\": \"" + message + "\"}";
    }

    public static Object generateNMSBlockPos(Vector3i blockPosition) {
        try {
            return blockPosConstructor.newInstance(blockPosition.x, blockPosition.y, blockPosition.z);
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

    public static Object generateVec3D(Vector3f vector) {
        return generateVec3D(vector.x, vector.y, vector.z);
    }

    public static Object generateVec3D(Vector3d vector) {
        return generateVec3D(vector.x, vector.y, vector.z);
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
        try {
            return dataWatcherConstructor.newInstance(entity);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UUID generateUUID() {
        long var1 = randomThreadLocal.get().nextLong() & -61441L | 16384L;
        long var3 = randomThreadLocal.get().nextLong() & 4611686018427387903L | -9223372036854775808L;
        return new UUID(var1, var3);
    }

    public static int generateEntityId() {
        Field field = Reflection.getField(nmsEntityClass, "entityCount");
        if (field == null) {
            field = Reflection.getField(nmsEntityClass, AtomicInteger.class, 0);
        }
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

    public static int getEffectId(Object nmsMobEffectList) {
        try {
            return (int) getMobEffectListId.invoke(null, nmsMobEffectList);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Object getMobEffectListById(int effectID) {
        try {
            return getMobEffectListById.invoke(null, effectID);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getNMSItemId(Object nmsItem) {
        try {
            return (int) getItemId.invoke(null, nmsItem);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Object getNMSItemById(int id) {
        try {
            return getItemById.invoke(null, id);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object generatePacketDataSerializer(Object byteBuf) {
        try {
            return packetDataSerializerConstructor.newInstance(byteBuf);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
