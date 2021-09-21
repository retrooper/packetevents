package io.github.retrooper.packetevents.wrapper.play.server;

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.utils.vector.Vector3i;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockAction extends PacketWrapper<WrapperPlayServerBlockAction> {
    Vector3i blockPos;
    int actionID, actionParam;
    int block;

    public WrapperPlayServerBlockAction(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBlockAction(Vector3i blockPos, int actionID, int actionParam, int block) {
        super(PacketType.Play.Server.UNLOAD_CHUNK.getID());
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

    public Vector3i getBlockPos() {
        return blockPos;
    }

    public void setBlockPos(Vector3i blockPos) {
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
