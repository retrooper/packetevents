package com.github.retrooper.packetevents.protocol.world.damagetype;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.Nullable;

public class StaticDamageType extends AbstractMappedEntity implements DamageType {
    private String messageId;
    private DamageScaling scaling;
    private float exhaustion;
    private DamageEffects effects;
    private DeathMessageType deathMessageType;

    protected StaticDamageType(@Nullable TypesBuilderData data, String messageId, DamageScaling scaling,
            float exhaustion, DamageEffects effects, DeathMessageType deathMessageType) {
        super(data);

        this.messageId = messageId;
        this.scaling = scaling;
        this.exhaustion = exhaustion;
        this.effects = effects;
        this.deathMessageType = deathMessageType;
    }

    @Override
    public DamageType copy(@Nullable TypesBuilderData newData) {
        return new StaticDamageType(newData, this.messageId, this.scaling, this.exhaustion, this.effects,
                this.deathMessageType);
    }

    @Override
    public String getMessageId() {
        return this.messageId;
    }

    @Override
    public DamageScaling getScaling() {
        return this.scaling;
    }

    @Override
    public float getExhaustion() {
        return this.exhaustion;
    }

    @Override
    public DamageEffects getEffects() {
        return this.effects;
    }

    @Override
    public DeathMessageType getDeathMessageType() {
        return this.deathMessageType;
    }
}
