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

package com.github.retrooper.packetevents.protocol.recipe;

import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.recipe.display.slot.SlotDisplay;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This is a representation of some strange vanilla concept
 * specific to stonecutters.
 * <p>
 * Seems like a hack to make stonecutters work with their
 * rewritten recipe system, but I'm not too sure about what
 * this really is for.
 */
public class SingleInputOptionDisplay {

    private MappedEntitySet<ItemType> input;
    private SlotDisplay<?> optionDisplay;

    public SingleInputOptionDisplay(MappedEntitySet<ItemType> input, SlotDisplay<?> optionDisplay) {
        this.input = input;
        this.optionDisplay = optionDisplay;
    }

    public static SingleInputOptionDisplay read(PacketWrapper<?> wrapper) {
        MappedEntitySet<ItemType> ingredient = MappedEntitySet.read(wrapper, ItemTypes.getRegistry());
        SlotDisplay<?> optionDisplay = SlotDisplay.read(wrapper);
        return new SingleInputOptionDisplay(ingredient, optionDisplay);
    }

    public static void write(PacketWrapper<?> wrapper, SingleInputOptionDisplay recipe) {
        MappedEntitySet.write(wrapper, recipe.input);
        SlotDisplay.write(wrapper, recipe.optionDisplay);
    }

    public MappedEntitySet<ItemType> getInput() {
        return this.input;
    }

    public void setInput(MappedEntitySet<ItemType> input) {
        this.input = input;
    }

    public SlotDisplay<?> getOptionDisplay() {
        return this.optionDisplay;
    }

    public void setOptionDisplay(SlotDisplay<?> optionDisplay) {
        this.optionDisplay = optionDisplay;
    }
}
