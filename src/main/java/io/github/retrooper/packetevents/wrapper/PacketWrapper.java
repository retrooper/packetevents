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

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufUtil;
import io.netty.handler.codec.EncoderException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.nio.charset.StandardCharsets;
import java.util.UUID;


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
        this(clientVersion, PacketEvents.get().getServerManager().getVersion(), ByteBufUtil.buffer(), packetID);
    }

    public PacketWrapper(int packetID) {
        this(ClientVersion.UNKNOWN, PacketEvents.get().getServerManager().getVersion(), ByteBufUtil.buffer(), packetID);
    }

    public static PacketWrapper createUniversalPacketWrapper(ByteBufAbstract byteBuf) {
        return new PacketWrapper(ClientVersion.UNKNOWN, PacketEvents.get().getServerManager().getVersion(), byteBuf, -1);
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
/*
    public ItemStack readItemStackRaw() {
        net.minecraft.server.v1_7_R4.ItemStack itemstack = null;
        short short1 = this.readShort();
        if (short1 >= 0) {
            byte b0 = this.readByte();
            short short2 = this.readShort();
            itemstack = new net.minecraft.server.v1_7_R4.ItemStack(Item.getById(short1), b0, short2);
            itemstack.tag = this.b();
            if (itemstack.tag != null) {
                if (clientVersion.isNewerThanOrEquals(ClientVersion.v_1_8) && itemstack.getItem() == Items.WRITTEN_BOOK && itemstack.tag.hasKeyOfType("pages", 9)) {
                    NBTTagList nbttaglist = itemstack.tag.getList("pages", 8);
                    NBTTagList newList = new NBTTagList();

                    for(int i = 0; i < nbttaglist.size(); ++i) {
                        IChatBaseComponent s = ChatSerializer.a(nbttaglist.getString(i));
                        String newString = SpigotComponentReverter.toLegacy(s);
                        newList.add(new NBTTagString(newString));
                    }

                    itemstack.tag.set("pages", newList);
                }

                CraftItemStack.setItemMeta(itemstack, CraftItemStack.getItemMeta(itemstack));
            }
        }

        return CraftItemStack.asBukkitCopy(itemstack);
    }*/

    //TODO Finish
    public ItemStack readItemStack() {
        ItemStack itemStack = null;
        short itemID = readShort();
        if (itemID >= 0) {
            byte count = readByte();
            short data = readShort();
            itemStack = new ItemStack(Material.values()[itemID], count, data);
            int readerIndex = byteBuf.readerIndex();
            boolean hasMetaData = readByte() != 0;
            if (hasMetaData) {
                byteBuf.readerIndex(readerIndex);
                //TODO this is the NMS ItemStack#setTag NBTCompressedStreamTools.a(new ByteBufInputStream(byteBuf.rawByteBuf()), new NBTReadLimiter(2097152L));
                return itemStack;
            } else {
                return itemStack;
            }
        }
        throw new RuntimeException("Invalid ItemStack material ID: " + itemID + ". The ID may not be less than 0.");
    }

    //TODO Finish
    public void writeItemStack(ItemStack itemStack) {

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
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > maxLen) {
            throw new EncoderException("String too big (was " + bytes.length + " bytes encoded, max " + maxLen + ")");
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

    public boolean readBoolean() {
        return byteBuf.readBoolean();
    }

    public void writeBoolean(boolean value) {
        byteBuf.writeBoolean(value);
    }

    public byte[] readByteArray(int length) {
        byte[] ret = new byte[length];
        byteBuf.readBytes(ret);
        return ret;
    }

    public void writeByteArray(byte[] array) {
        byteBuf.writeBytes(array);
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
}
