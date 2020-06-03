package io.github.retrooper.packetevents.utils.vector;

public class Vector2f {
    public float x, y;
    public Vector2f(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y;
    }
}
