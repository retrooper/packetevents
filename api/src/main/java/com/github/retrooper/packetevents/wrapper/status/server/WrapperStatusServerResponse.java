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

package com.github.retrooper.packetevents.wrapper.status.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.google.gson.JsonObject;

public class WrapperStatusServerResponse extends PacketWrapper<WrapperStatusServerResponse> {
    private String componentJson;
    private boolean enforceSecureChat;

    public WrapperStatusServerResponse(PacketSendEvent event) {
        super(event);
    }

    public WrapperStatusServerResponse(JsonObject component, boolean enforceSecureChat) {
        this(component.toString(), enforceSecureChat);
    }

    public WrapperStatusServerResponse(String componentJson, boolean enforceSecureChat) {
        super(PacketType.Status.Server.RESPONSE);
        this.componentJson = componentJson;
        this.enforceSecureChat = enforceSecureChat;
    }

    @Override
    public void read() {
        componentJson = readString();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
            enforceSecureChat = readBoolean();
        }
    }

    @Override
    public void write() {
        writeString(componentJson);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_1)) {
            writeBoolean(enforceSecureChat);
        }
    }

    @Override
    public void copy(WrapperStatusServerResponse wrapper) {
        componentJson = wrapper.componentJson;
        enforceSecureChat = wrapper.enforceSecureChat;
    }

    public JsonObject getComponent() {
        return AdventureSerializer.getGsonSerializer().serializer().fromJson(componentJson, JsonObject.class);
    }

    public void setComponent(JsonObject component) {
        this.componentJson = AdventureSerializer.getGsonSerializer().serializer().toJson(component);
    }

    public String getComponentJson() {
        return componentJson;
    }

    public void setComponentJson(String componentJson) {
        this.componentJson = componentJson;
    }

    public boolean isEnforceSecureChat() {
        return enforceSecureChat;
    }

    public void setEnforceSecureChat(boolean enforceSecureChat) {
        this.enforceSecureChat = enforceSecureChat;
    }
}

