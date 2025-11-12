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
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.HashSet;
import java.util.Set;

public class WrapperPlayServerUpdateEnabledFeatures extends PacketWrapper<WrapperPlayServerUpdateEnabledFeatures> {
    private Set<ResourceLocation> features;
    public WrapperPlayServerUpdateEnabledFeatures(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUpdateEnabledFeatures(Set<ResourceLocation> features) {
        super(PacketType.Play.Server.UPDATE_ENABLED_FEATURES);
        this.features = features;
    }

    @Override
    public void read() {
        features = readCollection(HashSet::new, PacketWrapper::readIdentifier);
    }

    @Override
    public void write() {
        writeCollection(features, PacketWrapper::writeIdentifier);
    }

    @Override
    public void copy(WrapperPlayServerUpdateEnabledFeatures wrapper) {
        this.features = wrapper.features;
    }

    public Set<ResourceLocation> getFeatures() {
        return features;
    }

    public void setFeatures(Set<ResourceLocation> features) {
        this.features = features;
    }
}
