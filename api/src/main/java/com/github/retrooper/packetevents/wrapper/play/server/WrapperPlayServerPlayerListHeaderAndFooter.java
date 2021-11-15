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

public class WrapperPlayServerPlayerListHeaderAndFooter extends PacketWrapper<WrapperPlayServerPlayerListHeaderAndFooter> {
    public static boolean HANDLE_JSON = true;
    private static final int MODERN_MESSAGE_LENGTH = 262144;
    private static final int LEGACY_MESSAGE_LENGTH = 32767;
    private String jsonHeader;
    private String jsonFooter;
    private List<TextComponent> headerComponents;
    private List<TextComponent> footerComponents;
    public WrapperPlayServerPlayerListHeaderAndFooter(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPlayerListHeaderAndFooter(List<TextComponent> headerComponents, List<TextComponent> footerComponents) {
        super(PacketType.Play.Server.PLAYER_LIST_HEADER_AND_FOOTER);
        this.headerComponents = headerComponents;
        this.footerComponents = footerComponents;
    }

    public WrapperPlayServerPlayerListHeaderAndFooter(String jsonHeader, String jsonFooter) {
        super(PacketType.Play.Server.PLAYER_LIST_HEADER_AND_FOOTER);
        this.jsonHeader = jsonHeader;
        this.jsonFooter = jsonFooter;
    }

    @Override
    public void readData() {
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? MODERN_MESSAGE_LENGTH : LEGACY_MESSAGE_LENGTH;
        jsonHeader = readString(maxMessageLength);
        jsonFooter = readString(maxMessageLength);
        if (HANDLE_JSON) {
            headerComponents = ComponentSerializer.parseJSONString(jsonHeader);
            footerComponents = ComponentSerializer.parseJSONString(jsonFooter);
        }
    }

    @Override
    public void readData(WrapperPlayServerPlayerListHeaderAndFooter wrapper) {
        jsonHeader = wrapper.jsonHeader;
        jsonFooter = wrapper.jsonFooter;
        headerComponents = wrapper.headerComponents;
        footerComponents = wrapper.footerComponents;
    }

    @Override
    public void writeData() {
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? MODERN_MESSAGE_LENGTH : LEGACY_MESSAGE_LENGTH;
        if (HANDLE_JSON) {
            jsonHeader = ComponentSerializer.buildJSONString(headerComponents);
            jsonFooter = ComponentSerializer.buildJSONString(footerComponents);
        }
        writeString(jsonHeader, maxMessageLength);
        writeString(jsonFooter, maxMessageLength);
    }

    public String getJSONHeader() {
        return jsonHeader;
    }

    public void setJSONHeader(String jsonHeader) {
        this.jsonHeader = jsonHeader;
    }

    public String getJSONFooter() {
        return jsonFooter;
    }

    public void setJSONFooter(String jsonFooter) {
        this.jsonFooter = jsonFooter;
    }

    public List<TextComponent> getHeaderComponents() {
        return headerComponents;
    }

    public void setHeaderComponents(List<TextComponent> headerComponents) {
        this.headerComponents = headerComponents;
    }

    public List<TextComponent> getFooterComponents() {
        return footerComponents;
    }

    public void setFooterComponents(List<TextComponent> footerComponents) {
        this.footerComponents = footerComponents;
    }
}
