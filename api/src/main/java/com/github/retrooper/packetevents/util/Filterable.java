/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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


import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class Filterable<T> {

    private T raw;
    private @Nullable T filtered;

    public Filterable(T raw) {
        this(raw, (T) null);
    }

    public Filterable(T raw, Optional<T> filtered) {
        this(raw, filtered.orElse(null));
    }

    public Filterable(T raw, @Nullable T filtered) {
        this.raw = raw;
        this.filtered = filtered;
    }

    public static <T> Filterable<T> read(
            PacketWrapper<?> wrapper,
            PacketWrapper.Reader<T> reader
    ) {
        T raw = reader.apply(wrapper);
        T filtered = wrapper.readOptional(reader);
        return new Filterable<>(raw, filtered);
    }

    public static <T> void write(
            PacketWrapper<?> wrapper,
            Filterable<T> filterable,
            PacketWrapper.Writer<T> writer
    ) {
        writer.accept(wrapper, filterable.raw);
        wrapper.writeOptional(filterable.filtered, writer);
    }

    public T getRaw() {
        return this.raw;
    }

    public void setRaw(T raw) {
        this.raw = raw;
    }

    public @Nullable T getFiltered() {
        return this.filtered;
    }

    public void setFiltered(@Nullable T filtered) {
        this.filtered = filtered;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Filterable)) return false;
        Filterable<?> that = (Filterable<?>) obj;
        if (!this.raw.equals(that.raw)) return false;
        return Objects.equals(this.filtered, that.filtered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.raw, this.filtered);
    }
}
