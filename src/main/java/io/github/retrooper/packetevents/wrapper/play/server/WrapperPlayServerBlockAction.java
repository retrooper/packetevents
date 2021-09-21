package io.github.retrooper.packetevents.wrapper.play.server;

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.BlockPosition;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockAction extends PacketWrapper<WrapperPlayServerBlockAction> {
    BlockPosition blockPos;
    int actionID, actionParam;
    int block;

    public WrapperPlayServerBlockAction(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBlockAction(BlockPosition blockPos, int actionID, int actionParam, int block) {
        super(PacketType.Play.Server.BLOCK_ACTION.getID());
        this.blockPos = blockPos;
        this.actionID = actionID;
        this.actionParam = actionParam;
        this.block = block;
    }

    @Override
    public void readData() {
        this.blockPos = readBlockPos();
        this.actionID = readUnsignedByte();
        this.actionParam = readUnsignedByte();
        this.block = readInt();
    }

    @Override
    public void writeData() {
        writeBlockPos(blockPos);
        writeByte(actionID);
        writeByte(actionParam);
        writeVarInt(block);
    }

    @Override
    public void readData(WrapperPlayServerBlockAction wrapper) {
        this.blockPos = wrapper.getBlockPos();
        this.actionID = wrapper.getActionID();
        this.actionParam = wrapper.getActionParam();
        this.block = wrapper.getBlock();
    }

    public BlockPosition getBlockPos() {
        return blockPos;
    }

    public void setBlockPos(BlockPosition blockPos) {
        this.blockPos = blockPos;
    }

    public int getActionID() {
        return actionID;
    }

    public void setActionID(int actionID) {
        this.actionID = actionID;
    }

    public int getActionParam() {
        return actionParam;
    }

    public void setActionParam(int actionParam) {
        this.actionParam = actionParam;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }
}
