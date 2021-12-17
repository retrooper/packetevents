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

package com.github.retrooper.packetevents.wrapper;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.protocol.chat.component.BaseComponent;
import com.github.retrooper.packetevents.protocol.chat.component.serializer.ComponentSerializer;
import com.github.retrooper.packetevents.protocol.datawatcher.WatchableObject;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.codec.NBTCodec;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.StringUtil;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PacketWrapper<T extends PacketWrapper> {
    public final ByteBufAbstract buffer;
    protected ClientVersion clientVersion;
    protected ServerVersion serverVersion;
    private final int packetID;
    private boolean hasPreparedForSending;

    private static final int MODERN_MESSAGE_LENGTH = 262144;
    private static final int LEGACY_MESSAGE_LENGTH = 32767;

    public PacketWrapper(ClientVersion clientVersion, ServerVersion serverVersion, ByteBufAbstract buffer, int packetID) {
        this.clientVersion = clientVersion;
        this.serverVersion = serverVersion;
        this.buffer = buffer;
        this.packetID = packetID;
    }

    public PacketWrapper(PacketReceiveEvent event) {
        this.clientVersion = event.getClientVersion();
        this.serverVersion = event.getServerVersion();
        this.buffer = event.getByteBuf();
        this.packetID = event.getPacketId();
        if (event.getLastUsedWrapper() == null) {
            event.setLastUsedWrapper(this);
            readData();
        } else {
            readData((T) event.getLastUsedWrapper());
        }
    }

    public PacketWrapper(PacketSendEvent event) {
        this.clientVersion = event.getClientVersion();
        this.serverVersion = event.getServerVersion();
        this.buffer = event.getByteBuf();
        this.packetID = event.getPacketId();
        if (event.getLastUsedWrapper() == null) {
            event.setLastUsedWrapper(this);
            readData();
        } else {
            readData((T) event.getLastUsedWrapper());
        }
    }

    public PacketWrapper(int packetID, ClientVersion clientVersion) {
        this(clientVersion, PacketEvents.getAPI().getServerManager().getVersion(),
                PacketEvents.getAPI().getNettyManager().buffer(), packetID);
    }

    public PacketWrapper(int packetID) {
        this(ClientVersion.UNKNOWN,
                PacketEvents.getAPI().getServerManager().getVersion(),
                PacketEvents.getAPI().getNettyManager().buffer(), packetID);
    }

    public PacketWrapper(PacketTypeCommon packetType) {
        this(packetType.getId());
    }

    public static PacketWrapper<?> createUniversalPacketWrapper(ByteBufAbstract byteBuf) {
        return new PacketWrapper(ClientVersion.UNKNOWN, PacketEvents.getAPI().getServerManager().getVersion(), byteBuf, -1);
    }

    public final void prepareForSend() {
        if (!hasPreparedForSending) {
            writeVarInt(packetID);
            writeData();
            hasPreparedForSending = true;
        }
    }

    public void readData() {

    }

    public void readData(T wrapper) {

    }

    public void writeData() {

    }

    public boolean hasPreparedForSending() {
        return hasPreparedForSending;
    }

    public void setHasPrepareForSending(boolean hasPreparedForSending) {
        this.hasPreparedForSending = hasPreparedForSending;
    }

    public ClientVersion getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(ClientVersion clientVersion) {
        this.clientVersion = clientVersion;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(ServerVersion serverVersion) {
        this.serverVersion = serverVersion;
    }

    public ByteBufAbstract getBuffer() {
        return buffer;
    }

    public int getPacketId() {
        return packetID;
    }

    public int getMaxMessageLength() {
        return serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? MODERN_MESSAGE_LENGTH : LEGACY_MESSAGE_LENGTH;
    }

    public void resetByteBuf() {
        buffer.clear();
        writeVarInt(packetID);
    }

    public byte readByte() {
        return buffer.readByte();
    }

    public void writeByte(int value) {
        buffer.writeByte(value);
    }

    public short readUnsignedByte() {
        return (short) (readByte() & 255);
    }

    public boolean readBoolean() {
        return readByte() != 0;
    }

    public void writeBoolean(boolean value) {
        writeByte(value ? 1 : 0);
    }

    public int readInt() {
        return buffer.readInt();
    }

    public void writeInt(int value) {
        buffer.writeInt(value);
    }

    public int readVarInt() {
        byte b0;
        int i = 0;
        int j = 0;
        do {
            b0 = buffer.readByte();
            i |= (b0 & Byte.MAX_VALUE) << j++ * 7;
            if (j > 5)
                throw new RuntimeException("VarInt too big");
        } while ((b0 & 0x80) == 128);
        return i;
    }

    public void writeVarInt(int value) {
        while ((value & -128) != 0) {
            buffer.writeByte(value & 127 | 128);
            value >>>= 7;
        }

        buffer.writeByte(value);
    }

    public <K, V> Map<K, V> readMap(Function<PacketWrapper<?>, K> keyFunction, Function<PacketWrapper<?>, V> valueFunction) {
        int size = readVarInt();
        Map<K, V> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            K key = keyFunction.apply(this);
            V value = valueFunction.apply(this);
            map.put(key, value);
        }
        return map;
    }

    public <K, V> void writeMap(Map<K, V> map, BiConsumer<PacketWrapper<?>, K> keyConsumer, BiConsumer<PacketWrapper<?>, V> valueConsumer) {
        writeVarInt(map.size());
        for (K key : map.keySet()) {
            V value = map.get(key);
            keyConsumer.accept(this, key);
            valueConsumer.accept(this, value);
        }
    }

    @NotNull
    public ItemStack readItemStack() {
        boolean v1_13_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_2);
        if (v1_13_2) {
            if (!readBoolean()) {
                return ItemStack.NULL;
            }
        }
        int typeID = v1_13_2 ? readVarInt() : readShort();
        if (typeID < 0) {
            return ItemStack.NULL;
        }
        ItemType type = ItemTypes.getById(typeID);
        int amount = readByte();
        int legacyData = v1_13_2 ? -1 : readShort();
        NBTCompound nbt = readNBT();
        return ItemStack.builder()
                .type(type)
                .amount(amount)
                .nbt(nbt)
                .legacyData(legacyData)
                .build();
    }

    public void writeItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            itemStack = ItemStack.NULL;
        }
        boolean v1_13_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_2);
        if (v1_13_2) {
            if (ItemStack.NULL.equals(itemStack)) {
                writeBoolean(false);
            } else {
                writeBoolean(true);
                int typeID;
                if (itemStack.getType() == null || ItemStack.NULL.equals(itemStack)) {
                    typeID = -1;
                } else {
                    typeID = itemStack.getType().getId();
                }
                writeVarInt(typeID);
                if (typeID >= 0) {
                    writeByte(itemStack.getAmount());
                    writeNBT(itemStack.getNBT());
                }
            }
        } else {
            int typeID;
            if (itemStack.getType() == null || itemStack.getAmount() == -1) {
                typeID = -1;
            } else {
                typeID = itemStack.getType().getId();
            }
            writeShort(typeID);
            if (typeID >= 0) {
                writeByte(itemStack.getAmount());
                writeShort(itemStack.getLegacyData());
                writeNBT(itemStack.getNBT());
            }
        }
    }

    public NBTCompound readNBT() {
        return NBTCodec.readNBT(buffer, serverVersion);
    }

    public void writeNBT(NBTCompound nbt) {
        NBTCodec.writeNBT(buffer, serverVersion, nbt);
    }

    public String readString() {
        return readString(32767);
    }

    public String readString(int maxLen) {
        int j = readVarInt();
        if (j > maxLen * 4) {
            throw new RuntimeException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + maxLen * 4 + ")");
        } else if (j < 0) {
            throw new RuntimeException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            String s = buffer.toString(buffer.readerIndex(), j, StandardCharsets.UTF_8);
            buffer.readerIndex(buffer.readerIndex() + j);
            if (s.length() > maxLen) {
                throw new RuntimeException("The received string length is longer than maximum allowed (" + j + " > " + maxLen + ")");
            } else {
                return s;
            }
        }
    }

    public void writeString(String s) {
        writeString(s, 32767);
    }

    public void writeString(String s, int maxLen) {
        writeString(s, maxLen, true);
    }

    public void writeString(String s, int maxLen, boolean substr) {
        if (substr) {
            s = StringUtil.maximizeLength(s, maxLen);
        }
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        if (!substr && bytes.length > maxLen) {
            throw new IllegalStateException("String too big (was " + bytes.length + " bytes encoded, max " + maxLen + ")");
        } else {
            writeVarInt(bytes.length);
            buffer.writeBytes(bytes);
        }
    }

    public BaseComponent readComponent() {
        return ComponentSerializer.parseJsonComponent(readString(getMaxMessageLength()));
    }

    public void writeComponent(BaseComponent component) {
        writeString(ComponentSerializer.buildJsonObject(component).toString());
    }

    public ResourceLocation readIdentifier(int maxLen) {
        return new ResourceLocation(readString(maxLen));
    }

    public ResourceLocation readIdentifier() {
        return readIdentifier(32767);
    }

    public void writeIdentifier(ResourceLocation identifier, int maxLen) {
        writeString(identifier.toString(), maxLen);
    }

    public void writeIdentifier(ResourceLocation identifier) {
        writeIdentifier(identifier, 32767);
    }

    public int readUnsignedShort() {
        return buffer.readUnsignedShort();
    }

    public short readShort() {
        return buffer.readShort();
    }

    public void writeShort(int value) {
        buffer.writeShort(value);
    }

    public int readVarShort() {
        int low = buffer.readUnsignedShort();
        int high = 0;
        if ((low & 0x8000) != 0) {
            low = low & 0x7FFF;
            high = buffer.readUnsignedByte();
        }
        return ((high & 0xFF) << 15) | low;
    }

    public void writeVarShort(int value) {
        int low = value & 0x7FFF;
        int high = (value & 0x7F8000) >> 15;
        if (high != 0) {
            low = low | 0x8000;
        }
        buffer.writeShort(low);
        if (high != 0) {
            buffer.writeByte(high);
        }
    }

    public long readLong() {
        return buffer.readLong();
    }

    public void writeLong(long value) {
        buffer.writeLong(value);
    }

    public float readFloat() {
        return buffer.readFloat();
    }

    public void writeFloat(float value) {
        buffer.writeFloat(value);
    }

    public double readDouble() {
        return buffer.readDouble();
    }

    public void writeDouble(double value) {
        buffer.writeDouble(value);
    }

    public byte[] readBytes(int size) {
        byte[] bytes = new byte[size];
        buffer.readBytes(bytes);
        return bytes;
    }

    public void writeBytes(byte[] array) {
        buffer.writeBytes(array);
    }

    public byte[] readByteArray(int maxLength) {
        int len = readVarInt();
        if (len > maxLength) {
            throw new RuntimeException("The received byte array length is longer than maximum allowed (" + len + " > " + maxLength + ")");
        }
        return readBytes(len);
    }

    public byte[] readByteArray() {
        int len = readVarInt();
        return readBytes(len);
    }

    public void writeByteArray(byte[] array) {
        writeVarInt(array.length);
        writeBytes(array);
    }

    public int[] readVarIntArray() {
        int readableBytes = buffer.readableBytes();
        int size = readVarInt();
        if (size > readableBytes) {
            throw new IllegalStateException("VarIntArray with size " + size + " is bigger than allowed " + readableBytes);
        }

        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = readVarInt();
        }
        return array;
    }

    public void writeVarIntArray(int[] array) {
        writeVarInt(array.length);
        for (int i : array) {
            writeVarInt(i);
        }
    }

    public long[] readLongArray(int size) {
        long[] array = new long[size];

        for (int i = 0; i < array.length; i++) {
            array[i] = readLong();
        }
        return array;
    }

    public long[] readLongArray() {
        int readableBytes = buffer.readableBytes() / 8;
        int size = readVarInt();
        if (size > readableBytes) {
            throw new IllegalStateException("LongArray with size " + size + " is bigger than allowed " + readableBytes);
        }
        long[] array = new long[size];

        for (int i = 0; i < array.length; i++) {
            array[i] = readLong();
        }
        return array;
    }

    public void writeLongArray(long[] array) {
        writeVarInt(array.length);
        for (long l : array) {
            writeLong(l);
        }
    }

    public UUID readUUID() {
        long mostSigBits = readLong();
        long leastSigBits = readLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    public void writeUUID(UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    public Vector3i readBlockPosition() {
        long val = readLong();
        return new Vector3i(val, serverVersion);
    }

    public void writeBlockPosition(Vector3i pos) {
        long val = pos.getSerializedPosition(serverVersion);
        writeLong(val);
    }

    public GameMode readGameMode() {
        return GameMode.getById(readByte());
    }

    public void writeGameMode(@Nullable GameMode mode) {
        int id = mode == null ? -1 : mode.getId();
        writeByte(id);
    }

    private List<WatchableObject> readWatchableObjectsLegacy() {
        List<WatchableObject> list = new ArrayList<>();
        //1.7.10 -> 1.8.8 code
        for (byte b0 = readByte(); b0 != 127; b0 = readByte()) {
            int i = (b0 & 224) >> 5;
            int j = b0 & 31;
            WatchableObject watchableObject = null;
            switch (i) {
                case 0:
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

    private void writeWatchableObjectsLegacy(List<WatchableObject> list) {
        //TODO
    }


    public List<WatchableObject> readWatchableObjects() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            List<WatchableObject> list = new ArrayList<>();
            short index;
            while ((index = readUnsignedByte()) != 255) {

                short typeID = readUnsignedByte();
                WatchableObject.Type type = WatchableObject.Type.values()[typeID];
                Object value = type.getReadDataConsumer().apply(this);
                list.add(new WatchableObject(index, type, value));
            }
            return list;
        } else {
            return readWatchableObjectsLegacy();
        }
    }

    public void writeWatchableObjects(List<WatchableObject> list) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            for (WatchableObject watchableObject : list) {
                writeByte(watchableObject.getIndex());
                writeByte(watchableObject.getType().ordinal());
                watchableObject.getType().getWriteDataConsumer().accept(this, watchableObject.getValue());
            }
        } else {
            writeWatchableObjectsLegacy(list);
        }
    }
}
