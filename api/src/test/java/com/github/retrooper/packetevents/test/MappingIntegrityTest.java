package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.test.base.BaseDummyAPITest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MappingIntegrityTest extends BaseDummyAPITest {

    @Test
    @DisplayName("Test attribute mapping")
    public void testAttributeMapping() {
        Assertions.assertEquals(11, Attributes.GENERIC_GRAVITY.getId(ClientVersion.V_1_20_5));
        Assertions.assertEquals(1, Attributes.GENERIC_FOLLOW_RANGE.getId(ClientVersion.V_1_20_2));
    }

    @Test
    @DisplayName("Test enchantment mapping")
    public void testEnchantmentMapping() {
        Assertions.assertEquals(14, EnchantmentTypes.SMITE.getId(ClientVersion.V_1_20_5));
        Assertions.assertEquals(13, EnchantmentTypes.SMITE.getId(ClientVersion.V_1_17_1));
        Assertions.assertEquals(12, EnchantmentTypes.SMITE.getId(ClientVersion.V_1_13_2));
        Assertions.assertEquals(17, EnchantmentTypes.SMITE.getId(ClientVersion.V_1_12));
    }
}
