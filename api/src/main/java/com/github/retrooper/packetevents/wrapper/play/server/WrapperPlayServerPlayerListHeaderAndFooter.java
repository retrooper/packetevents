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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

public class WrapperPlayServerPlayerListHeaderAndFooter extends PacketWrapper<WrapperPlayServerPlayerListHeaderAndFooter> {
    public static boolean HANDLE_JSON = true;
    private String headerJson;
    private String footerJson;
    private Component headerComponent;
    private Component footerComponent;

    public WrapperPlayServerPlayerListHeaderAndFooter(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPlayerListHeaderAndFooter(Component headerComponent, Component footerComponent) {
        super(PacketType.Play.Server.PLAYER_LIST_HEADER_AND_FOOTER);
        this.headerComponent = headerComponent;
        this.footerComponent = footerComponent;
    }

    public WrapperPlayServerPlayerListHeaderAndFooter(String headerJson, String footerJson) {
        super(PacketType.Play.Server.PLAYER_LIST_HEADER_AND_FOOTER);
        this.headerJson = headerJson;
        this.footerJson = footerJson;
    }

    @Override
    public void read() {
        headerJson = readComponentJSON();
        footerJson = readComponentJSON();
        if (HANDLE_JSON) {
            headerComponent = AdventureSerializer.parseComponent(headerJson);
            footerComponent = AdventureSerializer.parseComponent(footerJson);
        }
    }

    @Override
    public void write() {
        if (HANDLE_JSON && headerComponent != null && footerComponent != null) {
            headerJson = AdventureSerializer.toJson(headerComponent);
            footerJson = AdventureSerializer.toJson(footerComponent);
        }
        writeComponentJSON(headerJson);
        writeComponentJSON(footerJson);
    }

    @Override
    public void copy(WrapperPlayServerPlayerListHeaderAndFooter wrapper) {
        headerJson = wrapper.headerJson;
        footerJson = wrapper.footerJson;
        headerComponent = wrapper.headerComponent;
        footerComponent = wrapper.footerComponent;
    }

    public String getHeaderJson() {
        return headerJson;
    }

    public void setHeaderJson(String headerJson) {
        this.headerJson = headerJson;
    }

    public String getFooterJson() {
        return footerJson;
    }

    public void setFooterJson(String footerJson) {
        this.footerJson = footerJson;
    }

    public Component getHeaderComponent() {
        return headerComponent;
    }

    public void setHeaderComponent(Component headerComponent) {
        this.headerComponent = headerComponent;
    }

    public Component getFooterComponent() {
        return footerComponent;
    }

    public void setFooterComponent(Component footerComponent) {
        this.footerComponent = footerComponent;
    }
}
