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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.potion.PotionEffect;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class FoodProperties {

    private int nutrition;
    private float saturation;
    private boolean canAlwaysEat;
    private float eatSeconds;
    private List<PossibleEffect> effects;
    private @Nullable ItemStack usingConvertsTo;

    public FoodProperties(
            int nutrition, float saturation, boolean canAlwaysEat,
            float eatSeconds, List<PossibleEffect> effects
    ) {
        this(nutrition, saturation, canAlwaysEat, eatSeconds, effects, null);
    }

    public FoodProperties(
            int nutrition, float saturation, boolean canAlwaysEat, float eatSeconds,
            List<PossibleEffect> effects, @Nullable ItemStack usingConvertsTo
    ) {
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.canAlwaysEat = canAlwaysEat;
        this.eatSeconds = eatSeconds;
        this.effects = effects;
        this.usingConvertsTo = usingConvertsTo;
    }

    public static FoodProperties read(PacketWrapper<?> wrapper) {
        int nutrition = wrapper.readVarInt();
        float saturation = wrapper.readFloat();
        boolean canAlwaysEat = wrapper.readBoolean();
        float eatSeconds = wrapper.readFloat();
        List<PossibleEffect> effects = wrapper.readList(PossibleEffect::read);
        ItemStack usingConvertsTo = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21)
                ? wrapper.readOptional(PacketWrapper::readItemStack) : null;
        return new FoodProperties(nutrition, saturation, canAlwaysEat, eatSeconds, effects, usingConvertsTo);
    }

    public static void write(PacketWrapper<?> wrapper, FoodProperties props) {
        wrapper.writeVarInt(props.nutrition);
        wrapper.writeFloat(props.saturation);
        wrapper.writeBoolean(props.canAlwaysEat);
        wrapper.writeFloat(props.eatSeconds);
        wrapper.writeList(props.effects, PossibleEffect::write);
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
            wrapper.writeOptional(props.usingConvertsTo, PacketWrapper::writeItemStack);
        }
    }

    public int getNutrition() {
        return this.nutrition;
    }

    public void setNutrition(int nutrition) {
        this.nutrition = nutrition;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public boolean isCanAlwaysEat() {
        return this.canAlwaysEat;
    }

    public void setCanAlwaysEat(boolean canAlwaysEat) {
        this.canAlwaysEat = canAlwaysEat;
    }

    public float getEatSeconds() {
        return this.eatSeconds;
    }

    public void setEatSeconds(float eatSeconds) {
        this.eatSeconds = eatSeconds;
    }

    public void addEffect(PossibleEffect effect) {
        this.effects.add(effect);
    }

    public List<PossibleEffect> getEffects() {
        return this.effects;
    }

    public void setEffects(List<PossibleEffect> effects) {
        this.effects = effects;
    }

    public @Nullable ItemStack getUsingConvertsTo() {
        return this.usingConvertsTo;
    }

    public void setUsingConvertsTo(@Nullable ItemStack usingConvertsTo) {
        this.usingConvertsTo = usingConvertsTo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FoodProperties)) return false;
        FoodProperties that = (FoodProperties) obj;
        if (this.nutrition != that.nutrition) return false;
        if (Float.compare(that.saturation, this.saturation) != 0) return false;
        if (this.canAlwaysEat != that.canAlwaysEat) return false;
        if (Float.compare(that.eatSeconds, this.eatSeconds) != 0) return false;
        if (!this.effects.equals(that.effects)) return false;
        return Objects.equals(this.usingConvertsTo, that.usingConvertsTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nutrition, this.saturation, this.canAlwaysEat, this.eatSeconds, this.effects, this.usingConvertsTo);
    }

    public static class PossibleEffect {

        private PotionEffect effect;
        private float probability;

        public PossibleEffect(PotionEffect effect, float probability) {
            this.effect = effect;
            this.probability = probability;
        }

        public static PossibleEffect read(PacketWrapper<?> wrapper) {
            PotionEffect effect = PotionEffect.read(wrapper);
            float probability = wrapper.readFloat();
            return new PossibleEffect(effect, probability);
        }

        public static void write(PacketWrapper<?> wrapper, PossibleEffect effect) {
            PotionEffect.write(wrapper, effect.effect);
            wrapper.writeFloat(effect.probability);
        }

        public PotionEffect getEffect() {
            return this.effect;
        }

        public void setEffect(PotionEffect effect) {
            this.effect = effect;
        }

        public float getProbability() {
            return this.probability;
        }

        public void setProbability(float probability) {
            this.probability = probability;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof PossibleEffect)) return false;
            PossibleEffect that = (PossibleEffect) obj;
            if (Float.compare(that.probability, this.probability) != 0) return false;
            return this.effect.equals(that.effect);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.effect, this.probability);
        }
    }
}


