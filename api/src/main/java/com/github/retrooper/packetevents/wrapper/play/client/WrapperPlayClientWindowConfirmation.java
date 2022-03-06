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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * A response to the window confirmation packet by the client
 * It is processed on the main thread, and is therefore very useful for anticheat purposes
 * <p>
 * If a confirmation sent by the client was not accepted, the server will reply with a
 * {@link WrapperPlayServerWindowConfirmation}
 * packet with the Accepted field set to false. When this happens,
 * the client must send this packet to apologize (as with movement),
 * otherwise the server ignores any successive confirmations.
 * <p>
 * Replaced in 1.17 with the more efficient {@link WrapperPlayClientPong}
 *
 * @see WrapperPlayServerWindowConfirmation
 */
public class WrapperPlayClientWindowConfirmation extends PacketWrapper<WrapperPlayClientWindowConfirmation> {
    private int windowId;
    private short actionId;
    private boolean accepted;

    public WrapperPlayClientWindowConfirmation(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientWindowConfirmation(int windowId, short actionId, boolean accepted) {
        super(PacketType.Play.Client.WINDOW_CONFIRMATION);
        this.windowId = windowId;
        this.actionId = actionId;
        this.accepted = accepted;
    }

    @Override
    public void readData() {
        windowId = readUnsignedByte();
        actionId = readShort();
        accepted = readBoolean();
    }

    @Override
    public void readData(WrapperPlayClientWindowConfirmation wrapper) {
        windowId = wrapper.windowId;
        actionId = wrapper.actionId;
        accepted = wrapper.accepted;
    }

    @Override
    public void writeData() {
        writeByte(windowId);
        writeShort(actionId);
        writeBoolean(accepted);
    }

    public int getWindowId() {
        return windowId;
    }

    public void setWindowId(int windowID) {
        this.windowId = windowID;
    }

    public short getActionId() {
        return actionId;
    }

    public void setActionId(short actionId) {
        this.actionId = actionId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
