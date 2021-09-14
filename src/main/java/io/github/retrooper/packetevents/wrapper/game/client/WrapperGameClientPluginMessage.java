package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * Mods and plugins can use this to send their data.
 * Minecraft itself uses some plugin channels.
 * These internal channels are in the minecraft namespace.
 */
public class WrapperGameClientPluginMessage extends PacketWrapper<WrapperGameClientPluginMessage> {
    private String channelName;
    private byte[] data;

    public WrapperGameClientPluginMessage(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperGameClientPluginMessage(String channelName, byte[] data) {
        super(PacketType.Game.Client.PLUGIN_MESSAGE.getID());
        this.channelName = channelName;
        this.data = data;
    }

    @Override
    public void readData() {
        this.channelName = readString();
        int dataLength = clientVersion.isNewerThanOrEquals(ClientVersion.v_1_8) ? byteBuf.readableBytes() : readShort();
        this.data = readByteArray(dataLength);
    }

    @Override
    public void readData(WrapperGameClientPluginMessage wrapper) {
        this.channelName = wrapper.channelName;
        this.data = wrapper.data;
    }

    @Override
    public void writeData() {
        writeString(channelName);
        if (clientVersion.isOlderThan(ClientVersion.v_1_8)) {
            writeShort(this.data.length);
        }
        writeByteArray(this.data);
    }

    /**
     * Name of the plugin channel used to send the data.
     *
     * @return Plugin channel name
     */
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * Any data, depending on the channel.
     *
     * @return Data
     */
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
