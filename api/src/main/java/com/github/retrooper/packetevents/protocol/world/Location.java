/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world;

import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import org.jetbrains.annotations.NotNull;

public class Location {
    private Vector3d position;
    private float yaw;
    private float pitch;

    public Location(Vector3d position, float yaw, float pitch) {
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location(double x, double y, double z, float yaw, float pitch) {
        this(new Vector3d(x, y, z), yaw, pitch);
    }

    public Vector3d getPosition() {
        return position;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getZ() {
        return position.getZ();
    }

    public void setPosition(Vector3d position) {
        this.position = position;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public Vector3f getDirection() {
        double rotX = (double)this.getYaw();
        double rotY = (double)this.getPitch();
        float y = (float) -Math.sin(Math.toRadians(rotY));
        double xz = Math.cos(Math.toRadians(rotY));
        float x = (float) (-xz * Math.sin(Math.toRadians(rotX)));
        float z = (float) (xz * Math.cos(Math.toRadians(rotX)));
        return new Vector3f(x, y, z);
    }
    
    public void setDirection(Vector3f vector) {
        double _2PI = 6.283185307179586D;
        double x = vector.getX();
        double z = vector.getZ();
        if (x == 0.0D && z == 0.0D) {
            this.pitch = vector.getY() > 0.0D ? -90.0F : 90.0F;
        } else {
            double theta = Math.atan2(-x, z);
            this.yaw = (float)Math.toDegrees((theta + 6.283185307179586D) % 6.283185307179586D);
            double x2 = x * x;
            double z2 = z * z;
            double xz = Math.sqrt(x2 + z2);
            this.pitch = (float)Math.toDegrees(Math.atan(-vector.getY() / xz));
        }
    }

    @Override
    public Location clone() {
        return new Location(position, yaw, pitch);
    }

    @Override
    public String toString() {
        return "Location {[" + position.toString() + "]," + " yaw: " + yaw + ", pitch: " + pitch + "}";
    }
}
