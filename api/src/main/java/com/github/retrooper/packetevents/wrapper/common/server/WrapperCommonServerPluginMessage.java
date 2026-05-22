package com.github.retrooper.packetevents.wrapper.common.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public abstract class WrapperCommonServerPluginMessage<T extends WrapperCommonServerPluginMessage<T>> extends PacketWrapper<T> {

    protected String channelName;
    protected byte[] data;

    public WrapperCommonServerPluginMessage(PacketSendEvent event) {
        super(event);
    }

    public WrapperCommonServerPluginMessage(PacketTypeCommon packetType) {
        super(packetType);
    }

    public WrapperCommonServerPluginMessage(PacketTypeCommon packetType, ResourceLocation channelName, byte[] data) {
        this(packetType, channelName.toString(), data);
    }

    public WrapperCommonServerPluginMessage(PacketTypeCommon packetType, String channelName, byte[] data) {
        super(packetType);
        this.channelName = channelName;
        this.data = data;
    }

    @Override
    public void copy(T wrapper) {
        this.channelName = wrapper.channelName;
        this.data = wrapper.data;
    }

    /**
     * The channel name of the plugin message.
     * @return The channel name.
     */
    public final String getChannelName() {
        return channelName;
    }

    /**
     * Sets the channel name of the plugin message.
     * @param channelName The channel name.
     */
    public final void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * The data of the plugin message.
     *
     * @return The data.
     */
    public final byte[] getData() {
        return data;
    }

    /**
     * Sets the data of the plugin message.
     *
     * @param data The data.
     */
    public final void setData(byte[] data) {
        this.data = data;
    }

}
