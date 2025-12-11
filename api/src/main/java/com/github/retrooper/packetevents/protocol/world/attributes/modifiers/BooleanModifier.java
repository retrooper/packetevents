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

package com.github.retrooper.packetevents.protocol.world.attributes.modifiers;

import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import org.jspecify.annotations.NullMarked;

/**
 * @versions 1.21.11+
 */
@NullMarked
public enum BooleanModifier implements AttributeModifier<Boolean, Boolean> {

    AND {
        @Override
        public Boolean apply(Boolean value, Boolean arg) {
            return arg && value;
        }
    },
    NAND {
        @Override
        public Boolean apply(Boolean value, Boolean arg) {
            return !arg || !value;
        }
    },
    OR {
        @Override
        public Boolean apply(Boolean value, Boolean arg) {
            return arg || value;
        }
    },
    NOR {
        @Override
        public Boolean apply(Boolean value, Boolean arg) {
            return !arg && !value;
        }
    },
    XOR {
        @Override
        public Boolean apply(Boolean value, Boolean arg) {
            return arg ^ value;
        }
    },
    XNOR {
        @Override
        public Boolean apply(Boolean value, Boolean arg) {
            return arg == value;
        }
    },
    ;

    @Override
    public NbtCodec<Boolean> argumentCodec(EnvironmentAttribute<Boolean> attribute) {
        return NbtCodecs.BOOLEAN;
    }
}
