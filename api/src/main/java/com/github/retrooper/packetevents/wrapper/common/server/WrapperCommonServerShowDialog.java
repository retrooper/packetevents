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

package com.github.retrooper.packetevents.wrapper.common.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperCommonServerShowDialog<T extends WrapperCommonServerShowDialog<T>> extends PacketWrapper<T> {

    protected Dialog dialog;

    public WrapperCommonServerShowDialog(PacketSendEvent event) {
        super(event);
    }

    public WrapperCommonServerShowDialog(PacketTypeCommon packetType, Dialog dialog) {
        super(packetType);
        this.dialog = dialog;
    }

    @Override
    public void read() {
        this.dialog = Dialog.read(this);
    }

    @Override
    public void write() {
        Dialog.write(this, this.dialog);
    }

    @Override
    public void copy(T wrapper) {
        this.dialog = wrapper.getDialog();
    }

    public Dialog getDialog() {
        return this.dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }
}
