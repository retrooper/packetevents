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

public class WrapperPlayServerSetTitleText extends PacketWrapper<WrapperPlayServerSetTitleText> {
    public static boolean HANDLE_JSON = true;
    private String titleJson;
    private Component title;

    public WrapperPlayServerSetTitleText(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSetTitleText(Component title) {
        super(PacketType.Play.Server.SET_TITLE_TEXT);
        this.title = title;
    }

    public WrapperPlayServerSetTitleText(String titleJson) {
        super(PacketType.Play.Server.SET_TITLE_TEXT);
        this.titleJson = titleJson;
    }

    @Override
    public void read() {
        titleJson = readComponentJSON();
        if (HANDLE_JSON) {
            title = AdventureSerializer.parseComponent(titleJson);
        }
    }

    @Override
    public void write() {
        if (HANDLE_JSON && title != null) {
            titleJson = AdventureSerializer.toJson(title);
        }
        writeComponentJSON(titleJson);
    }

    @Override
    public void copy(WrapperPlayServerSetTitleText wrapper) {
        titleJson = wrapper.titleJson;
        title = wrapper.title;
    }

    public Component getTitle() {
        return title;
    }

    public void setTitle(Component title) {
        this.title = title;
    }

    public String getTitleJson() {
        return titleJson;
    }

    public void setTitleJson(String titleJson) {
        this.titleJson = titleJson;
    }
}
