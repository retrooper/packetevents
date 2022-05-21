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

package io.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.google.common.collect.MapMaker;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class SpigotReflectionUtil {
    private static final String MODIFIED_PACKAGE_NAME = Bukkit.getServer().getClass().getPackage().getName()
            .replace(".", ",").split(",")[3];
    //Example: net.minecraft.server.v1_8_R3.
    public static final String LEGACY_NMS_PACKAGE = "net.minecraft.server." + MODIFIED_PACKAGE_NAME + ".";
    //Example: org.bukkit.craftbukkit.v1_8_R3.
    public static final String OBC_PACKAGE = "org.bukkit.craftbukkit." + MODIFIED_PACKAGE_NAME + ".";
    public static ServerVersion VERSION;
    //Booleans
    public static boolean V_1_17_OR_HIGHER;
    public static boolean V_1_12_OR_HIGHER;
    //Minecraft classes
    public static Class<?> MINECRAFT_SERVER_CLASS, NMS_PACKET_DATA_SERIALIZER_CLASS, NMS_ITEM_STACK_CLASS,
            NMS_IMATERIAL_CLASS, NMS_ENTITY_CLASS, ENTITY_PLAYER_CLASS, BOUNDING_BOX_CLASS,
            ENTITY_HUMAN_CLASS, PLAYER_CONNECTION_CLASS, SERVER_CONNECTION_CLASS, NETWORK_MANAGER_CLASS,
            MOB_EFFECT_LIST_CLASS, NMS_ITEM_CLASS, DEDICATED_SERVER_CLASS, WORLD_SERVER_CLASS, ENUM_PROTOCOL_DIRECTION_CLASS,
            GAME_PROFILE_CLASS, CRAFT_WORLD_CLASS, CRAFT_SERVER_CLASS, CRAFT_PLAYER_CLASS, CRAFT_ENTITY_CLASS, CRAFT_ITEM_STACK_CLASS,
            LEVEL_ENTITY_GETTER_CLASS, PERSISTENT_ENTITY_SECTION_MANAGER_CLASS, CRAFT_MAGIC_NUMBERS_CLASS, IBLOCK_DATA_CLASS,
            BLOCK_CLASS, CRAFT_BLOCK_DATA_CLASS, PROPERTY_MAP_CLASS;

    //Netty classes
    public static Class<?> CHANNEL_CLASS, BYTE_BUF_CLASS, BYTE_TO_MESSAGE_DECODER, MESSAGE_TO_BYTE_ENCODER;

    //Fields
    public static Field ENTITY_PLAYER_PING_FIELD, ENTITY_BOUNDING_BOX_FIELD, BYTE_BUF_IN_PACKET_DATA_SERIALIZER;

    //Methods
    public static Method IS_DEBUGGING, GET_CRAFT_PLAYER_HANDLE_METHOD, GET_CRAFT_ENTITY_HANDLE_METHOD, GET_CRAFT_WORLD_HANDLE_METHOD,
            GET_MOB_EFFECT_LIST_ID_METHOD, GET_MOB_EFFECT_LIST_BY_ID_METHOD, GET_ITEM_ID_METHOD, GET_ITEM_BY_ID_METHOD,
            GET_BUKKIT_ENTITY_METHOD, GET_LEVEL_ENTITY_GETTER_ITERABLE_METHOD, GET_ENTITY_BY_ID_METHOD,
            CRAFT_ITEM_STACK_AS_BUKKIT_COPY, CRAFT_ITEM_STACK_AS_NMS_COPY,
            READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD,
            WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD, GET_COMBINED_ID,
            GET_BY_COMBINED_ID, GET_CRAFT_BLOCK_DATA_FROM_IBLOCKDATA, PROPERTY_MAP_GET_METHOD;

    //Constructors
    private static Constructor<?> NMS_ITEM_STACK_CONSTRUCTOR, NMS_PACKET_DATA_SERIALIZER_CONSTRUCTOR;

    private static Object MINECRAFT_SERVER_INSTANCE;
    private static Object MINECRAFT_SERVER_CONNECTION_INSTANCE;

    //Initialized in PacketEvents#load
    public static Map<Integer, Entity> ENTITY_ID_CACHE = new MapMaker().weakValues().makeMap();
    ;

    private static void initConstructors() {
        Class<?> itemClass = NMS_IMATERIAL_CLASS != null ? NMS_IMATERIAL_CLASS : NMS_ITEM_CLASS;
        try {
            NMS_ITEM_STACK_CONSTRUCTOR = NMS_ITEM_STACK_CLASS.getConstructor(itemClass, int.class);
            NMS_PACKET_DATA_SERIALIZER_CONSTRUCTOR = NMS_PACKET_DATA_SERIALIZER_CLASS.getConstructor(BYTE_BUF_CLASS);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static void initMethods() {
        IS_DEBUGGING = Reflection.getMethod(MINECRAFT_SERVER_CLASS, "isDebugging", 0);
        GET_BUKKIT_ENTITY_METHOD = Reflection.getMethod(NMS_ENTITY_CLASS, CRAFT_ENTITY_CLASS, 0);
        GET_CRAFT_PLAYER_HANDLE_METHOD = Reflection.getMethod(CRAFT_PLAYER_CLASS, "getHandle", 0);
        GET_CRAFT_ENTITY_HANDLE_METHOD = Reflection.getMethod(CRAFT_ENTITY_CLASS, "getHandle", 0);
        GET_CRAFT_WORLD_HANDLE_METHOD = Reflection.getMethod(CRAFT_WORLD_CLASS, "getHandle", 0);
        GET_MOB_EFFECT_LIST_ID_METHOD = Reflection.getMethod(MOB_EFFECT_LIST_CLASS, "getId", 0);
        GET_MOB_EFFECT_LIST_BY_ID_METHOD = Reflection.getMethod(MOB_EFFECT_LIST_CLASS, "fromId", 0);
        GET_ITEM_ID_METHOD = Reflection.getMethod(NMS_ITEM_CLASS, "getId", 0);
        GET_ITEM_BY_ID_METHOD = Reflection.getMethod(NMS_ITEM_CLASS, "getById", 0);
        if (V_1_17_OR_HIGHER) {
            GET_LEVEL_ENTITY_GETTER_ITERABLE_METHOD = Reflection.getMethod(LEVEL_ENTITY_GETTER_CLASS, Iterable.class, 0);
        }
        String getEntityByIdMethodName = (VERSION.getProtocolVersion() == (short) 47)
                ? "a" : "getEntity";
        GET_ENTITY_BY_ID_METHOD = Reflection.getMethod(WORLD_SERVER_CLASS, getEntityByIdMethodName, NMS_ENTITY_CLASS, int.class);
        if (GET_ENTITY_BY_ID_METHOD == null) {
            GET_ENTITY_BY_ID_METHOD = Reflection.getMethod(WORLD_SERVER_CLASS, "getEntity", NMS_ENTITY_CLASS, int.class);
        }

        CRAFT_ITEM_STACK_AS_BUKKIT_COPY = Reflection.getMethod(CRAFT_ITEM_STACK_CLASS, "asBukkitCopy", 0);
        CRAFT_ITEM_STACK_AS_NMS_COPY = Reflection.getMethod(CRAFT_ITEM_STACK_CLASS, "asNMSCopy", 0);
        READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD = Reflection.getMethod(NMS_PACKET_DATA_SERIALIZER_CLASS, NMS_ITEM_STACK_CLASS, 0);
        WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD = Reflection.getMethod(NMS_PACKET_DATA_SERIALIZER_CLASS, 0, NMS_ITEM_STACK_CLASS);
        GET_COMBINED_ID = Reflection.getMethod(BLOCK_CLASS, "getCombinedId", int.class, IBLOCK_DATA_CLASS);
        GET_BY_COMBINED_ID = Reflection.getMethod(BLOCK_CLASS, "getByCombinedId", IBLOCK_DATA_CLASS, int.class);
        if (CRAFT_BLOCK_DATA_CLASS != null) {
            GET_CRAFT_BLOCK_DATA_FROM_IBLOCKDATA = Reflection.getMethod(CRAFT_BLOCK_DATA_CLASS, "fromData", CRAFT_BLOCK_DATA_CLASS, IBLOCK_DATA_CLASS);
        }
    }

    private static void initFields() {
        ENTITY_BOUNDING_BOX_FIELD = Reflection.getField(NMS_ENTITY_CLASS, BOUNDING_BOX_CLASS, 0, true);
        ENTITY_PLAYER_PING_FIELD = Reflection.getField(ENTITY_PLAYER_CLASS, "ping");
        BYTE_BUF_IN_PACKET_DATA_SERIALIZER = Reflection.getField(NMS_PACKET_DATA_SERIALIZER_CLASS, BYTE_BUF_CLASS, 0, true);
    }

    private static void initClasses() {
        MINECRAFT_SERVER_CLASS = getServerClass("server.MinecraftServer", "MinecraftServer");
        NMS_PACKET_DATA_SERIALIZER_CLASS = getServerClass("network.PacketDataSerializer", "PacketDataSerializer");
        NMS_ITEM_STACK_CLASS = getServerClass("world.item.ItemStack", "ItemStack");
        NMS_IMATERIAL_CLASS = getServerClass("world.level.IMaterial", "IMaterial");
        NMS_ENTITY_CLASS = getServerClass("world.entity.Entity", "Entity");
        ENTITY_PLAYER_CLASS = getServerClass("server.level.EntityPlayer", "EntityPlayer");
        BOUNDING_BOX_CLASS = getServerClass("world.phys.AxisAlignedBB", "AxisAlignedBB");
        ENTITY_HUMAN_CLASS = getServerClass("world.entity.player.EntityHuman", "EntityHuman");
        PLAYER_CONNECTION_CLASS = getServerClass("server.network.PlayerConnection", "PlayerConnection");
        SERVER_CONNECTION_CLASS = getServerClass("server.network.ServerConnection", "ServerConnection");
        NETWORK_MANAGER_CLASS = getServerClass("network.NetworkManager", "NetworkManager");
        MOB_EFFECT_LIST_CLASS = getServerClass("world.effect.MobEffectList", "MobEffectList");
        NMS_ITEM_CLASS = getServerClass("world.item.Item", "Item");
        DEDICATED_SERVER_CLASS = getServerClass("server.dedicated.DedicatedServer", "DedicatedServer");
        WORLD_SERVER_CLASS = getServerClass("server.level.WorldServer", "WorldServer");
        ENUM_PROTOCOL_DIRECTION_CLASS = getServerClass("network.protocol.EnumProtocolDirection", "EnumProtocolDirection");
        if (V_1_17_OR_HIGHER) {
            LEVEL_ENTITY_GETTER_CLASS = getServerClass("world.level.entity.LevelEntityGetter", "");
            PERSISTENT_ENTITY_SECTION_MANAGER_CLASS = getServerClass("world.level.entity.PersistentEntitySectionManager", "");
        }

        CRAFT_MAGIC_NUMBERS_CLASS = getOBCClass("util.CraftMagicNumbers");
        //IBlockData does not exist on 1.7.10
        IBLOCK_DATA_CLASS = getServerClass("world.level.block.state.IBlockData", "IBlockData");
        BLOCK_CLASS = getServerClass("world.level.block.Block", "Block");
        CRAFT_BLOCK_DATA_CLASS = getOBCClass("block.data.CraftBlockData");

        GAME_PROFILE_CLASS = Reflection.getClassByNameWithoutException("com.mojang.authlib.GameProfile");

        CRAFT_WORLD_CLASS = getOBCClass("CraftWorld");
        CRAFT_PLAYER_CLASS = getOBCClass("entity.CraftPlayer");
        CRAFT_SERVER_CLASS = getOBCClass("CraftServer");
        CRAFT_ENTITY_CLASS = getOBCClass("entity.CraftEntity");
        CRAFT_ITEM_STACK_CLASS = getOBCClass("inventory.CraftItemStack");

        CHANNEL_CLASS = getNettyClass("channel.Channel");
        BYTE_BUF_CLASS = getNettyClass("buffer.ByteBuf");
        BYTE_TO_MESSAGE_DECODER = getNettyClass("handler.codec.ByteToMessageDecoder");
        MESSAGE_TO_BYTE_ENCODER = getNettyClass("handler.codec.MessageToByteEncoder");
    }

    public static void init() {
        VERSION = PacketEvents.getAPI().getServerManager().getVersion();
        V_1_17_OR_HIGHER = VERSION.isNewerThanOrEquals(ServerVersion.V_1_17);
        V_1_12_OR_HIGHER = VERSION.isNewerThanOrEquals(ServerVersion.V_1_12);

        initClasses();
        initFields();
        initMethods();
        initConstructors();
    }


    @Nullable
    public static Class<?> getServerClass(String modern, String legacy) {
        if (V_1_17_OR_HIGHER) {
            return Reflection.getClassByNameWithoutException("net.minecraft." + modern);
        } else {
            return Reflection.getClassByNameWithoutException(LEGACY_NMS_PACKAGE + legacy);
        }
    }

    public static boolean isMinecraftServerInstanceDebugging() {
        Object minecraftServerInstance = getMinecraftServerInstance(Bukkit.getServer());
        if (minecraftServerInstance != null && IS_DEBUGGING != null) {
            try {
                return (boolean) IS_DEBUGGING.invoke(minecraftServerInstance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                IS_DEBUGGING = null;
                return false;
            }
        }
        return false;
    }

    public static Object getMinecraftServerInstance(Server server) {
        if (MINECRAFT_SERVER_INSTANCE == null) {
            try {
                MINECRAFT_SERVER_INSTANCE = Reflection.getField(CRAFT_SERVER_CLASS, MINECRAFT_SERVER_CLASS, 0)
                        .get(server);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return MINECRAFT_SERVER_INSTANCE;
    }

    public static Object getMinecraftServerConnectionInstance() {
        if (MINECRAFT_SERVER_CONNECTION_INSTANCE == null) {
            try {
                MINECRAFT_SERVER_CONNECTION_INSTANCE = Reflection.getField(MINECRAFT_SERVER_CLASS, SERVER_CONNECTION_CLASS, 0).get(getMinecraftServerInstance(Bukkit.getServer()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return MINECRAFT_SERVER_CONNECTION_INSTANCE;
    }

    public static double getTPS() {
        return recentTPS()[0];
    }

    public static double[] recentTPS() {
        return new ReflectionObject(getMinecraftServerInstance(Bukkit.getServer()), MINECRAFT_SERVER_CLASS).readDoubleArray(0);
    }

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName(LEGACY_NMS_PACKAGE + name);
    }

    public static Class<?> getOBCClass(String name) {
        return Reflection.getClassByNameWithoutException(OBC_PACKAGE + name);
    }

    public static Class<?> getNettyClass(String name) {
        return Reflection.getClassByNameWithoutException("io.netty." + name);
    }

    public static Entity getBukkitEntity(Object nmsEntity) {
        Object craftEntity = null;
        try {
            craftEntity = GET_BUKKIT_ENTITY_METHOD.invoke(nmsEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return (Entity) craftEntity;
    }

    public static Object getNMSEntity(final Entity entity) {
        final Object craftEntity = CRAFT_ENTITY_CLASS.cast(entity);
        try {
            return GET_CRAFT_ENTITY_HANDLE_METHOD.invoke(craftEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getNMSAxisAlignedBoundingBox(Object nmsEntity) {
        try {
            return ENTITY_BOUNDING_BOX_FIELD.get(NMS_ENTITY_CLASS.cast(nmsEntity));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getCraftPlayer(Player player) {
        return CRAFT_PLAYER_CLASS.cast(player);
    }

    public static Object getEntityPlayer(Player player) {
        Object craftPlayer = getCraftPlayer(player);
        try {
            return GET_CRAFT_PLAYER_HANDLE_METHOD.invoke(craftPlayer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getPlayerConnection(Player player) {
        Object entityPlayer = getEntityPlayer(player);
        if (entityPlayer == null) {
            return null;
        }
        ReflectionObject wrappedEntityPlayer = new ReflectionObject(entityPlayer, ENTITY_PLAYER_CLASS);
        return wrappedEntityPlayer.readObject(0, SpigotReflectionUtil.PLAYER_CONNECTION_CLASS);
    }

    public static Object getGameProfile(Player player) {
        Object entityPlayer = getEntityPlayer(player);
        ReflectionObject entityHumanWrapper = new ReflectionObject(entityPlayer, SpigotReflectionUtil.ENTITY_HUMAN_CLASS);
        return entityHumanWrapper.readObject(0, SpigotReflectionUtil.GAME_PROFILE_CLASS);
    }

    public static List<TextureProperty> getUserProfile(Player player) {
        if (PROPERTY_MAP_CLASS == null) {
            PROPERTY_MAP_CLASS = Reflection.getClassByNameWithoutException("" +
                    "com.mojang.authlib.properties.PropertyMap");
            PROPERTY_MAP_GET_METHOD = Reflection.getMethod(PROPERTY_MAP_CLASS, "get", Collection.class, Object.class);
        }

        //Get the player's game profile in NMS
        Object nmsGameProfile = SpigotReflectionUtil.getGameProfile(player);
        ReflectionObject reflectGameProfile = new ReflectionObject(nmsGameProfile);
        Object nmsPropertyMap = reflectGameProfile.readObject(0, PROPERTY_MAP_CLASS);
        //Convert the nms property map into a java one to avoid direct GSON access.
        Collection<Object> nmsProperties = null;

        try {
            nmsProperties = (Collection<Object>) PROPERTY_MAP_GET_METHOD.invoke(nmsPropertyMap, "textures");
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        List<TextureProperty> properties = new ArrayList<>();

        for (Object nmsProperty : nmsProperties) {
            //Read the NMS texture property data
            ReflectionObject reflectProperty = new ReflectionObject(nmsProperty);
            String name = "textures"; //Save us a reflection call :)
            String value = reflectProperty.readString(1);
            String signature = reflectProperty.readString(2);
            TextureProperty textureProperty = new TextureProperty(name, value, signature);
            //Add it to our profile.
            properties.add(textureProperty);
        }

        return properties;
    }

    public static Object getNetworkManager(Player player) {
        Object playerConnection = getPlayerConnection(player);
        if (playerConnection == null) {
            return null;
        }
        ReflectionObject wrapper = new ReflectionObject(playerConnection, PLAYER_CONNECTION_CLASS);
        try {
            return wrapper.readObject(0, NETWORK_MANAGER_CLASS);
        } catch (Exception ex) {
            //Don't ask me why I do this, but this is terrible.
            wrapper = new ReflectionObject(playerConnection);
            try {
                return wrapper.readObject(0, NETWORK_MANAGER_CLASS);
            } catch (Exception ex2) {
                //Support for some custom plugins.
                playerConnection = wrapper.read(0, PLAYER_CONNECTION_CLASS);
                wrapper = new ReflectionObject(playerConnection, PLAYER_CONNECTION_CLASS);
                return wrapper.readObject(0, NETWORK_MANAGER_CLASS);
            }
        }
    }

    public static Object getChannel(Player player) {
        Object networkManager = getNetworkManager(player);
        if (networkManager == null) {
            return null;
        }
        ReflectionObject wrapper = new ReflectionObject(networkManager, NETWORK_MANAGER_CLASS);
        return wrapper.readObject(0, CHANNEL_CLASS);
    }

    @Deprecated
    public static int getPlayerPingLegacy(Player player) {
        if (V_1_17_OR_HIGHER) {
            return -1;
        }
        if (ENTITY_PLAYER_PING_FIELD != null) {
            Object entityPlayer = getEntityPlayer(player);
            try {
                return ENTITY_PLAYER_PING_FIELD.getInt(entityPlayer);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static List<Object> getNetworkManagers() {
        ReflectionObject serverConnectionWrapper = new ReflectionObject(getMinecraftServerConnectionInstance());
        for (int i = 0; true; i++) {
            try {
                List<?> list = (List<?>) serverConnectionWrapper.readObject(i, List.class);
                for (Object obj : list) {
                    if (obj.getClass().isAssignableFrom(NETWORK_MANAGER_CLASS)) {
                        return (List<Object>) list;
                    }
                }
            } catch (Exception ex) {
                break;
            }
        }

        return (List<Object>) serverConnectionWrapper.readObject(1, List.class);
    }

    public static Object convertBukkitServerToNMSServer(Server server) {
        Object craftServer = CRAFT_SERVER_CLASS.cast(server);
        ReflectionObject wrapper = new ReflectionObject(craftServer);
        try {
            return wrapper.readObject(0, MINECRAFT_SERVER_CLASS);
        } catch (Exception ex) {
            wrapper.readObject(0, DEDICATED_SERVER_CLASS);
        }
        return null;
    }

    public static Object convertBukkitWorldToWorldServer(World world) {
        Object craftWorld = CRAFT_WORLD_CLASS.cast(world);
        try {
            return GET_CRAFT_WORLD_HANDLE_METHOD.invoke(craftWorld);
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

    public static int generateEntityId() {
        Field field = Reflection.getField(NMS_ENTITY_CLASS, "entityCount");
        if (field == null) {
            field = Reflection.getField(NMS_ENTITY_CLASS, AtomicInteger.class, 0);
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
            return (int) GET_MOB_EFFECT_LIST_ID_METHOD.invoke(null, nmsMobEffectList);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Object getMobEffectListById(int effectID) {
        try {
            return GET_MOB_EFFECT_LIST_BY_ID_METHOD.invoke(null, effectID);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getNMSItemId(Object nmsItem) {
        try {
            return (int) GET_ITEM_ID_METHOD.invoke(null, nmsItem);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Object getNMSItemById(int id) {
        try {
            return GET_ITEM_BY_ID_METHOD.invoke(null, id);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object createNMSItemStack(Object nmsItem, int count) {
        try {
            return NMS_ITEM_STACK_CONSTRUCTOR.newInstance(nmsItem, count);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static com.github.retrooper.packetevents.protocol.item.ItemStack decodeBukkitItemStack(ItemStack in) {
        Object buffer = UnpooledByteBufAllocationHelper.buffer();
        //3 reflection calls
        Object packetDataSerializer = createPacketDataSerializer(buffer);
        Object nmsItemStack = toNMSItemStack(in);
        writeNMSItemStackPacketDataSerializer(packetDataSerializer, nmsItemStack);
        //No more reflection from here on.
        PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
        com.github.retrooper.packetevents.protocol.item.ItemStack stack = wrapper.readItemStack();
        ByteBufHelper.release(buffer);
        return stack;
    }

    public static ItemStack encodeBukkitItemStack(com.github.retrooper.packetevents.protocol.item.ItemStack in) {
        Object buffer = UnpooledByteBufAllocationHelper.buffer();
        PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
        wrapper.writeItemStack(in);
        //3 reflection calls
        Object packetDataSerializer = createPacketDataSerializer(wrapper.getBuffer());
        Object nmsItemStack = readNMSItemStackPacketDataSerializer(packetDataSerializer);
        ItemStack stack = toBukkitItemStack(nmsItemStack);
        ByteBufHelper.release(buffer);
        return stack;
    }

    public static int getBlockDataCombinedId(MaterialData materialData) {
        if (PacketEvents.getAPI().getServerManager().getVersion() == ServerVersion.V_1_7_10) {
            //TODO Finish for 1.7.10
            throw new IllegalStateException("This operation is not supported yet on 1.7.10!");
        }
        //TODO Finish
        int combinedID;
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
            combinedID = -1;
        } else {
            combinedID = materialData.getItemType().getId() << 4 | materialData.getData();
            //System.out.println("id: " + combinedID + ", material: " + materialData.getItemType().name());
        }
        return combinedID;
        /*
        Object iBlockDataObj = new ReflectionObject(blockData).readObject(0, IBLOCK_DATA_CLASS);
        int combinedID = 0;
        try {
            combinedID = (int) GET_COMBINED_ID.invoke(null, iBlockDataObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return combinedID;*/
    }

    public static MaterialData getBlockDataByCombinedId(int combinedID) {
        if (PacketEvents.getAPI().getServerManager().getVersion() == ServerVersion.V_1_7_10) {
            //TODO Finish for 1.7.10
            throw new IllegalStateException("This operation is not supported yet on 1.7.10!");
        }
        /*Object iBlockDataObj = null;
        try {
            iBlockDataObj = GET_BY_COMBINED_ID.invoke(null, combinedID);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }*/
        /*
        try {
            return (BlockData) GET_CRAFT_BLOCK_DATA_FROM_IBLOCKDATA.invoke(null, iBlockDataObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }*/

        return null;
    }

    public static Object createNMSItemStack(int itemID, int count) {
        try {
            Object nmsItem = getNMSItemById(itemID);
            return NMS_ITEM_STACK_CONSTRUCTOR.newInstance(nmsItem, count);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object createPacketDataSerializer(Object byteBuf) {
        try {
            return NMS_PACKET_DATA_SERIALIZER_CONSTRUCTOR.newInstance(byteBuf);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack toBukkitItemStack(Object nmsItemStack) {
        try {
            return (ItemStack) CRAFT_ITEM_STACK_AS_BUKKIT_COPY.invoke(null, nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object toNMSItemStack(ItemStack itemStack) {
        try {
            return CRAFT_ITEM_STACK_AS_NMS_COPY.invoke(null, itemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Object readNMSItemStackPacketDataSerializer(Object packetDataSerializer) {
        try {
            return READ_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD.invoke(packetDataSerializer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object writeNMSItemStackPacketDataSerializer(Object packetDataSerializer, Object nmsItemStack) {
        try {
            return WRITE_ITEM_STACK_IN_PACKET_DATA_SERIALIZER_METHOD.invoke(packetDataSerializer, nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Entity getEntityByIdWithWorldUnsafe(World world, int id) {
        if (world == null) {
            return null;
        }

        Object craftWorld = CRAFT_WORLD_CLASS.cast(world);

        try {
            Object worldServer = GET_CRAFT_WORLD_HANDLE_METHOD.invoke(craftWorld);
            Object nmsEntity = GET_ENTITY_BY_ID_METHOD.invoke(worldServer, id);
            if (nmsEntity == null) {
                return null;
            }
            return getBukkitEntity(nmsEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private static Entity getEntityByIdUnsafe(World origin, int id) {
        Entity e = getEntityByIdWithWorldUnsafe(origin, id);
        if (e != null) {
            return e;
        }
        for (World world : Bukkit.getWorlds()) {
            Entity entity = getEntityByIdWithWorldUnsafe(world, id);
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
    public static Entity getEntityById(@Nullable World world, int entityID) {
        Entity e = ENTITY_ID_CACHE.get(entityID);
        if (e != null) {
            return e;
        }

        if (V_1_17_OR_HIGHER) {
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
            return getEntityByIdUnsafe(world, entityID);
        }
        return null;
    }

    @Nullable
    public static Entity getEntityById(int entityID) {
        return getEntityById(null, entityID);
    }

    public static List<Entity> getEntityList(World world) {
        if (V_1_17_OR_HIGHER) {
            Object worldServer = convertBukkitWorldToWorldServer(world);
            ReflectionObject wrappedWorldServer = new ReflectionObject(worldServer);
            Object persistentEntitySectionManager = wrappedWorldServer.readObject(0, PERSISTENT_ENTITY_SECTION_MANAGER_CLASS);
            ReflectionObject wrappedPersistentEntitySectionManager = new ReflectionObject(persistentEntitySectionManager);
            Object levelEntityGetter = wrappedPersistentEntitySectionManager.readObject(0, LEVEL_ENTITY_GETTER_CLASS);
            Iterable<Object> nmsEntitiesIterable = null;
            try {
                nmsEntitiesIterable = (Iterable<Object>) GET_LEVEL_ENTITY_GETTER_ITERABLE_METHOD.invoke(levelEntityGetter);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            List<Entity> entityList = new ArrayList<>();
            if (nmsEntitiesIterable != null) {
                for (Object nmsEntity : nmsEntitiesIterable) {
                    Entity bukkitEntity = getBukkitEntity(nmsEntity);
                    entityList.add(bukkitEntity);
                }
            }
            return entityList;
        } else {
            return world.getEntities();
        }
    }
}
