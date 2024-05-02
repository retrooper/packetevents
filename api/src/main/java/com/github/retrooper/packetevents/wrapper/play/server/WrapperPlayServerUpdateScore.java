/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.score.ScoreFormat;
import com.github.retrooper.packetevents.protocol.score.ScoreFormatTypes;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayServerUpdateScore extends PacketWrapper<WrapperPlayServerUpdateScore> {

    private String entityName;
    private Action action;
    private String objectiveName;
    private Optional<Integer> value;
    private @Nullable Component entityDisplayName;
    private @Nullable ScoreFormat scoreFormat;

    public WrapperPlayServerUpdateScore(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUpdateScore(String entityName, Action action, String objectiveName, Optional<Integer> value) {
        super(PacketType.Play.Server.UPDATE_SCORE);
        this.entityName = entityName;
        this.action = action;
        this.objectiveName = objectiveName;
        this.value = value;
    }

    public WrapperPlayServerUpdateScore(String entityName, Action action, String objectiveName, int value,
                                        @Nullable Component entityDisplayName, @Nullable ScoreFormat scoreFormat) {
        super(PacketType.Play.Server.UPDATE_SCORE);
        this.entityName = entityName;
        this.action = action;
        this.objectiveName = objectiveName;
        this.value = Optional.of(value);
        this.entityDisplayName = entityDisplayName;
        this.scoreFormat = scoreFormat;
    }

    @Override
    public void read() {
        this.entityName = this.readString();
        this.objectiveName = this.readString();
        this.value = Optional.of(this.readVarInt());
        this.entityDisplayName = this.readOptional(PacketWrapper::readComponent);
        this.scoreFormat = this.readOptional(ScoreFormatTypes::read);
    }

    @Override
    public void write() {
        this.writeString(this.entityName);
        this.writeString(this.objectiveName);
        this.writeVarInt(this.value.orElse(0));
        this.writeOptional(this.entityDisplayName, PacketWrapper::writeComponent);
        this.writeOptional(this.scoreFormat, ScoreFormatTypes::write);

    }

    @Override
    public void copy(WrapperPlayServerUpdateScore wrapper) {
        entityName = wrapper.entityName;
        action = wrapper.action;
        objectiveName = wrapper.objectiveName;
        value = wrapper.value;
        entityDisplayName = wrapper.entityDisplayName;
        scoreFormat = wrapper.scoreFormat;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getObjectiveName() {
        return objectiveName;
    }

    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }

    /**
     * Always present for >=1.20.3
     */
    public Optional<Integer> getValue() {
        return value;
    }

    /**
     * Always present for >=1.20.3
     */
    public void setValue(Optional<Integer> value) {
        this.value = value;
    }

    public @Nullable Component getEntityDisplayName() {
        return this.entityDisplayName;
    }

    public void setEntityDisplayName(@Nullable Component entityDisplayName) {
        this.entityDisplayName = entityDisplayName;
    }

    public @Nullable ScoreFormat getScoreFormat() {
        return this.scoreFormat;
    }

    public void setScoreFormat(@Nullable ScoreFormat scoreFormat) {
        this.scoreFormat = scoreFormat;
    }

    public enum Action {

        CREATE_OR_UPDATE_ITEM,
        REMOVE_ITEM;

        public static final Action[] VALUES = values();
    }
}
