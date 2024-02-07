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

public class WrapperPlayServerSetActionBarText extends PacketWrapper<WrapperPlayServerSetActionBarText> {

    @Deprecated
    public static boolean HANDLE_JSON = true;

    private Component actionBar;

    public WrapperPlayServerSetActionBarText(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSetActionBarText(String actionBarJson) {
        this(AdventureSerializer.parseComponent(actionBarJson));
    }

    public WrapperPlayServerSetActionBarText(Component actionBar) {
        super(PacketType.Play.Server.ACTION_BAR);
        this.actionBar = actionBar;
    }

    @Override
    public void read() {
        this.actionBar = this.readComponent();
    }

    @Override
    public void write() {
        this.writeComponent(this.actionBar);
    }

    @Override
    public void copy(WrapperPlayServerSetActionBarText wrapper) {
        this.actionBar = wrapper.actionBar;
    }

    public Component getActionBar() {
        return actionBar;
    }

    public void setActionBar(Component actionBar) {
        this.actionBar = actionBar;
    }

    @Deprecated
    public String getActionBarJson() {
        return AdventureSerializer.toJson(this.getActionBar());
    }

    @Deprecated
    public void setActionBarJson(String titleJson) {
        this.setActionBar(AdventureSerializer.parseComponent(titleJson));
    }
}
