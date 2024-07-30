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

public interface DamageType extends MappedEntity, CopyableEntity<DamageType> {
    String getMessageId();

    DamageScaling getScaling();

    float getExhaustion();

    DamageEffects getEffects();

    DeathMessageType getDeathMessageType();

    static DamageType decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        String messageId = ((NBTCompound) nbt).getStringTagValueOrThrow("message_id");
        DamageScaling scaling = DamageScaling.ID_INDEX.valueOrThrow(
                ((NBTCompound) nbt).getStringTagValueOrThrow("scaling"));
        float exhaustion = ((NBTCompound) nbt).getNumberTagOrThrow("exhaustion").getAsFloat();
        DamageEffects effects = Optional.ofNullable(compound.getStringTagValueOrNull("effects"))
                .map(DamageEffects.ID_INDEX::valueOrThrow).orElse(DamageEffects.HURT);
        DeathMessageType deathMessageType = Optional.ofNullable(compound.getStringTagValueOrNull("death_message_type"))
                .map(DeathMessageType.ID_INDEX::valueOrThrow).orElse(DeathMessageType.DEFAULT);

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