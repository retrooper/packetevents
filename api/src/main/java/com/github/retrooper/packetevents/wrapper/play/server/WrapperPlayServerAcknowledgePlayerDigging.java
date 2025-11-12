/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerAcknowledgePlayerDigging extends PacketWrapper<WrapperPlayServerAcknowledgePlayerDigging> {
    private DiggingAction action;
    private boolean successful;
    private Vector3i blockPosition;
    private int blockID;

    public WrapperPlayServerAcknowledgePlayerDigging(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerAcknowledgePlayerDigging(DiggingAction action, boolean successful, Vector3i blockPosition, int blockID) {
        super(PacketType.Play.Server.ACKNOWLEDGE_PLAYER_DIGGING);
        this.action = action;
        this.successful = successful;
        this.blockPosition = blockPosition;
        this.blockID = blockID;
    }

    @Override
    public void read() {
        blockPosition = readBlockPosition();
        blockID = readVarInt();
        action = DiggingAction.getById(readVarInt());
        successful = readBoolean();
    }

    @Override
    public void write() {
        writeBlockPosition(blockPosition);
        writeVarInt(blockID);
        writeVarInt(action.getId());
        writeBoolean(successful);
    }

    @Override
    public void copy(WrapperPlayServerAcknowledgePlayerDigging wrapper) {
        action = wrapper.action;
        successful = wrapper.successful;
        blockPosition = wrapper.blockPosition;
        blockID = wrapper.blockID;
    }

    public DiggingAction getAction() {
        return action;
    }

    public void setAction(DiggingAction action) {
        this.action = action;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public int getBlockId() {
        return blockID;
    }

    public void setBlockId(int blockID) {
        this.blockID = blockID;
    }
}
