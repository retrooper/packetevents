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

import com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEvent;
import com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEventAction;
import com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEventActions;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class StaticAction implements Action {

    private final ActionType<?> actionType;
    private final ClickEvent clickEvent;

    public StaticAction(ClickEvent clickEvent) {
        if (!clickEvent.getAction().isAllowFromServer()) {
            throw new IllegalArgumentException("Can't create action for unreadable "
                    + "click event with action " + clickEvent.getAction());
        }
        this.actionType = ActionTypes.getRegistry().getByNameOrThrow(clickEvent.getAction().getName());
        this.clickEvent = clickEvent;
    }

    public static StaticAction decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        String actionName = compound.getStringTagValueOrThrow("type");
        ClickEventAction<?> action = ClickEventActions.getRegistry().getByNameOrThrow(actionName);
        ClickEvent clickEvent = action.decode(compound, wrapper);
        return new StaticAction(clickEvent);
    }

    @SuppressWarnings("unchecked")
    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, StaticAction action) {
        ((ClickEventAction<? super ClickEvent>) action.clickEvent.getAction()).encode(compound, wrapper, action.clickEvent);
    }

    @Override
    public ActionType<?> getType() {
        return this.actionType;
    }

    public ClickEvent getClickEvent() {
        return this.clickEvent;
    }
}
