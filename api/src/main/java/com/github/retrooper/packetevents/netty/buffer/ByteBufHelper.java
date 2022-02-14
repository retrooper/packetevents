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

package com.github.retrooper.packetevents.netty.buffer;

import com.github.retrooper.packetevents.PacketEvents;

public class ByteBufHelper {
    public static int readerIndex(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readerIndex(buffer);
    }

    public static Object readerIndex(Object buffer, int readerIndex) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readerIndex(buffer, readerIndex);
    }

    public static int writerIndex(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().writerIndex(buffer);
    }

    public static Object writerIndex(Object buffer, int writerIndex) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().writerIndex(buffer, writerIndex);
    }

    public static int readableBytes(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readableBytes(buffer);
    }

    public static int writableBytes(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().writableBytes(buffer);
    }

    public static Object clear(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().clear(buffer);
    }

    public static byte readByte(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readByte(buffer);
    }

    public static void writeByte(Object buffer, int value) {
        PacketEvents.getAPI().getNettyManager().getByteBufHandler().writeByte(buffer, value);
    }

    public static boolean readBoolean(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readBoolean(buffer);
    }

    public static void writeBoolean(Object buffer, boolean value) {
        PacketEvents.getAPI().getNettyManager().getByteBufHandler().writeBoolean(buffer, value);
    }

    public static int readUnsignedByte(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readUnsignedByte(buffer);
    }

    public static char readChar(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readChar(buffer);
    }

    public static void writeChar(Object buffer, char value) {
        PacketEvents.getAPI().getNettyManager().getByteBufHandler().writeChar(buffer, value);
    }

    public static short readShort(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readShort(buffer);
    }

    public static int readUnsignedShort(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readUnsignedShort(buffer);
    }

    public static void writeShort(Object buffer, int value) {
        PacketEvents.getAPI().getNettyManager().getByteBufHandler().writeShort(buffer, value);
    }

    public static int readInt(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readInt(buffer);
    }

    public static void writeInt(Object buffer, int value) {
        PacketEvents.getAPI().getNettyManager().getByteBufHandler().writeInt(buffer, value);
    }

    public static long readUnsignedInt(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readUnsignedInt(buffer);
    }

    public static long readLong(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readLong(buffer);
    }

    public static void writeLong(Object buffer, long value) {
        PacketEvents.getAPI().getNettyManager().getByteBufHandler().writeLong(buffer, value);
    }

    public static float readFloat(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readFloat(buffer);
    }

    public static void writeFloat(Object buffer, float value) {
        PacketEvents.getAPI().getNettyManager().getByteBufHandler().writeFloat(buffer, value);
    }

    public static double readDouble(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readDouble(buffer);
    }

    public static void writeDouble(Object buffer, double value) {
        PacketEvents.getAPI().getNettyManager().getByteBufHandler().writeDouble(buffer, value);
    }

    public static Object copy(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().copy(buffer);
    }

    public static Object duplicate(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().duplicate(buffer);
    }

    public static boolean hasArray(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().hasArray(buffer);
    }

    public static byte[] array(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().array(buffer);
    }

    public static Object retain(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().retain(buffer);
    }

    public static Object readBytes(Object buffer, int length) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().readBytes(buffer, length);
    }

    public static void readBytes(Object buffer, byte[] bytes) {
        PacketEvents.getAPI().getNettyManager().getByteBufHandler().readBytes(buffer, bytes);
    }

    public static boolean release(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().release(buffer);
    }

    public static int refCnt(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().refCnt(buffer);
    }

    public static Object skipBytes(Object buffer, int length) {
        return PacketEvents.getAPI().getNettyManager().getByteBufHandler().skipBytes(buffer, length);
    }
}
