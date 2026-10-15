package com.github.retrooper.packetevents.protocol.entity.sulfurcube;

import com.github.retrooper.packetevents.protocol.attribute.AttributeEntry;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @versions 26.2+
 */
@NullMarked
@ApiStatus.Internal
final class StaticSulfurCubeArchtype extends AbstractMappedEntity implements SulfurCubeArchtype {

    private final MappedEntitySet<ItemType> items;
    private final List<AttributeEntry> attributeModifiers;
    private final boolean buoyant;
    private final @Nullable SulfurCubeExplosionData explosion;
    private final @Nullable SulfurCubeContactDamage contactDamage;
    private final SulfurCubeKnockbackModifiers knockbackModifiers;
    private final SulfurCubeSoundSettings soundSettings;

    @ApiStatus.Internal
    public StaticSulfurCubeArchtype(
            @Nullable TypesBuilderData data, MappedEntitySet<ItemType> items, List<AttributeEntry> attributeModifiers,
            boolean buoyant, @Nullable SulfurCubeExplosionData explosion, @Nullable SulfurCubeContactDamage contactDamage,
            SulfurCubeKnockbackModifiers knockbackModifiers, SulfurCubeSoundSettings soundSettings
    ) {
        super(data);
        this.items = items;
        this.attributeModifiers = Collections.unmodifiableList(attributeModifiers);
        this.buoyant = buoyant;
        this.explosion = explosion;
        this.contactDamage = contactDamage;
        this.knockbackModifiers = knockbackModifiers;
        this.soundSettings = soundSettings;
    }

    @Override
    public SulfurCubeArchtype copy(@Nullable TypesBuilderData newData) {
        return new StaticSulfurCubeArchtype(newData, this.items, this.attributeModifiers, this.buoyant,
                this.explosion, this.contactDamage, this.knockbackModifiers, this.soundSettings);
    }

    @Override
    public MappedEntitySet<ItemType> getItems() {
        return this.items;
    }

    @Override
    public List<AttributeEntry> getAttributeModifiers() {
        return this.attributeModifiers;
    }

    @Override
    public boolean isBuoyant() {
        return this.buoyant;
    }

    @Override
    public @Nullable SulfurCubeExplosionData getExplosion() {
        return this.explosion;
    }

    @Override
    public @Nullable SulfurCubeContactDamage getContactDamage() {
        return this.contactDamage;
    }

    @Override
    public SulfurCubeKnockbackModifiers getKnockbackModifiers() {
        return this.knockbackModifiers;
    }

    @Override
    public SulfurCubeSoundSettings getSoundSettings() {
        return this.soundSettings;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        StaticSulfurCubeArchtype that = (StaticSulfurCubeArchtype) obj;
        if (this.buoyant != that.buoyant) return false;
        if (!this.items.equals(that.items)) return false;
        if (!this.attributeModifiers.equals(that.attributeModifiers)) return false;
        if (!Objects.equals(this.explosion, that.explosion)) return false;
        if (!Objects.equals(this.contactDamage, that.contactDamage)) return false;
        if (!this.knockbackModifiers.equals(that.knockbackModifiers)) return false;
        return this.soundSettings.equals(that.soundSettings);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.items, this.attributeModifiers, this.buoyant, this.explosion, this.contactDamage, this.knockbackModifiers, this.soundSettings);
    }
}
