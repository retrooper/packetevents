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

package com.github.retrooper.packetevents.protocol.component.builtin;

import com.github.retrooper.packetevents.protocol.world.blockentity.decopot.DecoratedPotPattern;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public class PotteryPatternComponent {

    private DecoratedPotPattern pattern;

    public PotteryPatternComponent(DecoratedPotPattern pattern) {
        this.pattern = pattern;
    }

    public static PotteryPatternComponent read(PacketWrapper<?> wrapper) {
        DecoratedPotPattern variant = DecoratedPotPattern.read(wrapper);
        return new PotteryPatternComponent(variant);
    }

    public static void write(PacketWrapper<?> wrapper, PotteryPatternComponent component) {
        DecoratedPotPattern.write(wrapper, component.pattern);
    }

    public DecoratedPotPattern getPattern() {
        return this.pattern;
    }

    public void setPattern(DecoratedPotPattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PotteryPatternComponent)) return false;
        PotteryPatternComponent that = (PotteryPatternComponent) obj;
        return this.pattern.equals(that.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.pattern);
    }
}
