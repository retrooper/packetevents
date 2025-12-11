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

import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;

@NullMarked
public final class CubicBezierControls {

    public static final NbtCodec<CubicBezierControls> CODEC = NbtCodecs.FLOAT.applyList()
            .validate(floats -> floats.size() == 4)
            .apply(l -> new CubicBezierControls(l.get(0), l.get(1), l.get(2), l.get(3)),
                    v -> Arrays.asList(v.x1, v.y1, v.x2, v.y2))
            .validate(CubicBezierControls::isValid);

    private final float x1;
    private final float y1;
    private final float x2;
    private final float y2;

    public CubicBezierControls(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public boolean isValid() {
        return this.x1 >= 0f && this.x1 <= 1f
                && this.x2 >= 0f && this.x2 <= 1f;
    }

    public CubicCurve calcCurveX() {
        // based on Mojang's CubicBezier#curveFromControls
        return new CubicCurve(
                3f * this.x1 - 3f * this.x2 + 1f,
                -6f * this.x1 + 3f * this.x2,
                3f * this.x1
        );
    }

    public CubicCurve calcCurveY() {
        // based on Mojang's CubicBezier#curveFromControls
        return new CubicCurve(
                3f * this.y1 - 3f * this.y2 + 1f,
                -6f * this.y1 + 3f * this.y2,
                3f * this.y1
        );
    }

    public float getX1() {
        return this.x1;
    }

    public float getY1() {
        return this.y1;
    }

    public float getX2() {
        return this.x2;
    }

    public float getY2() {
        return this.y2;
    }
}
