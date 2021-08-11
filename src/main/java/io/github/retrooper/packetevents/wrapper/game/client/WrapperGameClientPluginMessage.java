package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.utils.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperGameClientPluginMessage extends PacketWrapper {
    private final String channelName;
    private final byte[] data;

    public WrapperGameClientPluginMessage(ClientVersion version, ByteBufAbstract byteBuf) {
        super(version, byteBuf);
        this.channelName = readString();
        if (version.isNewerThanOrEquals(ClientVersion.v_1_8)) {
            this.data = readByteArray(byteBuf.readableBytes());
        }
        else {
            int dataLength = readShort();
            this.data = readByteArray(dataLength);
        }
    }

    public String getChannelName(){
        return channelName;
    }

    public byte[] getData(){
        return data;
    }
}
