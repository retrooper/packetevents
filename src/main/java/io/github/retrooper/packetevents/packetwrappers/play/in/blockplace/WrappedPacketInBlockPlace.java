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

import io.github.retrooper.packetevents.exceptions.WrapperFieldNotFoundException;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.Direction;
import io.github.retrooper.packetevents.utils.player.Hand;
import io.github.retrooper.packetevents.utils.server.ServerVersion;


public final class WrappedPacketInBlockPlace extends WrappedPacket {
    private static boolean isHigherThan_v_1_8_8, isHigherThan_v_1_7_10, isOlderThan_v_1_9;
    private static int handEnumIndex;

    public WrappedPacketInBlockPlace(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        isHigherThan_v_1_8_8 = version.isNewerThan(ServerVersion.v_1_8_8);
        isHigherThan_v_1_7_10 = version.isNewerThan(ServerVersion.v_1_7_10);
        isOlderThan_v_1_9 = version.isOlderThan(ServerVersion.v_1_9);
        try {
            Object handEnum = readObject(1, NMSUtils.enumHandClass);
            handEnumIndex = 1;
        }
        catch (Exception ex) {
            handEnumIndex = 0;//Most likely a newer version
        }
    }

    public Hand getHand() {
        if (version.isOlderThan(ServerVersion.v_1_9)) {
            return Hand.MAIN_HAND;
        }
        else {
            Object enumHandObj = read(handEnumIndex, NMSUtils.enumHandClass);
            return Hand.valueOf(enumHandObj.toString());
        }
    }

    public Direction getDirection() {
        if (isHigherThan_v_1_8_8) {
            WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(new NMSPacket(packet.getRawNMSPacket()));
            return Direction.valueOf(((Enum) blockPlace_1_9.getEnumDirectionObject()).name());
        } else if (isHigherThan_v_1_7_10) {
            WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(new NMSPacket(packet.getRawNMSPacket()));
            return Direction.getDirection(blockPlace_1_8.getFace());
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(new NMSPacket(packet.getRawNMSPacket()));
            return Direction.getDirection(blockPlace_1_7_10.face);
        }
    }

    public int getX() {
        if (isHigherThan_v_1_8_8) {
            WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(packet);
            return blockPlace_1_9.getX();
        } else if (isHigherThan_v_1_7_10) {
            WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
            return blockPlace_1_8.getX();
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
            return blockPlace_1_7_10.x;
        }
    }

    public int getY() {
        if (isHigherThan_v_1_8_8) {
            WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(packet);
            return blockPlace_1_9.getY();
        } else if (isHigherThan_v_1_7_10) {
            WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
            return blockPlace_1_8.getY();
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
            return blockPlace_1_7_10.y;
        }
    }

    public int getZ() {
        if (isHigherThan_v_1_8_8) {
            WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(packet);
            return blockPlace_1_9.getZ();
        } else if (isHigherThan_v_1_7_10) {
            WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
            return blockPlace_1_8.getZ();
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
            return blockPlace_1_7_10.z;
        }
    }

    /**
     * Get the X position of the crosshair on the block.
     * Only supported on versions 1.7.10 to 1.8.8.
     *
     * @return cursorX from PacketPlayInBlockPlace
     */
    @SupportedVersions(versions = {ServerVersion.v_1_7_10, ServerVersion.v_1_8, ServerVersion.v_1_8_3,
            ServerVersion.v_1_8_4, ServerVersion.v_1_8_5, ServerVersion.v_1_8_6, ServerVersion.v_1_8_7,
            ServerVersion.v_1_8_8})
    public float getCursorX() {
        if (isOlderThan_v_1_9) {
            if (isHigherThan_v_1_7_10) {
                WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
                return blockPlace_1_8.getCursorX();
            } else {
                WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
                return blockPlace_1_7_10.cursorX;
            }
        } else {
            throw new UnsupportedOperationException("Operation WrappedPacketInBlockPlace#getCursorX is not available for versions higher than 1.8.8");
        }
    }

    /**
     * Get the Y position of the crosshair on the block.
     * Only supported on versions 1.7.10 to 1.8.8.
     *
     * @return cursorY from PacketPlayInBlockPlace
     */
    @SupportedVersions(versions = {ServerVersion.v_1_7_10, ServerVersion.v_1_8, ServerVersion.v_1_8_3,
            ServerVersion.v_1_8_4, ServerVersion.v_1_8_5, ServerVersion.v_1_8_6, ServerVersion.v_1_8_7,
            ServerVersion.v_1_8_8})
    public float getCursorY() {
        if (isOlderThan_v_1_9) {
            if (isHigherThan_v_1_7_10) {
                WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
                return blockPlace_1_8.getCursorY();
            } else {
                WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
                return blockPlace_1_7_10.cursorY;
            }
        } else {
            throw new UnsupportedOperationException("Operation WrappedPacketInBlockPlace#getCursorY is not available for versions higher than 1.8.8");
        }
    }

    /**
     * Get the Z position of the crosshair on the block.
     * Only supported on versions 1.7 to 1.8.8.
     *
     * @return cursorZ from PacketPlayInBlockPlace
     */
    @SupportedVersions(versions = {ServerVersion.v_1_7_10, ServerVersion.v_1_8, ServerVersion.v_1_8_3,
            ServerVersion.v_1_8_4, ServerVersion.v_1_8_5, ServerVersion.v_1_8_6, ServerVersion.v_1_8_7,
            ServerVersion.v_1_8_8})
    public float getCursorZ() {
        if (isOlderThan_v_1_9) {
            if (isHigherThan_v_1_7_10) {
                WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
                return blockPlace_1_8.getCursorZ();
            } else {
                WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
                return blockPlace_1_7_10.cursorZ;
            }
        } else {
            throw new UnsupportedOperationException("Operation WrappedPacketInBlockPlace#getCursorZ is not available for versions higher than 1.8.8");
        }
    }
}
