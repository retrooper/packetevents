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

package io.github.retrooper.packetevents.packetwrappers.play.in.blockplace;


import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Direction;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

import java.lang.reflect.InvocationTargetException;

final class WrappedPacketInBlockPlace_1_9 extends WrappedPacket {
    private Object movingObjPos;

    public WrappedPacketInBlockPlace_1_9(final NMSPacket packet) {
        super(packet);
    }

    public Vector3i getBlockPosition() {
        if (NMSUtils.movingObjectPositionBlockClass == null) {
            return readBlockPosition(0);
        } else {
            if (movingObjPos == null) {
                movingObjPos = readObject(0, NMSUtils.movingObjectPositionBlockClass);
            }
            WrappedPacket movingObjectPosWrapper = new WrappedPacket(new NMSPacket(movingObjPos));
            return movingObjectPosWrapper.readBlockPosition(0);
        }
    }

    public void setBlockPosition(Vector3i blockPos) {
        if (NMSUtils.movingObjectPositionBlockClass == null) {
            writeBlockPosition(0, blockPos);
        } else {
            if (movingObjPos == null) {
                movingObjPos = readObject(0, NMSUtils.movingObjectPositionBlockClass);
            }
            WrappedPacket movingObjectPosWrapper = new WrappedPacket(new NMSPacket(movingObjPos));
            movingObjectPosWrapper.writeBlockPosition(0, blockPos);
        }
    }

    public Direction getDirection() {
        Enum<?> enumConst;
        if (NMSUtils.movingObjectPositionBlockClass == null) {
            enumConst = readEnumConstant(0, NMSUtils.enumDirectionClass);
        } else {
            if (movingObjPos == null) {
                movingObjPos = readObject(0, NMSUtils.movingObjectPositionBlockClass);
            }
            WrappedPacket movingObjectPosWrapper = new WrappedPacket(new NMSPacket(movingObjPos));
            enumConst = movingObjectPosWrapper.readEnumConstant(0, NMSUtils.enumDirectionClass);
        }
        return Direction.values()[enumConst.ordinal()];
    }

    public void setDirection(Direction direction) {
        Enum<?> enumConst = EnumUtil.valueByIndex(NMSUtils.enumDirectionClass, direction.ordinal());
        if (NMSUtils.movingObjectPositionBlockClass == null) {
            writeEnumConstant(0, enumConst);
        } else {
            if (movingObjPos == null) {
                movingObjPos = readObject(0, NMSUtils.movingObjectPositionBlockClass);
            }
            WrappedPacket movingObjectPosWrapper = new WrappedPacket(new NMSPacket(movingObjPos));
            movingObjectPosWrapper.writeEnumConstant(0, enumConst);
        }
    }
}
