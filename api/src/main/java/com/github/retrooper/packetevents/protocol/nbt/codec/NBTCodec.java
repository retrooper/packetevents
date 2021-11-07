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
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstract;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstractInputStream;
import com.github.retrooper.packetevents.netty.buffer.ByteBufAbstractOutputStream;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import com.github.retrooper.packetevents.protocol.nbt.serializer.DefaultNBTSerializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NBTCodec {
    public static NBTCompound readNBT(ByteBufAbstract byteBuf, ServerVersion serverVersion) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_8)) {
            try {
                return (NBTCompound) DefaultNBTSerializer.INSTANCE.deserializeTag(new ByteBufAbstractInputStream(byteBuf));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                final short length = byteBuf.readShort();
                if (length < 0) {
                    return null;
                }
                try (DataInputStream stream = new DataInputStream(new GZIPInputStream(new ByteBufAbstractInputStream(byteBuf.readSlice(length))))) {
                    return (NBTCompound) DefaultNBTSerializer.INSTANCE.deserializeTag(stream);
                }
            }
            catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
        return null;
    }

    public static void writeNBT(ByteBufAbstract byteBuf, ServerVersion serverVersion, NBTCompound tag) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_8)) {
            try (ByteBufAbstractOutputStream outputStream = new ByteBufAbstractOutputStream(byteBuf)) {
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
                byteBuf.writeShort(-1);
            } else {
                int lengthWriterIndex = byteBuf.writerIndex();
                byteBuf.writeShort(0);
                int writerIndexDataStart = byteBuf.writerIndex();
                try (DataOutputStream outputstream = new DataOutputStream(new GZIPOutputStream(new ByteBufAbstractOutputStream(byteBuf)))) {
                    DefaultNBTSerializer.INSTANCE.serializeTag(outputstream, tag);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
                int writerIndexDataEnd = byteBuf.writerIndex();
                byteBuf.writerIndex(lengthWriterIndex);
                byteBuf.writeShort(writerIndexDataEnd - writerIndexDataStart);
                byteBuf.writerIndex(writerIndexDataEnd);
            }
        }
    }
}
