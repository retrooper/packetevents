package me.purplex.packetevents.utils.math;

public class Vector3f {
    public float x, y, z;

    public Vector3f(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z;
    }
}
