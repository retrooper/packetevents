package io.github.retrooper.packetevents.wrapper.play.server;

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.github.retrooper.packetevents.protocol.data.world.BlockPosition;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerBlockAction extends PacketWrapper<WrapperPlayServerBlockAction> {
    private BlockPosition blockPosition;
    private int actionID, actionData;
    private int blockTypeID;

    public WrapperPlayServerBlockAction(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerBlockAction(BlockPosition blockPosition, int actionID, int actionParam, int blockTypeID) {
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
            blockPosition = new BlockPosition(x, y, z);
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

    public BlockPosition getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(BlockPosition blockPosition) {
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
