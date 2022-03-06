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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;

/**
 * Using this packet, you may confirm with 100% certainty whether the client could have received a packet
 * or has 100% received and processed the previous packet, by sending a window confirmation before and after
 * the packet you want to track. Very useful for anticheat purposes.
 * <p>
 * To get a client to respond every time, use windowID 0, any action ID, and accepted = false
 * <p>
 * A packet from the server indicating whether a request from the client was accepted, or whether there was a conflict
 * (due to lag). If the packet was not accepted, the client must respond with a serverbound window confirmation packet.
 * <p>
 * Replaced in 1.17 with the more efficient {@link WrapperPlayServerPing}
 *
 * @see WrapperPlayClientWindowConfirmation
 */
public class WrapperPlayServerWindowConfirmation extends PacketWrapper<WrapperPlayServerWindowConfirmation> {
    int windowId;
    short actionId;
    boolean accepted;

    public WrapperPlayServerWindowConfirmation(PacketSendEvent event) {
        super(event);
    }

    /**
     * @param windowId The ID of the window that the action occurred in. Use 0 for the player's inventory,
     *                 as to always get a response (in combination with accepted = false)
     * @param actionId Every action that is to be accepted has a unique number.
     *                 This number is an incrementing integer (starting at 1) with separate counts for each window ID.
     *                 Using non-positive numbers is recommended to avoid conflicting with vanilla transactions
     * @param accepted Whether the action was accepted. Use false to get a response from the client.
     */
    public WrapperPlayServerWindowConfirmation(int windowId, short actionId, boolean accepted) {
        super(PacketType.Play.Server.WINDOW_CONFIRMATION);
        this.windowId = windowId;
        this.actionId = actionId;
        this.accepted = accepted;
    }

    @Override
    public void readData() {
        this.windowId = readUnsignedByte();
        this.actionId = readShort();
        this.accepted = readBoolean();
    }

    @Override
    public void writeData() {
        writeByte(windowId);
        writeShort(actionId);
        writeBoolean(accepted);
    }

    @Override
    public void readData(WrapperPlayServerWindowConfirmation wrapper) {
        this.windowId = wrapper.getWindowId();
        this.actionId = wrapper.getActionId();
        this.accepted = wrapper.isAccepted();
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

    public void setActionId(short actionID) {
        this.actionId = actionID;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
