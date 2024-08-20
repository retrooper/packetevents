/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.particle.data;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleItemStackData extends ParticleData implements LegacyConvertible {
    private ItemStack itemStack;

    public ParticleItemStackData(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static ParticleItemStackData read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
            return new ParticleItemStackData(wrapper.readItemStack());
        } else {
            return new ParticleItemStackData(ItemStack.builder()
                    .type(ItemTypes.getById(wrapper.getClientVersion(), wrapper.readVarInt()))
                    .build());
        }
    }

    public static void write(PacketWrapper<?> wrapper, ParticleItemStackData data) {
        wrapper.writeItemStack(data.getItemStack());
    }

    public static ParticleItemStackData decode(NBTCompound compound, ClientVersion version) {
        String key = version.isNewerThanOrEquals(ClientVersion.V_1_20_5) ? "item" : "value";
        ItemStack stack = ItemStack.decode(compound.getTagOrThrow(key), version);
        return new ParticleItemStackData(stack);
    }

    public static void encode(ParticleItemStackData data, ClientVersion version, NBTCompound compound) {
        String key = version.isNewerThanOrEquals(ClientVersion.V_1_20_5) ? "item" : "value";
        compound.setTag(key, ItemStack.encodeForParticle(data.itemStack, version));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public LegacyParticleData toLegacy(ClientVersion version) {
        return LegacyParticleData.ofTwo(itemStack.getType().getId(version), itemStack.getLegacyData());
    }

}
