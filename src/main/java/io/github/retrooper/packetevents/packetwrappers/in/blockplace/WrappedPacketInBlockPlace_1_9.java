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

package io.github.retrooper.packetevents.packetwrappers.in.blockplace;


import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;

import java.lang.reflect.InvocationTargetException;

final class WrappedPacketInBlockPlace_1_9 extends WrappedPacket {
    private static Class<?> movingObjectPositionBlockClass;
    private Object blockPosObj;

    public WrappedPacketInBlockPlace_1_9(final Object packet) {
        super(packet);
    }

    public static void load() {
        movingObjectPositionBlockClass = NMSUtils.getNMSClassWithoutException("MovingObjectPositionBlock");
    }

    public int getX() {
        if (blockPosObj == null) {
            if (movingObjectPositionBlockClass == null) {
                blockPosObj = readObject(0, NMSUtils.blockPosClass);
            } else {
                Object movingObjectPos = readObject(0, movingObjectPositionBlockClass);
                WrappedPacket movingObjectPosWrapper = new WrappedPacket(movingObjectPos);
                blockPosObj = movingObjectPosWrapper.readObject(0, NMSUtils.blockPosClass);
            }
        }
        try {
            return (int) Reflection.getMethod(blockPosObj.getClass().getSuperclass(), "getX", 0).invoke(blockPosObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getY() {
        if (blockPosObj == null) {
            if (movingObjectPositionBlockClass == null) {
                blockPosObj = readObject(0, NMSUtils.blockPosClass);
            } else {
                Object movingObjectPos = readObject(0, movingObjectPositionBlockClass);
                WrappedPacket movingObjectPosWrapper = new WrappedPacket(movingObjectPos);
                blockPosObj = movingObjectPosWrapper.readObject(0, NMSUtils.blockPosClass);
            }
        }
        try {
            return (int) Reflection.getMethod(blockPosObj.getClass().getSuperclass(), "getY", 0).invoke(blockPosObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getZ() {
        if (blockPosObj == null) {
            if (movingObjectPositionBlockClass == null) {
                blockPosObj = readObject(0, NMSUtils.blockPosClass);
            } else {
                Object movingObjectPos = readObject(0, movingObjectPositionBlockClass);
                WrappedPacket movingObjectPosWrapper = new WrappedPacket(movingObjectPos);
                blockPosObj = movingObjectPosWrapper.readObject(0, NMSUtils.blockPosClass);
            }
        }
        try {
            return (int) Reflection.getMethod(blockPosObj.getClass().getSuperclass(), "getZ", 0).invoke(blockPosObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Object getEnumDirectionObject() {
        if (movingObjectPositionBlockClass == null) {
            return readObject(0, NMSUtils.enumDirectionClass);
        } else {
            Object movingObjectPos = readObject(0, movingObjectPositionBlockClass);
            WrappedPacket movingObjectPosWrapper = new WrappedPacket(movingObjectPos);
            return movingObjectPosWrapper.readObject(0, NMSUtils.enumDirectionClass);
        }
    }
}
