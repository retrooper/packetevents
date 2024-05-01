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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;
import java.util.Objects;

public class ItemFireworks {

    private int flightDuration;
    private List<FireworkExplosion> explosions;

    public ItemFireworks(int flightDuration, List<FireworkExplosion> explosions) {
        this.flightDuration = flightDuration;
        this.explosions = explosions;
    }

    public static ItemFireworks read(PacketWrapper<?> wrapper) {
        int flightDuration = wrapper.readVarInt();
        List<FireworkExplosion> explosions = wrapper.readList(FireworkExplosion::read);
        return new ItemFireworks(flightDuration, explosions);
    }

    public static void write(PacketWrapper<?> wrapper, ItemFireworks fireworks) {
        wrapper.writeVarInt(fireworks.flightDuration);
        wrapper.writeList(fireworks.explosions, FireworkExplosion::write);
    }

    public int getFlightDuration() {
        return this.flightDuration;
    }

    public void setFlightDuration(int flightDuration) {
        this.flightDuration = flightDuration;
    }

    public void addExplosion(FireworkExplosion explosion) {
        this.explosions.add(explosion);
    }

    public List<FireworkExplosion> getExplosions() {
        return this.explosions;
    }

    public void setExplosions(List<FireworkExplosion> explosions) {
        this.explosions = explosions;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemFireworks)) return false;
        ItemFireworks that = (ItemFireworks) obj;
        if (this.flightDuration != that.flightDuration) return false;
        return this.explosions.equals(that.explosions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.flightDuration, this.explosions);
    }
}
