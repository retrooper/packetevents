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

package com.github.retrooper.packetevents.protocol.entity.data;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.util.TypesBuilder;
import com.github.retrooper.packetevents.util.TypesBuilderData;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class EntityDataTypes {
    //1.7 -> 1.18 block_position is just 3 ints, not serialized with a long
    //short was removed in 1.9+
    //boolean was added in 1.9
    //nbt was added in 1.12

    private static final Map<String, EntityDataType<?>> ENTITY_DATA_TYPE_MAP = new HashMap<>();
    private static final Map<Byte, Map<Integer, EntityDataType<?>>> ENTITY_DATA_TYPE_ID_MAP = new HashMap<>();
    protected static final TypesBuilder TYPES_BUILDER = new TypesBuilder("entity/entity_data_type_mappings",
            ClientVersion.V_1_8,
            ClientVersion.V_1_9,
            ClientVersion.V_1_11,
            ClientVersion.V_1_13,
            ClientVersion.V_1_14,
            ClientVersion.V_1_19);

    public static final EntityDataType<Byte> BYTE = define("byte", PacketWrapper::readByte, PacketWrapper::writeByte);
    public static final EntityDataType<Integer> INT = define("int", wrapper -> {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return wrapper.readVarInt();
        } else {
            return wrapper.readInt();
        }
    }, (wrapper, value) -> {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeVarInt(value);
        }
        else {
            wrapper.writeInt(value);
        }
    });
    public static final EntityDataType<Float> FLOAT = define("float", PacketWrapper::readFloat, PacketWrapper::writeFloat);
    public static final EntityDataType<String> STRING = define("string", PacketWrapper::readString, PacketWrapper::writeString);
    public static final EntityDataType<Component> COMPONENT = define("component", PacketWrapper::readComponent, PacketWrapper::writeComponent);
    public static final EntityDataType<Optional<Component>> OPTIONAL_COMPONENT = define("optional_component", readOptionalComponentDeserializer(), writeOptionalComponentSerializer());
    public static final EntityDataType<ItemStack> ITEMSTACK = define("itemstack", PacketWrapper::readItemStack, PacketWrapper::writeItemStack);
    public static final EntityDataType<Optional<Integer>> OPTIONAL_BLOCK_STATE =
            define("optional_block_state", wrapper -> Optional.ofNullable(wrapper.readOptional(PacketWrapper::readVarInt)),
                    (wrapper, value) -> wrapper.writeOptional(value.orElse(null), PacketWrapper::writeVarInt));
    public static final EntityDataType<Boolean> BOOLEAN = define("boolean", PacketWrapper::readBoolean, PacketWrapper::writeBoolean);
    public static final EntityDataType<Integer> PARTICLE = define("particle", PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final EntityDataType<Vector3f> ROTATION = define("rotation",
            (PacketWrapper<?> wrapper) -> new Vector3f(wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat()),
            (PacketWrapper<?> wrapper, Vector3f value) -> {
                wrapper.writeFloat(value.x);
                wrapper.writeFloat(value.y);
                wrapper.writeFloat(value.z);
            });
    public static final EntityDataType<Vector3i> BLOCK_POSITION = define("block_position", (PacketWrapper<?> wrapper) -> {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return wrapper.readBlockPosition();
        } else {
            int x = wrapper.readInt();
            int y = wrapper.readInt();
            int z = wrapper.readInt();
            return new Vector3i(x, y, z);
        }
    }, (wrapper, blockPosition) -> {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeBlockPosition(blockPosition);
        } else {
            wrapper.writeInt(blockPosition.getX());
            wrapper.writeInt(blockPosition.getY());
            wrapper.writeInt(blockPosition.getZ());
        }
    });
    public static final EntityDataType<Optional<Vector3i>> OPTIONAL_BLOCK_POSITION = define("optional_block_position",
            readOptionalBlockPositionDeserializer(), writeOptionalBlockPositionSerializer());
    public static final EntityDataType<BlockFace> BLOCK_FACE = define("block_face", (PacketWrapper<?> wrapper) -> {
                int id = wrapper.readVarInt();
                return BlockFace.getBlockFaceByValue(id);
            },
            (PacketWrapper<?> wrapper, BlockFace value) -> wrapper.writeVarInt(value.getFaceValue()));

    public static final EntityDataType<Optional<UUID>> OPTIONAL_UUID = define("optional_uuid",
            (PacketWrapper<?> wrapper) -> Optional.ofNullable(wrapper.readOptional(PacketWrapper::readUUID)),
            (PacketWrapper<?> wrapper, Optional<UUID> value) ->
                    wrapper.writeOptional(value.orElse(null), PacketWrapper::writeUUID));

    public static final EntityDataType<NBTCompound> NBT = define("nbt", PacketWrapper::readNBT, PacketWrapper::writeNBT);
    public static final EntityDataType<VillagerData> VILLAGER_DATA = define("villager_data", PacketWrapper::readVillagerData, PacketWrapper::writeVillagerData);
    public static final EntityDataType<Optional<Integer>> OPTIONAL_INT = define("optional_int", (PacketWrapper<?> wrapper) -> {
        int i = wrapper.readVarInt();
        return i == 0 ? Optional.empty() : Optional.of(i - 1);
    }, (PacketWrapper<?> wrapper, Optional<Integer> value) -> {
        wrapper.writeVarInt(value.orElse(-1) + 1);
    });

    public static final EntityDataType<EntityPose> ENTITY_POSE = define("entity_pose", (PacketWrapper<?> wrapper) -> {
        int id = wrapper.readVarInt();
        return EntityPose.getById(wrapper.getServerVersion().toClientVersion(), id);
    }, (PacketWrapper<?> wrapper, EntityPose value) -> wrapper.writeVarInt(value.getId(wrapper.getServerVersion().toClientVersion())));

    public static final EntityDataType<Integer> CAT_VARIANT = define("cat_variant_type", readIntDeserializer(), writeIntSerializer());
    public static final EntityDataType<Integer> FROG_VARIANT = define("frog_variant_type", readIntDeserializer(), writeIntSerializer());

    public static final EntityDataType<Optional<Vector3i>> OPTIONAL_GLOBAL_POSITION = define("optional_global_position",
            (PacketWrapper<?> wrapper) -> Optional.ofNullable(wrapper.readOptional(PacketWrapper::readBlockPosition)),
            (PacketWrapper<?> wrapper, Optional<Vector3i> value) ->
                    wrapper.writeOptional(value.orElse(null), PacketWrapper::writeBlockPosition));

    public static final EntityDataType<Integer> PAINTING_VARIANT_TYPE = define("painting_variant_type", readIntDeserializer(), writeIntSerializer());

    @Deprecated
    public static final EntityDataType<Short> SHORT = define("short", PacketWrapper::readShort, PacketWrapper::writeShort);

    public static EntityDataType<?> getById(ClientVersion version, int id) {
        int index = TYPES_BUILDER.getDataIndex(version);
        Map<Integer, EntityDataType<?>> typeIdMap = ENTITY_DATA_TYPE_ID_MAP.get((byte) index);
        return typeIdMap.get(id);
    }

    public static EntityDataType<?> getByName(String name) {
        return ENTITY_DATA_TYPE_MAP.get(name);
    }

    public static <T> EntityDataType<T> define(String name, Function<PacketWrapper<?>, T> deserializer, BiConsumer<PacketWrapper<?>, T> serializer) {
        TypesBuilderData data = TYPES_BUILDER.define(name);
        EntityDataType<T> type = new EntityDataType<>(name, data.getData(), deserializer,
                (BiConsumer<PacketWrapper<?>, Object>) serializer);
        ENTITY_DATA_TYPE_MAP.put(type.getName(), type);
        for (ClientVersion version : TYPES_BUILDER.getVersions()) {
            int index = TYPES_BUILDER.getDataIndex(version);
            if (index == -1) continue;
            Map<Integer, EntityDataType<?>> typeIdMap = ENTITY_DATA_TYPE_ID_MAP
                    .computeIfAbsent((byte) index, k -> new HashMap<>());
            typeIdMap.put(type.getId(version), type);
        }
        return type;
    }

    private static <T> Function<PacketWrapper<?>, T> readIntDeserializer() {
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return (PacketWrapper<?> wrapper) -> (T) ((Object) wrapper.readVarInt());
        } else {
            return (PacketWrapper<?> wrapper) -> (T) ((Object) wrapper.readInt());
        }
    }

    private static <T> BiConsumer<PacketWrapper<?>, T> writeIntSerializer() {
        return (PacketWrapper<?> wrapper, T value) -> {
            int output = 0;
            if (value instanceof Byte) {
                output = ((Byte) value).intValue();
            } else if (value instanceof Short) {
                output = ((Short) value).intValue();
            } else if (value instanceof Integer) {
                output = (Integer) value;
            } else if (value instanceof Long) {
                output = ((Long) value).intValue();
            }
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
                wrapper.writeVarInt(output);
            } else {
                wrapper.writeInt(output);
            }
        };
    }

    private static <T> Function<PacketWrapper<?>, T> readOptionalComponentDeserializer() {
        return (PacketWrapper<?> wrapper) -> {
            if (wrapper.readBoolean()) {
                return (T) Optional.of(wrapper.readComponent());
            } else {
                return (T) Optional.empty();
            }
        };
    }

    private static <T> BiConsumer<PacketWrapper<?>, T> writeOptionalComponentSerializer() {
        return (PacketWrapper<?> wrapper, T value) -> {
            if (value instanceof Optional) {
                Optional<Component> optional = (Optional<Component>) value;
                if (optional.isPresent()) {
                    wrapper.writeBoolean(true);
                    wrapper.writeComponent(optional.get());
                } else {
                    wrapper.writeBoolean(false);
                }
            } else {
                wrapper.writeBoolean(false);
            }
        };
    }

    private static <T> Function<PacketWrapper<?>, T> readOptionalBlockPositionDeserializer() {
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return (PacketWrapper<?> wrapper) -> {
                if (wrapper.readBoolean()) {
                    return (T) Optional.of(wrapper.readBlockPosition());
                } else {
                    return (T) Optional.empty();
                }
            };
        } else {
            return (PacketWrapper<?> wrapper) -> {
                if (wrapper.readBoolean()) {
                    int x = wrapper.readInt();
                    int y = wrapper.readInt();
                    int z = wrapper.readInt();
                    return (T) Optional.of(new Vector3i(x, y, z));
                } else {
                    return (T) Optional.empty();
                }
            };
        }
    }

    private static <T> BiConsumer<PacketWrapper<?>, T> writeOptionalBlockPositionSerializer() {
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return (PacketWrapper<?> wrapper, T value) -> {
                if (value instanceof Optional) {
                    Optional<?> optional = (Optional<?>) value;
                    if (optional.isPresent()) {
                        wrapper.writeBoolean(true);
                        wrapper.writeBlockPosition((Vector3i) optional.get());
                    } else {
                        wrapper.writeBoolean(false);
                    }
                } else {
                    wrapper.writeBoolean(false);
                }
            };
        } else {
            return (PacketWrapper<?> wrapper, T value) -> {
                if (value instanceof Optional) {
                    Optional<?> optional = (Optional<?>) value;
                    if (optional.isPresent()) {
                        wrapper.writeBoolean(true);
                        Vector3i position = (Vector3i) optional.get();
                        wrapper.writeInt(position.getX());
                        wrapper.writeInt(position.getY());
                        wrapper.writeInt(position.getZ());
                    } else {
                        wrapper.writeBoolean(false);
                    }
                } else {
                    wrapper.writeBoolean(false);
                }
            };
        }
    }
}
