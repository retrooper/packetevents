package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayServerServerData extends PacketWrapper<WrapperPlayServerServerData> {
    private @Nullable Component motd;
    private @Nullable String icon;
    private boolean previewsChat;
    private boolean enforceSecureChat;

    public WrapperPlayServerServerData(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerServerData(@Nullable Component motd, @Nullable String icon, boolean previewsChat) {
        this(motd, icon, previewsChat, false);
    }

    public WrapperPlayServerServerData(@Nullable Component motd, @Nullable String icon, boolean previewsChat, boolean enforceSecureChat) {
        super(PacketType.Play.Server.SERVER_DATA);
        this.motd = motd;
        this.icon = icon;
        this.previewsChat = previewsChat;
        this.enforceSecureChat = enforceSecureChat;
    }

    @Override
    public void read() {
        motd = readOptional(PacketWrapper::readComponent);
        icon = readOptional(PacketWrapper::readString);
        previewsChat = readBoolean();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
            enforceSecureChat = readBoolean();
        }
    }

    @Override
    public void write() {
        writeOptional(motd, PacketWrapper::writeComponent);
        writeOptional(icon, PacketWrapper::writeString);
        writeBoolean(previewsChat);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
            writeBoolean(enforceSecureChat);
        }
    }

    @Override
    public void copy(WrapperPlayServerServerData wrapper) {
        motd = wrapper.motd;
        icon = wrapper.icon;
        previewsChat = wrapper.previewsChat;
        enforceSecureChat = wrapper.enforceSecureChat;
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

    public boolean isEnforceSecureChat() {
        return enforceSecureChat;
    }

    public void setEnforceSecureChat(boolean enforceSecureChat) {
        this.enforceSecureChat = enforceSecureChat;
    }
}