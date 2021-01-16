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
 * The {@code direction} enum represents the face of a block being hit/placed.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Player_Digging">https://wiki.vg/Protocol#Player_Digging</a>
 * @since 1.7.8
 */
public class Direction {
    /**
     * -Y offset
     */
    public static final Direction DOWN =  new Direction((byte)0);

    /**
     * +Y offset
     */
    public static final Direction UP = new Direction((byte)1);

    /**
     * -Z offset
     */
    public static final Direction NORTH = new Direction((byte)2);

    /**
     * +Z offset
     */
    public static final Direction SOUTH = new Direction((byte)3);

    /**
     * -X offset
     */
    public static final Direction WEST = new Direction((byte)4);

    /**
     * +X offset
     */
    public static final Direction EAST = new Direction((byte)5);

    private final byte value;

    public Direction(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public boolean isInvalid() {
        return value < 0 || value > 5;
    }

    public Direction clone() {
        return new Direction(value);
    }

    public static Direction getFromName(String name) {
        switch (name) {
            case "DOWN":
                return DOWN.clone();
            case "UP":
                return UP.clone();
            case "NORTH":
                return NORTH.clone();
            case "SOUTH":
                return SOUTH.clone();
            case "WEST":
                return WEST.clone();
            case "EAST":
                return EAST.clone();
            default:
                return null;
        }
    }
}
