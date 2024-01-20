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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WrapperPlayServerResourcePackSend extends PacketWrapper<WrapperPlayServerResourcePackSend> {

    public static final int MAX_HASH_LENGTH = 40;

    private UUID packId;
    private String url;
    private String hash;
    private boolean required;
    private Component prompt;

    public WrapperPlayServerResourcePackSend(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerResourcePackSend(String url, String hash, boolean required, @Nullable Component prompt) {
        this(UUID.randomUUID(), url, hash, required, prompt);
    }

    public WrapperPlayServerResourcePackSend(UUID packId, String url, String hash, boolean required, @Nullable Component prompt) {
        super(PacketType.Play.Server.RESOURCE_PACK_SEND);

        if (hash.length() > MAX_HASH_LENGTH) {
            throw new IllegalArgumentException("Hash is too long (max " + MAX_HASH_LENGTH + ", was " + hash.length() + ")");
        }

        this.packId = packId;
        this.url = url;
        this.hash = hash;
        this.required = required;
        this.prompt = prompt;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.packId = this.readUUID();
        }

        url = readString();
        hash = readString(MAX_HASH_LENGTH);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            required = readBoolean();
            boolean hasPrompt = readBoolean();
            if (hasPrompt) {
                prompt = readComponent();
            }
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.writeUUID(this.packId);
        }

        writeString(url);
        writeString(hash, MAX_HASH_LENGTH);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            writeBoolean(required);
            writeBoolean(prompt != null);
            if (prompt != null) {
                writeComponent(prompt);
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerResourcePackSend wrapper) {
        packId = wrapper.packId;
        url = wrapper.url;
        hash = wrapper.hash;
        required = wrapper.required;
        prompt = wrapper.prompt;
    }

    public UUID getPackId() {
        return this.packId;
    }

    public void setPackId(UUID packId) {
        this.packId = packId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Component getPrompt() {
        return prompt;
    }
}
