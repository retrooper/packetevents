/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

    public @Nullable Component getMOTD() {
        return motd;
    }

    public void setMOTD(@Nullable Component motd) {
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