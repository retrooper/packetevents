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

package io.github.retrooper.packetevents.packetwrappers.play.out.openwindowhorse;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Wrapper for the OpenWindowHorse packet.
 *
 * @author retrooper, Tecnio
 * @since 1.8
 */
public final class WrappedPacketOutOpenWindowHorse extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private int windowID;
    private int slotCount;
    private int entityID;

    public WrappedPacketOutOpenWindowHorse(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        try {
            packetConstructor = PacketTypeClasses.Play.Server.OPEN_WINDOW_HORSE.getConstructor(int.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getWindowId() {
        if (packet != null) {
            return readInt(0);
        }
        else {
            return windowID;
        }
    }

    public void setWindowId(int windowID) {
        if (packet != null) {
            writeInt(0, windowID);
        }
        else {
            this.windowID = windowID;
        }
    }

    public int getSlotCount() {
        if (packet != null) {
            return readInt(1);
        }
        else {
            return slotCount;
        }
    }

    public void setSlotCount(int slotCount) {
        if (packet != null) {
            writeInt(1, slotCount);
        }
        else {
            this.slotCount = slotCount;
        }
    }

    public int getEntityId() {
        if (packet != null) {
            return readInt(2);
        }
        else {
            return entityID;
        }
    }

    public void setEntityId(int entityID) {
        if (packet != null) {
            writeInt(2, entityID);
        }
        else {
            this.entityID = entityID;
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(getWindowId(),getSlotCount(), getEntityId());
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_15_2);
    }
}
