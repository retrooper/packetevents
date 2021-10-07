package com.retrooper.packetevents.wrapper.play.server;

import com.retrooper.packetevents.event.impl.PacketSendEvent;
import com.retrooper.packetevents.manager.server.ServerVersion;
import com.retrooper.packetevents.protocol.packettype.PacketType;
import com.retrooper.packetevents.util.Vector3i;
import com.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockAction extends PacketWrapper<WrapperPlayServerBlockAction> {
    private Vector3i blockPosition;
    private int actionID, actionData;
    private int blockTypeID;

    public WrapperPlayServerBlockAction(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBlockAction(Vector3i blockPosition, int actionID, int actionParam, int blockTypeID) {
        super(PacketType.Play.Server.BLOCK_ACTION);
        this.blockPosition = blockPosition;
        this.actionID = actionID;
        this.actionData = actionParam;
        this.blockTypeID = blockTypeID;
    }

    @Override
    public void readData() {
        if (serverVersion == ServerVersion.v_1_7_10) {
            int x = readInt();
            int y = readShort();
            int z = readInt();
            blockPosition = new Vector3i(x, y, z);
        }
        else {
            this.blockPosition = readBlockPosition();
        }
        this.actionID = readUnsignedByte();
        this.actionData = readUnsignedByte();
        this.blockTypeID = readVarInt();
    }

    @Override
    public void writeData() {
        if (serverVersion == ServerVersion.v_1_7_10) {
            writeInt(blockPosition.x);
            writeShort(blockPosition.y);
            writeInt(blockPosition.z);
        }
        else {
            writeBlockPosition(blockPosition);
        }
        writeByte(actionID);
        writeByte(actionData);
        writeVarInt(blockTypeID);
    }

    @Override
    public void readData(WrapperPlayServerBlockAction wrapper) {
        this.blockPosition = wrapper.blockPosition;
        this.actionID = wrapper.actionID;
        this.actionData = wrapper.actionData;
        this.blockTypeID = wrapper.blockTypeID;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
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

    public int getBlockTypeID() {
        return blockTypeID;
    }

    public void setBlockTypeID(int blockTypeID) {
        this.blockTypeID = blockTypeID;
    }
}
