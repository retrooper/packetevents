/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2023 retrooper and contributors
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
import org.jetbrains.annotations.Nullable;

public class WrapperPlayServerResetScore extends PacketWrapper<WrapperPlayServerResetScore> {

    private String targetName;
    private @Nullable String objective;

    public WrapperPlayServerResetScore(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerResetScore(String targetName, @Nullable String objective) {
        super(PacketType.Play.Server.RESET_SCORE);
        this.targetName = targetName;
        this.objective = objective;
    }

    @Override
    public void read() {
        this.targetName = this.readString();
        this.objective = this.readOptional(PacketWrapper::readString);
    }

    @Override
    public void write() {
        this.writeString(this.targetName);
        this.writeOptional(this.objective, PacketWrapper::writeString);
    }

    @Override
    public void copy(WrapperPlayServerResetScore wrapper) {
        this.targetName = wrapper.targetName;
        this.objective = wrapper.objective;
    }

    /**
     * Scoreboard name of the target score holder.
     * This can be an entity, player, or nothing at all.
     */
    public String getTargetName() {
        return this.targetName;
    }

    /**
     * Scoreboard name of the target score holder.
     * This can be an entity, player, or nothing at all.
     */
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public @Nullable String getObjective() {
        return this.objective;
    }

    public void setObjective(@Nullable String objective) {
        this.objective = objective;
    }
}
