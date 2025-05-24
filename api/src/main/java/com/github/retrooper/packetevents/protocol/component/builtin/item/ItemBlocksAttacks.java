/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ItemBlocksAttacks {

    private float blockDelaySeconds;
    private float disableCooldownScale;
    private List<DamageReduction> damageReductions;
    private ItemDamageFunction itemDamage;
    private @Nullable ResourceLocation bypassedBy;
    private @Nullable Sound blockSound;
    private @Nullable Sound disableSound;

    public ItemBlocksAttacks(
            float blockDelaySeconds, float disableCooldownScale, List<DamageReduction> damageReductions,
            ItemDamageFunction itemDamage, @Nullable ResourceLocation bypassedBy,
            @Nullable Sound blockSound, @Nullable Sound disableSound
    ) {
        this.blockDelaySeconds = blockDelaySeconds;
        this.disableCooldownScale = disableCooldownScale;
        this.damageReductions = damageReductions;
        this.itemDamage = itemDamage;
        this.bypassedBy = bypassedBy;
        this.blockSound = blockSound;
        this.disableSound = disableSound;
    }

    public static ItemBlocksAttacks read(PacketWrapper<?> wrapper) {
        float blockDelaySeconds = wrapper.readFloat();
        float disableCooldownScale = wrapper.readFloat();
        List<DamageReduction> damageReductions = wrapper.readList(DamageReduction::read);
        ItemDamageFunction itemDamage = ItemDamageFunction.read(wrapper);
        ResourceLocation bypassedBy = wrapper.readOptional(PacketWrapper::readIdentifier);
        Sound blockSound = wrapper.readOptional(Sound::read);
        Sound disableSound = wrapper.readOptional(Sound::read);
        return new ItemBlocksAttacks(
                blockDelaySeconds, disableCooldownScale, damageReductions,
                itemDamage, bypassedBy, blockSound, disableSound);
    }

    public static void write(PacketWrapper<?> wrapper, ItemBlocksAttacks attack) {
        wrapper.writeFloat(attack.blockDelaySeconds);
        wrapper.writeFloat(attack.disableCooldownScale);
        wrapper.writeList(attack.damageReductions, DamageReduction::write);
        ItemDamageFunction.write(wrapper, attack.itemDamage);
        wrapper.writeOptional(attack.bypassedBy, PacketWrapper::writeIdentifier);
        wrapper.writeOptional(attack.blockSound, Sound::write);
        wrapper.writeOptional(attack.disableSound, Sound::write);
    }

    public float getBlockDelaySeconds() {
        return this.blockDelaySeconds;
    }

    public void setBlockDelaySeconds(float blockDelaySeconds) {
        this.blockDelaySeconds = blockDelaySeconds;
    }

    public float getDisableCooldownScale() {
        return this.disableCooldownScale;
    }

    public void setDisableCooldownScale(float disableCooldownScale) {
        this.disableCooldownScale = disableCooldownScale;
    }

    public List<DamageReduction> getDamageReductions() {
        return this.damageReductions;
    }

    public void setDamageReductions(List<DamageReduction> damageReductions) {
        this.damageReductions = damageReductions;
    }

    public ItemDamageFunction getItemDamage() {
        return this.itemDamage;
    }

    public void setItemDamage(ItemDamageFunction itemDamage) {
        this.itemDamage = itemDamage;
    }

    public @Nullable ResourceLocation getBypassedBy() {
        return this.bypassedBy;
    }

    public void setBypassedBy(@Nullable ResourceLocation bypassedBy) {
        this.bypassedBy = bypassedBy;
    }

    public @Nullable Sound getBlockSound() {
        return this.blockSound;
    }

    public void setBlockSound(@Nullable Sound blockSound) {
        this.blockSound = blockSound;
    }

    public @Nullable Sound getDisableSound() {
        return this.disableSound;
    }

    public void setDisableSound(@Nullable Sound disableSound) {
        this.disableSound = disableSound;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemBlocksAttacks)) return false;
        ItemBlocksAttacks that = (ItemBlocksAttacks) obj;
        if (Float.compare(that.blockDelaySeconds, this.blockDelaySeconds) != 0) return false;
        if (Float.compare(that.disableCooldownScale, this.disableCooldownScale) != 0) return false;
        if (!this.damageReductions.equals(that.damageReductions)) return false;
        if (!this.itemDamage.equals(that.itemDamage)) return false;
        if (!Objects.equals(this.bypassedBy, that.bypassedBy)) return false;
        if (!Objects.equals(this.blockSound, that.blockSound)) return false;
        return Objects.equals(this.disableSound, that.disableSound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.blockDelaySeconds, this.disableCooldownScale, this.damageReductions, this.itemDamage, this.bypassedBy, this.blockSound, this.disableSound);
    }

    public static final class DamageReduction {

        private float horizontalBlockingAngle;
        private @Nullable MappedEntitySet<DamageType> type;
        private float base;
        private float factor;

        public DamageReduction(float horizontalBlockingAngle, @Nullable MappedEntitySet<DamageType> type, float base, float factor) {
            this.horizontalBlockingAngle = horizontalBlockingAngle;
            this.type = type;
            this.base = base;
            this.factor = factor;
        }

        public static DamageReduction read(PacketWrapper<?> wrapper) {
            float horizontalBlockingAngle = wrapper.readFloat();
            MappedEntitySet<DamageType> type = wrapper.readOptional(ew ->
                    MappedEntitySet.read(ew, DamageTypes.getRegistry()));
            float base = wrapper.readFloat();
            float factor = wrapper.readFloat();
            return new DamageReduction(horizontalBlockingAngle, type, base, factor);
        }

        public static void write(PacketWrapper<?> wrapper, DamageReduction reduction) {
            wrapper.writeFloat(reduction.horizontalBlockingAngle);
            wrapper.writeOptional(reduction.type, MappedEntitySet::write);
            wrapper.writeFloat(reduction.base);
            wrapper.writeFloat(reduction.factor);
        }

        public float getHorizontalBlockingAngle() {
            return this.horizontalBlockingAngle;
        }

        public void setHorizontalBlockingAngle(float horizontalBlockingAngle) {
            this.horizontalBlockingAngle = horizontalBlockingAngle;
        }

        public @Nullable MappedEntitySet<DamageType> getType() {
            return this.type;
        }

        public void setType(@Nullable MappedEntitySet<DamageType> type) {
            this.type = type;
        }

        public float getBase() {
            return this.base;
        }

        public void setBase(float base) {
            this.base = base;
        }

        public float getFactor() {
            return this.factor;
        }

        public void setFactor(float factor) {
            this.factor = factor;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof DamageReduction)) return false;
            DamageReduction that = (DamageReduction) obj;
            if (Float.compare(that.horizontalBlockingAngle, this.horizontalBlockingAngle) != 0) return false;
            if (Float.compare(that.base, this.base) != 0) return false;
            if (Float.compare(that.factor, this.factor) != 0) return false;
            return Objects.equals(this.type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.horizontalBlockingAngle, this.type, this.base, this.factor);
        }
    }

    public static final class ItemDamageFunction {

        private float threshold;
        private float base;
        private float factor;

        public ItemDamageFunction(float threshold, float base, float factor) {
            this.threshold = threshold;
            this.base = base;
            this.factor = factor;
        }

        public static ItemDamageFunction read(PacketWrapper<?> wrapper) {
            float threshold = wrapper.readFloat();
            float base = wrapper.readFloat();
            float factor = wrapper.readFloat();
            return new ItemDamageFunction(threshold, base, factor);
        }

        public static void write(PacketWrapper<?> wrapper, ItemDamageFunction function) {
            wrapper.writeFloat(function.threshold);
            wrapper.writeFloat(function.base);
            wrapper.writeFloat(function.factor);
        }

        public float getThreshold() {
            return this.threshold;
        }

        public void setThreshold(float threshold) {
            this.threshold = threshold;
        }

        public float getBase() {
            return this.base;
        }

        public void setBase(float base) {
            this.base = base;
        }

        public float getFactor() {
            return this.factor;
        }

        public void setFactor(float factor) {
            this.factor = factor;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ItemDamageFunction)) return false;
            ItemDamageFunction that = (ItemDamageFunction) obj;
            if (Float.compare(that.threshold, this.threshold) != 0) return false;
            if (Float.compare(that.base, this.base) != 0) return false;
            return Float.compare(that.factor, this.factor) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.threshold, this.base, this.factor);
        }
    }
}
