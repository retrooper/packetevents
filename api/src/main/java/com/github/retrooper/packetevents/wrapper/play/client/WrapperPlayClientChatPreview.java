package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientChatPreview extends PacketWrapper<WrapperPlayClientChatPreview> {
    private int queryId;
    private String message;

    public WrapperPlayClientChatPreview(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientChatPreview(int queryId, String message) {
        super(PacketType.Play.Client.CHAT_PREVIEW);
        this.queryId = queryId;
        this.message = message;
    }

    @Override
    public void read() {
        queryId = readInt();
        message = readString(256);
    }

    @Override
    public void write() {
        writeInt(queryId);
        writeString(message, 256);
    }

    @Override
    public void copy(WrapperPlayClientChatPreview wrapper) {
        queryId = wrapper.queryId;
        message = wrapper.message;
    }

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}