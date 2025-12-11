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

package com.github.retrooper.packetevents.protocol.world.attributes.easing;

import com.github.retrooper.packetevents.util.FloatUnaryOperator;
import com.github.retrooper.packetevents.util.easing.EasingFunctions;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.11+
 */
@NullMarked
public final class EasingTypes {

    private static final VersionedRegistry<EasingType> REGISTRY = new VersionedRegistry<>("easing_type");

    private EasingTypes() {
    }

    @ApiStatus.Internal
    public static EasingType define(String name, FloatUnaryOperator operator) {
        return REGISTRY.define(name, data -> new StaticEasingType(data, operator));
    }

    public static VersionedRegistry<EasingType> getRegistry() {
        return REGISTRY;
    }

    public static final EasingType CONSTANT = define("constant", EasingFunctions::constant);
    public static final EasingType LINEAR = define("linear", EasingFunctions::linear);
    public static final EasingType IN_BACK = define("in_back", EasingFunctions::inBack);
    public static final EasingType IN_BOUNCE = define("in_bounce", EasingFunctions::inBounce);
    public static final EasingType IN_CIRC = define("in_circ", EasingFunctions::inCirc);
    public static final EasingType IN_CUBIC = define("in_cubic", EasingFunctions::inCubic);
    public static final EasingType IN_ELASTIC = define("in_elastic", EasingFunctions::inElastic);
    public static final EasingType IN_EXPO = define("in_expo", EasingFunctions::inExpo);
    public static final EasingType IN_QUAD = define("in_quad", EasingFunctions::inQuad);
    public static final EasingType IN_QUART = define("in_quart", EasingFunctions::inQuart);
    public static final EasingType IN_QUINT = define("in_quint", EasingFunctions::inQuint);
    public static final EasingType IN_SINE = define("in_sine", EasingFunctions::inSine);
    public static final EasingType IN_OUT_BACK = define("in_out_back", EasingFunctions::inOutBack);
    public static final EasingType IN_OUT_BOUNCE = define("in_out_bounce", EasingFunctions::inOutBounce);
    public static final EasingType IN_OUT_CIRC = define("in_out_circ", EasingFunctions::inOutCirc);
    public static final EasingType IN_OUT_CUBIC = define("in_out_cubic", EasingFunctions::inOutCubic);
    public static final EasingType IN_OUT_ELASTIC = define("in_out_elastic", EasingFunctions::inOutElastic);
    public static final EasingType IN_OUT_EXPO = define("in_out_expo", EasingFunctions::inOutExpo);
    public static final EasingType IN_OUT_QUAD = define("in_out_quad", EasingFunctions::inOutQuad);
    public static final EasingType IN_OUT_QUART = define("in_out_quart", EasingFunctions::inOutQuart);
    public static final EasingType IN_OUT_QUINT = define("in_out_quint", EasingFunctions::inOutQuint);
    public static final EasingType IN_OUT_SINE = define("in_out_sine", EasingFunctions::inOutSine);
    public static final EasingType OUT_BACK = define("out_back", EasingFunctions::outBack);
    public static final EasingType OUT_BOUNCE = define("out_bounce", EasingFunctions::outBounce);
    public static final EasingType OUT_CIRC = define("out_circ", EasingFunctions::outCirc);
    public static final EasingType OUT_CUBIC = define("out_cubic", EasingFunctions::outCubic);
    public static final EasingType OUT_ELASTIC = define("out_elastic", EasingFunctions::outElastic);
    public static final EasingType OUT_EXPO = define("out_expo", EasingFunctions::outExpo);
    public static final EasingType OUT_QUAD = define("out_quad", EasingFunctions::outQuad);
    public static final EasingType OUT_QUART = define("out_quart", EasingFunctions::outQuart);
    public static final EasingType OUT_QUINT = define("out_quint", EasingFunctions::outQuint);
    public static final EasingType OUT_SINE = define("out_sine", EasingFunctions::outSine);

    static {
        REGISTRY.unloadMappings();
    }
}
