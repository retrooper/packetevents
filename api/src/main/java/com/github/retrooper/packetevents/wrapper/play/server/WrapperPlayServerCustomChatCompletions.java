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
import com.github.retrooper.packetevents.protocol.chat.ChatCompletionAction;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.List;

public class WrapperPlayServerCustomChatCompletions extends PacketWrapper<WrapperPlayServerCustomChatCompletions> {
    private ChatCompletionAction action;
    private List<String> entries;

    public WrapperPlayServerCustomChatCompletions(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerCustomChatCompletions(ChatCompletionAction action, List<String> entries) {
        super(PacketType.Play.Server.CUSTOM_CHAT_COMPLETIONS);
        this.action = action;
        this.entries = entries;
    }

    @Override
    public void read() {
        this.action = ChatCompletionAction.fromId(readVarInt());
        this.entries = readList(PacketWrapper::readString);
    }

    @Override
    public void write() {
        writeVarInt(action.ordinal());
        writeList(entries, PacketWrapper::writeString);
    }

    @Override
    public void copy(WrapperPlayServerCustomChatCompletions wrapper) {
        this.action = wrapper.action;
        this.entries = wrapper.entries;
    }

    public ChatCompletionAction getAction() {
        return action;
    }

    public void setAction(ChatCompletionAction action) {
        this.action = action;
    }

    public List<String> getEntries() {
        return entries;
    }

    public void setEntries(List<String> entries) {
        this.entries = entries;
    }
}
