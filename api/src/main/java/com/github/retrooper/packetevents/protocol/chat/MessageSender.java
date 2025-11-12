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

package com.github.retrooper.packetevents.protocol.chat;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MessageSender {
    private @NotNull UUID uuid;
    private @Nullable Component displayName;
    private @Nullable Component teamName;

    public MessageSender(@NotNull UUID uuid, @Nullable Component displayName, @Nullable Component teamName) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.teamName = teamName;
    }

    public MessageSender(@Nullable Component displayName, @Nullable Component teamName) {
        this(new UUID(0L, 0L), displayName, teamName);
    }

    public MessageSender() {
        this(null, null);
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(@NotNull UUID uuid) {
        this.uuid = uuid;
    }

    public @Nullable Component getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@Nullable Component displayName) {
        this.displayName = displayName;
    }

    public @Nullable Component getTeamName() {
        return teamName;
    }

    public void setTeamName(@Nullable Component teamName) {
        this.teamName = teamName;
    }
}
