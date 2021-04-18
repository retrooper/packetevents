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

package io.github.retrooper.packetevents.utils.vector;

import org.bukkit.Location;

/**
 * 3D int Vector.
 * This vector can represent coordinates, angles, or anything you want.
 * You can use this to represent an array if you really want.
 * PacketEvents usually uses this for block positions as they don't need any decimals.
 *
 * @author retrooper
 * @since 1.7
 */
public class Vector3i {
    /**
     * This is the invalid vector.
     * In wrappers, when a vector is null in the actual packet, PacketEvents will set our high level vector X,Y,Z values
     * to -1 to avoid null pointer exceptions.
     */
    public static final Vector3i INVALID = new Vector3i(-1, -1, -1);
    /**
     * X (coordinate/angle/whatever you wish)
     */
    public int x;
    /**
     * Y (coordinate/angle/whatever you wish)
     */
    public int y;
    /**
     * Z (coordinate/angle/whatever you wish)
     */
    public int z;

    /**
     * Default constructor setting all coordinates/angles/values to their default values (=0).
     */
    public Vector3i() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3i(Location location) {
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    /**
     * Constructor allowing you to set the values.
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructor allowing you to specify an array.
     * X will be set to the first index of an array(if it exists, otherwise 0).
     * Y will be set to the second index of an array(if it exists, otherwise 0).
     * Z will be set to the third index of an array(if it exists, otherwise 0).
     *
     * @param array Array.
     */
    public Vector3i(int[] array) {
        if (array.length > 0) {
            x = array[0];
        } else {
            x = 0;
            y = 0;
            z = 0;
            return;
        }
        if (array.length > 1) {
            y = array[1];
        } else {
            y = 0;
            z = 0;
            return;
        }
        if (array.length > 2) {
            z = array[2];
        } else {
            z = 0;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    /**
     * Is the object we are comparing to equal to us?
     * It must be of type Vector3d or Vector3i and all values must be equal to the values in this class.
     *
     * @param obj Compared object.
     * @return Are they equal?
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector3i) {
            Vector3i vec = (Vector3i) obj;
            return x == vec.x && y == vec.y && z == vec.z;
        } else if (obj instanceof Vector3d) {
            Vector3d vec = (Vector3d) obj;
            return x == vec.x && y == vec.y && z == vec.z;
        } else if (obj instanceof Vector3f) {
            Vector3f vec = (Vector3f) obj;
            return x == vec.x && y == vec.y && z == vec.z;
        }
        return false;
    }

    /**
     * Simply clone an instance of this class.
     *
     * @return Clone.
     */
    @Override
    public Vector3i clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z;
    }
}
