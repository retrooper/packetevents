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
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import com.github.retrooper.packetevents.protocol.world.attributes.modifiers.AttributeModifier;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.11+
 */
@NullMarked
public class TimelineTrack<T, A> {

    private final AttributeModifier<T, A> modifier;
    private final KeyframeTrack<A> argumentTrack;

    public TimelineTrack(AttributeModifier<T, A> modifier, KeyframeTrack<A> argumentTrack) {
        this.modifier = modifier;
        this.argumentTrack = argumentTrack;
    }

    public static <T> NbtCodec<TimelineTrack<T, ?>> codec(EnvironmentAttribute<T> attribute) {
        NbtCodec<AttributeModifier<T, ?>> modifierCodec = attribute.getType().getModifierCodec();
        return new NbtMapCodec<TimelineTrack<T, ?>>() {
            @Override
            public TimelineTrack<T, ?> decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
                AttributeModifier<T, ?> modifier = compound.getOr("modifier", modifierCodec, AttributeModifier.override(), wrapper);
                return TimelineTrack.codec(attribute, modifier).decode(compound, wrapper);
            }

            @Override
            public void encode(NBTCompound compound, PacketWrapper<?> wrapper, TimelineTrack<T, ?> value) throws NbtCodecException {
                this.encode0(compound, wrapper, value);
            }

            private <A> void encode0(NBTCompound compound, PacketWrapper<?> wrapper, TimelineTrack<T, A> value) throws NbtCodecException {
                if (value.modifier != AttributeModifier.override()) {
                    compound.set("modifier", value.modifier, modifierCodec, wrapper);
                }
                TimelineTrack.codec(attribute, value.modifier).encode(compound, wrapper, value);
            }
        }.codec();
    }

    public static <T, A> NbtMapCodec<TimelineTrack<T, A>> codec(EnvironmentAttribute<T> attribute, AttributeModifier<T, A> modifier) {
        return KeyframeTrack.mapCodec(modifier.argumentCodec(attribute))
                .apply(track -> new TimelineTrack<>(modifier, track), TimelineTrack::getArgumentTrack);
    }

    public AttributeModifier<T, A> getModifier() {
        return this.modifier;
    }

    public KeyframeTrack<A> getArgumentTrack() {
        return this.argumentTrack;
    }
}
