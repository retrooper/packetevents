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
import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.AdventureSerializer;
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
    public static boolean HANDLE_JSON = true;
    private @Nullable ChatType type;
    private boolean overlay;
    private String messageJson;
    private Component message;

    public WrapperPlayServerSystemChatMessage(PacketSendEvent event) {
        super(event);
    }

    @Deprecated
    public WrapperPlayServerSystemChatMessage(@NotNull ChatType type, Component message) {
        super(PacketType.Play.Server.SYSTEM_CHAT_MESSAGE);
        this.type = type;
        if (type == ChatType.GAME_INFO) {
            this.overlay = true;
        }
        this.message = message;
    }

    @Deprecated
    public WrapperPlayServerSystemChatMessage(@NotNull ChatType type, String messageJson) {
        super(PacketType.Play.Server.SYSTEM_CHAT_MESSAGE);
        this.messageJson = messageJson;
        this.type = type;
        if (type == ChatType.GAME_INFO) {
            this.overlay = true;
        }
    }

    public WrapperPlayServerSystemChatMessage(boolean overlay, Component message) {
        super(PacketType.Play.Server.SYSTEM_CHAT_MESSAGE);
        this.message = message;
        this.overlay = overlay;
        this.type = overlay ? ChatType.GAME_INFO : ChatType.SYSTEM;
    }

    @Deprecated
    public WrapperPlayServerSystemChatMessage(boolean overlay, String messageJson) {
        super(PacketType.Play.Server.SYSTEM_CHAT_MESSAGE);
        this.messageJson = messageJson;
        this.overlay = overlay;
        this.type = overlay ? ChatType.GAME_INFO : ChatType.SYSTEM;
    }


    @Override
    public void read() {
        this.messageJson = readComponentJSON();
        // Parse JSON message
        if (HANDLE_JSON) {
            message = AdventureSerializer.parseComponent(this.messageJson);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
            overlay = readBoolean();
        }
        else {
            type = ChatType.getById(readVarInt());
        }
    }

    @Override
    public void write() {
        if (HANDLE_JSON && message != null) {
            messageJson = AdventureSerializer.toJson(message);
        }
        writeComponentJSON(messageJson);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
           writeBoolean(overlay);
        } else {
            if (type == null) {
                if (overlay) {
                    writeVarInt(ChatType.GAME_INFO.getId());
                }
                else {
                    writeVarInt(ChatType.SYSTEM.getId());
                }
            }
            else {
                writeVarInt(type.getId());
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerSystemChatMessage wrapper) {
        this.messageJson = wrapper.messageJson;
        this.message = wrapper.message;
        this.type = wrapper.type;
        this.overlay = wrapper.overlay;
    }

    @Deprecated
    public @Nullable ChatType getType() {
        return type;
    }

    @Deprecated
    public void setType(@Nullable ChatType type) {
        this.type = type;
    }

    public String getMessageJson() {
        return messageJson;
    }

    public void setMessageJson(String messageJson) {
        this.messageJson = messageJson;
    }

    public Component getMessage() {
        return message;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public boolean overlay() {
        return overlay;
    }

    public void setOverlay(boolean overlay) {
        this.overlay = overlay;
    }
}
