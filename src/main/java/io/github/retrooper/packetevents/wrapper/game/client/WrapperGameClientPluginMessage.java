package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.utils.player.ClientVersion;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperGameClientPluginMessage extends PacketWrapper {
    private final String channelName;
    private final byte[] data;

    public WrapperGameClientPluginMessage(ClientVersion version, ByteBufAbstract byteBuf) {
        super(version, byteBuf);
        this.channelName = readString();
        this.data = readByteArray(byteBuf.readableBytes());
    }

    public String getChannelName(){
        return channelName;
    }

    public byte[] getData(){
        return data;
    }
}
