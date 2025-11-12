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

package com.github.retrooper.packetevents.protocol.dialog.action;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DynamicRunCommandAction implements Action {

    private final DialogTemplate template;

    public DynamicRunCommandAction(DialogTemplate template) {
        this.template = template;
    }

    public static DynamicRunCommandAction decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        DialogTemplate template = compound.getOrThrow("template", DialogTemplate::decode, wrapper);
        return new DynamicRunCommandAction(template);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, DynamicRunCommandAction action) {
        compound.set("template", action.template, DialogTemplate::encode, wrapper);
    }

    public DialogTemplate getTemplate() {
        return this.template;
    }

    @Override
    public ActionType<?> getType() {
        return ActionTypes.DYNAMIC_RUN_COMMAND;
    }
}
