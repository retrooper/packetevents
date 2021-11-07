/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.blockstate;

public class MagicBlockState implements BaseBlockState {
    private final int id;
    private final int data;

    public MagicBlockState(int combinedID) {
        this.id = combinedID & 0xFF;
        this.data = combinedID >> 12;
    }

    public MagicBlockState(int id, int data) {
        this.id = id;
        this.data = data;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getCombinedId() {
        return id + (data << 12);
    }

    public int getBlockData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MagicBlockState)) return false;

        MagicBlockState that = (MagicBlockState) o;
        return this.id == that.getId() &&
                this.data == that.getBlockData();
    }
}