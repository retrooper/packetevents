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

public class WrapperPlayClientWindowConfirmation extends PacketWrapper<WrapperPlayClientWindowConfirmation> {
    private int windowID;
    private short actionNumber;
    private boolean accepted;

    public WrapperPlayClientWindowConfirmation(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientWindowConfirmation(int windowID, short actionNumber, boolean accepted) {
        super(PacketType.Play.Client.WINDOW_CONFIRMATION.getID());
        this.windowID = windowID;
        this.actionNumber = actionNumber;
        this.accepted = accepted;
    }

    @Override
    public void readData() {
        windowID = readByte();
        actionNumber = readShort();
        accepted = readBoolean();
    }

    @Override
    public void readData(WrapperPlayClientWindowConfirmation wrapper) {
        windowID = wrapper.windowID;
        actionNumber = wrapper.actionNumber;
        accepted = wrapper.accepted;
    }

    @Override
    public void writeData() {
        writeByte(windowID);
        writeShort(actionNumber);
        writeBoolean(accepted);
    }

    public int getWindowID() {
        return windowID;
    }

    public void setWindowID(int windowID) {
        this.windowID = windowID;
    }

    public short getActionNumber() {
        return actionNumber;
    }

    public void setActionNumber(short actionNumber) {
        this.actionNumber = actionNumber;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
