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

package com.github.retrooper.packetevents.util;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Either<L, R> {

    private final @Nullable L left;
    private final @Nullable R right;

    private Either(@Nullable L left, @Nullable R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Either<L, R> createLeft(L left) {
        return new Either<>(left, null);
    }

    public static <L, R> Either<L, R> createRight(R right) {
        return new Either<>(null, right);
    }

    public Object get() {
        return this.left != null ? this.left : this.right;
    }

    public boolean isLeft() {
        return this.left != null;
    }

    public @Nullable L getLeft() {
        return this.left;
    }

    public boolean isRight() {
        return this.right != null;
    }

    public @Nullable R getRight() {
        return this.right;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Either)) return false;
        Either<?, ?> either = (Either<?, ?>) obj;
        if (!Objects.equals(this.left, either.left)) return false;
        return Objects.equals(this.right, either.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.left, this.right);
    }
}
