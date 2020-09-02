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

package io.github.retrooper.packetevents.packetwrappers.out.transaction;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutTransaction extends WrappedPacket implements Sendable {
    private static Class<?> packetClass;
    private static Constructor<?> packetConstructor;
    private int windowId;
    private short actionNumber;
    private boolean accepted;
    public WrappedPacketOutTransaction(final Object packet) {
        super(packet);
    }

    public WrappedPacketOutTransaction(final int windowId, final short actionNumber, final boolean accepted) {
        super();
        this.windowId = windowId;
        this.actionNumber = actionNumber;
        this.accepted = accepted;
    }

    public static void load() {
        packetClass = PacketTypeClasses.Server.TRANSACTION;

        try {
            packetConstructor = packetClass.getConstructor(int.class, short.class, boolean.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setup() {
        try {
            this.windowId = Reflection.getField(packetClass, int.class, 0).getInt(packet);
            this.actionNumber = Reflection.getField(packetClass, short.class, 0).getShort(packet);
            this.accepted = Reflection.getField(packetClass, boolean.class, 0).getBoolean(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public int getWindowId() {
        return windowId;
    }

    public short getActionNumber() {
        return actionNumber;
    }

    public boolean isAccepted() {
        return accepted;
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(windowId, actionNumber, accepted);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
