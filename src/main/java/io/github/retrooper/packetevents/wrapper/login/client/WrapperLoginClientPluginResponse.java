package io.github.retrooper.packetevents.wrapper.login.client;

import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginClientPluginResponse extends PacketWrapper {
    private final int messageid;
    private final boolean successful;
    private final byte[] data;

    public WrapperLoginClientPluginResponse(ByteBufAbstract byteBuf) {
        super(byteBuf);
        this.messageid = readVarInt();
        this.successful = readBoolean();
        this.data = readByteArray(byteBuf.readableBytes());
    }

    public int getMessageId(){
        return this.messageid;
    }

    public boolean isSuccessful(){
        return this.successful;
    }

    public byte[] getData(){
        return this.data;
    }
}
