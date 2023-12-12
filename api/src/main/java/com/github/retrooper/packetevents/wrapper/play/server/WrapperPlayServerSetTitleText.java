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

    @Deprecated
    public static boolean HANDLE_JSON = true;

    private Component title;

    public WrapperPlayServerSetTitleText(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSetTitleText(String titleJson) {
        this(AdventureSerializer.parseComponent(titleJson));
    }

    public WrapperPlayServerSetTitleText(Component title) {
        super(PacketType.Play.Server.SET_TITLE_TEXT);
        this.title = title;
    }

    @Override
    public void read() {
        this.title = this.readComponent();
    }

    @Override
    public void write() {
        this.writeComponent(this.title);
    }

    @Override
    public void copy(WrapperPlayServerSetTitleText wrapper) {
        this.title = wrapper.title;
    }

    public Component getTitle() {
        return title;
    }

    public void setTitle(Component title) {
        this.title = title;
    }

    @Deprecated
    public String getTitleJson() {
        return AdventureSerializer.toJson(this.getTitle());
    }

    @Deprecated
    public void setTitleJson(String titleJson) {
        this.setTitle(AdventureSerializer.parseComponent(titleJson));
    }
}
