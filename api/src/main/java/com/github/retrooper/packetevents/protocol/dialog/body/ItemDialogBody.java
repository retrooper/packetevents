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

package com.github.retrooper.packetevents.protocol.dialog.body;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ItemDialogBody implements DialogBody {

    private final ItemStack item;
    private final @Nullable PlainMessage description;
    private final boolean showDecorations;
    private final boolean showTooltip;
    private final int width;
    private final int height;

    public ItemDialogBody(
            ItemStack item, @Nullable PlainMessage description,
            boolean showDecorations, boolean showTooltip,
            int width, int height
    ) {
        this.item = item;
        this.description = description;
        this.showDecorations = showDecorations;
        this.showTooltip = showTooltip;
        this.width = width;
        this.height = height;
    }

    public static ItemDialogBody decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        ItemStack item = compound.getOrThrow("item", ItemStack::decode, wrapper);
        PlainMessage description = compound.getOrNull("description", PlainMessage::decode, wrapper);
        boolean showDecorations = compound.getBooleanOr("show_decorations", true);
        boolean showTooltip = compound.getBooleanOr("show_tooltip", true);
        int width = compound.getNumberTagValueOrDefault("width", 16).intValue();
        int height = compound.getNumberTagValueOrDefault("height", 16).intValue();
        return new ItemDialogBody(item, description, showDecorations, showTooltip, width, height);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, ItemDialogBody body) {
        compound.set("item", body.item, ItemStack::encode, wrapper);
        if (body.description != null) {
            compound.set("description", body.description, PlainMessage::encode, wrapper);
        }
        if (!body.showDecorations) {
            compound.setTag("show_decorations", new NBTByte(false));
        }
        if (!body.showTooltip) {
            compound.setTag("show_tooltip", new NBTByte(false));
        }
        if (body.width != 16) {
            compound.setTag("width", new NBTInt(body.width));
        }
        if (body.height != 16) {
            compound.setTag("height", new NBTInt(body.height));
        }
    }

    @Override
    public DialogBodyType<?> getType() {
        return DialogBodyTypes.ITEM;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public @Nullable PlainMessage getDescription() {
        return this.description;
    }

    public boolean isShowDecorations() {
        return this.showDecorations;
    }

    public boolean isShowTooltip() {
        return this.showTooltip;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
