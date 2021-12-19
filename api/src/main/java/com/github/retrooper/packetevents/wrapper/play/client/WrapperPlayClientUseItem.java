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

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

public class WrapperPlayClientUseItem extends PacketWrapper<WrapperPlayClientUseItem> {
    private InteractionHand interactionHand;
    private Vector3i blockPosition;
    private BlockFace face;
    private Vector3f cursorPosition;
    private Optional<Boolean> insideBlock;

    public WrapperPlayClientUseItem(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientUseItem(InteractionHand interactionHand, Vector3i blockPosition, BlockFace face, Vector3f cursorPosition, Optional<Boolean> insideBlock) {
        super(PacketType.Play.Client.USE_ITEM);
        this.interactionHand = interactionHand;
        this.blockPosition = blockPosition;
        this.face = face;
        this.cursorPosition= cursorPosition;
        this.insideBlock = insideBlock;
    }

    @Override
    public void readData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            interactionHand = InteractionHand.getById(readVarInt());
            blockPosition = readBlockPosition();
            face = BlockFace.getBlockFaceByValue(readVarInt());
            cursorPosition = new Vector3f(readFloat(), readFloat(), readFloat());
            insideBlock = Optional.of(readBoolean());
        } else {
            blockPosition = readBlockPosition();
            face = BlockFace.getBlockFaceByValue(readVarInt());
            interactionHand = InteractionHand.getById(readVarInt());
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11)) {
                cursorPosition = new Vector3f(readFloat(), readFloat(), readFloat());
            } else {
                cursorPosition = new Vector3f(readUnsignedByte() / 16.0F, readUnsignedByte() / 16.0F, readUnsignedByte() / 16.0F);
            }
            insideBlock = Optional.empty();
        }
    }

    @Override
    public void readData(WrapperPlayClientUseItem wrapper) {
        interactionHand = wrapper.interactionHand;
        blockPosition = wrapper.blockPosition;
        face = wrapper.face;
        cursorPosition = wrapper.cursorPosition;
        insideBlock = wrapper.insideBlock;
    }

    @Override
    public void writeData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            writeVarInt(interactionHand.getId());
            writeBlockPosition(blockPosition);
            writeVarInt(face.getFaceValue());
            writeFloat(cursorPosition.x);
            writeFloat(cursorPosition.y);
            writeFloat(cursorPosition.z);
            writeBoolean(insideBlock.orElse(false));
            insideBlock = Optional.of(readBoolean());
        } else {
            writeBlockPosition(blockPosition);
            writeVarInt(face.getFaceValue());
            writeVarInt(interactionHand.getId());
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11)) {
                writeFloat(cursorPosition.x);
                writeFloat(cursorPosition.y);
                writeFloat(cursorPosition.z);
            } else {
                writeByte((int) (cursorPosition.x * 16.0F));
                writeByte((int) (cursorPosition.y * 16.0F));
                writeByte((int) (cursorPosition.z * 16.0F));
            }
        }
    }

    public InteractionHand getHand() {
        return interactionHand;
    }

    public void setHand(InteractionHand interactionHand) {
        this.interactionHand = interactionHand;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public BlockFace getFace() {
        return face;
    }

    public void setFace(BlockFace face) {
        this.face = face;
    }

    public Vector3f getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(Vector3f cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public Optional<Boolean> getInsideBlock() {
        return insideBlock;
    }

    public void setInsideBlock(Optional<Boolean> insideBlock) {
        this.insideBlock = insideBlock;
    }
}
