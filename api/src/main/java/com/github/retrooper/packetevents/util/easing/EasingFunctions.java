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

import com.github.retrooper.packetevents.util.MathUtil;
import org.jspecify.annotations.NullMarked;

// mostly copied from Mojang's Ease class, who took it from https://robertpenner.com/easing/
@NullMarked
public final class EasingFunctions {

    private EasingFunctions() {
    }

    public static float constant(float ignoredX) {
        return 0f;
    }

    public static float linear(float x) {
        return x;
    }

    public static float inBack(float x) {
        float c1 = 1.70158F;
        float c3 = c1 + 1f;
        return (x * x) * (c3 * x - c1);
    }

    public static float inBounce(float x) {
        return 1f - outBounce(1f - x);
    }

    public static float inCubic(float x) {
        return x * x * x;
    }

    public static float inElastic(float x) {
        if (x == 0f) {
            return 0f;
        } else if (x == 1f) {
            return 1f;
        }
        float c4 = (float) (Math.PI * 2d / 3d);
        return (float) (-Math.pow(2d, 10d * (double) x - 10d) * Math.sin(((double) x * 10d - 10.75d) * c4));
    }

    public static float inExpo(float x) {
        return x == 0f ? 0f : (float) Math.pow(2d, 10d * (double) x - 10d);
    }

    public static float inQuart(float x) {
        return (x * x) * (x * x);
    }

    public static float inQuint(float x) {
        return ((x * x) * (x * x)) * x;
    }

    public static float inSine(float x) {
        return 1f - (float) Math.cos(x * (float) (Math.PI / 2));
    }

    public static float inOutBounce(float x) {
        return x < 0.5f
                ? (1f - outBounce(1f - 2f * x)) / 2f
                : (1f + outBounce(2f * x - 1f)) / 2f;
    }

    public static float inOutCirc(float x) {
        return x < 0.5f
                ? (float) ((1d - Math.sqrt(1d - Math.pow(2d * (double) x, 2d))) / 2d)
                : (float) ((Math.sqrt(1d - Math.pow(-2d * (double) x + 2d, 2d)) + 1d) / 2d);
    }

    public static float inOutCubic(float x) {
        return x < 0.5f
                ? 4f * (x * x * x)
                : (float) (1d - Math.pow(-2d * (double) x + 2d, 3d) / 2d);
    }

    public static float inOutQuad(float x) {
        return x < 0.5f
                ? 2f * (x * x)
                : (float) (1d - Math.pow(-2d * (double) x + 2d, 2d) / 2d);
    }

    public static float inOutQuart(float x) {
        return x < 0.5f
                ? 8f * ((x * x) * (x * x))
                : (float) (1d - Math.pow(-2d * (double) x + 2d, 4d) / 2d);
    }

    public static float inOutQuint(float x) {
        return (double) x < 0.5d
                ? 16f * x * x * x * x * x
                : (float) (1d - Math.pow(-2d * (double) x + 2d, 5d) / 2d);
    }

    public static float outBounce(float x) {
        float n1 = 7.5625f;
        float d1 = 2.75f;
        if (x < 1f / d1) {
            return n1 * (x * x);
        } else if (x < 2f / d1) {
            return n1 * MathUtil.square(x - (1.5f / d1)) + (3f / 4f);
        } else if ((double) x < 2.5f / d1) {
            return n1 * MathUtil.square(x - (2.25f / d1)) + (15f / 16f);
        }
        return n1 * MathUtil.square(x - (2.625f / d1)) + (63f / 64f);
    }

    public static float outElastic(float x) {
        float c4 = (float) (Math.PI * 2d / 3d);
        if (x == 0f) {
            return 0f;
        } else if (x == 1f) {
            return 1f;
        }
        return (float) (Math.pow(2d, -10d * (double) x) * Math.sin(((double) x * 10d - 0.75d) * c4) + 1d);
    }

    public static float outExpo(float x) {
        return x == 1f ? 1f : 1f - (float) Math.pow(2d, -10d * (double) x);
    }

    public static float outQuad(float x) {
        return 1f - MathUtil.square(1f - x);
    }

    public static float outQuint(float x) {
        return 1f - (float) Math.pow(1d - (double) x, 5d);
    }

    public static float outSine(float x) {
        return (float) Math.sin(x * (float) (Math.PI / 2));
    }

    public static float inOutSine(float x) {
        return -((float) Math.cos((float) Math.PI * x) - 1f) / 2f;
    }

    public static float outBack(float x) {
        float c1 = 1.70158F;
        float c3 = 2.70158F;
        return 1f + c3 * MathUtil.cube(x - 1f) + c1 * MathUtil.square(x - 1f);
    }

    public static float outQuart(float x) {
        return 1f - MathUtil.square(MathUtil.square(1f - x));
    }

    public static float outCubic(float x) {
        return 1f - MathUtil.cube(1f - x);
    }

    public static float inOutExpo(float x) {
        if (x == 0f) {
            return 0f;
        } else if (x == 1f) {
            return 1f;
        }
        return x < 0.5f
                ? (float) (Math.pow(2d, 20d * (double) x - 10d) / 2d)
                : (float) ((2d - Math.pow(2d, -20d * (double) x + 10d)) / 2d);
    }

    public static float inQuad(float x) {
        return x * x;
    }

    public static float outCirc(float x) {
        return (float) Math.sqrt(1f - MathUtil.square(x - 1f));
    }

    public static float inOutElastic(float x) {
        float c5 = (float) Math.PI * 4.0F / 9.0F;
        if (x == 0f) {
            return 0f;
        } else if (x == 1f) {
            return 1f;
        }
        double sin = Math.sin((20d * (double) x - 11.125d) * c5);
        return x < 0.5f
                ? (float) (-(Math.pow(2d, 20d * (double) x - 10d) * sin) / 2d)
                : (float) (Math.pow(2d, -20d * (double) x + 10d) * sin / 2d + 1d);
    }

    public static float inCirc(float x) {
        return (float) (-Math.sqrt(1f - x * x)) + 1f;
    }

    public static float inOutBack(float x) {
        float c1 = 1.70158f;
        float c2 = c1 * 1.525f;
        if (x < 0.5F) {
            return 2f * 2f * x * x * (2f * (c2 + 1f) * x - c2) / 2f;
        } else {
            float dt = 2f * x - 2f;
            return (dt * dt * ((c2 + 1f) * dt + c2) + 2f) / 2f;
        }
    }
}
