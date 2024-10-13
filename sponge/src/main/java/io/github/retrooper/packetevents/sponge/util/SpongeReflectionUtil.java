/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package io.github.retrooper.packetevents.sponge.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public final class SpongeReflectionUtil {

    public static ServerVersion VERSION;

    // Minecraft classes
    public static Class<?> MINECRAFT_SERVER_CLASS, NMS_PACKET_DATA_SERIALIZER_CLASS, NMS_ITEM_STACK_CLASS,
            ENTITY_PLAYER_CLASS,
            NMS_MINECRAFT_KEY_CLASS,
            PLAYER_CONNECTION_CLASS, SERVER_COMMON_PACKETLISTENER_IMPL_CLASS, SERVER_CONNECTION_CLASS, NETWORK_MANAGER_CLASS,
            NMS_NBT_COMPOUND_CLASS, NBT_COMPRESSION_STREAM_TOOLS_CLASS,
            STREAM_CODEC, STREAM_DECODER, STREAM_ENCODER, REGISTRY_FRIENDLY_BYTE_BUF,
            REGISTRY_ACCESS, REGISTRY_ACCESS_FROZEN;

    // Fields
    public static Field BYTE_BUF_IN_PACKET_DATA_SERIALIZER, NMS_MK_KEY_FIELD;

    // Methods
    public static Method IS_DEBUGGING,
            READ_NBT_FROM_STREAM_METHOD, WRITE_NBT_TO_STREAM_METHOD,
            STREAM_DECODER_DECODE, STREAM_ENCODER_ENCODE;

    // Constructors
    private static Constructor<?> REGISTRY_FRIENDLY_BYTE_BUF_CONSTRUCTOR;

    private static Object MINECRAFT_SERVER_CONNECTION_INSTANCE;
    private static Object ITEM_STACK_OPTIONAL_STREAM_CODEC;
    private static Object MINECRAFT_SERVER_REGISTRY_ACCESS;

    private static void initConstructors() {
        try {
            REGISTRY_FRIENDLY_BYTE_BUF_CONSTRUCTOR = REGISTRY_FRIENDLY_BYTE_BUF.getConstructor(
                    ByteBuf.class, REGISTRY_ACCESS);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static void initMethods() {
        IS_DEBUGGING = Reflection.getMethod(MINECRAFT_SERVER_CLASS, "isDebugging", 0);

        READ_NBT_FROM_STREAM_METHOD = Reflection.getMethod(NBT_COMPRESSION_STREAM_TOOLS_CLASS, 0, DataInputStream.class);
        if (READ_NBT_FROM_STREAM_METHOD == null) {
            READ_NBT_FROM_STREAM_METHOD = Reflection.getMethod(NBT_COMPRESSION_STREAM_TOOLS_CLASS, 0, DataInput.class);
        }
        WRITE_NBT_TO_STREAM_METHOD = Reflection.getMethod(NBT_COMPRESSION_STREAM_TOOLS_CLASS, 0, NMS_NBT_COMPOUND_CLASS, DataOutput.class);

        STREAM_DECODER_DECODE = STREAM_DECODER.getMethods()[0];
        STREAM_ENCODER_ENCODE = STREAM_ENCODER.getMethods()[0];
    }

    private static void initFields() {
        BYTE_BUF_IN_PACKET_DATA_SERIALIZER = Reflection.getField(NMS_PACKET_DATA_SERIALIZER_CLASS, ByteBuf.class, 0, true);
        NMS_MK_KEY_FIELD = Reflection.getField(NMS_MINECRAFT_KEY_CLASS, "key");
    }

    private static void initClasses() {
        MINECRAFT_SERVER_CLASS = getServerClass("server.MinecraftServer");
        NMS_PACKET_DATA_SERIALIZER_CLASS = getServerClass("network.FriendlyByteBuf");
        NMS_ITEM_STACK_CLASS = getServerClass("world.item.ItemStack");
        NMS_MINECRAFT_KEY_CLASS = getServerClass("resources.ResourceLocation");

        ENTITY_PLAYER_CLASS = getServerClass("server.level.ServerPlayer");

        PLAYER_CONNECTION_CLASS = getServerClass("server.network.ServerGamePacketListenerImpl");

        SERVER_COMMON_PACKETLISTENER_IMPL_CLASS = getServerClass("server.network.ServerCommonPacketListenerImpl");

        SERVER_CONNECTION_CLASS = getServerClass("server.network.ServerConnectionListener");
        NETWORK_MANAGER_CLASS = getServerClass("network.Connection");

        NMS_NBT_COMPOUND_CLASS = getServerClass("nbt.CompoundTag");
        NBT_COMPRESSION_STREAM_TOOLS_CLASS = getServerClass("nbt.NbtIo");

        STREAM_CODEC = Reflection.getClassByNameWithoutException("net.minecraft.network.codec.StreamCodec");
        STREAM_DECODER = Reflection.getClassByNameWithoutException("net.minecraft.network.codec.StreamDecoder");
        STREAM_ENCODER = Reflection.getClassByNameWithoutException("net.minecraft.network.codec.StreamEncoder");
        REGISTRY_FRIENDLY_BYTE_BUF = Reflection.getClassByNameWithoutException("net.minecraft.network.RegistryFriendlyByteBuf");

        REGISTRY_ACCESS = getServerClass("core.RegistryAccess");
        REGISTRY_ACCESS_FROZEN = getServerClass("core.RegistryAccess$Frozen");
    }

    private static void initObjects() {
        try {
            if (VERSION.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                ITEM_STACK_OPTIONAL_STREAM_CODEC = Reflection.getField(NMS_ITEM_STACK_CLASS, STREAM_CODEC, 0).get(null);
            }
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    public static void init() {
        VERSION = PacketEvents.getAPI().getServerManager().getVersion();

        initClasses();
        initFields();
        initMethods();
        initConstructors();
        initObjects();
    }

    @Nullable
    public static Class<?> getServerClass(String modern) {
        return Reflection.getClassByNameWithoutException("net.minecraft." + modern);
    }

    public static boolean isMinecraftServerInstanceDebugging() {
        if (IS_DEBUGGING != null) {
            try {
                return (boolean) IS_DEBUGGING.invoke(Sponge.server());
            } catch (IllegalAccessException | InvocationTargetException e) {
                IS_DEBUGGING = null;
                return false;
            }
        }
        return false;
    }

    public static Object getMinecraftServerConnectionInstance() {
        if (MINECRAFT_SERVER_CONNECTION_INSTANCE == null) {
            try {
                MINECRAFT_SERVER_CONNECTION_INSTANCE = Reflection.getField(MINECRAFT_SERVER_CLASS, SERVER_CONNECTION_CLASS, 0).get(Sponge.server());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return MINECRAFT_SERVER_CONNECTION_INSTANCE;
    }

    public static Class<?> getNettyClass(String name) {
        return Reflection.getClassByNameWithoutException("io.netty." + name);
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

    public static Object getNetworkManager(ServerPlayer player) {
        Object playerConnection = getPlayerConnection(player);
        if (playerConnection == null) {
            return null;
        }
        Class<?> playerConnectionClass = SERVER_COMMON_PACKETLISTENER_IMPL_CLASS != null ?
                SERVER_COMMON_PACKETLISTENER_IMPL_CLASS : PLAYER_CONNECTION_CLASS;
        ReflectionObject wrapper = new ReflectionObject(playerConnection, playerConnectionClass);
        try {
            return wrapper.readObject(0, NETWORK_MANAGER_CLASS);
        } catch (Exception ex) {
            //Support for some weird custom plugins.
            try {
                playerConnection = wrapper.read(0, PLAYER_CONNECTION_CLASS);
                wrapper = new ReflectionObject(playerConnection, PLAYER_CONNECTION_CLASS);
                return wrapper.readObject(0, NETWORK_MANAGER_CLASS);
            }
            catch (Exception ex2) {
                //Print the original error!
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static Object getChannel(ServerPlayer player) {
        Object networkManager = getNetworkManager(player);
        if (networkManager == null) {
            return null;
        }
        ReflectionObject wrapper = new ReflectionObject(networkManager, NETWORK_MANAGER_CLASS);
        return wrapper.readObject(0, Channel.class);
    }

    public static Object getPlayerConnection(ServerPlayer player) {
        ReflectionObject wrappedEntityPlayer = new ReflectionObject(player, ENTITY_PLAYER_CLASS);
        return wrappedEntityPlayer.readObject(0, PLAYER_CONNECTION_CLASS);
    }

    public static com.github.retrooper.packetevents.protocol.item.ItemStack decodeSpongeItemStack(ItemStack in) {
        Object buffer = PooledByteBufAllocator.DEFAULT.buffer();
        try {
            // 3 reflection calls
            Object packetDataSerializer = createPacketDataSerializer(buffer);
            writeNMSItemStackPacketDataSerializer(packetDataSerializer, in);
            // No more reflection from here on.
            PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
            return wrapper.readItemStack();
        } finally {
            ByteBufHelper.release(buffer);
        }
    }

    public static ItemStack encodeSpongeItemStack(com.github.retrooper.packetevents.protocol.item.ItemStack in) {
        Object buffer = PooledByteBufAllocator.DEFAULT.buffer();
        try {
            PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
            wrapper.writeItemStack(in);
            //3 reflection calls
            Object packetDataSerializer = createPacketDataSerializer(wrapper.getBuffer());
            Object nmsItemStack = readNMSItemStackPacketDataSerializer(packetDataSerializer);
            return (ItemStack) nmsItemStack;
        } finally {
            ByteBufHelper.release(buffer);
        }
    }

    public static Object createPacketDataSerializer(Object byteBuf) {
        try {
            return REGISTRY_FRIENDLY_BYTE_BUF_CONSTRUCTOR.newInstance(byteBuf, getFrozenRegistryAccess());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object readNMSItemStackPacketDataSerializer(Object packetDataSerializer) {
        try {
            return STREAM_DECODER_DECODE.invoke(ITEM_STACK_OPTIONAL_STREAM_CODEC, packetDataSerializer);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getFrozenRegistryAccess() {
        if (MINECRAFT_SERVER_REGISTRY_ACCESS == null) {
            try {
                MINECRAFT_SERVER_REGISTRY_ACCESS = Reflection.getMethod(MINECRAFT_SERVER_CLASS,
                                REGISTRY_ACCESS_FROZEN,
                                0)
                        .invoke(Sponge.server());
            } catch (IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        }
        return MINECRAFT_SERVER_REGISTRY_ACCESS;
    }

    public static Object writeNMSItemStackPacketDataSerializer(Object packetDataSerializer, Object nmsItemStack) {
        try {
            return STREAM_ENCODER_ENCODE.invoke(ITEM_STACK_OPTIONAL_STREAM_CODEC, packetDataSerializer, nmsItemStack);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NBTCompound fromMinecraftNBT(Object nbtCompound) {
        byte[] bytes;
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
             DataOutputStream stream = new DataOutputStream(byteStream)) {
            writeNmsNbtToStream(nbtCompound, stream);
            bytes = byteStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Object buffer = UnpooledByteBufAllocationHelper.wrappedBuffer(bytes);
        PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
        NBTCompound nbt = wrapper.readNBT();
        ByteBufHelper.release(buffer);
        return nbt;
    }

    public static Object toMinecraftNBT(NBTCompound nbtCompound) {
        Object buffer = UnpooledByteBufAllocationHelper.buffer();
        PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(buffer);
        wrapper.writeNBT(nbtCompound);
        byte[] bytes = ByteBufHelper.copyBytes(buffer);
        ByteBufHelper.release(buffer);
        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
             DataInputStream stream = new DataInputStream(byteStream)) {
            return readNmsNbtFromStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeNmsNbtToStream(Object compound, DataOutput out) {
        try {
            WRITE_NBT_TO_STREAM_METHOD.invoke(null, compound, out);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static Object readNmsNbtFromStream(DataInputStream in) {
        try {
            return READ_NBT_FROM_STREAM_METHOD.invoke(null, in);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
