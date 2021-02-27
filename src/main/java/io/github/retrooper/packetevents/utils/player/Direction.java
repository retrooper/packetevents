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

package io.github.retrooper.packetevents.utils.player;

/**
 * The {@code Direction} enum contains constants for the different valid faces in the minecraft protocol.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Player_Digging">https://wiki.vg/Protocol#Player_Digging</a>
 * @since 1.7.8
 */
public enum Direction {
    /**
     * -Y offset
     */
    DOWN,

    /**
     * +Y offset
     */
    UP,

    /**
     * -Z offset
     */
    NORTH,

    /**
     * +Z offset
     */
    SOUTH,

    /**
     * -X offset
     */
    WEST,

    /**
     * +X offset
     */
    EAST,

    /**
     * Face is set to 255
     */
    OTHER((short) 255),

    /**
     * Should not happen.... Invalid value?
     */
    INVALID;

    final short face;

    Direction(short face) {
        this.face = face;
    }

    Direction() {
        this.face = (short) ordinal();
    }

    public static Direction getDirection(int face) {
        if (face == 255) {
            return OTHER;
        } else if (face < 0 || face > 5) {
            return INVALID;
        }
        return values()[face];
    }

    public short getFaceValue() {
        return face;
    }
}
