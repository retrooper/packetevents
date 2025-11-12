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

package com.github.retrooper.packetevents.protocol.dialog;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum DialogAction {

    CLOSE("close", true),
    NONE("none", false),
    WAIT_FOR_RESPONSE("wait_for_response", true);

    public static final Index<String, DialogAction> NAME_INDEX = Index.create(
            DialogAction.class, DialogAction::getName);

    private final String name;
    private final boolean willUnpause;

    DialogAction(String name, boolean willUnpause) {
        this.name = name;
        this.willUnpause = willUnpause;
    }

    public static DialogAction decode(NBT nbt, PacketWrapper<?> wrapper) {
        return AdventureIndexUtil.indexValueOrThrow(NAME_INDEX, ((NBTString) nbt).getValue());
    }

    public static NBT encode(PacketWrapper<?> wrapper, DialogAction action) {
        return new NBTString(action.name);
    }

    public String getName() {
        return this.name;
    }

    public boolean isWillUnpause() {
        return this.willUnpause;
    }
}
