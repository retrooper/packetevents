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

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetCooldown;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class ItemUseCooldown {

    private float seconds;
    private Optional<ResourceLocation> cooldownGroup;

    public ItemUseCooldown(float seconds, @Nullable ResourceLocation cooldownGroup) {
        this(seconds, Optional.ofNullable(cooldownGroup));
    }

    public ItemUseCooldown(float seconds, Optional<ResourceLocation> cooldownGroup) {
        this.seconds = seconds;
        this.cooldownGroup = cooldownGroup;
    }

    public static ItemUseCooldown read(PacketWrapper<?> wrapper) {
        float seconds = wrapper.readFloat();
        ResourceLocation cooldownGroup = wrapper.readOptional(PacketWrapper::readIdentifier);
        return new ItemUseCooldown(seconds, cooldownGroup);
    }

    public static void write(PacketWrapper<?> wrapper, ItemUseCooldown cooldown) {
        wrapper.writeFloat(cooldown.seconds);
        wrapper.writeOptional(cooldown.cooldownGroup.orElse(null), PacketWrapper::writeIdentifier);
    }

    public WrapperPlayServerSetCooldown buildWrapper(ItemStack fallbackStack) {
        return this.buildWrapper(fallbackStack.getType());
    }

    public WrapperPlayServerSetCooldown buildWrapper(ItemType fallbackItem) {
        int ticks = (int) (this.seconds * 20f);
        return this.cooldownGroup
                .map(resourceLocation -> new WrapperPlayServerSetCooldown(resourceLocation, ticks))
                .orElseGet(() -> new WrapperPlayServerSetCooldown(fallbackItem, ticks));
    }

    public float getSeconds() {
        return this.seconds;
    }

    public void setSeconds(float seconds) {
        this.seconds = seconds;
    }

    public Optional<ResourceLocation> getCooldownGroup() {
        return this.cooldownGroup;
    }

    public void setCooldownGroup(Optional<ResourceLocation> cooldownGroup) {
        this.cooldownGroup = cooldownGroup;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemUseCooldown)) return false;
        ItemUseCooldown that = (ItemUseCooldown) obj;
        if (Float.compare(that.seconds, this.seconds) != 0) return false;
        return this.cooldownGroup.equals(that.cooldownGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.seconds, this.cooldownGroup);
    }

    @Override
    public String toString() {
        return "ItemUseCooldown{seconds=" + this.seconds + ", cooldownGroup=" + this.cooldownGroup + '}';
    }
}
