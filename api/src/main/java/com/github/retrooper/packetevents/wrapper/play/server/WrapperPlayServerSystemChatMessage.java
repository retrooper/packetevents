/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Introduced in 1.19.
 * WrapperPlayServerChatMessage is used for broadcasting messages sent by clients.
 * This on the other hand is used whenever the vanilla server needs to send the client a message.
 * For example, join messages, quit messages, etc...
 */
public class WrapperPlayServerSystemChatMessage extends PacketWrapper<WrapperPlayServerSystemChatMessage> {

    @Deprecated
    public static boolean HANDLE_JSON = true;

    private @Nullable ChatType type;
    //Overlay is a replacement for the chat type field. If overlay is true, the message will appear above your hot bar.
    //If not, it will appear in the normal chat.
    private boolean overlay;
    private Component message;

    public WrapperPlayServerSystemChatMessage(PacketSendEvent event) {
        super(event);
    }

    @Deprecated
    public WrapperPlayServerSystemChatMessage(@NotNull ChatType type, Component message) {
        super(PacketType.Play.Server.SYSTEM_CHAT_MESSAGE);
        this.type = type;
        if (type == ChatTypes.GAME_INFO) {
            this.overlay = true;
        }
        this.message = message;
    }

    @Deprecated
    public WrapperPlayServerSystemChatMessage(@NotNull ChatType type, String messageJson) {
        super(PacketType.Play.Server.SYSTEM_CHAT_MESSAGE);
        this.message = AdventureSerializer.parseComponent(messageJson);
        this.type = type;
        if (type == ChatTypes.GAME_INFO) {
            this.overlay = true;
        }
    }

    public WrapperPlayServerSystemChatMessage(boolean overlay, Component message) {
        super(PacketType.Play.Server.SYSTEM_CHAT_MESSAGE);
        this.message = message;
        this.overlay = overlay;
        this.type = overlay ? ChatTypes.GAME_INFO : ChatTypes.SYSTEM;
    }

    @Deprecated
    public WrapperPlayServerSystemChatMessage(boolean overlay, String messageJson) {
        super(PacketType.Play.Server.SYSTEM_CHAT_MESSAGE);
        this.message = AdventureSerializer.parseComponent(messageJson);
        this.overlay = overlay;
        this.type = overlay ? ChatTypes.GAME_INFO : ChatTypes.SYSTEM;
    }

    @Override
    public void read() {
        this.message = this.readComponent();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
            overlay = readBoolean();
        } else {
            this.type = this.readMappedEntity(ChatTypes.getRegistry());
        }
    }

    @Override
    public void write() {
        this.writeComponent(this.message);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
            writeBoolean(overlay);
        } else {
            if (type == null) {
                if (overlay) {
                    this.writeMappedEntity(ChatTypes.GAME_INFO);
                } else {
                    this.writeMappedEntity(ChatTypes.SYSTEM);
                }
            } else {
                this.writeMappedEntity(this.type);
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerSystemChatMessage wrapper) {
        this.type = wrapper.type;
        this.overlay = wrapper.overlay;
        this.message = wrapper.message;
    }

    @Deprecated
    public @Nullable ChatType getType() {
        return type;
    }

    @Deprecated
    public void setType(@Nullable ChatType type) {
        this.type = type;
    }

    @Deprecated
    public String getMessageJson() {
        return AdventureSerializer.toJson(this.getMessage());
    }

    @Deprecated
    public void setMessageJson(String messageJson) {
        this.setMessage(AdventureSerializer.parseComponent(messageJson));
    }

    public Component getMessage() {
        return message;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public boolean isOverlay() {
        return overlay;
    }

    public void setOverlay(boolean overlay) {
        this.overlay = overlay;
    }
}
