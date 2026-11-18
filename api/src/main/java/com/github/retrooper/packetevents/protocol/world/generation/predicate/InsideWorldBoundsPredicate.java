/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.generation.predicate;

import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.generation.BlockPredicate;
import com.github.retrooper.packetevents.protocol.world.generation.BlockPredicateType;
import com.github.retrooper.packetevents.protocol.world.generation.BlockPredicateTypes;
import com.github.retrooper.packetevents.util.Vector3i;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

/**
 * @versions 26.3+
 */
@NullMarked
public final class InsideWorldBoundsPredicate implements BlockPredicate {

    public static final NbtMapCodec<InsideWorldBoundsPredicate> MAP_CODEC = StateTestingPredicate.OFFSET_CODEC
            .apply(InsideWorldBoundsPredicate::new, InsideWorldBoundsPredicate::getOffset);

    private final Vector3i offset;

    public InsideWorldBoundsPredicate(Vector3i offset) {
        this.offset = offset;
    }

    public Vector3i getOffset() {
        return this.offset;
    }

    @Override
    public BlockPredicateType<?> getType() {
        return BlockPredicateTypes.INSIDE_WORLD_BOUNDS;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        InsideWorldBoundsPredicate that = (InsideWorldBoundsPredicate) obj;
        return this.offset.equals(that.offset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.offset);
    }
}
