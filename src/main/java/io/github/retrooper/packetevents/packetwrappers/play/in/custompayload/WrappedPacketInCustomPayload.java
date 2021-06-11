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

package io.github.retrooper.packetevents.packetwrappers.play.in.custompayload;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import org.bukkit.entity.Player;

public final class WrappedPacketInCustomPayload extends WrappedPacket {
    private static boolean v_1_17, strPresent, byteArrayPresent;

    public WrappedPacketInCustomPayload(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Client.CUSTOM_PAYLOAD;
        strPresent = Reflection.getField(packetClass, String.class, 0) != null;
        byteArrayPresent = Reflection.getField(packetClass, byte[].class, 0) != null;
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
    }

    public String getTag() {
        if (strPresent) {
            return readString(0);
        } else {
            Object minecraftKey = readObject(v_1_17 ? 1 : 0, NMSUtils.minecraftKeyClass);
            return NMSUtils.getStringFromMinecraftKey(minecraftKey);
        }
    }

    public void setTag(String tag) {
        if (strPresent) {
            writeString(0, tag);
        } else {
            Object minecraftKey = NMSUtils.generateMinecraftKey(tag);
            write(NMSUtils.minecraftKeyClass, v_1_17 ? 1 : 0, minecraftKey);
        }
    }

    public byte[] getData() {
        if (byteArrayPresent) {
            return readByteArray(0);
        } else {
            return PacketEvents.get().getByteBufUtil().getBytes(getBuffer());
        }
    }

    public void setData(byte[] data) {
        if (byteArrayPresent) {
            writeByteArray(0, data);
        } else {
            PacketEvents.get().getByteBufUtil().setBytes(getBuffer(), data);
        }
    }

    private Object getBuffer() {
        Object dataSerializer = readObject(0, NMSUtils.packetDataSerializerClass);
        WrappedPacket dataSerializerWrapper = new WrappedPacket(new NMSPacket(dataSerializer));

        return dataSerializerWrapper.readObject(0, NMSUtils.byteBufClass);
    }

    public void retain() {
        if (packet != null && !byteArrayPresent) {
            PacketEvents.get().getByteBufUtil().retain(getBuffer());
        }
    }

    public void release() {
        if (packet != null && !byteArrayPresent) {
            PacketEvents.get().getByteBufUtil().release(getBuffer());
        }
    }

}
