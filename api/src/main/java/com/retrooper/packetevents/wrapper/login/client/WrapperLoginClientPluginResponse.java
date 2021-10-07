package com.retrooper.packetevents.wrapper.login.client;

import com.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.retrooper.packetevents.protocol.packettype.PacketType;
import com.retrooper.packetevents.protocol.data.player.ClientVersion;
import com.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginClientPluginResponse extends PacketWrapper<WrapperLoginClientPluginResponse> {
    private int messageID;
    private boolean successful;
    private byte[] data;

    public WrapperLoginClientPluginResponse(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperLoginClientPluginResponse(ClientVersion clientVersion, int messageID, boolean successful, byte[] data) {
        super(PacketType.Login.Client.LOGIN_PLUGIN_RESPONSE.getID(), clientVersion);
        this.messageID = messageID;
        this.successful = successful;
        this.data = data;
    }

    @Override
    public void readData() {
        this.messageID = readVarInt();
        this.successful = readBoolean();
        if (this.successful) {
            this.data = readByteArray(byteBuf.readableBytes());
        } else {
            this.data = new byte[0];
        }
    }

    @Override
    public void readData(WrapperLoginClientPluginResponse wrapper) {
        this.messageID = wrapper.messageID;
        this.successful = wrapper.successful;
        this.data = wrapper.data;
    }

    @Override
    public void writeData() {
        writeVarInt(messageID);
        writeBoolean(successful);
        if (successful) {
            writeByteArray(data);
        }
    }

    public int getMessageID() {
        return this.messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
