package io.github.retrooper.packetevents.wrapper.play.server;

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.github.retrooper.packetevents.protocol.data.world.BlockPosition;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockAction extends PacketWrapper<WrapperPlayServerBlockAction> {
    private BlockPosition blockPosition;
    private int actionID, actionData;
    private int blockID;

    public WrapperPlayServerBlockAction(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBlockAction(BlockPosition blockPosition, int actionID, int actionParam, int block) {
        super(PacketType.Play.Server.BLOCK_ACTION);
        this.blockPosition = blockPosition;
        this.actionID = actionID;
        this.actionData = actionParam;
        this.blockID = block;
    }

    @Override
    public void readData() {
        this.blockPosition = readBlockPosition();
        this.actionID = readUnsignedByte();
        this.actionData = readUnsignedByte();
        this.blockID = readInt();
    }

    @Override
    public void writeData() {
        writeBlockPosition(blockPosition);
        writeByte(actionID);
        writeByte(actionData);
        writeVarInt(blockID);
    }

    @Override
    public void readData(WrapperPlayServerBlockAction wrapper) {
        this.blockPosition = wrapper.blockPosition;
        this.actionID = wrapper.actionID;
        this.actionData = wrapper.actionData;
        this.blockID = wrapper.blockID;
    }

    public BlockPosition getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(BlockPosition blockPos) {
        this.blockPosition = blockPos;
    }

    public int getActionID() {
        return actionID;
    }

    public void setActionID(int actionID) {
        this.actionID = actionID;
    }

    public int getActionData() {
        return actionData;
    }

    public void setActionData(int actionData) {
        this.actionData = actionData;
    }

    public int getBlockID() {
        return blockID;
    }

    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }
}
