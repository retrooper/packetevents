/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.utils.vector;

import io.github.retrooper.packetevents.protocol.data.world.BlockPosition;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * 3D double Vector.
 * This vector can represent coordinates, angles, or anything you want.
 * You can use this to represent an array if you really want.
 *
 * @author retrooper
 * @since 1.8
 */
public class Vector3d {
    /**
     * This is the invalid vector.
     * In wrappers, when a vector is null in the actual packet, PacketEvents will set our high level vector X,Y,Z values
     * to -1 to avoid null pointer exceptions.
     */
    public static final Vector3d INVALID = new Vector3d(-1, -1, -1);
    /**
     * X (coordinate/angle/whatever you wish)
     */
    public double x;
    /**
     * Y (coordinate/angle/whatever you wish)
     */
    public double y;
    /**
     * Z (coordinate/angle/whatever you wish)
     */
    public double z;

    /**
     * Default constructor setting all coordinates/angles/values to their default values (=0).
     */
    public Vector3d() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public Vector3d(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
    }

    /**
     * Constructor allowing you to set the values.
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public Vector3d(double x, double y, double z) {
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
    public Vector3d(double[] array) {
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

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
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
        if (obj instanceof Vector3d) {
            Vector3d vec = (Vector3d) obj;
            return x == vec.x && y == vec.y && z == vec.z;
        } else if (obj instanceof Vector3f) {
            Vector3f vec = (Vector3f) obj;
            return x == vec.x && y == vec.y && z == vec.z;
        } else if (obj instanceof BlockPosition) {
            BlockPosition vec = (BlockPosition) obj;
            return x == (double) vec.x && y == (double) vec.y && z == (double) vec.z;
        }
        return false;
    }

    /**
     * Simply clone an instance of this class.
     *
     * @return Clone.
     */
    @Override
    public Vector3d clone() {
        return new Vector3d(getX(), getY(), getZ());
    }

    public Vector3d add(Vector3d target) {
        Vector3d result = new Vector3d(x, y, z);
        result.x += target.x;
        result.y += target.y;
        result.z += target.z;
        return result;
    }

    public Vector3d subtract(Vector3d target) {
        Vector3d result = new Vector3d(x, y, z);
        result.x -= target.x;
        result.y -= target.y;
        result.z -= target.z;
        return result;
    }

    public double distance(Vector3d target) {
        return Math.sqrt(distanceSquared(target));
    }

    public double distanceSquared(Vector3d target) {
        double distX = (x - target.x) * (x - target.x);
        double distY = (y - target.y) * (y - target.y);
        double distZ = (z - target.z) * (z - target.z);
        return distX + distY + distZ;
    }

    public Location asLocation(World world, float yaw, float pitch) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z;
    }
}
