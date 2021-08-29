package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.utils.netty.buffer.ByteBufAbstract;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * Mods and plugins can use this to send their data.
 * Minecraft itself uses some plugin channels.
 * These internal channels are in the minecraft namespace.
 */
public class WrapperGameClientPluginMessage extends PacketWrapper {
    private final String channelName;
    private final byte[] data;

    public WrapperGameClientPluginMessage(PacketReceiveEvent event) {
        super(event);
        this.channelName = readString();
        if (clientVersion.isNewerThanOrEquals(ClientVersion.v_1_8)) {
            this.data = readByteArray(byteBuf.readableBytes());
        } else {
            int dataLength = readShort();
            this.data = readByteArray(dataLength);
        }
    }

    /**
     * Name of the plugin channel used to send the data.
     *
     * @return Plugin channel name
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * Any data, depending on the channel.
     *
     * @return Data
     */
    public byte[] getData() {
        return data;
    }
}
