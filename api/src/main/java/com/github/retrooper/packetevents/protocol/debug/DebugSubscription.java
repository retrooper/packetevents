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

import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @versions 1.21.9+
 */
@NullMarked
public interface DebugSubscription<T> extends MappedEntity {

    T read(PacketWrapper<?> wrapper);

    void write(PacketWrapper<?> wrapper, T value);

    final class Event<T> {

        private final DebugSubscription<T> subscription;
        private final T value;

        public Event(DebugSubscription<T> subscription, T value) {
            this.subscription = subscription;
            this.value = value;
        }

        public static Event<?> read(PacketWrapper<?> wrapper) {
            @SuppressWarnings("unchecked")
            DebugSubscription<? super Object> subscription = (DebugSubscription<? super Object>) wrapper.readMappedEntity(DebugSubscriptions.getRegistry());
            Object value = subscription.read(wrapper);
            return new Event<>(subscription, value);
        }

        public static <T> void write(PacketWrapper<?> wrapper, Event<T> event) {
            wrapper.writeMappedEntity(event.subscription);
            event.subscription.write(wrapper, event.value);
        }

        public DebugSubscription<T> getSubscription() {
            return this.subscription;
        }

        public T getValue() {
            return this.value;
        }
    }

    final class Update<T> {

        private final DebugSubscription<T> subscription;
        private final @Nullable T value;

        public Update(DebugSubscription<T> subscription, @Nullable T value) {
            this.subscription = subscription;
            this.value = value;
        }

        public static Update<?> read(PacketWrapper<?> wrapper) {
            @SuppressWarnings("unchecked")
            DebugSubscription<? super Object> subscription = (DebugSubscription<? super Object>) wrapper.readMappedEntity(DebugSubscriptions.getRegistry());
            Object value = wrapper.readOptional(subscription::read);
            return new Update<>(subscription, value);
        }

        public static <T> void write(PacketWrapper<?> wrapper, Update<T> event) {
            wrapper.writeMappedEntity(event.subscription);
            wrapper.writeOptional(event.value, event.subscription::write);
        }

        public DebugSubscription<T> getSubscription() {
            return this.subscription;
        }

        public @Nullable T getValue() {
            return this.value;
        }
    }
}
