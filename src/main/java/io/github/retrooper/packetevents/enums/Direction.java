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

public enum Direction {
    /**
     * -Y offset
     */
    DOWN((short) 0),

    /**
     * +Y offset
     */
    UP((short) 1),

    /**
     * -Z offset
     */
    NORTH((short) 2),

    /**
     * +Z offset
     */
    SOUTH((short) 3),

    /**
     * -X offset
     */
    WEST((short) 4),

    /**
     * +X offset
     */
    EAST((short) 5),

    OTHER((short) 255);

    final short value;

    Direction(final short value) {
        this.value = value;
    }

    public static Direction getDirection(final short value) {
        for (Direction direction : values()) {
            if (direction.value == value) {
                return direction;
            }
        }
        return OTHER;
    }
}
