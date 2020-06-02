package io.github.explored.packetevents.utils.vector;

public class Vector2d {
    public double x, y;

    public Vector2d(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y;
    }
}
