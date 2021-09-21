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

package io.github.retrooper.packetevents.wrapper;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.manager.server.ServerManager;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.utils.BlockPosition;
import io.github.retrooper.packetevents.utils.MinecraftReflection;
import io.github.retrooper.packetevents.utils.StringUtil;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufUtil;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import org.bukkit.inventory.ItemStack;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;


public class PacketWrapper<T extends PacketWrapper> {
    public final ByteBufAbstract byteBuf;
    protected final ClientVersion clientVersion;
    protected final ServerVersion serverVersion;
    private final int packetID;

    public PacketWrapper(ClientVersion clientVersion, ServerVersion serverVersion, ByteBufAbstract byteBuf, int packetID) {
        this.clientVersion = clientVersion;
        this.serverVersion = serverVersion;
        this.byteBuf = byteBuf;
        this.packetID = packetID;
    }

    public PacketWrapper(PacketReceiveEvent event) {
        this.clientVersion = event.getClientVersion();
        this.serverVersion = event.getServerVersion();
        this.byteBuf = event.getByteBuf();
        this.packetID = event.getPacketID();
        if (event.getCurrentPacketWrapper() == null) {
            event.setCurrentPacketWrapper(this);
            readData();
        } else {
            readData((T) event.getCurrentPacketWrapper());
        }
    }

    public PacketWrapper(PacketSendEvent event) {
        this.clientVersion = event.getClientVersion();
        this.serverVersion = event.getServerVersion();
        this.byteBuf = event.getByteBuf();
        this.packetID = event.getPacketID();
        if (event.getCurrentPacketWrapper() == null) {
            event.setCurrentPacketWrapper(this);
            readData();
        } else {
            readData((T) event.getCurrentPacketWrapper());
        }
    }

    public PacketWrapper(int packetID, ClientVersion clientVersion) {
        this(clientVersion, ServerManager.getVersion(), ByteBufUtil.buffer(), packetID);
    }

    public PacketWrapper(int packetID) {
        this(ClientVersion.UNKNOWN, ServerManager.getVersion(), ByteBufUtil.buffer(), packetID);
    }

    public static PacketWrapper createUniversalPacketWrapper(ByteBufAbstract byteBuf) {
        return new PacketWrapper(ClientVersion.UNKNOWN, ServerManager.getVersion(), byteBuf, -1);
    }

    public void createPacket() {
        writeVarInt(packetID);
        writeData();
    }

    public void readData() {

    }

    public void readData(T wrapper) {

    }

    public void writeData() {

    }

    public ClientVersion getClientVersion() {
        return clientVersion;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public ByteBufAbstract getByteBuf() {
        return byteBuf;
    }

    public int getPacketID() {
        return packetID;
    }

    public void resetByteBuf() {
        byteBuf.clear();
        writeVarInt(packetID);
    }

    public byte readByte() {
        return byteBuf.readByte();
    }

    public void writeByte(int value) {
        byteBuf.writeByte(value);
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
        return byteBuf.readInt();
    }

    public void writeInt(int value) {
        byteBuf.writeInt(value);
    }

    public int readVarInt() {
        byte b0;
        int i = 0;
        int j = 0;
        do {
            b0 = byteBuf.readByte();
            i |= (b0 & Byte.MAX_VALUE) << j++ * 7;
            if (j > 5)
                throw new RuntimeException("VarInt too big");
        } while ((b0 & 0x80) == 128);
        return i;
    }

    public void writeVarInt(int value) {
        while ((value & -128) != 0) {
            byteBuf.writeByte(value & 127 | 128);
            value >>>= 7;
        }

        byteBuf.writeByte(value);
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

    public ItemStack readItemStack() {
        Object packetDataSerializer = MinecraftReflection.createPacketDataSerializer(byteBuf.rawByteBuf());
        Object nmsItemStack = MinecraftReflection.readNMSItemStackPacketDataSerializer(packetDataSerializer);
        return MinecraftReflection.toBukkitItemStack(nmsItemStack);
    }

    public void writeItemStack(ItemStack itemStack) {
        Object packetDataSerializer = MinecraftReflection.createPacketDataSerializer(byteBuf.rawByteBuf());
        Object nmsItemStack = MinecraftReflection.toNMSItemStack(itemStack);
        MinecraftReflection.writeNMSItemStackPacketDataSerializer(packetDataSerializer, nmsItemStack);
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
            String s = byteBuf.toString(byteBuf.readerIndex(), j, StandardCharsets.UTF_8);
            byteBuf.readerIndex(byteBuf.readerIndex() + j);
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
        if (bytes.length > maxLen) {
            throw new IllegalStateException("String too big (was " + bytes.length + " bytes encoded, max " + maxLen + ")");
        } else {
            writeVarInt(bytes.length);
            byteBuf.writeBytes(bytes);
        }
    }

    public int readUnsignedShort() {
        return byteBuf.readUnsignedShort();
    }

    public short readShort() {
        return byteBuf.readShort();
    }

    public void writeShort(int value) {
        byteBuf.writeShort(value);
    }

    public long readLong() {
        return byteBuf.readLong();
    }

    public void writeLong(long value) {
        byteBuf.writeLong(value);
    }

    public float readFloat() {
        return byteBuf.readFloat();
    }

    public void writeFloat(float value) {
        byteBuf.writeFloat(value);
    }

    public double readDouble() {
        return byteBuf.readDouble();
    }

    public void writeDouble(double value) {
        byteBuf.writeDouble(value);
    }

    public byte[] readByteArray(int length) {
        byte[] ret = new byte[length];
        byteBuf.readBytes(ret);
        return ret;
    }

    public void writeByteArray(byte[] array) {
        byteBuf.writeBytes(array);
    }

    public long[] readLongArray() {
        int readableBytes = byteBuf.readableBytes() / 8;
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

    public BlockPosition readBlockPos() {
        long val = readLong();
        int x = (int) (val >> 38);

        // 1.14 method for this is storing X Z Y
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_14)) {
            int y = (int) (val & 0xFFF);
            int z = (int) (val << 26 >> 38);
            return new BlockPosition(x, y, z);
        }
        // 1.13 and below store X Y Z
        int y = (int) ((val >> 26) & 0xFFF);
        int z = (int) (val << 38 >> 38);
        return new BlockPosition(x, y, z);
    }

    public void writeBlockPos(BlockPosition pos) {
        // 1.14 method for this is storing X Z Y
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_14)) {
            long val = ((long) (pos.getX() & 0x3FFFFFF) << 38) | ((long) (pos.getZ() & 0x3FFFFFF) << 12) | (pos.getY() & 0xFFF);
            writeLong(val);
        }
        // 1.13 and below store X Y Z
        long val = ((long) (pos.getX() & 0x3FFFFFF) << 38) | ((long) (pos.getY() & 0xFFF) << 26) | (pos.getZ() & 0x3FFFFFF);
        writeLong(val);
    }
}
