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

import com.github.retrooper.packetevents.protocol.component.ComponentType;
import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.nbt.*;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class ItemTooltipDisplay {

    private boolean hideTooltip;
    private Set<ComponentType<?>> hiddenComponents;

    public ItemTooltipDisplay(boolean hideTooltip, Set<ComponentType<?>> hiddenComponents) {
        this.hideTooltip = hideTooltip;
        this.hiddenComponents = hiddenComponents;
    }

    public static ItemTooltipDisplay read(PacketWrapper<?> wrapper) {
        boolean hideTooltip = wrapper.readBoolean();
        Set<ComponentType<?>> hiddenComponents = wrapper.readCollection(LinkedHashSet::new,
                ew -> ew.readMappedEntity(ComponentTypes.getRegistry()));
        return new ItemTooltipDisplay(hideTooltip, hiddenComponents);
    }

    public static void write(PacketWrapper<?> wrapper, ItemTooltipDisplay tooltipDisplay) {
        wrapper.writeBoolean(tooltipDisplay.hideTooltip);
        wrapper.writeCollection(tooltipDisplay.hiddenComponents, PacketWrapper::writeMappedEntity);
    }

    public static ItemTooltipDisplay decode(NBT nbt, ClientVersion version) {
        NBTCompound compound = (NBTCompound) nbt;
        boolean hideTooltip = compound.getBooleanOr("hide_tooltip", false);
        NBTList<NBTString> hiddenComponentsList = compound.getStringListTagOrNull("hidden_components");
        Set<ComponentType<?>> hiddenComponents = new LinkedHashSet<>();
        if (hiddenComponentsList != null) {
            for (NBTString component : hiddenComponentsList.getTags()) {
                ComponentType<?> type = ComponentTypes.getRegistry().getByName(component.getValue());
                if (type != null) {
                    hiddenComponents.add(type);
                }
            }
        }
        return new ItemTooltipDisplay(hideTooltip, hiddenComponents);
    }

    public static NBT encode(ItemTooltipDisplay tooltipDisplay, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("hide_tooltip", new NBTByte(tooltipDisplay.hideTooltip ? (byte) 1 : (byte) 0));
        if (!tooltipDisplay.hiddenComponents.isEmpty()) {
            NBTList<NBTString> hiddenComponentsList = new NBTList<>(NBTType.STRING);
            for (ComponentType<?> component : tooltipDisplay.hiddenComponents) {
                hiddenComponentsList.addTag(new NBTString(component.getName().toString()));
            }
            compound.setTag("hidden_components", hiddenComponentsList);
        }
        return compound;
    }

    public boolean isHideTooltip() {
        return this.hideTooltip;
    }

    public void setHideTooltip(boolean hideTooltip) {
        this.hideTooltip = hideTooltip;
    }

    public Set<ComponentType<?>> getHiddenComponents() {
        return this.hiddenComponents;
    }

    public void setHiddenComponents(Set<ComponentType<?>> hiddenComponents) {
        this.hiddenComponents = hiddenComponents;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemTooltipDisplay)) return false;
        ItemTooltipDisplay that = (ItemTooltipDisplay) obj;
        if (this.hideTooltip != that.hideTooltip) return false;
        return this.hiddenComponents.equals(that.hiddenComponents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.hideTooltip, this.hiddenComponents);
    }
}
