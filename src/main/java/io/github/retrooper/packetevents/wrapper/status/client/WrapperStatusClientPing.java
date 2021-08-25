package io.github.retrooper.packetevents.wrapper.status.client;

import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.utils.netty.bytebuf.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperStatusClientPing extends PacketWrapper {
    private final long time;

    public WrapperStatusClientPing(ClientVersion version, ByteBufAbstract byteBuf) {
        super(version, byteBuf);

        this.time = readLong();
    }

    public long getTime(){
        return this.time;
    }
}
