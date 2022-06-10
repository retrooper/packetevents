/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021-2022 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

public class WrapperPlayClientAdvancementTab extends PacketWrapper<WrapperPlayClientAdvancementTab> {
    private Action action;
    private Optional<String> tabID;

    public WrapperPlayClientAdvancementTab(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientAdvancementTab(Action action, Optional<String> tabID) {
        super(PacketType.Play.Client.ADVANCEMENT_TAB);
        this.action = action;
        this.tabID = tabID;
    }

    @Override
    public void read() {
        action = Action.VALUES[readVarInt()];
        if (action == Action.OPENED_TAB) {
            String key = readString();
            tabID = Optional.of(key);
        } else {
            tabID = Optional.empty();
        }
    }

    @Override
    public void copy(WrapperPlayClientAdvancementTab wrapper) {
        action = wrapper.action;
        tabID = wrapper.tabID;
    }

    @Override
    public void write() {
        writeVarInt(action.ordinal());
        if (action == Action.OPENED_TAB) {
            writeString(tabID.get());
        }
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Optional<String> getTabId() {
        return tabID;
    }

    public void setTabId(Optional<String> tabID) {
        this.tabID = tabID;
    }

    public enum Action {
        OPENED_TAB,
        CLOSED_SCREEN;

        public static final Action[] VALUES = values();
    }
}
