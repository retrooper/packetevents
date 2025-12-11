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

package com.github.retrooper.packetevents.protocol.world.attributes.timelines;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.attributes.easing.EasingType;
import com.github.retrooper.packetevents.protocol.world.attributes.easing.EasingTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * @versions 1.21.11+
 */
@NullMarked
public class KeyframeTrack<T> {

    private final List<Keyframe<T>> keyframes;
    private final EasingType easingType;

    public KeyframeTrack(List<Keyframe<T>> keyframes, EasingType easingType) {
        this.keyframes = keyframes;
        this.easingType = easingType;
    }

    public static <T> NbtMapCodec<KeyframeTrack<T>> mapCodec(NbtCodec<T> valueCodec) {
        NbtCodec<List<Keyframe<T>>> keyframesCodec = Keyframe.codec(valueCodec).applyList();
        return new NbtMapCodec<KeyframeTrack<T>>() {
            @Override
            public KeyframeTrack<T> decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
                List<Keyframe<T>> keyframes = compound.getOrThrow("keyframes", keyframesCodec, wrapper);
                EasingType easingType = compound.getOr("ease", EasingType.CODEC, EasingTypes.LINEAR, wrapper);
                return new KeyframeTrack<>(keyframes, easingType);
            }

            @Override
            public void encode(NBTCompound compound, PacketWrapper<?> wrapper, KeyframeTrack<T> value) throws NbtCodecException {
                compound.set("keyframes", value.keyframes, keyframesCodec, wrapper);
                if (value.easingType != EasingTypes.LINEAR) {
                    compound.set("ease", value.easingType, EasingType.CODEC, wrapper);
                }
            }
        };
    }

    public List<Keyframe<T>> getKeyframes() {
        return this.keyframes;
    }

    public EasingType getEasingType() {
        return this.easingType;
    }
}
