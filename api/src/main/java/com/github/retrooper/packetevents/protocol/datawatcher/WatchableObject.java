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

package com.github.retrooper.packetevents.protocol.datawatcher;

import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class WatchableObject {
    private int index;
    private Type type;
    private Object value;

    public WatchableObject(int index, Type type, Object value) {
        this.index = index;
        this.type = type;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public enum Type {
        BYTE(PacketWrapper::readByte,
                (packetWrapper, o) -> {
                    byte output = 0;
                    if (o instanceof Long) {
                        output =
                                ((Long) o).byteValue();
                    } else if (o instanceof Integer) {
                        output = ((Integer) o).byteValue();
                    } else if (o instanceof Short) {
                        output = ((Short) o).byteValue();
                    } else if (o instanceof Byte) {
                        output = (byte) o;
                    }
                    packetWrapper.writeByte(output);
                }
        ),
        INTEGER(PacketWrapper::readVarInt,
                (packetWrapper, o) -> {
                    int output = 0;
                    if (o instanceof Long) {
                        output =
                                ((Long) o).intValue();
                    } else if (o instanceof Integer) {
                        output = ((Integer) o);
                    } else if (o instanceof Short) {
                        output = ((Short) o).intValue();
                    } else if (o instanceof Byte) {
                        output = ((Byte) o).intValue();
                    }
                    packetWrapper.writeVarInt((int) o);
                }),
        FLOAT(PacketWrapper::readFloat,
                (packetWrapper, o) -> {
                    packetWrapper.writeFloat((float) o);
                }),
        STRING(PacketWrapper::readString,
                (packetWrapper, o) -> {
                    packetWrapper.writeString((String) o);
                }),
        //TODO Make some Chat abstraction
        //TODO Double check string size on newer versions
        CHAT(PacketWrapper::readString,
                (packetWrapper, o) -> {
                    packetWrapper.writeString((String) o);
                }),
        OPTIONAL_CHAT(packetWrapper -> {
            if (packetWrapper.readBoolean()) {
                return Optional.of(packetWrapper.readString());
            } else {
                return Optional.empty();
            }
        }, (packetWrapper, o) -> {
            Optional<String> optString = (Optional<String>) o;
            if (optString.isPresent()) {
                packetWrapper.writeBoolean(true);
                packetWrapper.writeString(optString.get());
            } else {
                packetWrapper.writeBoolean(false);
            }
        }),
        ITEM_STACK(PacketWrapper::readItemStack,
                (packetWrapper, o) -> {
                    packetWrapper.writeItemStack((ItemStack) o);
                }),
        BOOLEAN(PacketWrapper::readBoolean,
                (packetWrapper, o) -> {
                    packetWrapper.writeBoolean((boolean) o);
                }),
        //3 floats
        ROTATION(packetWrapper -> {
            return new Vector3f(packetWrapper.readFloat(), packetWrapper.readFloat(), packetWrapper.readFloat());
        },
                (packetWrapper, o) -> {
                    Vector3f vec = (Vector3f) o;
                    packetWrapper.writeFloat(vec.x);
                    packetWrapper.writeFloat(vec.y);
                    packetWrapper.writeFloat(vec.z);
                }),
        //blockposition
        POSITION(packetWrapper -> {
            return packetWrapper.readBlockPosition();
        },
                (packetWrapper, o) -> {
                    Vector3i vec = (Vector3i) o;
                    packetWrapper.writeBlockPosition(vec);
                }),
        OPTIONAL_POSITION(packetWrapper -> {
            if (packetWrapper.readBoolean()) {
                return Optional.of(packetWrapper.readBlockPosition());
            } else {
                return Optional.empty();
            }
        },
                (packetWrapper, o) -> {
                    Optional<Vector3i> optVec = (Optional<Vector3i>) o;
                    if (optVec.isPresent()) {
                        packetWrapper.writeBoolean(true);
                        packetWrapper.writeBlockPosition(optVec.get());
                    } else {
                        packetWrapper.writeBoolean(false);
                    }
                }),
        //Var int
        BLOCK_FACE(packetWrapper -> {
            return BlockFace.getBlockFaceByValue(packetWrapper.readVarInt());
        },
                (packetWrapper, o) -> {
                    packetWrapper.writeVarInt(((BlockFace) o).getFaceValue());
                }),
        OPTIONAL_UUID(packetWrapper -> {
            if (packetWrapper.readBoolean()) {
                return Optional.of(packetWrapper.readUUID());
            } else {
                return Optional.empty();
            }
        },
                (packetWrapper, o) -> {
                    Optional<UUID> optUUID = (Optional<UUID>) o;
                    if (optUUID.isPresent()) {
                        packetWrapper.writeBoolean(true);
                        packetWrapper.writeUUID(optUUID.get());
                    } else {
                        packetWrapper.writeBoolean(false);
                    }
                }),
        OPTIONAL_BLOCK_TYPE_ID(PacketWrapper::readVarInt,
                (packetWrapper, o) -> {
                    packetWrapper.writeVarInt((int) o);
                }),
        //1.9 has the above
        NBT(PacketWrapper::readNBT,
                (packetWrapper, o) -> {
                    packetWrapper.writeNBT((NBTCompound) o);
                }),
        //TODO https://wiki.vg/Entity_metadata#Entity_Metadata_Format
        PARTICLE(null, null),
        //TODO
        VILLAGER_DATA(null, null),
        OPTIONAL_INTEGER(packetWrapper -> {
            if (packetWrapper.readBoolean()) {
                return Optional.of(packetWrapper.readVarInt());
            } else {
                return Optional.empty();
            }
        }, (packetWrapper, o) -> {
            Optional<Integer> optInt = (Optional<Integer>) o;
            if (optInt.isPresent()) {
                packetWrapper.writeBoolean(true);
                packetWrapper.writeVarInt(optInt.get());
            } else {
                packetWrapper.writeBoolean(false);
            }
        }),
        POSE(packetWrapper -> EntityPose.VALUES[packetWrapper.readVarInt()], (packetWrapper, o) -> {
            packetWrapper.writeVarInt(((EntityPose) o).ordinal());
        }),

        //Not present on 1.9+
        @Deprecated
        SHORT(PacketWrapper::readShort,
                (packetWrapper, o) -> {
                    packetWrapper.writeShort((short) o);
                });

        private final Function<PacketWrapper<?>, Object> readDataConsumer;
        private final BiConsumer<PacketWrapper<?>, Object> writeDataConsumer;

        Type(Function<PacketWrapper<?>, Object> readDataConsumer, BiConsumer<PacketWrapper<?>, Object> writeDataConsumer) {
            this.readDataConsumer = readDataConsumer;
            this.writeDataConsumer = writeDataConsumer;
        }

        public Function<PacketWrapper<?>, Object> getReadDataConsumer() {
            return readDataConsumer;
        }

        public BiConsumer<PacketWrapper<?>, Object> getWriteDataConsumer() {
            return writeDataConsumer;
        }
    }
}