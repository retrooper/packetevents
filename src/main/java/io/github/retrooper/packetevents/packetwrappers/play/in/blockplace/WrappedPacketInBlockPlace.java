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
import io.github.retrooper.packetevents.utils.player.Direction;
import io.github.retrooper.packetevents.utils.server.ServerVersion;


public final class WrappedPacketInBlockPlace extends WrappedPacket {
    private static boolean isHigherThan_v_1_8_8, isHigherThan_v_1_7_10, isOlderThan_v_1_9;

    public WrappedPacketInBlockPlace(final NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        isHigherThan_v_1_8_8 = version.isNewerThan(ServerVersion.v_1_8_8);
        isHigherThan_v_1_7_10 = version.isNewerThan(ServerVersion.v_1_7_10);
        isOlderThan_v_1_9 = version.isOlderThan(ServerVersion.v_1_9);
    }

    public Direction getDirection() {
        if (isHigherThan_v_1_8_8) {
            WrappedPacketInBlockPlace_1_9 blockPlace_1_9 = new WrappedPacketInBlockPlace_1_9(packet);
            return Direction.valueOf(((Enum) blockPlace_1_9.getEnumDirectionObject()).name());
        } else if (isHigherThan_v_1_7_10) {
            WrappedPacketInBlockPlace_1_8 blockPlace_1_8 = new WrappedPacketInBlockPlace_1_8(packet);
            return Direction.values()[blockPlace_1_8.getFace()];
        } else {
            WrappedPacketInBlockPlace_1_7_10 blockPlace_1_7_10 = new WrappedPacketInBlockPlace_1_7_10(packet);
            return Direction.values()[blockPlace_1_7_10.face];
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
     * Returns the cursorX value from the packet inputted.
     * Only supported from 1.7 to 1.8.8.
     *
     * @return cursorX from PacketPlayInBlockPlace
     */
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
     * Returns the cursorY value from the packet inputted.
     * Only supported from 1.7 to 1.8.8.
     *
     * @return cursorY from PacketPlayInBlockPlace
     */
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
     * Returns the cursorZ value from the packet inputted.
     * Only supported from 1.7 to 1.8.8.
     *
     * @return cursorZ from PacketPlayInBlockPlace
     */
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
