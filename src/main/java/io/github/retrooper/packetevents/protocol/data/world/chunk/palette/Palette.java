/*
 * This class was taken from MCProtocolLib.
 *
 * https://github.com/Steveice10/MCProtocolLib
 */

package io.github.retrooper.packetevents.protocol.data.world.chunk.palette;

/**
 * A palette for mapping block states to storage IDs.
 */
public interface Palette {
    /**
     * Gets the number of block states known by this palette.
     *
     * @return The palette's size.
     */
    int size();

    /**
     * Converts a block state to a storage ID. If the state has not been mapped,
     * the palette will attempt to map it, returning -1 if it cannot.
     *
     * @param state Block state to convert.
     * @return The resulting storage ID.
     */
    int stateToId(int state);

    /**
     * Converts a storage ID to a block state. If the storage ID has no mapping,
     * it will return a block state of 0.
     *
     * @param id Storage ID to convert.
     * @return The resulting block state.
     */
    int idToState(int id);
}
