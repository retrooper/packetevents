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

import com.github.retrooper.packetevents.protocol.component.ComponentPredicate;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ComponentMatchers {

    private ComponentPredicate components;
    private List<TypedComponentPredicate<?>> predicates;

    public ComponentMatchers() {
        this(new ComponentPredicate(), new ArrayList<>());
    }

    public ComponentMatchers(ComponentPredicate components, List<TypedComponentPredicate<?>> predicates) {
        this.components = components;
        this.predicates = predicates;
    }

    public static ComponentMatchers read(PacketWrapper<?> wrapper) {
        ComponentPredicate components = ComponentPredicate.read(wrapper);
        List<TypedComponentPredicate<?>> predicates = wrapper.readList(TypedComponentPredicate::read);
        return new ComponentMatchers(components, predicates);
    }

    public static void write(PacketWrapper<?> wrapper, ComponentMatchers matchers) {
        ComponentPredicate.write(wrapper, matchers.components);
        wrapper.writeList(matchers.predicates, TypedComponentPredicate::write);
    }

    public ComponentPredicate getComponents() {
        return this.components;
    }

    public void setComponents(ComponentPredicate components) {
        this.components = components;
    }

    public List<TypedComponentPredicate<?>> getPredicates() {
        return this.predicates;
    }

    public void setPredicates(List<TypedComponentPredicate<?>> predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ComponentMatchers)) return false;
        ComponentMatchers that = (ComponentMatchers) obj;
        if (!this.components.equals(that.components)) return false;
        return this.predicates.equals(that.predicates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.components, this.predicates);
    }
}
