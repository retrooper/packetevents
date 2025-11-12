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

package com.github.retrooper.packetevents.protocol.component.predicates;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class TypedComponentPredicate<T extends IComponentPredicate> {

    private final ComponentPredicateType<T> type;
    private final T predicate;

    public TypedComponentPredicate(ComponentPredicateType<T> type, T predicate) {
        this.type = type;
        this.predicate = predicate;
    }

    @SuppressWarnings("unchecked")
    public static TypedComponentPredicate<?> read(PacketWrapper<?> wrapper) {
        ComponentPredicateType<?> type = wrapper.readMappedEntity(ComponentPredicateTypes.getRegistry());
        IComponentPredicate predicate = type.read(wrapper);
        return new TypedComponentPredicate<>((ComponentPredicateType<IComponentPredicate>) type, predicate);
    }

    public static <T extends IComponentPredicate> void write(PacketWrapper<?> wrapper, TypedComponentPredicate<T> predicate) {
        wrapper.writeMappedEntity(predicate.type);
        predicate.type.write(wrapper, predicate.predicate);
    }

    public ComponentPredicateType<T> getType() {
        return this.type;
    }

    public T getPredicate() {
        return this.predicate;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TypedComponentPredicate)) return false;
        TypedComponentPredicate<?> that = (TypedComponentPredicate<?>) obj;
        if (!this.type.equals(that.type)) return false;
        return this.predicate.equals(that.predicate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.predicate);
    }
}
