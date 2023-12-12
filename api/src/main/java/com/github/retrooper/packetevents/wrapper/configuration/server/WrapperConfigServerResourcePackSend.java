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

package com.github.retrooper.packetevents.wrapper.configuration.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WrapperConfigServerResourcePackSend extends PacketWrapper<WrapperConfigServerResourcePackSend> {

    public static final int MAX_HASH_LENGTH = 40;

    private UUID packId;
    private String url;
    private String hash;
    private boolean required;
    private Component prompt;

    public WrapperConfigServerResourcePackSend(PacketSendEvent event) {
        super(event);
    }

    public WrapperConfigServerResourcePackSend(String url, String hash, boolean required, @Nullable Component prompt) {
        this(UUID.randomUUID(), url, hash, required, prompt);
    }

    public WrapperConfigServerResourcePackSend(UUID packId, String url, String hash, boolean required, @Nullable Component prompt) {
        super(PacketType.Configuration.Server.RESOURCE_PACK_SEND);

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

        this.url = this.readString();
        this.hash = this.readString(MAX_HASH_LENGTH);
        this.required = this.readBoolean();
        if (this.readBoolean()) {
            this.prompt = this.readComponent();
        }
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.writeUUID(this.packId);
        }

        this.writeString(this.url);
        this.writeString(this.hash, MAX_HASH_LENGTH);
        this.writeBoolean(this.required);
        if (this.prompt != null) {
            this.writeBoolean(true);
            this.writeComponent(this.prompt);
        } else {
            this.writeBoolean(false);
        }
    }

    @Override
    public void copy(WrapperConfigServerResourcePackSend wrapper) {
        this.packId = wrapper.packId;
        this.url = wrapper.url;
        this.hash = wrapper.hash;
        this.required = wrapper.required;
        this.prompt = wrapper.prompt;
    }

    public UUID getPackId() {
        return this.packId;
    }

    public void setPackId(UUID packId) {
        this.packId = packId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHash() {
        return this.hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Component getPrompt() {
        return this.prompt;
    }

    public void setPrompt(Component prompt) {
        this.prompt = prompt;
    }
}
