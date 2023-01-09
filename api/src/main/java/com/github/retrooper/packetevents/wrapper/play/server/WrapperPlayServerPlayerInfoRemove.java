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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class WrapperPlayServerPlayerInfoRemove extends PacketWrapper<WrapperPlayServerPlayerInfoRemove> {
    private List<UUID> profileIds;

    public WrapperPlayServerPlayerInfoRemove(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPlayerInfoRemove(List<UUID> profileIds) {
        super(PacketType.Play.Server.PLAYER_INFO_REMOVE);
        this.profileIds = profileIds;
    }

    public WrapperPlayServerPlayerInfoRemove(UUID... profileIds) {
        super(PacketType.Play.Server.PLAYER_INFO_REMOVE);
        this.profileIds = new ArrayList<>();
        Collections.addAll(this.profileIds, profileIds);
    }

    @Override
    public void read() {
        profileIds = readList(PacketWrapper::readUUID);
    }

    @Override
    public void write() {
        writeList(profileIds, PacketWrapper::writeUUID);
    }

    @Override
    public void copy(WrapperPlayServerPlayerInfoRemove wrapper) {
        this.profileIds = wrapper.profileIds;
    }

    public List<UUID> getProfileIds() {
        return profileIds;
    }

    public void setProfileIds(List<UUID> profileIds) {
        this.profileIds = profileIds;
    }
}
