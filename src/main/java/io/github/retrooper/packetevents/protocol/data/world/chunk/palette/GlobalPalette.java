/*
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */

package io.github.retrooper.packetevents.protocol.data.world.chunk.palette;

/**
 * A global palette that maps 1:1.
 */
//TODO Equals & hashcode
public class GlobalPalette implements Palette {
    @Override
    public int size() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int stateToId(int state) {
        return state;
    }

    @Override
    public int idToState(int id) {
        return id;
    }
}
