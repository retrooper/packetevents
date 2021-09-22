package io.github.retrooper.packetevents.wrapper.play.server;

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.BlockPosition;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockAction extends PacketWrapper<WrapperPlayServerBlockAction> {
    private BlockPosition blockPos;
    private int actionID, actionParam;
    private int blockID;

    public WrapperPlayServerBlockAction(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBlockAction(BlockPosition blockPos, int actionID, int actionParam, int block) {
        super(PacketType.Play.Server.BLOCK_ACTION.getID());
        this.blockPos = blockPos;
        this.actionID = actionID;
        this.actionParam = actionParam;
        this.blockID = block;
    }

    @Override
    public void readData() {
        this.blockPos = readBlockPosition();
        this.actionID = readUnsignedByte();
        this.actionParam = readUnsignedByte();
        this.blockID = readInt();
    }

    @Override
    public void writeData() {
        writeBlockPosition(blockPos);
        writeByte(actionID);
        writeByte(actionParam);
        writeVarInt(blockID);
    }

    @Override
    public void readData(WrapperPlayServerBlockAction wrapper) {
        this.blockPos = wrapper.getBlockPos();
        this.actionID = wrapper.getActionID();
        this.actionParam = wrapper.getActionParam();
        this.blockID = wrapper.getBlockID();
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

    public int getBlockID() {
        return blockID;
    }

    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }
}
