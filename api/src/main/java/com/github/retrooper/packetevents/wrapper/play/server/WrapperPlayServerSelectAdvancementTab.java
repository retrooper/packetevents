/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayServerSelectAdvancementTab extends PacketWrapper<WrapperPlayServerSelectAdvancementTab> {
    private boolean hasId;
    private @Nullable ResourceLocation identifier;

    public WrapperPlayServerSelectAdvancementTab(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSelectAdvancementTab(boolean hasId, @Nullable ResourceLocation identifier) {
        super(PacketType.Play.Server.SELECT_ADVANCEMENT_TAB);
        this.hasId = hasId;
        this.identifier = identifier;
    }

    @Override
    public void read() {
        hasId = readBoolean();
        if (hasId) {
            identifier = readIdentifier();
        }
    }

    @Override
    public void write() {
        writeBoolean(hasId);
        if (hasId) {
            writeIdentifier(identifier);
        }
    }

    @Override
    public void copy(WrapperPlayServerSelectAdvancementTab wrapper) {
        this.hasId = wrapper.hasId;
        this.identifier = wrapper.identifier;
    }

    public boolean isHasId() {
        return hasId;
    }

    public void setHasId(boolean hasId) {
        this.hasId = hasId;
    }

    public Optional<ResourceLocation> getIdentifier() {
        return Optional.ofNullable(identifier);
    }

    public void setIdentifier(@Nullable ResourceLocation identifier) {
        this.identifier = identifier;
    }
}
