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

package com.github.retrooper.packetevents.protocol.data.world.blockstate;

public class FlatBlockState implements BaseBlockState {
    //TODO Check https://github.com/MWHunter/Grim/blob/4b037ddbba1348c3383caf80c9b94a6e9489cfd4/src/main/java/ac/grim/grimac/utils/latency/CompensatedWorldFlat.java#L33
    private int blockData;
    private int globalID;


    public FlatBlockState(int globalID) {
        //this.blockData = CompensatedWorldFlat.globalPaletteToBlockData.get(globalID);
        this.globalID = globalID;
    }

    @Override
    public int getID() {
        return -1;
        //TODO Implement return blockData.getMaterial() //.getID();
    }

    public int getCombinedID() {
        return globalID;
    }
    /*

    public BlockData getBlockData() {
        return blockData;
    }

*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlatBlockState)) return false;

        FlatBlockState that = (FlatBlockState) o;
        return getCombinedID() == that.getCombinedID();
    }
}
