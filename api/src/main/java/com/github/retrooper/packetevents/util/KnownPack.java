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

public class KnownPack {

    private final String namespace;
    private final String id;
    private final String version;

    public KnownPack(String namespace, String id, String version) {
        this.namespace = namespace;
        this.id = id;
        this.version = version;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getId() {
        return this.id;
    }

    public String getVersion() {
        return this.version;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof KnownPack)) return false;
        KnownPack knownPack = (KnownPack) obj;
        if (!this.namespace.equals(knownPack.namespace)) return false;
        if (!this.id.equals(knownPack.id)) return false;
        return this.version.equals(knownPack.version);
    }

    @Override
    public int hashCode() {
        int result = this.namespace.hashCode();
        result = 31 * result + this.id.hashCode();
        result = 31 * result + this.version.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "KnownPack{namespace='" + this.namespace + '\'' + ", id='" + this.id + '\'' + ", version='" + this.version + '\'' + '}';
    }
}
