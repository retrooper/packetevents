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

package io.github.retrooper.packetevents.packetwrappers.login.in.custompayload;

import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;

/**
 * This packet exists since 1.13.2
 */
public class WrappedPacketLoginInCustomPayload extends WrappedPacket {
    private static Class<?> byteBufClass;
    private static Class<?> packetDataSerializerClass;

    public WrappedPacketLoginInCustomPayload(Object packet) {
        super(packet);
    }

    public static void load() {
        packetDataSerializerClass = NMSUtils.getNMSClassWithoutException("PacketDataSerializer");
        try {
            byteBufClass = NMSUtils.getNettyClass("buffer.ByteBuf");
        } catch (ClassNotFoundException ignored) {
        }
    }

    public int getMessageId() {
        return readInt(0);
    }

    /* TODO wrappers
     * net.minecraft.server.v1_16_R2.PacketLoginOutCustomPayload outCP;
     * net.minecraft.server.v1_16_R2.PacketPlayOutLogin outLogin;
     * Find out about the Status Response packet
     */

    public byte[] getData() {
        Object dataSerializer = readObject(0, packetDataSerializerClass);
        WrappedPacket byteBufWrapper = new WrappedPacket(dataSerializer);
        Object byteBuf = byteBufWrapper.readObject(0, byteBufClass);
        return ByteBufUtil.getBytes(byteBuf);
    }
}

