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

package io.github.retrooper.packetevents.packetwrappers.play.in.useitem;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Hand;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketInUseItem extends WrappedPacket {

    private static boolean v_1_14;

    public WrappedPacketInUseItem(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_14 = version.isNewerThanOrEquals(ServerVersion.v_1_14);
    }

    public Hand getHand() {
        return Hand.values()[readEnumConstant(0, NMSUtils.enumHandClass).ordinal()];
    }

    public void setHand(Hand hand) {
        Enum<?> enumConstant = EnumUtil.valueByIndex(NMSUtils.enumHandClass, hand.ordinal());
        writeEnumConstant(0, enumConstant);
    }

    public Vector3i getBlockPosition() {
            if (v_1_14) {
                Object movingBlockPosition = readObject(0, NMSUtils.movingObjectPositionBlockClass);
                WrappedPacket movingBlockPositionWrapper = new WrappedPacket(new NMSPacket(movingBlockPosition));
                return movingBlockPositionWrapper.readBlockPosition(0);
            }
            else {
                return readBlockPosition(0);
            }
    }

    public void setBlockPosition(Vector3i blockPosition) {
        if (v_1_14) {
            Object movingBlockPosition = readObject(0, NMSUtils.movingObjectPositionBlockClass);
            WrappedPacket movingBlockPositionWrapper = new WrappedPacket(new NMSPacket(movingBlockPosition));
            movingBlockPositionWrapper.writeBlockPosition(0, blockPosition);
        }
        else {
            writeBlockPosition(0, blockPosition);
        }
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThanOrEquals(ServerVersion.v_1_9);
    }
}
