package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class EnsureCorrectBlockTagsTest {

    @Test
    @DisplayName("Test block tags order")
    public void testTagsOrder() {
        assertDoesNotThrow(BlockTags.DRIPSTONE_REPLACEABLE::getName); // init the class
    }
}
