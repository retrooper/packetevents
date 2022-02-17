/*
 * This file is part of ProtocolSupport - https://github.com/ProtocolSupport/ProtocolSupport
 * Copyright (C) 2021 ProtocolSupport
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.protocol.nbt.codec;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufInputStream;
import com.github.retrooper.packetevents.netty.buffer.ByteBufOutputStream;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import com.github.retrooper.packetevents.protocol.nbt.serializer.DefaultNBTSerializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NBTCodec {
    public static NBTCompound readNBT(Object byteBuf, ServerVersion serverVersion) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            try {
                return (NBTCompound) DefaultNBTSerializer.INSTANCE.deserializeTag(
                        new ByteBufInputStream(byteBuf));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                final short length = ByteBufHelper.readShort(byteBuf);
                if (length < 0) {
                    return null;
                }
                Object slicedBuffer = ByteBufHelper.readSlice(byteBuf, length);
                try (DataInputStream stream = new DataInputStream(
                        new GZIPInputStream(new ByteBufInputStream(slicedBuffer)))) {
                    return (NBTCompound) DefaultNBTSerializer.INSTANCE.deserializeTag(stream);
                }
            }
            catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
        return null;
    }

    public static void writeNBT(Object byteBuf, ServerVersion serverVersion, NBTCompound tag) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            try (ByteBufOutputStream outputStream = new ByteBufOutputStream(byteBuf)) {
                if (tag != null) {
                    DefaultNBTSerializer.INSTANCE.serializeTag(outputStream, tag);
                } else {
                    DefaultNBTSerializer.INSTANCE.serializeTag(outputStream, NBTEnd.INSTANCE);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        else {
            if (tag == null) {
                ByteBufHelper.writeShort(byteBuf, -1);
            } else {
                int lengthWriterIndex = ByteBufHelper.writerIndex(byteBuf);
                ByteBufHelper.writeShort(byteBuf, 0);
                int writerIndexDataStart = ByteBufHelper.writerIndex(byteBuf);
                try (DataOutputStream outputstream = new DataOutputStream(new GZIPOutputStream(new ByteBufOutputStream(byteBuf)))) {
                    DefaultNBTSerializer.INSTANCE.serializeTag(outputstream, tag);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
                int writerIndexDataEnd = ByteBufHelper.writerIndex(byteBuf);
                ByteBufHelper.writerIndex(byteBuf, lengthWriterIndex);
                ByteBufHelper.writeShort(byteBuf, writerIndexDataEnd - writerIndexDataStart);
                ByteBufHelper.writerIndex(byteBuf, writerIndexDataEnd);
            }
        }
    }
}
