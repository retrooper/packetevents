package com.github.retrooper.packetevents.wrapper.common.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public abstract class WrapperCommonClientPluginMessage<T extends WrapperCommonClientPluginMessage<T>> extends PacketWrapper<T> {

    protected String channelName;
    protected byte[] data;

    public WrapperCommonClientPluginMessage(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperCommonClientPluginMessage(PacketTypeCommon packetType) {
        super(packetType);
    }

    public WrapperCommonClientPluginMessage(PacketTypeCommon packetType, ResourceLocation channelName, byte[] data) {
        this(packetType, channelName.toString(), data);
    }

    public WrapperCommonClientPluginMessage(PacketTypeCommon packetType, String channelName, byte[] data) {
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
