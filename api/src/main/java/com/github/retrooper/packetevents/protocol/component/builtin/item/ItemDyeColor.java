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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public class ItemDyeColor {

    private int rgb;
    /**
     * Removed in 1.21.5
     */
    @ApiStatus.Obsolete
    private boolean showInTooltip;

    public ItemDyeColor(int rgb) {
        this(rgb, true);
    }

    /**
     * {@link #showInTooltip} has been removed in 1.21.5
     */
    @ApiStatus.Obsolete
    public ItemDyeColor(int rgb, boolean showInTooltip) {
        this.rgb = rgb;
        this.showInTooltip = showInTooltip;
    }

    public static ItemDyeColor read(PacketWrapper<?> wrapper) {
        int rgb = wrapper.readInt();
        boolean showInTooltip = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) || wrapper.readBoolean();
        return new ItemDyeColor(rgb, showInTooltip);
    }

    public static void write(PacketWrapper<?> wrapper, ItemDyeColor color) {
        wrapper.writeInt(color.rgb);
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
            wrapper.writeBoolean(color.showInTooltip);
        }
    }

    public int getRgb() {
        return this.rgb;
    }

    public void setRgb(int rgb) {
        this.rgb = rgb;
    }

    /**
     * Removed in 1.21.5
     */
    @ApiStatus.Obsolete
    public boolean isShowInTooltip() {
        return this.showInTooltip;
    }

    /**
     * Removed in 1.21.5
     */
    @ApiStatus.Obsolete
    public void setShowInTooltip(boolean showInTooltip) {
        this.showInTooltip = showInTooltip;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemDyeColor)) return false;
        ItemDyeColor that = (ItemDyeColor) obj;
        if (this.rgb != that.rgb) return false;
        return this.showInTooltip == that.showInTooltip;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rgb, this.showInTooltip);
    }
}
