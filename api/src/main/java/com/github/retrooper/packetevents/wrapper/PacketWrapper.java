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
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.netty.buffer.UnpooledByteBufAllocationHelper;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.codec.NBTCodec;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.util.StringUtil;
import com.github.retrooper.packetevents.util.Vector3i;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PacketWrapper<T extends PacketWrapper> {
    public Object buffer;
    protected ClientVersion clientVersion;
    protected ServerVersion serverVersion;
    private int packetID;
    private boolean hasPreparedForSending;
    // For sending chunk data packets, which need this data
    @Nullable
    protected User user;

    private static final int MODERN_MESSAGE_LENGTH = 262144;
    private static final int LEGACY_MESSAGE_LENGTH = 32767;

    public PacketWrapper(ClientVersion clientVersion, ServerVersion serverVersion, int packetID) {
        if (packetID == -1) {
            throw new IllegalArgumentException("Packet does not exist on this protocol version!");
        }
        this.clientVersion = clientVersion;
        this.serverVersion = serverVersion;
        this.buffer = null;
        this.packetID = packetID;
    }

    public PacketWrapper(PacketReceiveEvent event) {
        this(event, true);
    }

    public PacketWrapper(PacketReceiveEvent event, boolean readData) {
        this.clientVersion = event.getUser().getClientVersion();
        this.serverVersion = event.getServerVersion();
        this.user = event.getUser();
        this.buffer = event.getByteBuf();
        this.packetID = event.getPacketId();
        if (readData) {
            readEvent(event);
        }
    }

    public PacketWrapper(PacketSendEvent event) {
        this(event, true);
    }

    public PacketWrapper(PacketSendEvent event, boolean readData) {
        this.clientVersion = event.getUser().getClientVersion();
        this.serverVersion = event.getServerVersion();
        this.buffer = event.getByteBuf();
        this.packetID = event.getPacketId();
        this.user = event.getUser();
        if (readData) {
            readEvent(event);
        }
    }

    public PacketWrapper(int packetID, ClientVersion clientVersion) {
        this(clientVersion, PacketEvents.getAPI().getServerManager().getVersion(), packetID);
    }

    public PacketWrapper(int packetID) {
        this(ClientVersion.UNKNOWN,
                PacketEvents.getAPI().getServerManager().getVersion(),
                packetID);
    }

    public PacketWrapper(PacketTypeCommon packetType) {
        this(packetType.getId(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()));
    }

    public static PacketWrapper<?> createUniversalPacketWrapper(Object byteBuf) {
        PacketWrapper<?> wrapper = new PacketWrapper(ClientVersion.UNKNOWN, PacketEvents.getAPI().getServerManager().getVersion(), -2);
        wrapper.buffer = byteBuf;
        return wrapper;
    }

    public final void prepareForSend() {
        // Null means the packet was manually created and wasn't sent by the server itself
        // A reference count of 0 means that the packet was freed (it was already sent)
        if (buffer == null || ByteBufHelper.refCnt(buffer) == 0) {
            buffer = UnpooledByteBufAllocationHelper.buffer();
        }

        if (!hasPreparedForSending) {
            writeVarInt(packetID);
            write();
            hasPreparedForSending = true;
        }
    }

    public void read() {
    }

    //TODO Rename to copyFrom, as it copies data from the passed in wrapper.
    public void copy(T wrapper) {

    }

    public void write() {

    }

    //TODO public void transform(int protocolVersion) {}
    //Current idea change server version, but still think more

    public final void readEvent(ProtocolPacketEvent<?> event) {
        PacketWrapper<?> last = event.getLastUsedWrapper();
        if (last != null) {
            copy((T) last);
        } else {
            read();
        }
        event.setLastUsedWrapper(this);
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

    public Object getBuffer() {
        return buffer;
    }

    public int getPacketId() {
        return packetID;
    }

    public void setPacketId(int packetID) {
        this.packetID = packetID;
    }

    public int getMaxMessageLength() {
        return serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? MODERN_MESSAGE_LENGTH : LEGACY_MESSAGE_LENGTH;
    }

    public void resetByteBuf() {
        ByteBufHelper.clear(buffer);
        writeVarInt(packetID);
    }

    public byte readByte() {
        return ByteBufHelper.readByte(buffer);
    }

    public void writeByte(int value) {
        ByteBufHelper.writeByte(buffer, value);
    }

    public short readUnsignedByte() {
        return ByteBufHelper.readUnsignedByte(buffer);
    }

    public boolean readBoolean() {
        return readByte() != 0;
    }

    public void writeBoolean(boolean value) {
        writeByte(value ? 1 : 0);
    }

    public int readInt() {
        return ByteBufHelper.readInt(buffer);
    }

    public void writeInt(int value) {
        ByteBufHelper.writeInt(buffer, value);
    }

    public int readVarInt() {
        int value = 0;
        int length = 0;
        byte currentByte;
        do {
            currentByte = readByte();
            value |= (currentByte & 0x7F) << (length * 7);
            length++;
            if (length > 5) {
                throw new RuntimeException("VarInt is too large. Must be smaller than 5 bytes.");
            }
        } while ((currentByte & 0x80) == 0x80);
        return value;
    }

    public void writeVarInt(int value) {
        while (true) {
            if ((value & ~0x7F) == 0) {
                writeByte(value);
                return;
            }
            writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
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

    public VillagerData readVillagerData() {
        int villagerTypeId = readVarInt();
        int villagerProfessionId = readVarInt();
        int level = readVarInt();
        return new VillagerData(villagerTypeId, villagerProfessionId, level);
    }

    public void writeVillagerData(VillagerData data) {
        writeVarInt(data.getType().getId());
        writeVarInt(data.getProfession().getId());
        writeVarInt(data.getLevel());
    }

    @NotNull
    public ItemStack readItemStack() {
        boolean v1_13_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_2);
        if (v1_13_2) {
            if (!readBoolean()) {
                return ItemStack.EMPTY;
            }
        }
        int typeID = v1_13_2 ? readVarInt() : readShort();
        if (typeID < 0 && !v1_13_2) { // 1.13.2 doesn't have this logic
            return ItemStack.EMPTY;
        }
        ItemType type = ItemTypes.getById(serverVersion.toClientVersion(), typeID);
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
            itemStack = ItemStack.EMPTY;
        }
        boolean v1_13_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_2);
        if (v1_13_2) {
            if (itemStack.isEmpty()) {
                writeBoolean(false);
            } else {
                writeBoolean(true);
                int typeID = itemStack.getType().getId(serverVersion.toClientVersion());
                writeVarInt(typeID);
                writeByte(itemStack.getAmount());
                writeNBT(itemStack.getNBT());
            }
        } else {
            int typeID;
            if (itemStack.isEmpty()) {
                typeID = -1;
            } else {
                typeID = itemStack.getType().getId(serverVersion.toClientVersion());
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
            String s = ByteBufHelper.toString(buffer, ByteBufHelper.readerIndex(buffer), j, StandardCharsets.UTF_8);
            ByteBufHelper.readerIndex(buffer, ByteBufHelper.readerIndex(buffer) + j);
            if (s.length() > maxLen) {
                throw new RuntimeException("The received string length is longer than maximum allowed (" + j + " > " + maxLen + ")");
            } else {
                return s;
            }
        }
    }

    public String readComponentJSON() {
        return readString(getMaxMessageLength());
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
            ByteBufHelper.writeBytes(buffer, bytes);
        }
    }

    public void writeComponentJSON(String json) {
        writeString(json, getMaxMessageLength());
    }

    public Component readComponent() {
        return AdventureSerializer.parseComponent(readComponentJSON());
    }

    public void writeComponent(Component component) {
        writeComponentJSON(AdventureSerializer.toJson(component));
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
        return ByteBufHelper.readUnsignedShort(buffer);
    }

    public short readShort() {
        return ByteBufHelper.readShort(buffer);
    }

    public void writeShort(int value) {
        ByteBufHelper.writeShort(buffer, value);
    }

    public int readVarShort() {
        int low = readUnsignedShort();
        int high = 0;
        if ((low & 0x8000) != 0) {
            low = low & 0x7FFF;
            high = readUnsignedByte();
        }
        return ((high & 0xFF) << 15) | low;
    }

    public void writeVarShort(int value) {
        int low = value & 0x7FFF;
        int high = (value & 0x7F8000) >> 15;
        if (high != 0) {
            low = low | 0x8000;
        }
        writeShort(low);
        if (high != 0) {
            writeByte(high);
        }
    }

    public long readLong() {
        return ByteBufHelper.readLong(buffer);
    }

    public void writeLong(long value) {
        ByteBufHelper.writeLong(buffer, value);
    }

    public long readVarLong() {
        long value = 0;
        int size = 0;
        int b;
        while (((b = readByte()) & 0x80) == 0x80) {
            value |= (long) (b & 0x7F) << (size++ * 7);
        }
        return value | ((long) (b & 0x7F) << (size * 7));
    }

    public void writeVarLong(long l) {
        while ((l & ~0x7F) != 0) {
            this.writeByte((int) (l & 0x7F) | 0x80);
            l >>>= 7;
        }

        this.writeByte((int) l);
    }

    public float readFloat() {
        return ByteBufHelper.readFloat(buffer);
    }

    public void writeFloat(float value) {
        ByteBufHelper.writeFloat(buffer, value);
    }

    public double readDouble() {
        return ByteBufHelper.readDouble(buffer);
    }

    public void writeDouble(double value) {
        ByteBufHelper.writeDouble(buffer, value);
    }

    public byte[] readRemainingBytes() {
        return readBytes(ByteBufHelper.readableBytes(buffer));
    }

    public byte[] readBytes(int size) {
        byte[] bytes = new byte[size];
        ByteBufHelper.readBytes(buffer, bytes);
        return bytes;
    }

    public void writeBytes(byte[] array) {
        ByteBufHelper.writeBytes(buffer, array);
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
        int readableBytes = ByteBufHelper.readableBytes(buffer);
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

    public byte[] readByteArrayOfSize(int size) {
        byte[] array = new byte[size];
        ByteBufHelper.readBytes(buffer, array);
        return array;
    }

    public void writeByteArrayOfSize(byte[] array) {
        ByteBufHelper.writeBytes(buffer, array);
    }

    public int[] readVarIntArrayOfSize(int size) {
        int[] array = new int[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = readVarInt();
        }
        return array;
    }

    public void writeVarIntArrayOfSize(int[] array) {
        for (int i : array) {
            writeVarInt(i);
        }
    }

    public long[] readLongArray() {
        int readableBytes = ByteBufHelper.readableBytes(buffer) / 8;
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

    public List<EntityData> readEntityMetadata() {
        List<EntityData> list = new ArrayList<>();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            boolean v1_10 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10);
            short index;
            while ((index = readUnsignedByte()) != 255) {
                int typeID = v1_10 ? readVarInt() : readUnsignedByte();
                EntityDataType<?> type = EntityDataTypes.getById(serverVersion.toClientVersion(), typeID);
                Object value = type.getDataDeserializer().apply(this);
                list.add(new EntityData(index, type, value));
            }
        } else {
            for (byte data = readByte(); data != 127; data = readByte()) {
                int typeID = (data & 224) >> 5;
                int index = data & 31;
                EntityDataType<?> type = EntityDataTypes.getById(serverVersion.toClientVersion(), typeID);
                Object value = type.getDataDeserializer().apply(this);
                EntityData entityData = new EntityData(index, type, value);
                list.add(entityData);
            }
        }
        return list;
    }

    public void writeEntityMetadata(List<EntityData> list) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            boolean v1_10 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10);
            for (EntityData entityData : list) {
                writeByte(entityData.getIndex());
                if (v1_10) {
                    writeVarInt(entityData.getType().getId(serverVersion.toClientVersion()));
                } else {
                    writeByte(entityData.getType().getId(serverVersion.toClientVersion()));
                }
                entityData.getType().getDataSerializer().accept(this, entityData.getValue());
            }
            writeByte(255); // End of metadata array
        } else {
            for (EntityData entityData : list) {
                int typeID = entityData.getType().getId(serverVersion.toClientVersion());
                int index = entityData.getIndex();
                int data = (typeID << 5 | index & 31) & 255;
                writeByte(data);
                entityData.getType().getDataSerializer().accept(this, entityData.getValue());
            }
            writeByte(127); // End of metadata array
        }
    }
}
