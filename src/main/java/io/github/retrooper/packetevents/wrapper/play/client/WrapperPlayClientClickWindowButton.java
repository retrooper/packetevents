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

package io.github.retrooper.packetevents.wrapper.play.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * This packet is used when clicking on window buttons. Until 1.14, this was only used by enchantment tables.
 */
public class WrapperPlayClientClickWindowButton extends PacketWrapper<WrapperPlayClientClickWindowButton> {
    private int windowID;
    private int buttonID;

    public WrapperPlayClientClickWindowButton(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientClickWindowButton(int windowID, int buttonID) {
        super(PacketType.Game.Client.CLICK_WINDOW_BUTTON.getID());
        this.windowID = windowID;
        this.buttonID = buttonID;
    }

    @Override
    public void readData() {
        this.windowID = readByte();
        this.buttonID = readByte();
    }

    @Override
    public void readData(WrapperPlayClientClickWindowButton wrapper) {
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
    public int getWindowID() {
        return windowID;
    }

    /**
     * Modify the ID of the window.
     *
     * @param windowID Window ID
     */
    public void setWindowID(int windowID) {
        this.windowID = windowID;
    }

    /**
     * Meaning depends on the window type.
     * Learn more on wiki.vg/Protocol.
     *
     * @return Button ID
     */
    public int getButtonID() {
        return buttonID;
    }

    /**
     * Modify the meaning.
     * Learn more on wiki.vg/Protocol.
     *
     * @param buttonID Button ID
     */
    public void setButtonID(int buttonID) {
        this.buttonID = buttonID;
    }
}
