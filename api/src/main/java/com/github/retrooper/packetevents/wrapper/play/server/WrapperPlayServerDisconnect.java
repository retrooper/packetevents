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
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

public class WrapperPlayServerDisconnect extends PacketWrapper<WrapperPlayServerDisconnect> {
    public static boolean HANDLE_JSON = true;
    private static final int MODERN_MESSAGE_LENGTH = 262144;
    private static final int LEGACY_MESSAGE_LENGTH = 32767;
    private String reasonJson;
    private Component reasonComponent;

    public WrapperPlayServerDisconnect(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerDisconnect(Component reasonComponent) {
        super(PacketType.Play.Server.DISCONNECT);
        this.reasonComponent = reasonComponent;
    }

    public WrapperPlayServerDisconnect(String reasonJson) {
        super(PacketType.Play.Server.DISCONNECT);
        this.reasonJson = reasonJson;
    }

    @Override
    public void readData() {
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? MODERN_MESSAGE_LENGTH : LEGACY_MESSAGE_LENGTH;
        reasonJson = readString(maxMessageLength);
        if (HANDLE_JSON) {
            reasonComponent = AdventureSerializer.parseComponent(reasonJson);
        }
    }

    @Override
    public void readData(WrapperPlayServerDisconnect wrapper) {
        reasonJson = wrapper.reasonJson;
        reasonComponent = wrapper.reasonComponent;
    }

    @Override
    public void writeData() {
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? MODERN_MESSAGE_LENGTH : LEGACY_MESSAGE_LENGTH;
        if (HANDLE_JSON) {
            reasonJson = AdventureSerializer.toJson(reasonComponent);
        }
        writeString(reasonJson, maxMessageLength);
    }

    public String getReasonJson() {
        return reasonJson;
    }

    public void setReasonJson(String reasonJson) {
        this.reasonJson = reasonJson;
    }

    public Component getReasonComponent() {
        return reasonComponent;
    }

    public void setReasonComponent(Component reasonComponent) {
        this.reasonComponent = reasonComponent;
    }
}
