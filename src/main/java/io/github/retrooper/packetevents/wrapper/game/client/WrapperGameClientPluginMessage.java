package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperGameClientPluginMessage extends PacketWrapper {
    private final String tag;
    private final byte[] data;

    public WrapperGameClientPluginMessage(ByteBufAbstract byteBuf) {
        super(byteBuf);
        this.tag = readString();
        this.data = readByteArray(byteBuf.readableBytes());
    }

    public String getTag(){
        return this.tag;
    }

    public byte[] getData(){
        return this.data;
    }
}
