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

package com.github.retrooper.packetevents.manager.npc;

import com.github.retrooper.packetevents.protocol.chat.component.BaseComponent;
import com.github.retrooper.packetevents.protocol.chat.component.impl.TextComponent;
import com.github.retrooper.packetevents.protocol.player.GameProfile;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;

public class NPC {
    private BaseComponent displayName;
    private final int id;
    private final GameProfile profile;
    private int displayPing = 0;
    private Location location = new Location(0.0, 0.0, 0.0, 0.0f, 0.0f);

    public NPC(BaseComponent displayName, int entityId, GameProfile profile) {
        this.displayName = displayName;
        this.id = entityId;
        this.profile = profile;
    }

    public BaseComponent getDisplayName() {
        return displayName;
    }

    public void setDisplayName(BaseComponent displayName) {
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public GameProfile getProfile() {
        return profile;
    }

    public WrapperPlayServerPlayerInfo.PlayerData getPlayerInfoData() {
        return new WrapperPlayServerPlayerInfo.PlayerData(getDisplayName(),
                getProfile(), GameMode.SURVIVAL,
                getDisplayPing());
    }

    public int getDisplayPing() {
        return displayPing;
    }

    public void setDisplayPing(int ping) {
        this.displayPing = ping;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
