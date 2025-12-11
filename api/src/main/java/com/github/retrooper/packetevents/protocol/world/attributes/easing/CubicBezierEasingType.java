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

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.easing.CubicBezierControls;
import com.github.retrooper.packetevents.util.easing.CubicCurve;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @versions 1.21.11+
 */
@NullMarked
public final class CubicBezierEasingType extends AbstractMappedEntity implements EasingType {

    public static final NbtCodec<CubicBezierEasingType> CODEC = new NbtMapCodec<CubicBezierEasingType>() {
        @Override
        public CubicBezierEasingType decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            CubicBezierControls controls = compound.getOrThrow("cubic_bezier", CubicBezierControls.CODEC, wrapper);
            return new CubicBezierEasingType(controls);
        }

        @Override
        public void encode(NBTCompound compound, PacketWrapper<?> wrapper, CubicBezierEasingType value) throws NbtCodecException {
            compound.set("cubic_bezier", value.controls, CubicBezierControls.CODEC, wrapper);
        }
    }.codec();

    private static final int NEWTON_RAPHSON_ITERATIONS = 4;

    private final CubicBezierControls controls;
    private final CubicCurve curveX;
    private final CubicCurve curveY;

    public CubicBezierEasingType(CubicBezierControls controls) {
        this(null, controls);
    }

    @ApiStatus.Internal
    public CubicBezierEasingType(@Nullable TypesBuilderData data, CubicBezierControls controls) {
        super(data);
        this.controls = controls;
        this.curveX = controls.calcCurveX();
        this.curveY = controls.calcCurveY();
    }

    @Override
    public float apply(float x) {
        // see https://en.wikipedia.org/w/index.php?title=Newton%27s_method&oldid=1321820598
        // implementation is based on Mojang's code
        float t = x;
        for (int i = 0; i < NEWTON_RAPHSON_ITERATIONS; i++) {
            float gradient = this.curveX.calcSampleGradient(t);
            if (gradient < MathUtil.EPSILON) {
                break;
            }
            float error = this.curveX.calcSample(t) - x;
            t -= error / gradient;
        }
        return this.curveY.calcSample(t);
    }
}
