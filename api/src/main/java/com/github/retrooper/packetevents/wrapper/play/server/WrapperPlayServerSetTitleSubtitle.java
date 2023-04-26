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

public class WrapperPlayServerSetTitleSubtitle extends PacketWrapper<WrapperPlayServerSetTitleSubtitle> {
    public static boolean HANDLE_JSON = true;
    private String subtitleJson;
    private Component subtitle;

    public WrapperPlayServerSetTitleSubtitle(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSetTitleSubtitle(Component subtitle) {
        super(PacketType.Play.Server.SET_TITLE_SUBTITLE);
        this.subtitle = subtitle;
    }

    public WrapperPlayServerSetTitleSubtitle(String subtitleJson) {
        super(PacketType.Play.Server.SET_TITLE_SUBTITLE);
        this.subtitleJson = subtitleJson;
    }

    @Override
    public void read() {
        subtitleJson = readComponentJSON();
        if (HANDLE_JSON) {
            subtitle = AdventureSerializer.parseComponent(subtitleJson);
        }
    }

    @Override
    public void write() {
        if (HANDLE_JSON && subtitle != null) {
            subtitleJson = AdventureSerializer.toJson(subtitle);
        }
        writeComponentJSON(subtitleJson);
    }

    @Override
    public void copy(WrapperPlayServerSetTitleSubtitle wrapper) {
        subtitleJson = wrapper.subtitleJson;
        subtitle = wrapper.subtitle;
    }

    public Component getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(Component subtitle) {
        this.subtitle = subtitle;
    }

    public String getSubtitleJson() {
        return subtitleJson;
    }

    public void setSubtitleJson(String subtitleJson) {
        this.subtitleJson = subtitleJson;
    }
}
