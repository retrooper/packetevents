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

package io.github.retrooper.packetevents.packetwrappers.in.blockdig;

import io.github.retrooper.packetevents.enums.Direction;
import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketInBlockDig extends WrappedPacket {
    private static Class<?> blockDigClass, blockPositionClass, enumDirectionClass, digTypeClass;
    private static boolean isVersionLowerThan_v_1_8;
    private Object blockPosObj;
    private Object enumDirObj;

    public WrappedPacketInBlockDig(Object packet) {
        super(packet);
    }

    public static void load() {
        blockDigClass = PacketTypeClasses.Client.BLOCK_DIG;
        try {
            if (version.isHigherThan(ServerVersion.v_1_7_10)) {
                blockPositionClass = NMSUtils.getNMSClass("BlockPosition");
                enumDirectionClass = NMSUtils.getNMSClass("EnumDirection");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        isVersionLowerThan_v_1_8 = version.isLowerThan(ServerVersion.v_1_8);

        if (version != ServerVersion.v_1_7_10) {
            try {
                digTypeClass = NMSUtils.getNMSClass("EnumPlayerDigType");
            } catch (ClassNotFoundException e) {
                //It is probably a subclass
                digTypeClass = SubclassUtil.getSubClass(blockDigClass, "EnumPlayerDigType");
            }
        }
    }

    /**
     * Get X position of the block
     *
     * @return Block Position X
     */
    public int getX() {
        if (isVersionLowerThan_v_1_8) {
            return readInt(0);
        } else {
            if (blockPosObj == null) {
                blockPosObj = readObject(0, blockPositionClass);
            }
            try {
                return (int) Reflection.getMethod(blockPosObj.getClass().getSuperclass(), "getX", 0).invoke(blockPosObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    /**
     * Get Y position of the block
     *
     * @return Block Position Y
     */
    public int getY() {
        if (isVersionLowerThan_v_1_8) {
            return readInt(1);
        } else {
            if (blockPosObj == null) {
                blockPosObj = readObject(0, blockPositionClass);
            }
            try {
                return (int) Reflection.getMethod(blockPosObj.getClass().getSuperclass(), "getY", 0).invoke(blockPosObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    /**
     * Get Z position of the block
     *
     * @return Block Position Z
     */
    public int getZ() {
        if (isVersionLowerThan_v_1_8) {
            return readInt(2);
        } else {
            if (blockPosObj == null) {
                blockPosObj = readObject(0, blockPositionClass);
            }
            try {
                return (int) Reflection.getMethod(blockPosObj.getClass().getSuperclass(), "getZ", 0).invoke(blockPosObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    /**
     * Get the direction / Get the face.
     *
     * @return Direction
     */
    public Direction getDirection() {
        if (isVersionLowerThan_v_1_8) {
            return Direction.getDirection((short) readInt(3));
        } else {
            if (enumDirObj == null) {
                enumDirObj = readObject(0, enumDirectionClass);
            }
            return Direction.valueOf(((Enum) enumDirObj).name());
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
            return PlayerDigType.valueOf(((Enum) readObject(0, digTypeClass)).name());
        }
    }

    public enum PlayerDigType {
        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        DROP_ALL_ITEMS,
        DROP_ITEM,
        RELEASE_USE_ITEM,
        SWAP_HELD_ITEMS,
        SWAP_ITEM_WITH_OFFHAND,
        UNKNOWN
    }
}
