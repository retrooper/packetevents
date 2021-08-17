package io.github.retrooper.packetevents.wrapper.login.client;

import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.utils.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginClientPluginResponse extends PacketWrapper {
    private final int messageID;
    private final boolean successful;
    private final byte[] data;

    public WrapperLoginClientPluginResponse(ClientVersion version, ByteBufAbstract byteBuf) {
        super(version, byteBuf);
        this.messageID = readVarInt();
        this.successful = readBoolean();
        if (this.successful) {
            this.data = readByteArray(byteBuf.readableBytes());
        }
        else {
            this.data = new byte[0];
        }
    }

    public int getMessageID(){
        return this.messageID;
    }

    public boolean isSuccessful(){
        return this.successful;
    }

    public byte[] getData(){
        return this.data;
    }
}
