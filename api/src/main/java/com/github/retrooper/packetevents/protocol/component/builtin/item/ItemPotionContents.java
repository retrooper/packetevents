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

import com.github.retrooper.packetevents.protocol.potion.Potion;
import com.github.retrooper.packetevents.protocol.potion.PotionEffect;
import com.github.retrooper.packetevents.protocol.potion.Potions;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ItemPotionContents {

    private @Nullable Potion potion;
    private @Nullable Integer customColor;
    private List<PotionEffect> customEffects;

    public ItemPotionContents(
            @Nullable Potion potion,
            @Nullable Integer customColor,
            List<PotionEffect> customEffects
    ) {
        this.potion = potion;
        this.customColor = customColor;
        this.customEffects = customEffects;
    }

    public static ItemPotionContents read(PacketWrapper<?> wrapper) {
        Potion potionId = wrapper.readOptional(ew -> ew.readMappedEntity(Potions::getById));
        Integer customColor = wrapper.readOptional(PacketWrapper::readInt);
        List<PotionEffect> customEffects = wrapper.readList(PotionEffect::read);
        return new ItemPotionContents(potionId, customColor, customEffects);
    }

    public static void write(PacketWrapper<?> wrapper, ItemPotionContents contents) {
        wrapper.writeOptional(contents.potion, PacketWrapper::writeMappedEntity);
        wrapper.writeOptional(contents.customColor, PacketWrapper::writeInt);
        wrapper.writeList(contents.customEffects, PotionEffect::write);
    }

    public @Nullable Potion getPotion() {
        return this.potion;
    }

    public void setPotion(@Nullable Potion potion) {
        this.potion = potion;
    }

    public @Nullable Integer getCustomColor() {
        return this.customColor;
    }

    public void setCustomColor(@Nullable Integer customColor) {
        this.customColor = customColor;
    }

    private void addCustomEffect(PotionEffect effect) {
        this.customEffects.add(effect);
    }

    public List<PotionEffect> getCustomEffects() {
        return this.customEffects;
    }

    public void setCustomEffects(List<PotionEffect> customEffects) {
        this.customEffects = customEffects;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemPotionContents)) return false;
        ItemPotionContents that = (ItemPotionContents) obj;
        if (!Objects.equals(this.potion, that.potion)) return false;
        if (!Objects.equals(this.customColor, that.customColor)) return false;
        return this.customEffects.equals(that.customEffects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.potion, this.customColor, this.customEffects);
    }
}
