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

package io.github.retrooper.packetevents.utils.gameprofile;

import java.util.UUID;

/**
 * Wrapper for the Player Game Profile.
 *
 * @author retrooper
 * @since 1.7
 */
public class WrappedGameProfile {
    private final UUID id;
    private final String name;

    public WrappedGameProfile(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getID() {
        return id;
    }

    public String getName() {
        return name;
    }


    public boolean isComplete() {
        return id != null && !isBlank(name);
    }

    private boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }
}
