/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.util.easing;

import org.jspecify.annotations.NullMarked;

// based on Mojang's CubicCurve
@NullMarked
public final class CubicCurve {

    private final float a;
    private final float b;
    private final float c;

    public CubicCurve(float a, float b, float c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public float calcSample(float t) {
        return ((this.a * t + this.b) * t + this.c) * t;
    }

    public float calcSampleGradient(float t) {
        return (3f * this.a * t + 2f * this.b) * t + this.c;
    }

    public float getA() {
        return this.a;
    }

    public float getB() {
        return this.b;
    }

    public float getC() {
        return this.c;
    }
}
