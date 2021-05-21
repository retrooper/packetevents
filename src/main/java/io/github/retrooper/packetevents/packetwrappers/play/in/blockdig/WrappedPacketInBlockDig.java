/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
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

package io.github.retrooper.packetevents.packetwrappers.play.in.blockdig;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.enums.EnumUtil;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Direction;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketInBlockDig extends WrappedPacket {
    private static Class<? extends Enum<?>> digTypeClass;
    private static boolean isVersionLowerThan_v_1_8;

    public WrappedPacketInBlockDig(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        Class<?> blockDigClass = PacketTypeClasses.Play.Client.BLOCK_DIG;
        isVersionLowerThan_v_1_8 = version.isOlderThan(ServerVersion.v_1_8);

        if (version != ServerVersion.v_1_7_10) {
            try {
                digTypeClass = NMSUtils.getNMSEnumClass("EnumPlayerDigType");
            } catch (ClassNotFoundException e) {
                //It is probably a subclass
                digTypeClass = SubclassUtil.getEnumSubClass(blockDigClass, "EnumPlayerDigType");
            }
        }
    }

    public Vector3i getBlockPosition() {
        int x = 0;
        int y = 0;
        int z = 0;
        if (isVersionLowerThan_v_1_8) {
            x = readInt(0);
            y = readInt(1);
            z = readInt(2);
        } else {
            Object blockPosObj = readObject(0, NMSUtils.blockPosClass);
            try {
                x = (int) NMSUtils.getBlockPosX.invoke(blockPosObj);
                y = (int) NMSUtils.getBlockPosY.invoke(blockPosObj);
                z = (int) NMSUtils.getBlockPosZ.invoke(blockPosObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new Vector3i(x, y, z);
    }

    public void setBlockPosition(Vector3i blockPos) {
        if (isVersionLowerThan_v_1_8) {
            writeInt(0, blockPos.x);
            writeInt(1, blockPos.y);
            writeInt(2, blockPos.z);
        } else {
            Object blockPosObj = NMSUtils.generateNMSBlockPos(blockPos.x, blockPos.y, blockPos.z);
            write(NMSUtils.blockPosClass, 0, blockPosObj);
        }
    }

    /**
     * Get the direction / Get the face.
     *
     * @return Direction
     */
    public Direction getDirection() {
        if (isVersionLowerThan_v_1_8) {
            return Direction.getDirection(readInt(3));
        } else {
            Enum<?> enumDir = readEnumConstant(0, NMSUtils.enumDirectionClass);
            return Direction.valueOf(enumDir.name());
        }
    }

    public void setDirection(Direction direction) {
        if (isVersionLowerThan_v_1_8) {
            writeInt(3, direction.getFaceValue());
        } else {
            Enum<?> enumConst = EnumUtil.valueOf(NMSUtils.enumDirectionClass, direction.name());
            write(NMSUtils.enumDirectionClass, 0, enumConst);
        }
    }

    /**
     * Get the PlayerDigType enum sent in this packet.
     *
     * @return Dig Type
     */
    public PlayerDigType getDigType() {
        if (isVersionLowerThan_v_1_8) {
            return PlayerDigType.values()[readInt(4)];
        } else {
            return PlayerDigType.valueOf(readObject(0, digTypeClass).name());
        }
    }

    public void setDigType(PlayerDigType type) {
        if (isVersionLowerThan_v_1_8) {
            writeInt(4, type.ordinal());
        } else {
            Enum<?> enumConst = EnumUtil.valueOf(digTypeClass, type.name());
            write(digTypeClass, 0, enumConst);
        }
    }

    public enum PlayerDigType {
        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        DROP_ALL_ITEMS,
        DROP_ITEM,
        RELEASE_USE_ITEM,

        /**
         * Doesn't exist on the newest versions.
         */
        @Deprecated
        SWAP_HELD_ITEMS,

        SWAP_ITEM_WITH_OFFHAND,
        UNKNOWN
    }
}
