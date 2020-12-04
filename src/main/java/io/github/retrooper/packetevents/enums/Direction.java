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

package io.github.retrooper.packetevents.enums;

/**
 * The {@code direction} enum represents the face of a block being hit/placed.
 * @see <a href="https://wiki.vg/Protocol#Player_Digging">https://wiki.vg/Protocol#Player_Digging</a>
 * @author retrooper
 * @since 1.7.8
 */
public enum Direction {
    /**
     * -Y offset
     */
    DOWN((byte) 0),

    /**
     * +Y offset
     */
    UP((byte) 1),

    /**
     * -Z offset
     */
    NORTH((byte) 2),

    /**
     * +Z offset
     */
    SOUTH((byte) 3),

    /**
     * -X offset
     */
    WEST((byte) 4),

    /**
     * +X offset
     */
    EAST((byte) 5),

    OTHER(Byte.MIN_VALUE);

    final byte value;

    Direction(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static Direction fromId(byte value) {
        for (Direction direction : values()) {
            if (direction.value == value) {
                return direction;
            }
        }
        return OTHER;
    }
}
