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
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.vector.Vector3i;

public final class WrappedPacketInBlockDig extends WrappedPacket {
    private static Class<?> blockDigClass, blockPositionClass, enumDirectionClass, digTypeClass;
    private static boolean isVersionLowerThan_v_1_8;
    private Vector3i blockPosition;
    private Direction direction;
    private PlayerDigType digType;
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

    @Override
    protected void setup() {
        Direction enumDirection = null;
        PlayerDigType enumDigType = null;
        int x = 0, y = 0, z = 0;
        //1.7.10
        try {
            if (isVersionLowerThan_v_1_8) {
                enumDigType = PlayerDigType.values()[(readInt(4))];
                x = readInt(0);
                y = readInt(1);
                z = readInt(2);
                enumDirection = null;
            } else {
                //1.8+
                final Object blockPosObj = readObject(0, blockPositionClass);
                final Object enumDirectionObj = readObject(0, enumDirectionClass);
                final Object digTypeObj = readObject(0, digTypeClass);

                x = Reflection.getField(blockPositionClass, int.class, 0).getInt(blockPosObj);
                y = Reflection.getField(blockPositionClass, int.class, 1).getInt(blockPosObj);
                z = Reflection.getField(blockPositionClass, int.class, 2).getInt(blockPosObj);

                //.toString() won't work so we must do this
                enumDirection = Direction.valueOf(((Enum)enumDirectionObj).name());
                enumDigType = PlayerDigType.valueOf(((Enum)digTypeObj).name());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.blockPosition = new Vector3i(x, y, z);
        if (enumDirection == null) {
            this.direction = Direction.NULL;
        } else {
            this.direction = enumDirection;
        }
        this.digType = enumDigType;
    }

    /**
     * Get X position of the block
     * @return Block Position X
     */
    public int getBlockPositionX() {
        return blockPosition.x;
    }

    /**
     * Get Y position of the block
     * @return Block Position Y
     */
    public int getBlockPositionY() {
        return blockPosition.y;
    }

    /**
     * Get Z position of the block
     * @return Block Position Z
     */
    public int getBlockPositionZ() {
        return blockPosition.z;
    }

    /**
     * Use {@link #getBlockPositionX()}, {@link #getBlockPositionY()}, {@link #getBlockPositionZ()}
     * @return Block Position
     */
    @Deprecated
    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    /**
     * Get the direction
     * Is Direction.NULL on 1.7.10 FOR NOW
     * @return Direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Get the PlayerDigType enum sent in this packet.
     * @return Dig Type
     */
    public PlayerDigType getDigType() {
        return digType;
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
        WRONG_PACKET
    }


}
