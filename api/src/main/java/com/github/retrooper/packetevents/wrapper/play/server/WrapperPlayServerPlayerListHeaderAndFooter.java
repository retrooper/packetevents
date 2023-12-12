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

    @Deprecated
    public static boolean HANDLE_JSON = true;

    private Component header;
    private Component footer;

    public WrapperPlayServerPlayerListHeaderAndFooter(PacketSendEvent event) {
        super(event);
    }

    @Deprecated
    public WrapperPlayServerPlayerListHeaderAndFooter(String headerJson, String footerJson) {
        this(AdventureSerializer.parseComponent(headerJson), AdventureSerializer.parseComponent(footerJson));
    }

    public WrapperPlayServerPlayerListHeaderAndFooter(Component header, Component footer) {
        super(PacketType.Play.Server.PLAYER_LIST_HEADER_AND_FOOTER);
        this.header = header;
        this.footer = footer;
    }

    @Override
    public void read() {
        this.header = this.readComponent();
        this.footer = this.readComponent();
    }

    @Override
    public void write() {
        this.writeComponent(this.header);
        this.writeComponent(this.footer);
    }

    @Override
    public void copy(WrapperPlayServerPlayerListHeaderAndFooter wrapper) {
        this.header = wrapper.header;
        this.footer = wrapper.footer;
    }

    public Component getHeader() {
        return this.header;
    }

    public void setHeader(Component header) {
        this.header = header;
    }

    public Component getFooter() {
        return this.footer;
    }

    public void setFooter(Component footer) {
        this.footer = footer;
    }

    @Deprecated
    public String getHeaderJson() {
        return AdventureSerializer.toJson(this.getHeader());
    }

    @Deprecated
    public void setHeaderJson(String headerJson) {
        this.setHeader(AdventureSerializer.parseComponent(headerJson));
    }

    @Deprecated
    public String getFooterJson() {
        return AdventureSerializer.toJson(this.getFooter());
    }

    @Deprecated
    public void setFooterJson(String footerJson) {
        this.setFooter(AdventureSerializer.parseComponent(footerJson));
    }

    @Deprecated
    public Component getHeaderComponent() {
        return this.getHeader();
    }

    @Deprecated
    public void setHeaderComponent(Component headerComponent) {
        this.setHeader(headerComponent);
    }

    @Deprecated
    public Component getFooterComponent() {
        return this.getFooter();
    }

    @Deprecated
    public void setFooterComponent(Component footerComponent) {
        this.setFooter(footerComponent);
    }
}
