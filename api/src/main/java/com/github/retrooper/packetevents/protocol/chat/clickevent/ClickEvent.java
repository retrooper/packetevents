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

package com.github.retrooper.packetevents.protocol.chat.clickevent;

import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.adventure.NbtTagHolder;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.event.ClickEvent.Payload;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ClickEvent {

    static ClickEvent decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        String actionName = compound.getStringTagValueOrThrow("action");
        ClickEventAction<?> action = ClickEventActions.getRegistry().getByNameOrThrow(actionName);
        return action.decode(compound, wrapper);
    }

    @SuppressWarnings("unchecked") // not unchecked
    static void encode(NBTCompound compound, PacketWrapper<?> wrapper, ClickEvent clickEvent) {
        compound.set("action", clickEvent.getAction().getName(), ResourceLocation::encode, wrapper);
        ((ClickEventAction<? super ClickEvent>) clickEvent.getAction()).encode(compound, wrapper, clickEvent);
    }

    ClickEventAction<?> getAction();

    static ClickEvent fromAdventure(net.kyori.adventure.text.event.ClickEvent clickEvent) {
        switch (clickEvent.action()) {
            case OPEN_URL:
                return new OpenUrlClickEvent(clickEvent.value());
            case OPEN_FILE:
                return new OpenFileClickEvent(clickEvent.value());
            case RUN_COMMAND:
                return new RunCommandClickEvent(clickEvent.value());
            case SUGGEST_COMMAND:
                return new SuggestCommandClickEvent(clickEvent.value());
            case CHANGE_PAGE:
                return new ChangePageClickEvent(clickEvent.value());
            case COPY_TO_CLIPBOARD:
                return new CopyToClipboardClickEvent(clickEvent.value());
            case SHOW_DIALOG:
                return new ShowDialogClickEvent((Dialog) ((Payload.Dialog) clickEvent.payload()).dialog());
            case CUSTOM:
                Payload.Custom payload = (Payload.Custom) clickEvent.payload();
                NbtTagHolder nbtTag = (NbtTagHolder) payload.nbt();
                return new CustomClickEvent(
                        new ResourceLocation(payload.key()),
                        nbtTag.getTag() instanceof NBTEnd ? null : nbtTag.getTag()
                );
            default:
                throw new UnsupportedOperationException("Unsupported clickevent: " + clickEvent);
        }
    }

    net.kyori.adventure.text.event.ClickEvent asAdventure();
}
