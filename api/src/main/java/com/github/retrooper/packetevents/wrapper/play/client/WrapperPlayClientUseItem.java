package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.data.player.Hand;
import com.github.retrooper.packetevents.protocol.data.world.BlockFace;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

public class WrapperPlayClientUseItem extends PacketWrapper<WrapperPlayClientUseItem> {
    private Hand hand;
    private Vector3i blockPosition;
    private BlockFace face;
    private Vector3f cursorPosition;
    private Optional<Boolean> insideBlock;

    public WrapperPlayClientUseItem(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientUseItem(Hand hand, Vector3i blockPosition, BlockFace face, Vector3f cursorPosition, Optional<Boolean> insideBlock) {
        super(PacketType.Play.Client.USE_ITEM);
        this.hand = hand;
        this.blockPosition = blockPosition;
        this.face = face;
        this.cursorPosition= cursorPosition;
        this.insideBlock = insideBlock;
    }

    @Override
    public void readData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_14)) {
            hand = Hand.getByLegacyID(readVarInt());
            blockPosition = readBlockPosition();
            face = BlockFace.getBlockFaceByValue(readVarInt());
            cursorPosition = new Vector3f(readFloat(), readFloat(), readFloat());
            insideBlock = Optional.of(readBoolean());
        } else {
            insideBlock = Optional.empty();
            blockPosition = readBlockPosition();
            face = BlockFace.getBlockFaceByValue(readVarInt());
            hand = Hand.getByLegacyID(readVarInt());
            if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_11)) {
                cursorPosition = new Vector3f(readFloat(), readFloat(), readFloat());
            } else {
                cursorPosition = new Vector3f(readUnsignedByte() / 16.0F, readUnsignedByte() / 16.0F, readUnsignedByte() / 16.0F);
            }
        }
    }

    @Override
    public void readData(WrapperPlayClientUseItem wrapper) {
        hand = wrapper.hand;
        blockPosition = wrapper.blockPosition;
        face = wrapper.face;
        cursorPosition = wrapper.cursorPosition;
        insideBlock = wrapper.insideBlock;
    }

    @Override
    public void writeData() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_14)) {
            writeVarInt(hand.getLegacyID());
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
            writeVarInt(hand.getLegacyID());
            if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_11)) {
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

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
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
