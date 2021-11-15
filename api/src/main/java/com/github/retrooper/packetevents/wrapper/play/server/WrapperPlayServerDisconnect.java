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

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.chat.component.ComponentSerializer;
import com.github.retrooper.packetevents.protocol.chat.component.TextComponent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;

public class WrapperPlayServerDisconnect extends PacketWrapper<WrapperPlayServerDisconnect> {
    public static boolean HANDLE_JSON = true;
    private static final int MODERN_MESSAGE_LENGTH = 262144;
    private static final int LEGACY_MESSAGE_LENGTH = 32767;
    private String jsonReasonRaw;
    private List<TextComponent> reasonComponents;

    public WrapperPlayServerDisconnect(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDisconnect(List<TextComponent> reasonComponents) {
        super(PacketType.Play.Server.DISCONNECT);
        this.reasonComponents = reasonComponents;
    }

    public WrapperPlayServerDisconnect(String jsonReasonRaw) {
        super(PacketType.Play.Server.DISCONNECT);
        this.jsonReasonRaw = jsonReasonRaw;
    }

    @Override
    public void readData() {
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? MODERN_MESSAGE_LENGTH : LEGACY_MESSAGE_LENGTH;
        jsonReasonRaw = readString(maxMessageLength);
        if (HANDLE_JSON) {
            reasonComponents = ComponentSerializer.parseJSONString(jsonReasonRaw);
        }
    }

    @Override
    public void readData(WrapperPlayServerDisconnect wrapper) {
        jsonReasonRaw = wrapper.jsonReasonRaw;
        reasonComponents = wrapper.reasonComponents;
    }

    @Override
    public void writeData() {
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? MODERN_MESSAGE_LENGTH : LEGACY_MESSAGE_LENGTH;
        if (HANDLE_JSON) {
            jsonReasonRaw = ComponentSerializer.buildJSONString(reasonComponents);
        }
        writeString(jsonReasonRaw, maxMessageLength);
    }

    public String getJSONReasonRaw() {
        return jsonReasonRaw;
    }

    public void setJSONReasonRaw(String jsonReasonRaw) {
        this.jsonReasonRaw = jsonReasonRaw;
    }

    public List<TextComponent> getReasonComponents() {
        return reasonComponents;
    }

    public void setReasonComponents(List<TextComponent> reasonComponents) {
        this.reasonComponents = reasonComponents;
    }
}
