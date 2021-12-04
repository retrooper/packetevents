package com.github.retrooper.packetevents.protocol.world.chunk.palette;

import com.github.retrooper.packetevents.protocol.stream.NetStreamInput;
import com.github.retrooper.packetevents.protocol.stream.NetStreamOutput;

import java.io.IOException;

/**
 * A palette containing one state.
 * Credit to MCProtocolLib
 */
public class SingletonPalette implements Palette {
    private final int state;

    public SingletonPalette(NetStreamInput in) {
        this.state = in.readVarInt();
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public int stateToId(int state) {
        if (this.state == state) {
            return 0;
        }
        return -1;
    }

    @Override
    public int idToState(int id) {
        if (id == 0) {
            return this.state;
        }
        return 0;
    }
}
