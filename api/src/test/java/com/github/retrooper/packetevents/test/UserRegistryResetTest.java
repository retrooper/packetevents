/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import com.github.retrooper.packetevents.test.base.BaseDummyAPITest;
import com.github.retrooper.packetevents.util.mappings.SimpleRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserRegistryResetTest extends BaseDummyAPITest {

    @Test
    @DisplayName("Registry references don't resolve against a previous server's registries after a reset")
    public void testForwardReferenceAfterServerSwitch() {
        ClientVersion version = ClientVersion.V_26_2;
        User user = new User(null, ConnectionState.CONFIGURATION, version, null);
        PacketWrapper<?> wrapper = PacketWrapper.createDummyWrapper(version);
        wrapper.setRegistryHolder(user);
        NBTString reference = new NBTString("minecraft:sulfur_cube_hot");

        // the previous server's damage_type registry doesn't know this entry
        user.putRegistry(new SimpleRegistry<DamageType>(DamageTypes.getRegistry().getRegistryKey()));
        assertThrows(NbtCodecException.class, () -> DamageType.CODEC.decode(reference, wrapper));

        // registries are kept as long as they haven't been marked as outdated
        user.clearOutdatedRegistries();
        assertThrows(NbtCodecException.class, () -> DamageType.CODEC.decode(reference, wrapper));

        user.markRegistriesOutdated();
        assertThrows(NbtCodecException.class, () -> DamageType.CODEC.decode(reference, wrapper));

        user.clearOutdatedRegistries();
        assertEquals(DamageTypes.SULFUR_CUBE_HOT, DamageType.CODEC.decode(reference, wrapper));
    }
}
