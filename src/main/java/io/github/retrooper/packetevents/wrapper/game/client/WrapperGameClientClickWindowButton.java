/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This packet is used when clicking on window buttons. Until 1.14, this was only used by enchantment tables.
 */
public class WrapperGameClientClickWindowButton extends PacketWrapper<WrapperGameClientClickWindowButton> {
    private byte windowID;
    private byte buttonID;

    public WrapperGameClientClickWindowButton(PacketReceiveEvent event) {
        super(event);
    }

    @Override
    public void readData() {
        this.windowID = readByte();
        this.buttonID = readByte();
    }

    @Override
    public void readData(WrapperGameClientClickWindowButton wrapper) {
        this.windowID = wrapper.windowID;
        this.buttonID = wrapper.buttonID;
    }

    @Override
    public void writeData() {
        writeByte(this.windowID);
        writeByte(this.buttonID);
    }

    /**
     * The ID of the window.
     *
     * @return Window ID
     */
    public byte getWindowID() {
        return windowID;
    }

    public void setWindowID(byte windowID) {
        this.windowID = windowID;
    }

    /**
     * Meaning depends on the window type.
     * Learn more about its meaning on wiki.vg/Protocol.
     *
     * @return Button ID
     */
    public byte getButtonID() {
        return buttonID;
    }

    public void setButtonID(byte buttonID) {
        this.buttonID = buttonID;
    }
}
