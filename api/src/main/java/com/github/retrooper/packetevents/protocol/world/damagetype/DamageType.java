/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.damagetype;

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil.indexValueOrThrow;

public interface DamageType extends MappedEntity, CopyableEntity<DamageType> {
    String getMessageId();

    DamageScaling getScaling();

    float getExhaustion();

    DamageEffects getEffects();

    DeathMessageType getDeathMessageType();

    static DamageType decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        String messageId = ((NBTCompound) nbt).getStringTagValueOrThrow("message_id");
        DamageScaling scaling = indexValueOrThrow(DamageScaling.ID_INDEX,
                ((NBTCompound) nbt).getStringTagValueOrThrow("scaling"));
        float exhaustion = ((NBTCompound) nbt).getNumberTagOrThrow("exhaustion").getAsFloat();
        DamageEffects effects = Optional.ofNullable(compound.getStringTagValueOrNull("effects"))
                .map(id -> indexValueOrThrow(DamageEffects.ID_INDEX, id)).orElse(DamageEffects.HURT);
        DeathMessageType deathMessageType = Optional.ofNullable(compound.getStringTagValueOrNull("death_message_type"))
                .map(id -> indexValueOrThrow(DeathMessageType.ID_INDEX, id)).orElse(DeathMessageType.DEFAULT);

        return new StaticDamageType(data, messageId, scaling, exhaustion, effects, deathMessageType);
    }

    static NBT encode(DamageType damageType, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("message_id", new NBTString(damageType.getMessageId()));
        compound.setTag("scaling", new NBTString(damageType.getScaling().getId()));
        compound.setTag("exhaustion", new NBTFloat(damageType.getExhaustion()));

        if (damageType.getEffects() != DamageEffects.HURT) {
            compound.setTag("effects", new NBTString(damageType.getEffects().getId()));
        }

        if (damageType.getDeathMessageType() != DeathMessageType.DEFAULT) {
            compound.setTag("death_message_type", new NBTString(damageType.getDeathMessageType().getId()));
        }

        return compound;
    }
}
