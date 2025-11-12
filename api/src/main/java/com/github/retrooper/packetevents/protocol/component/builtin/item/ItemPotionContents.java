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
    private @Nullable String customName;

    public ItemPotionContents(
            @Nullable Potion potion,
            @Nullable Integer customColor,
            List<PotionEffect> customEffects
    ) {
        this(potion, customColor, customEffects, null);
    }

    public ItemPotionContents(
            @Nullable Potion potion,
            @Nullable Integer customColor,
            List<PotionEffect> customEffects,
            @Nullable String customName
    ) {
        this.potion = potion;
        this.customColor = customColor;
        this.customEffects = customEffects;
        this.customName = customName;
    }

    public static ItemPotionContents read(PacketWrapper<?> wrapper) {
        Potion potionId = wrapper.readOptional(ew -> ew.readMappedEntity(Potions::getById));
        Integer customColor = wrapper.readOptional(PacketWrapper::readInt);
        List<PotionEffect> customEffects = wrapper.readList(PotionEffect::read);
        String customName = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)
                ? wrapper.readOptional(PacketWrapper::readString) : null;
        return new ItemPotionContents(potionId, customColor, customEffects, customName);
    }

    public static void write(PacketWrapper<?> wrapper, ItemPotionContents contents) {
        wrapper.writeOptional(contents.potion, PacketWrapper::writeMappedEntity);
        wrapper.writeOptional(contents.customColor, PacketWrapper::writeInt);
        wrapper.writeList(contents.customEffects, PotionEffect::write);
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            wrapper.writeOptional(contents.customName, PacketWrapper::writeString);
        }
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

    public @Nullable String getCustomName() {
        return this.customName;
    }

    public void setCustomName(@Nullable String customName) {
        this.customName = customName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemPotionContents)) return false;
        ItemPotionContents that = (ItemPotionContents) obj;
        if (!Objects.equals(this.potion, that.potion)) return false;
        if (!Objects.equals(this.customColor, that.customColor)) return false;
        if (!this.customEffects.equals(that.customEffects)) return false;
        return Objects.equals(this.customName, that.customName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.potion, this.customColor, this.customEffects, this.customName);
    }
}
