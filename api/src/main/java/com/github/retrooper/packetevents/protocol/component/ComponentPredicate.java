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

package com.github.retrooper.packetevents.protocol.component;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class ComponentPredicate implements Predicate<IComponentMap> {

    private List<ComponentValue<?>> requiredComponents;

    public ComponentPredicate(List<ComponentValue<?>> requiredComponents) {
        this.requiredComponents = requiredComponents;
    }

    public static ComponentPredicate read(PacketWrapper<?> wrapper) {
        List<ComponentValue<?>> components = wrapper.readList(ComponentValue::read);
        return new ComponentPredicate(components);
    }

    public static void write(PacketWrapper<?> wrapper, ComponentPredicate predicate) {
        wrapper.writeList(predicate.requiredComponents, ComponentValue::write);
    }

    public static ComponentPredicate emptyPredicate() {
        return new ComponentPredicate(new ArrayList<>());
    }

    @SuppressWarnings("unchecked")
    public static ComponentPredicate fromPatches(PatchableComponentMap components) {
        Map<ComponentType<?>, Optional<?>> patches = components.getPatches();
        List<ComponentValue<?>> values = new ArrayList<>(patches.size());
        for (Map.Entry<ComponentType<?>, Optional<?>> patch : patches.entrySet()) {
            if (patch.getValue().isPresent()) {
                values.add(new ComponentValue<>(
                        (ComponentType<Object>) patch.getKey(),
                        patch.getValue().get()
                ));
            }
        }
        return new ComponentPredicate(values);
    }

    public PatchableComponentMap asPatches(StaticComponentMap base) {
        PatchableComponentMap patched = new PatchableComponentMap(base);
        for (ComponentValue<?> component : this.requiredComponents) {
            patched.set(component);
        }
        return patched;
    }

    @Override
    public boolean test(IComponentMap components) {
        for (ComponentValue<?> component : this.requiredComponents) {
            Optional<?> value = components.getOptional(component.getType());
            if (!value.isPresent() || !component.getValue().equals(value.get())) {
                return false;
            }
        }
        return true;
    }

    public List<ComponentValue<?>> getRequiredComponents() {
        return this.requiredComponents;
    }

    public void setRequiredComponents(List<ComponentValue<?>> requiredComponents) {
        this.requiredComponents = requiredComponents;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ComponentPredicate)) return false;
        ComponentPredicate that = (ComponentPredicate) obj;
        return this.requiredComponents.equals(that.requiredComponents);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.requiredComponents);
    }

    @Override
    public String toString() {
        return "ComponentPredicate{requiredComponents=" + this.requiredComponents + '}';
    }
}
