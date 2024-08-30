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

import com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffect;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;
import java.util.Objects;

public class ItemConsumable {

    private float consumeSeconds;
    private Animation animation;
    private Sound sound;
    private boolean consumeParticles;
    private List<ConsumeEffect> effects;

    public ItemConsumable(
            float consumeSeconds, Animation animation, Sound sound,
            boolean consumeParticles, List<ConsumeEffect> effects
    ) {
        this.consumeSeconds = consumeSeconds;
        this.animation = animation;
        this.sound = sound;
        this.consumeParticles = consumeParticles;
        this.effects = effects;
    }

    public static ItemConsumable read(PacketWrapper<?> wrapper) {
        float consumeSeconds = wrapper.readFloat();
        Animation animation = wrapper.readEnum(Animation.values());
        Sound sound = Sound.read(wrapper);
        boolean consumeParticles = wrapper.readBoolean();
        List<ConsumeEffect> effects = wrapper.readList(ConsumeEffect::readFull);
        return new ItemConsumable(consumeSeconds, animation, sound, consumeParticles, effects);
    }

    public static void write(PacketWrapper<?> wrapper, ItemConsumable consumable) {
        wrapper.writeFloat(consumable.consumeSeconds);
        wrapper.writeEnum(consumable.animation);
        Sound.write(wrapper, consumable.sound);
        wrapper.writeBoolean(consumable.consumeParticles);
        wrapper.writeList(consumable.effects, ConsumeEffect::writeFull);
    }

    public float getConsumeSeconds() {
        return this.consumeSeconds;
    }

    public void setConsumeSeconds(float consumeSeconds) {
        this.consumeSeconds = consumeSeconds;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Sound getSound() {
        return this.sound;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public boolean isConsumeParticles() {
        return this.consumeParticles;
    }

    public void setConsumeParticles(boolean consumeParticles) {
        this.consumeParticles = consumeParticles;
    }

    public List<ConsumeEffect> getEffects() {
        return this.effects;
    }

    public void setEffects(List<ConsumeEffect> effects) {
        this.effects = effects;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemConsumable)) return false;
        ItemConsumable that = (ItemConsumable) obj;
        if (Float.compare(that.consumeSeconds, this.consumeSeconds) != 0) return false;
        if (this.consumeParticles != that.consumeParticles) return false;
        if (this.animation != that.animation) return false;
        if (!this.sound.equals(that.sound)) return false;
        return this.effects.equals(that.effects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.consumeSeconds, this.animation, this.sound, this.consumeParticles, this.effects);
    }

    @Override
    public String toString() {
        return "ItemConsumable{consumeSeconds=" + this.consumeSeconds + ", animation=" + this.animation + ", sound=" + this.sound + ", consumeParticles=" + this.consumeParticles + ", effects=" + this.effects + '}';
    }

    public enum Animation {
        NONE,
        EAT,
        DRINK,
        BLOCK,
        BOW,
        SPEAR,
        CROSSBOW,
        SPYGLASS,
        TOOT_HORN,
        BRUSH,
    }
}
