package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18.Chunk_v1_18;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.test.base.BaseDummyAPITest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChunkFluidCountTest extends BaseDummyAPITest {

    @Test
    @DisplayName("Blocks that always hold water keep the 26.1+ section fluid count")
    public void testWaterHoldingBlocks() {
        ClientVersion version = ClientVersion.V_26_1;
        Chunk_v1_18 section = new Chunk_v1_18(version);
        // a new section's palette is empty, so palette id 0 has to be air before anything else is set
        section.set(0, 0, 0, WrappedBlockState.getDefaultState(version, StateTypes.AIR));

        section.set(0, 0, 0, WrappedBlockState.getDefaultState(version, StateTypes.WATER));
        assertEquals(1, section.getFluidCount());

        // e.g. soul sand under water turns it into a bubble column, the water is still there
        StateType[] waterHolding = {StateTypes.BUBBLE_COLUMN, StateTypes.KELP, StateTypes.KELP_PLANT,
                StateTypes.SEAGRASS, StateTypes.TALL_SEAGRASS};
        for (StateType type : waterHolding) {
            section.set(0, 0, 0, WrappedBlockState.getDefaultState(version, type));
            assertEquals(1, section.getFluidCount(), type.getName());
        }

        section.set(0, 0, 0, WrappedBlockState.getDefaultState(version, StateTypes.AIR));
        assertEquals(0, section.getFluidCount());
    }
}
