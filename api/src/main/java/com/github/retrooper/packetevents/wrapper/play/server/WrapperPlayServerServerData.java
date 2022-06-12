package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayServerServerData extends PacketWrapper<WrapperPlayServerServerData> {
    private @Nullable Component motd;
    private @Nullable String icon;
    private boolean previewsChat;

    public WrapperPlayServerServerData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerServerData(@Nullable Component motd, @Nullable String icon, boolean previewsChat) {
        super(PacketType.Play.Server.SERVER_DATA);
        this.motd = motd;
        this.icon = icon;
        this.previewsChat = previewsChat;
    }

    @Override
    public void read() {
        motd = readOptional(PacketWrapper::readComponent);
        icon = readOptional(PacketWrapper::readString);
        previewsChat = readBoolean();
    }

    @Override
    public void write() {
        writeOptional(motd, PacketWrapper::writeComponent);
        writeOptional(icon, PacketWrapper::writeString);
        writeBoolean(previewsChat);
    }

    @Override
    public void copy(WrapperPlayServerServerData wrapper) {
        motd = wrapper.motd;
        icon = wrapper.icon;
        previewsChat = wrapper.previewsChat;
    }

    public Optional<Component> getMotd() {
        return Optional.ofNullable(motd);
    }

    public void setMotd(@Nullable Component motd) {
        this.motd = motd;
    }

    public Optional<String> getIcon() {
        return Optional.ofNullable(icon);
    }

    public void setIcon(@Nullable String icon) {
        this.icon = icon;
    }

    public boolean isPreviewsChat() {
        return previewsChat;
    }

    public void setPreviewsChat(boolean previewsChat) {
        this.previewsChat = previewsChat;
    }
}