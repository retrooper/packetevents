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

package io.github.retrooper.packetevents.packetwrappers.login.out.setcompression;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

//This packet does not exist on 1.7.10
public class WrappedPacketLoginOutSetCompression extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> constructor;
    private int threshold;

    public WrappedPacketLoginOutSetCompression(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketLoginOutSetCompression(int threshold) {
        this.threshold = threshold;
    }

    @Override
    protected void load() {
        try {
            if (PacketTypeClasses.Login.Server.SET_COMPRESSION != null) {
                constructor = PacketTypeClasses.Login.Server.SET_COMPRESSION.getConstructor(int.class);
            }
        } catch (NoSuchMethodException e) {
            //probably 1.7.10
        }
    }

    /**
     * Maximum size of a packet before it can be compressed.
     * @return threshold Threshold
     */
    public int getThreshold() {
        if (packet != null) {
            return readInt(0);
        }
        return threshold;
    }

    @Override
    public Object asNMSPacket() {
        try {
            return constructor.newInstance(threshold);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isSupported() {
        return PacketTypeClasses.Login.Server.SET_COMPRESSION != null;
    }
}
