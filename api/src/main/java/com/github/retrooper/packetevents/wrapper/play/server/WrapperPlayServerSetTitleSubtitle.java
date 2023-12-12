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

    @Deprecated
    public static boolean HANDLE_JSON = true;

    private Component subtitle;

    public WrapperPlayServerSetTitleSubtitle(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSetTitleSubtitle(String subtitleJson) {
        this(AdventureSerializer.parseComponent(subtitleJson));
    }

    public WrapperPlayServerSetTitleSubtitle(Component subtitle) {
        super(PacketType.Play.Server.SET_TITLE_SUBTITLE);
        this.subtitle = subtitle;
    }

    @Override
    public void read() {
        this.subtitle = this.readComponent();
    }

    @Override
    public void write() {
        this.writeComponent(this.subtitle);
    }

    @Override
    public void copy(WrapperPlayServerSetTitleSubtitle wrapper) {
        this.subtitle = wrapper.subtitle;
    }

    public Component getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(Component subtitle) {
        this.subtitle = subtitle;
    }

    @Deprecated
    public String getSubtitleJson() {
        return AdventureSerializer.toJson(this.getSubtitle());
    }

    @Deprecated
    public void setSubtitleJson(String subtitleJson) {
        this.setSubtitle(AdventureSerializer.parseComponent(subtitleJson));
    }
}
