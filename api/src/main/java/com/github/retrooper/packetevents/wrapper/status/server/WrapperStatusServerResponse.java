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

package com.github.retrooper.packetevents.wrapper.status.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.google.gson.JsonObject;

public class WrapperStatusServerResponse extends PacketWrapper<WrapperStatusServerResponse> {
    private String componentJson;

    public WrapperStatusServerResponse(PacketSendEvent event) {
        super(event);
    }

    public WrapperStatusServerResponse(JsonObject component) {
        this(component.toString());
    }

    public WrapperStatusServerResponse(String componentJson) {
        super(PacketType.Status.Server.RESPONSE);
        this.componentJson = componentJson;
    }

    @Override
    public void read() {
        componentJson = readString();
    }

    @Override
    public void write() {
        writeString(componentJson);
    }

    @Override
    public void copy(WrapperStatusServerResponse wrapper) {
        componentJson = wrapper.componentJson;
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
}

