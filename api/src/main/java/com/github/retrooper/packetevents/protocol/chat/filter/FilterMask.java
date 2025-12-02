/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.chat.filter;

import java.util.BitSet;

public class FilterMask {
    public static final FilterMask FULLY_FILTERED;
    public static final FilterMask PASS_THROUGH;

    static {
        FULLY_FILTERED = new FilterMask(new BitSet(0), FilterMaskType.FULLY_FILTERED);
        PASS_THROUGH = new FilterMask(new BitSet(0), FilterMaskType.PASS_THROUGH);
    }

    private final BitSet mask;
    private final FilterMaskType type;

    private FilterMask(BitSet mask, FilterMaskType type) {
        this.type = type;
        this.mask = mask;
    }

    public FilterMask(BitSet mask) {
        this.type = FilterMaskType.PARTIALLY_FILTERED;
        this.mask = mask;
    }

    public BitSet getMask() {
        return mask;
    }

    public FilterMaskType getType() {
        return type;
    }
}
