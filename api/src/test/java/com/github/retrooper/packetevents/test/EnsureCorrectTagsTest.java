package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.protocol.world.states.defaulttags.BlockTags;
import com.github.retrooper.packetevents.protocol.world.states.defaulttags.ItemTags;
import com.github.retrooper.packetevents.test.base.BaseDummyAPITest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EnsureCorrectTagsTest extends BaseDummyAPITest {

    // These make sure the order of our tags are correct, or there's some other weird error on initialization
    // The order is wrong if we try to add a tag to another tag but the former has nothing in it
    @Test
    @DisplayName("Test block tags order")
    public void testBlockTagsOrder() {
        assertDoesNotThrow(BlockTags.DRIPSTONE_REPLACEABLE::getName); // init the class
    }

    @Test
    @DisplayName("Test item tags order")
    public void testItemTagsOrder() {
        assertDoesNotThrow(ItemTags.ACACIA_LOGS::getName); // init the class
    }

    // This makes sure we don't add a tag and forget to add stuff to it
    @Test
    @DisplayName("Ensure tags are not empty")
    public void testTagsAreNotEmpty() throws ReflectiveOperationException {
        for (Field field : ItemTags.class.getFields()) {
            final ItemTags tags = (ItemTags) field.get(ItemTags.class);
            if (tags.isReallyEmpty()) {
                continue;
            }
            assertFalse(tags.getStates().isEmpty(), "Item tag " + tags.getName() + " has nothing inside it! Did you forget to add types to it?");
        }

        for (Field field : BlockTags.class.getFields()) {
            final BlockTags tags = (BlockTags) field.get(BlockTags.class);
            if (tags.isReallyEmpty()) {
                continue;
            }
            assertFalse(tags.getStates().isEmpty(), "Block tag " + tags.getName() + " has nothing inside it! Did you forget to add types to it?");
        }
    }
}
