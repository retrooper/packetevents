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
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface DamageType extends MappedEntity, CopyableEntity<DamageType>, DeepComparableEntity {

    NbtCodec<DamageType> DIRECT_CODEC = new NbtMapCodec<DamageType>() {
        @Override
        public DamageType decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            String messageId = tag.getStringTagValueOrThrow("message_id");
            DamageScaling scaling = tag.getOrThrow("scaling", DamageScaling.CODEC, wrapper);
            float exhaustion = tag.getNumberTagValueOrThrow("exhaustion").floatValue();
            DamageEffects effects = tag.getOr("effects", DamageEffects.CODEC, DamageEffects.HURT, wrapper);
            DeathMessageType deathMessageType = tag.getOr("death_message_type", DeathMessageType.CODEC, DeathMessageType.DEFAULT, wrapper);
            return new StaticDamageType(null, messageId, scaling, exhaustion, effects, deathMessageType);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, DamageType value) throws NbtCodecException {
            tag.setTag("message_id", new NBTString(value.getMessageId()));
            tag.set("scaling", value.getScaling(), DamageScaling.CODEC, wrapper);
            tag.setTag("exhaustion", new NBTFloat(value.getExhaustion()));
            if (value.getEffects() != DamageEffects.HURT) {
                tag.set("effects", value.getEffects(), DamageEffects.CODEC, wrapper);
            }
            if (value.getDeathMessageType() != DeathMessageType.DEFAULT) {
                tag.set("death_message_type", value.getDeathMessageType(), DeathMessageType.CODEC, wrapper);
            }
        }
    }.codec();
    NbtCodec<DamageType> CODEC = NbtCodecs.forRegistry(DamageTypes.getRegistry());

    String getMessageId();

    DamageScaling getScaling();

    float getExhaustion();

    DamageEffects getEffects();

    DeathMessageType getDeathMessageType();

    static DamageType read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntity(DamageTypes.getRegistry());
    }

    static void write(PacketWrapper<?> wrapper, DamageType damageType) {
        wrapper.writeMappedEntity(damageType);
    }

    @Deprecated
    static DamageType decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        return CODEC.decode(nbt, PacketWrapper.createDummyWrapper(version)).copy(data);
    }

    @Deprecated
    static NBT encode(DamageType damageType, ClientVersion version) {
        return CODEC.encode(PacketWrapper.createDummyWrapper(version), damageType);
    }
}
