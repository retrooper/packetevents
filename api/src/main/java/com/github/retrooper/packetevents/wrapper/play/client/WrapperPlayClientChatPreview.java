package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayClientChatPreview extends PacketWrapper<WrapperPlayClientChatPreview> {
    private int query;
    private String message;

    public WrapperPlayClientChatPreview(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientChatPreview(int query, String message) {
        super(PacketType.Play.Client.CHAT_PREVIEW);
        this.query = query;
        this.message = message;
    }

    @Override
    public void read() {
        query = readInt();
        message = readString(256);
    }

    @Override
    public void write() {
        writeInt(query);
        writeString(message);
    }

    @Override
    public void copy(WrapperPlayClientChatPreview wrapper) {
        query = wrapper.query;
        message = wrapper.message;
    }

    public int getQuery() {
        return query;
    }

    public void setQuery(int query) {
        this.query = query;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}