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
import com.github.retrooper.packetevents.protocol.entity.sulfurcube.SulfurCubeArchtype;
import com.github.retrooper.packetevents.protocol.entity.sulfurcube.SulfurCubeArchtypes;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.test.base.BaseDummyAPITest;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import com.github.retrooper.packetevents.util.mappings.SimpleRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData.RegistryElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class UserRegistryResetTest extends BaseDummyAPITest {

    private static final ClientVersion VERSION = ClientVersion.V_26_2;
    private static final ResourceLocation CUSTOM_DAMAGE = new ResourceLocation("test:custom_damage");

    private ResourceLocation damageTypesKey;
    private ResourceLocation archetypesKey;
    private User user;
    private PacketWrapper<?> wrapper;
    private IRegistry<DamageType> previousDamageTypes;

    @BeforeEach
    public void setupUser() {
        this.damageTypesKey = DamageTypes.getRegistry().getRegistryKey();
        this.archetypesKey = SulfurCubeArchtypes.getRegistry().getRegistryKey();
        this.user = new User(null, ConnectionState.CONFIGURATION, VERSION, null);
        this.wrapper = PacketWrapper.createDummyWrapper(VERSION);
        this.wrapper.setRegistryHolder(this.user);
        this.previousDamageTypes = new SimpleRegistry<>(this.damageTypesKey);
        this.user.putRegistry(this.previousDamageTypes);
    }

    private void addPending(ResourceLocation registry, String name, NBT data) {
        this.user.addPendingRegistry(registry,
                Collections.singletonList(new RegistryElement(new ResourceLocation(name), data)));
    }

    private void addCustomDamageType() {
        this.addPending(this.damageTypesKey, CUSTOM_DAMAGE.toString(),
                DamageType.DIRECT_CODEC.encode(this.wrapper, DamageTypes.SULFUR_CUBE_HOT));
    }

    private NBT encodeArchetypeHittingCustomDamage() {
        NBTCompound archetype = (NBTCompound) SulfurCubeArchtype.DIRECT_CODEC.encode(this.wrapper, SulfurCubeArchtypes.HOT);
        archetype.getCompoundTagOrNull("contact_damage").setTag("damage_type", new NBTString(CUSTOM_DAMAGE.toString()));
        return archetype;
    }

    @Test
    @DisplayName("Registries resolve references to ones sent after them, and replace the previous ones on exit")
    public void testForwardReference() {
        this.addPending(this.archetypesKey, "minecraft:hot", this.encodeArchetypeHittingCustomDamage());
        this.addCustomDamageType();

        assertSame(this.previousDamageTypes, this.user.getRegistry(this.damageTypesKey, VERSION));
        assertNull(this.user.getRegistry(this.archetypesKey, VERSION));

        this.user.finalizeRegistries(this.wrapper);

        SulfurCubeArchtype hot = this.user.getRegistryOr(SulfurCubeArchtypes.getRegistry(), VERSION)
                .getByName(VERSION, "minecraft:hot");
        assertNotNull(hot);
        assertEquals(CUSTOM_DAMAGE, hot.getContactDamage().getDamageType().getName());
    }

    @Test
    @DisplayName("Registries are kept if the server didn't send new ones")
    public void testNoRegistriesSent() {
        this.user.finalizeRegistries(this.wrapper);
        assertSame(this.previousDamageTypes, this.user.getRegistry(this.damageTypesKey, VERSION));

        this.addCustomDamageType();
        this.user.finalizeRegistries(this.wrapper);
        IRegistry<?> damageTypes = this.user.getRegistry(this.damageTypesKey, VERSION);

        this.user.finalizeRegistries(this.wrapper);
        assertSame(damageTypes, this.user.getRegistry(this.damageTypesKey, VERSION));
    }

    @Test
    @DisplayName("An invalid registry doesn't prevent the other ones from being loaded")
    public void testInvalidRegistry() {
        this.addPending(this.archetypesKey, "minecraft:hot", new NBTCompound());
        this.addCustomDamageType();

        this.user.finalizeRegistries(this.wrapper);

        assertNull(this.user.getRegistry(this.archetypesKey, VERSION));
        assertNotNull(this.user.getRegistryOr(DamageTypes.getRegistry(), VERSION).getByName(VERSION, CUSTOM_DAMAGE.toString()));
    }
}
