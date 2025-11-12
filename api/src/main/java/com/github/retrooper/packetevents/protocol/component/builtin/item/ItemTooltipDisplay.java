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
