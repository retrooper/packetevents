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

package io.github.retrooper.packetevents.packetwrappers.play.in.blockplace;


import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Direction;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

import java.lang.reflect.InvocationTargetException;

final class WrappedPacketInBlockPlace_1_9 extends WrappedPacket {
    private static Class<?> movingObjectPositionBlockClass;
    private Object movingObjPos;

    public WrappedPacketInBlockPlace_1_9(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        movingObjectPositionBlockClass = NMSUtils.getNMSClassWithoutException("MovingObjectPositionBlock");
    }

    public Vector3i getBlockPosition() {
        Vector3i blockPos = new Vector3i();
        Object blockPosObj;
        if (movingObjectPositionBlockClass == null) {
            blockPosObj = readObject(0, NMSUtils.blockPosClass);
        } else {
            if (movingObjPos == null) {
                movingObjPos = readObject(0, movingObjectPositionBlockClass);
            }
            WrappedPacket movingObjectPosWrapper = new WrappedPacket(new NMSPacket(movingObjPos));
            blockPosObj = movingObjectPosWrapper.readObject(0, NMSUtils.blockPosClass);
        }
        try {
            blockPos.x = (int) NMSUtils.getBlockPosX.invoke(blockPosObj);
            blockPos.y = (int) NMSUtils.getBlockPosY.invoke(blockPosObj);
            blockPos.z = (int) NMSUtils.getBlockPosZ.invoke(blockPosObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return blockPos;
    }

    public void setBlockPosition(Vector3i blockPos) {
        Object blockPosObj = NMSUtils.generateNMSBlockPos(blockPos.x, blockPos.y, blockPos.z);
        if (movingObjectPositionBlockClass == null) {
            write(NMSUtils.blockPosClass, 0, blockPosObj);
        } else {
            if (movingObjPos == null) {
                movingObjPos = readObject(0, movingObjectPositionBlockClass);
            }
            WrappedPacket movingObjectPosWrapper = new WrappedPacket(new NMSPacket(movingObjPos));
            movingObjectPosWrapper.write(NMSUtils.blockPosClass, 0, blockPos);
        }
    }

    public Direction getDirection() {
        Enum<?> enumConst;
        if (movingObjectPositionBlockClass == null) {
            enumConst = readEnumConstant(0, NMSUtils.enumDirectionClass);
        } else {
            if (movingObjPos == null) {
                movingObjPos = readObject(0, movingObjectPositionBlockClass);
            }
            WrappedPacket movingObjectPosWrapper = new WrappedPacket(new NMSPacket(movingObjPos));
            enumConst = movingObjectPosWrapper.readEnumConstant(0, NMSUtils.enumDirectionClass);
        }
        return Direction.valueOf(enumConst.name());
    }

    public void setDirection(Direction direction) {
        Enum<?> enumConst = EnumUtil.valueOf(NMSUtils.enumDirectionClass, direction.name());
        if (movingObjectPositionBlockClass == null) {
            writeEnumConstant(0, enumConst);
        } else {
            if (movingObjPos == null) {
                movingObjPos = readObject(0, movingObjectPositionBlockClass);
            }
            WrappedPacket movingObjectPosWrapper = new WrappedPacket(new NMSPacket(movingObjPos));
            movingObjectPosWrapper.writeEnumConstant(0, enumConst);
        }
    }
}
