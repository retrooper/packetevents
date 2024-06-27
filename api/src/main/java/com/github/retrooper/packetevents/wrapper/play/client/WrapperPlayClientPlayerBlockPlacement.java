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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.InteractionHand;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

public class WrapperPlayClientPlayerBlockPlacement extends PacketWrapper<WrapperPlayClientPlayerBlockPlacement> {
    private InteractionHand interactionHand;
    private Vector3i blockPosition;
    private int faceId;
    private BlockFace face;
    private Vector3f cursorPosition;
    private Optional<ItemStack> itemStack;
    private Optional<Boolean> insideBlock;
    private int sequence;

    public WrapperPlayClientPlayerBlockPlacement(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientPlayerBlockPlacement(InteractionHand interactionHand, Vector3i blockPosition, BlockFace face, Vector3f cursorPosition, ItemStack itemStack, Boolean insideBlock, int sequence) {
        super(PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT);
        this.interactionHand = interactionHand;
        this.blockPosition = blockPosition;
        this.face = face;
        this.faceId = face.getFaceValue();
        this.cursorPosition = cursorPosition;
        this.itemStack = Optional.ofNullable(itemStack);
        this.insideBlock = Optional.ofNullable(insideBlock);
        this.sequence = sequence;
    }

    @Override
    public void read() {
        itemStack = Optional.empty();
        insideBlock = Optional.empty();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            interactionHand = InteractionHand.getById(readVarInt());
            blockPosition = readBlockPosition();
            faceId = readVarInt();
            face = BlockFace.getBlockFaceByValue(faceId);
            cursorPosition = new Vector3f(readFloat(), readFloat(), readFloat());
            insideBlock = Optional.of(readBoolean());
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
                sequence = readVarInt();
            }
        } else {
            if (serverVersion == ServerVersion.V_1_7_10) {
                blockPosition = new Vector3i(readInt(), readUnsignedByte(), readInt());
            } else {
                blockPosition = readBlockPosition();
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                faceId = readVarInt();
                face = BlockFace.getBlockFaceByValue(faceId);
                interactionHand = InteractionHand.getById(readVarInt());
            } else {
                faceId = readUnsignedByte();
                face = BlockFace.getLegacyBlockFaceByValue(faceId);
                //Optional itemstack
                itemStack = Optional.of(readItemStack());
                interactionHand = InteractionHand.MAIN_HAND;
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_11)) {
                cursorPosition = new Vector3f(readFloat(), readFloat(), readFloat());
            } else {
                cursorPosition = new Vector3f(readUnsignedByte() / 16.0F, readUnsignedByte() / 16.0F, readUnsignedByte() / 16.0F);
            }
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            writeVarInt(interactionHand.getId());
            writeBlockPosition(blockPosition);
            writeVarInt(faceId);
            writeFloat(cursorPosition.x);
            writeFloat(cursorPosition.y);
            writeFloat(cursorPosition.z);
            writeBoolean(insideBlock.orElse(false));
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
                writeVarInt(sequence);
            }
        } else {
            if (serverVersion == ServerVersion.V_1_7_10) {
                writeInt(blockPosition.x);
                writeByte(blockPosition.y);
                writeInt(blockPosition.z);
            } else {
                writeBlockPosition(blockPosition);
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                writeVarInt(faceId);
                writeVarInt(interactionHand.getId());
            } else {
                writeByte(faceId);
                writeItemStack(itemStack.orElse(ItemStack.EMPTY));
                //Hand is always the main hand
            }
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

    @Override
    public void copy(WrapperPlayClientPlayerBlockPlacement wrapper) {
        interactionHand = wrapper.interactionHand;
        blockPosition = wrapper.blockPosition;
        face = wrapper.face;
        faceId = wrapper.faceId;
        cursorPosition = wrapper.cursorPosition;
        itemStack = wrapper.itemStack;
        insideBlock = wrapper.insideBlock;
        sequence = wrapper.sequence;
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

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
        this.face = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)
                ? BlockFace.getBlockFaceByValue(faceId)
                : BlockFace.getLegacyBlockFaceByValue(faceId);
    }

    public BlockFace getFace() {
        return face;
    }

    public void setFace(BlockFace face) {
        this.face = face;
        this.faceId = face.getFaceValue();
    }

    public Vector3f getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(Vector3f cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public Optional<ItemStack> getItemStack() {
        return itemStack;
    }

    public void setItemStack(Optional<ItemStack> itemStack) {
        this.itemStack = itemStack;
    }

    public Optional<Boolean> getInsideBlock() {
        return insideBlock;
    }

    public void setInsideBlock(Optional<Boolean> insideBlock) {
        this.insideBlock = insideBlock;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
