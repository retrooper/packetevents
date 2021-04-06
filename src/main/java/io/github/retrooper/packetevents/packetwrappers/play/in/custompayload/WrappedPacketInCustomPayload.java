/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetwrappers.play.in.custompayload;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;

public final class WrappedPacketInCustomPayload extends WrappedPacket {
    private static boolean strPresent, byteArrayPresent;

    public WrappedPacketInCustomPayload(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Client.CUSTOM_PAYLOAD;
        strPresent = Reflection.getField(packetClass, String.class, 0) != null;
        byteArrayPresent = Reflection.getField(packetClass, byte[].class, 0) != null;
    }

    public String getTag() {
        if (strPresent) {
            return readString(0);
        } else {
            Object minecraftKey = readObject(0, NMSUtils.minecraftKeyClass);
            return NMSUtils.getStringFromMinecraftKey(minecraftKey);
        }
    }

    public void setTag(String tag) {
        if (strPresent) {
            writeString(0, tag);
        } else {
            Object minecraftKey = NMSUtils.generateMinecraftKey(tag);
            write(NMSUtils.minecraftKeyClass, 0, minecraftKey);
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
        if (!byteArrayPresent) {
            PacketEvents.get().getByteBufUtil().retain(getBuffer());
        }
    }

    public void release() {
        if (!byteArrayPresent) {
            PacketEvents.get().getByteBufUtil().release(getBuffer());
        }
    }

}
