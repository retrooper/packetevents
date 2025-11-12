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

package com.github.retrooper.packetevents.protocol.debug;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @versions 1.21.9+
 */
@NullMarked
public class StaticDebugSubscription<T> extends AbstractMappedEntity implements DebugSubscription<T> {

    private final PacketWrapper.@Nullable Reader<T> reader;
    private final PacketWrapper.@Nullable Writer<T> writer;

    @ApiStatus.Internal
    public StaticDebugSubscription(
            @Nullable TypesBuilderData data,
            PacketWrapper.@Nullable Reader<T> reader,
            PacketWrapper.@Nullable Writer<T> writer
    ) {
        super(data);
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public T read(PacketWrapper<?> wrapper) {
        if (this.reader == null) {
            throw new IllegalStateException(this + " doesn't support network reading");
        }
        return this.reader.apply(wrapper);
    }

    @Override
    public void write(PacketWrapper<?> wrapper, T value) {
        if (this.writer == null) {
            throw new IllegalStateException(this + " doesn't support network writing");
        }
        this.writer.accept(wrapper, value);
    }
}
