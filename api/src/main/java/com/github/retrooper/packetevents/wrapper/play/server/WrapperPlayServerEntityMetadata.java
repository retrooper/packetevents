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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.data.entity.EntityPose;
import com.github.retrooper.packetevents.protocol.data.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.data.world.Direction;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class WrapperPlayServerEntityMetadata extends PacketWrapper<WrapperPlayServerEntityMetadata> {
    private int entityID;
    private List<WatchableObject> watchableObjects;

    public WrapperPlayServerEntityMetadata(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityMetadata(int entityID, List<WatchableObject> watchableObjects) {
        super(PacketType.Play.Server.ENTITY_METADATA);
        this.entityID = entityID;
        this.watchableObjects = watchableObjects;
    }

    private List<WatchableObject> serializeWatchableObjectsLegacy() {
        List<WatchableObject> list = new ArrayList<>();
        //1.7.10 -> 1.8.8 code
        for (byte b0 = readByte(); b0 != 127; b0 = readByte()) {
            int i = (b0 & 224) >> 5;
            int j = b0 & 31;
            WatchableObject watchableObject = null;
            switch (i) {
                case 0:
                    //TODO Confirm j and i order
                    watchableObject = new WatchableObject(j, WatchableObject.Type.BYTE, readByte());
                    break;
                case 1:
                    watchableObject = new WatchableObject(j, WatchableObject.Type.SHORT, readShort());
                    break;
                case 2:
                    watchableObject = new WatchableObject(j, WatchableObject.Type.INTEGER, readInt());
                    break;
                case 3:
                    watchableObject = new WatchableObject(j, WatchableObject.Type.FLOAT, readFloat());
                    break;
                case 4:
                    watchableObject = new WatchableObject(j, WatchableObject.Type.STRING, readString());
                    break;
                case 5:
                    watchableObject = new WatchableObject(j, WatchableObject.Type.ITEM_STACK, readItemStack());
                    break;
                case 6: {
                    int x = readInt();
                    int y = readInt();
                    int z = readInt();
                    watchableObject = new WatchableObject(j, WatchableObject.Type.POSITION, new Vector3i(x, y, z));
                    break;
                }
                case 7: {
                    float x = readFloat();
                    float y = readFloat();
                    float z = readFloat();
                    watchableObject = new WatchableObject(j, WatchableObject.Type.ROTATION, new Vector3f(x, y, z));
                    break;
                }
            }
            list.add(watchableObject);
        }
        return list;
    }

    private List<WatchableObject> serializeWatchableObjectsModern() {
        List<WatchableObject> list = new ArrayList<>();
        short index;
        while ((index = readUnsignedByte()) != 255) {

            short typeID = readUnsignedByte();
            WatchableObject.Type type = WatchableObject.Type.values()[typeID];
            Object value = type.readDataConsumer.apply(this);
            //TODO Resolve index, is it the correct one?
            list.add(new WatchableObject(index, type, value));
        }
        return list;
    }

    @Override
    public void readData() {
        entityID = readInt();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_9)) {
            watchableObjects = serializeWatchableObjectsModern();
        } else {
            watchableObjects = serializeWatchableObjectsLegacy();
        }
    }

    @Override
    public void readData(WrapperPlayServerEntityMetadata wrapper) {
        entityID = wrapper.entityID;
        watchableObjects = wrapper.watchableObjects;
    }

    @Override
    public void writeData() {

    }

    public static class WatchableObject {

        public enum Type {
            BYTE(PacketWrapper::readByte,
                    (packetWrapper, o) -> {
                        packetWrapper.writeByte((int) o);
                    }
            ),
            INTEGER(PacketWrapper::readVarInt,
                    (packetWrapper, o) -> {
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
                        packetWrapper.writeItemStack((Object) o);
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
                return new Vector3i(packetWrapper.readInt(), packetWrapper.readInt(), packetWrapper.readInt());
            },
                    (packetWrapper, o) -> {
                        Vector3i vec = (Vector3i) o;
                        packetWrapper.writeBlockPosition(vec);
                    }),
            OPTIONAL_POSITION(packetWrapper -> {
                if (packetWrapper.readBoolean()) {
                    return Optional.of(new Vector3i(packetWrapper.readInt(), packetWrapper.readInt(), packetWrapper.readInt()));
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
            DIRECTION(packetWrapper -> {
                return Direction.getDirectionByFace(packetWrapper.readVarInt());
            },
                    (packetWrapper, o) -> {
                        packetWrapper.writeVarInt(((Direction) o).getFaceValue());
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
            NBT(PacketWrapper::readNBTTag,
                    (packetWrapper, o) -> {
                        packetWrapper.writeNBTTab((NBTCompound) o);
                    }),
            //TODO https://wiki.vg/Entity_metadata#Entity_Metadata_Format
            PARTICLE(null, null),
            //TODO
            VILLAGER_DATA(null, null),
            OPTIONAL_INTEGER(packetWrapper -> {
                if (packetWrapper.readBoolean()) {
                    return Optional.of(packetWrapper.readVarInt());
                }
                else {
                    return Optional.empty();
                }
            }, (packetWrapper, o) -> {
                Optional<Integer> optInt = (Optional<Integer>) o;
                if (optInt.isPresent()) {
                    packetWrapper.writeBoolean(true);
                    packetWrapper.writeVarInt(optInt.get());
                }
                else {
                    packetWrapper.writeBoolean(false);
                }
            }),
            POSE(packetWrapper -> EntityPose.VALUES[packetWrapper.readVarInt()], (packetWrapper, o) -> {
                packetWrapper.writeVarInt(((EntityPose)o).ordinal());
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

            public static final Type[] VALUES = values();
        }

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
    }
}
