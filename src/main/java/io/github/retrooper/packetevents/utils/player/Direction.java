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
 * The {@code Direction} class contains constants for the different valid faces in the minecraft protocol.
 *
 * @author retrooper
 * @see <a href="https://wiki.vg/Protocol#Player_Digging">https://wiki.vg/Protocol#Player_Digging</a>
 * @since 1.7.8
 */
public class Direction {
    /**
     * -Y offset
     */
    public static final byte DOWN =  0;

    /**
     * +Y offset
     */
    public static final byte UP = 1;

    /**
     * -Z offset
     */
    public static final byte NORTH = 2;

    /**
     * +Z offset
     */
    public static final byte SOUTH = 3;

    /**
     * -X offset
     */
    public static final byte WEST = 4;

    /**
     * +X offset
     */
    public static final byte EAST = 5;

    @Deprecated
    public static byte getFromName(String name) {
        return getByName(name);
    }

    public static byte getByName(String name) {
        switch (name.toUpperCase()) {
            case "DOWN":
                return DOWN;
            case "UP":
                return UP;
            case "NORTH":
                return NORTH;
            case "SOUTH":
                return SOUTH;
            case "WEST":
                return WEST;
            case "EAST":
                return EAST;
            default:
                return -1;
        }
    }
}
