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

package io.github.retrooper.packetevents.packetwrappers.play.out.keepalive;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.reflection.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutKeepAlive extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> keepAliveConstructor;
    private static boolean integerMode;
    private long id;


    public WrappedPacketOutKeepAlive(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutKeepAlive(long id) {
        this.id = id;
    }

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Server.KEEP_ALIVE;
        integerMode = Reflection.getField(packetClass, int.class, 0) != null;

        if (integerMode) {
            try {
                keepAliveConstructor = packetClass.getConstructor(int.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            try {
                keepAliveConstructor = packetClass.getConstructor(long.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public long getId() {
        if (packet != null) {
            if (integerMode) {
                return readInt(0);
            } else {
                return readLong(0);
            }
        } else {
            return id;
        }
    }

    public void setId(long id) throws UnsupportedOperationException {
        if (packet != null) {
            if (integerMode) {
                if (id < Integer.MIN_VALUE || id > Integer.MAX_VALUE) {
                    throw new UnsupportedOperationException("PacketEvents failed to set the Keep Alive ID in WrappedPacketOutKeepAlive. Your server version does not support IDs outside the range of an int primitive type. Your Keep Alive ID seems to be in the range of a long primitive type.");
                }
            }
        }
    }

    @Override
    public Object asNMSPacket() {
        if (integerMode) {
            try {
                return keepAliveConstructor.newInstance((int) getId());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return keepAliveConstructor.newInstance(getId());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
