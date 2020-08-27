/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.utils.vector;

public final class Vector3i {
    public int x, y, z;

    public Vector3i(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z;
    }
}
