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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public class WrapperPlayServerResourcePackSend extends PacketWrapper<WrapperPlayServerResourcePackSend> {
    private String url;
    private String hash;
    private boolean required;
    private Component prompt;

    public WrapperPlayServerResourcePackSend(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerResourcePackSend(String url, String hash, boolean required, @Nullable Component prompt) {
        super(PacketType.Play.Server.RESOURCE_PACK_SEND);
        this.url = url;
        this.hash = hash;
        this.required = required;
        this.prompt = prompt;
    }

    @Override
    public void read() {
        url = readString();
        hash = readString(40);

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            required = readBoolean();
            boolean hasPrompt = readBoolean();

            if (hasPrompt) {
                prompt = readComponent();
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerResourcePackSend wrapper) {
        url = wrapper.url;
        hash = wrapper.hash;
        required = wrapper.required;
        prompt = wrapper.prompt;
    }

    @Override
    public void write() {
        writeString(url);
        writeString(hash);

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            writeBoolean(required);
            writeBoolean(prompt != null);
            if (prompt != null) {
                writeComponent(prompt);
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public String getHash() {
        return hash;
    }

    public boolean isRequired() {
        return required;
    }

    public Component getPrompt() {
        return prompt;
    }
}
