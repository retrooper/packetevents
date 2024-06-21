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

package com.github.retrooper.packetevents.resources;

import java.util.Objects;

public class ResourceLocation {
    protected final String namespace;
    protected final String key;

    public ResourceLocation(String namespace, String key) {
        this.namespace = namespace;
        this.key = key;
    }

    public ResourceLocation(String location) {
        String[] array = new String[]{"minecraft", location};
        int index = location.indexOf(":");
        if (index != -1) {
            array[1] = location.substring(index + 1);
            if (index >= 1) {
                array[0] = location.substring(0, index);
            }
        }
        this.namespace = array[0];
        this.key = array[1];
    }

    public String getNamespace() {
        return namespace;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.namespace, this.key);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ResourceLocation) {
            ResourceLocation other = (ResourceLocation) obj;
            return other.namespace.equals(namespace) && other.key.equals(key);
        }
        return false;
    }

    @Override
    public String toString() {
        return namespace + ":" + key;
    }

    public static ResourceLocation minecraft(String key) {
        return new ResourceLocation("minecraft", key);
    }
}
