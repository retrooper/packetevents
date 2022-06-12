package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayServerChatPreview extends PacketWrapper<WrapperPlayServerChatPreview> {
    private int queryID;
    private @Nullable Component message;

    public WrapperPlayServerChatPreview(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChatPreview(int queryID, @Nullable Component message) {
        super(PacketType.Play.Server.CHAT_PREVIEW_PACKET);
        this.queryID = queryID;
        this.message = message;
    }

    @Override
    public void read() {
        queryID = readInt();
        message = readOptional(PacketWrapper::readComponent);
    }

    @Override
    public void write() {
        writeInt(queryID);
        writeOptional(message, PacketWrapper::writeComponent);
    }

    @Override
    public void copy(WrapperPlayServerChatPreview wrapper) {
        queryID = wrapper.queryID;
        message = wrapper.message;
    }

    public int getQueryID() {
        return queryID;
    }

    public void setQueryID(int queryID) {
        this.queryID = queryID;
    }

    public Optional<Component> getMessage() {
        return Optional.ofNullable(message);
    }

    public void setMessage(@Nullable Component message) {
        this.message = message;
    }
}