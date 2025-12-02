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

package com.github.retrooper.packetevents.protocol.player;

import java.util.*;

/**
 * Wrapper for the Player Game Profile.
 *
 * @author retrooper
 * @since 1.7
 */
public class UserProfile {
    private UUID uuid;
    private String name;
    private List<TextureProperty> textureProperties;

    public UserProfile(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        textureProperties = new ArrayList<>();
    }

    public UserProfile(UUID uuid, String name, List<TextureProperty> textureProperties) {
        this.uuid = uuid;
        this.name = name;
        this.textureProperties = textureProperties;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TextureProperty> getTextureProperties() {
        return textureProperties;
    }

    public void setTextureProperties(List<TextureProperty> textureProperties) {
        this.textureProperties = textureProperties;
    }
}
