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

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutTransaction extends WrappedPacket implements SendableWrapper {
    private static Class<?> packetClass;
    private static Constructor<?> packetConstructor;
    private int windowId;
    private short actionNumber;
    private boolean accepted;
    private boolean isListening = false;
    public WrappedPacketOutTransaction(final Object packet) {
        super(packet);
        isListening = true;
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

    /**
     * Get the Window ID.
     * @return Get Window ID
     */
    public int getWindowId() {
        if(isListening) {
            return readInt(0);
        }
        else {
            return windowId;
        }
    }

    /**
     * Get the action number.
     * @return Get Action Number
     */
    public short getActionNumber() {
        if(isListening) {
            return readShort(0);
        }
        else {
            return actionNumber;
        }
    }

    /**
     * Has the transaction packet been accepted?
     * @return Is Accepted
     */
    public boolean isAccepted() {
        if(isListening) {
            return readBoolean(0);
        }
        else {
            return accepted;
        }
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
