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

package com.github.retrooper.packetevents.protocol.dialog.button;

import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ActionButton {

    private final CommonButtonData button;
    private final @Nullable Action action;

    public ActionButton(CommonButtonData button, @Nullable Action action) {
        this.button = button;
        this.action = action;
    }

    public static ActionButton decode(NBT nbt, PacketWrapper<?> wrapper) {
        NBTCompound compound = (NBTCompound) nbt;
        CommonButtonData button = CommonButtonData.decode(compound, wrapper);
        Action action = compound.getOrNull("action", Action::decode, wrapper);
        return new ActionButton(button, action);
    }

    public static NBT encode(PacketWrapper<?> wrapper, ActionButton button) {
        NBTCompound compound = new NBTCompound();
        CommonButtonData.encode(compound, wrapper, button.button);
        if (button.action != null) {
            compound.set("action", button.action, Action::encode, wrapper);
        }
        return compound;
    }

    public CommonButtonData getButton() {
        return this.button;
    }

    public @Nullable Action getAction() {
        return this.action;
    }
}
