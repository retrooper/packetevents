package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayServerChatPreview extends PacketWrapper<WrapperPlayServerChatPreview> {
    private int queryId;
    private @Nullable Component message;

    public WrapperPlayServerChatPreview(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChatPreview(int queryId, @Nullable Component message) {
        super(PacketType.Play.Server.CHAT_PREVIEW_PACKET);
        this.queryId = queryId;
        this.message = message;
    }

    @Override
    public void read() {
        queryId = readInt();
        message = readOptional(PacketWrapper::readComponent);
    }

    @Override
    public void write() {
        writeInt(queryId);
        writeOptional(message, PacketWrapper::writeComponent);
    }

    @Override
    public void copy(WrapperPlayServerChatPreview wrapper) {
        queryId = wrapper.queryId;
        message = wrapper.message;
    }

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public Optional<Component> getMessage() {
        return Optional.ofNullable(message);
    }

    public void setMessage(@Nullable Component message) {
        this.message = message;
    }
}