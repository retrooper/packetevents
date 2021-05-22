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

package io.github.retrooper.packetevents.packetwrappers.login.in.custompayload;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

public class WrappedPacketLoginInCustomPayload extends WrappedPacket {
    public WrappedPacketLoginInCustomPayload(NMSPacket packet) {
        super(packet);
    }

    public int getMessageId() {
        return readInt(0);
    }

    public void setMessageId(int id) {
        writeInt(0, id);
    }

    public byte[] getData() {
        return PacketEvents.get().getByteBufUtil().getBytes(getBuffer());
    }

    public void setData(byte[] data) {
        Object dataSerializer = readObject(0, NMSUtils.packetDataSerializerClass);
        WrappedPacket dataSerializerWrapper = new WrappedPacket(new NMSPacket(dataSerializer));
        PacketEvents.get().getByteBufUtil().setBytes(getBuffer(), data);
    }

    private Object getBuffer() {
        Object dataSerializer = readObject(0, NMSUtils.packetDataSerializerClass);
        WrappedPacket byteBufWrapper = new WrappedPacket(new NMSPacket(dataSerializer));
        return byteBufWrapper.readObject(0, NMSUtils.byteBufClass);
    }

    public void retain() {
        PacketEvents.get().getByteBufUtil().retain(getBuffer());
    }

    public void release() {
        PacketEvents.get().getByteBufUtil().release(getBuffer());
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_12_2);
    }
}

