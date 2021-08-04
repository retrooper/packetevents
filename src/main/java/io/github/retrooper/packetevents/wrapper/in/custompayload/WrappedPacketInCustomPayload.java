package io.github.retrooper.packetevents.wrapper.in.custompayload;

import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrappedPacketInCustomPayload extends PacketWrapper {
    private final String tag;
    private byte[] data;

    public WrappedPacketInCustomPayload(ByteBufAbstract byteBuf) {
        super(byteBuf);
        this.tag = readString();
        this.data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
    }

    public String getTag(){
        return this.tag;
    }

    public byte[] getData(){
        return this.data;
    }
}
