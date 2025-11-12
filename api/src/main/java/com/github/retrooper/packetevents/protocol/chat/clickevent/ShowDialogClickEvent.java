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
import com.github.retrooper.packetevents.protocol.dialog.Dialogs;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntityRef;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ShowDialogClickEvent implements ClickEvent {

    private final MappedEntityRef<Dialog> dialog;

    public ShowDialogClickEvent(Dialog dialog) {
        this(new MappedEntityRef.Static<>(dialog));
    }

    public ShowDialogClickEvent(MappedEntityRef<Dialog> dialog) {
        this.dialog = dialog;
    }

    public static ShowDialogClickEvent decode(NBTCompound compound, PacketWrapper<?> wrapper) {
        MappedEntityRef<Dialog> dialog = MappedEntityRef.decode(compound.getTagOrThrow("dialog"),
                Dialogs.getRegistry(), Dialog::decode, wrapper);
        return new ShowDialogClickEvent(dialog);
    }

    public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, ShowDialogClickEvent clickEvent) {
        compound.setTag("dialog", MappedEntityRef.encode(wrapper, Dialog::encode, clickEvent.dialog));
    }

    @Override
    public ClickEventAction<?> getAction() {
        return ClickEventActions.SHOW_DIALOG;
    }

    @Override
    public net.kyori.adventure.text.event.ClickEvent asAdventure() {
        return net.kyori.adventure.text.event.ClickEvent.showDialog(this.dialog.get());
    }

    public MappedEntityRef<Dialog> getDialogRef() {
        return this.dialog;
    }

    public Dialog getDialog() {
        return this.dialog.get();
    }
}
