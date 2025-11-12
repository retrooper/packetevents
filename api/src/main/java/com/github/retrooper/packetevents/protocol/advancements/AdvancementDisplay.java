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

package com.github.retrooper.packetevents.protocol.advancements;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public final class AdvancementDisplay {

    public static final int FLAG_HAS_BACKGROUND = 0b001;
    public static final int FLAG_SHOW_TOAST = 0b010;
    public static final int FLAG_HIDDEN = 0b100;

    private Component title;
    private Component description;
    private ItemStack icon;
    private AdvancementType type;
    private boolean showToast;
    private boolean hidden;
    private @Nullable ResourceLocation background;
    private float x;
    private float y;

    public AdvancementDisplay(Component title, Component description, ItemStack icon, AdvancementType type, @Nullable ResourceLocation background, boolean showToast,
                              boolean hidden, float x, float y) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.type = type;
        this.showToast = showToast;
        this.hidden = hidden;
        this.background = background;
        this.x = x;
        this.y = y;
    }

    public static AdvancementDisplay read(PacketWrapper<?> wrapper) {
        Component title = wrapper.readComponent();
        Component description = wrapper.readComponent();
        ItemStack icon = wrapper.readItemStack();
        AdvancementType type = wrapper.readEnum(AdvancementType.class);
        int flags = wrapper.readInt();
        ResourceLocation background = (flags & FLAG_HAS_BACKGROUND) != 0 ? ResourceLocation.read(wrapper) : null;
        boolean showToast = (flags & FLAG_SHOW_TOAST) != 0;
        boolean hidden = (flags & FLAG_HIDDEN) != 0;
        float x = wrapper.readFloat();
        float y = wrapper.readFloat();
        return new AdvancementDisplay(title, description, icon, type, background, showToast, hidden, x, y);
    }

    public static void write(PacketWrapper<?> wrapper, AdvancementDisplay display) {
        wrapper.writeComponent(display.title);
        wrapper.writeComponent(display.description);
        wrapper.writeItemStack(display.icon);
        wrapper.writeEnum(display.type);
        wrapper.writeInt(display.packFlags());
        if (display.background != null) {
            ResourceLocation.write(wrapper, display.background);
        }
        wrapper.writeFloat(display.x);
        wrapper.writeFloat(display.y);
    }

    public int packFlags() {
        int flags = 0;
        if (this.background != null) {
            flags |= FLAG_HAS_BACKGROUND;
        }
        if (this.showToast) {
            flags |= FLAG_SHOW_TOAST;
        }
        if (this.hidden) {
            flags |= FLAG_HIDDEN;
        }
        return flags;
    }

    public Component getTitle() {
        return this.title;
    }

    public void setTitle(Component title) {
        this.title = title;
    }

    public Component getDescription() {
        return this.description;
    }

    public void setDescription(Component description) {
        this.description = description;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public AdvancementType getType() {
        return this.type;
    }

    public void setType(AdvancementType type) {
        this.type = type;
    }

    public boolean isShowToast() {
        return this.showToast;
    }

    public void setShowToast(boolean showToast) {
        this.showToast = showToast;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public @Nullable ResourceLocation getBackground() {
        return this.background;
    }

    public void setBackground(@Nullable ResourceLocation background) {
        this.background = background;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
