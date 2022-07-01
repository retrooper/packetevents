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
import com.github.retrooper.packetevents.manager.server.MultiVersion;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.scoreboard.*;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.util.ColorUtil;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

// TODO: This also needs a whole recode
public class WrapperPlayServerTeams extends PacketWrapper<WrapperPlayServerTeams> {
    private String teamName;
    private TeamMode teamMode;
    private @Nullable TeamInfo teamInfo;
    private Collection<String> players;

    public WrapperPlayServerTeams(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTeams(String teamName, TeamMode teamMode, String... entities) {
        this(teamName, teamMode, null, Arrays.asList(entities));
    }

    public WrapperPlayServerTeams(String teamName, TeamMode teamMode, @Nullable TeamInfo teamInfo, String... entities) {
        this(teamName, teamMode, teamInfo, Arrays.asList(entities));
    }

    public WrapperPlayServerTeams(String teamName, TeamMode teamMode, @Nullable TeamInfo teamInfo, Collection<String> entities) {
        super(PacketType.Play.Server.TEAMS);
        this.teamName = teamName;
        this.teamMode = teamMode;
        this.players = entities;
        this.teamInfo = teamInfo;
    }

    @Override
    public void read() {
        teamName = readString(16);
        teamMode = TeamMode.getById(readByte());
        AtomicReference<TeamInfo> info = new AtomicReference<>();
        if (teamMode == TeamMode.ADD || teamMode == TeamMode.CHANGE) {
            readMulti(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_13,
                    packetWrapper -> {
                        Component displayName = readComponent();
                        OptionData optionData = OptionData.fromValue(readByte());
                        NameTagVisibility nameTagVisibility = NameTagVisibility.getByName(readString());
                        CollisionRule collisionRule = CollisionRule.getByName(readString());
                        NamedTextColor color = ColorUtil.fromId(readByte());
                        Component prefix = readComponent();
                        Component suffix = readComponent();
                        info.set(new TeamInfo(displayName, prefix, suffix, nameTagVisibility,
                                collisionRule == null ? CollisionRule.ALWAYS : collisionRule, color, optionData));
                    }, packetWrapper -> {
                        Component displayName = AdventureSerializer.fromLegacyFormat(readString());
                        Component prefix = AdventureSerializer.fromLegacyFormat(readString());
                        Component suffix = AdventureSerializer.fromLegacyFormat(readString());
                        OptionData optionData = OptionData.fromValue(readByte());
                        NameTagVisibility nameTagVisibility;
                        NamedTextColor color;
                        CollisionRule collisionRule = null;
                        if (serverVersion == ServerVersion.V_1_7_10) {
                            nameTagVisibility = NameTagVisibility.ALWAYS;
                            color = NamedTextColor.WHITE;
                        } else {
                            nameTagVisibility = NameTagVisibility.getByName(readString());
                            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                                collisionRule = CollisionRule.getByName(readString());
                            }
                            color = ColorUtil.fromId(readByte());
                        }
                        info.set(new TeamInfo(displayName, prefix, suffix, nameTagVisibility,
                                collisionRule == null ? CollisionRule.ALWAYS : collisionRule, color, optionData));
                    });
        }

        teamInfo = info.get();
        players = new ArrayList<>();
        if (teamMode == TeamMode.ADD || teamMode == TeamMode.JOIN || teamMode == TeamMode.LEAVE) {
            int size = readMultiVersional(MultiVersion.NEWER_THAN_OR_EQUALS, ServerVersion.V_1_8,
                    PacketWrapper::readVarInt,
                    PacketWrapper::readShort);
            for (int i = 0; i < size; i++) {
                players.add(readString());
            }
        }
    }

    @Override
    public void write() {
        writeString(teamName, 16);
        writeByte(teamMode.ordinal());
        if (teamMode == TeamMode.ADD || teamMode == TeamMode.CHANGE) {
            TeamInfo info = teamInfo == null ? new TeamInfo(null, null, null,
                    NameTagVisibility.ALWAYS, CollisionRule.ALWAYS, NamedTextColor.WHITE, OptionData.NONE) : teamInfo;
            if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
                writeString(AdventureSerializer.asVanilla(info.getDisplayName()));
                writeString(AdventureSerializer.asVanilla(info.getPrefix()));
                writeString(AdventureSerializer.asVanilla(info.getSuffix()));
                writeByte(info.getOptionData().ordinal());
                if (serverVersion == ServerVersion.V_1_7_10) {
                    writeString(NameTagVisibility.ALWAYS.getName());
                    writeByte(15);
                } else {
                    writeString(info.getTagVisibility().getName());
                    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) writeString(info.getCollisionRule().getName());
                    writeByte(ColorUtil.getId(info.getColor()));
                }
            } else {
                writeComponent(info.getDisplayName());
                writeByte(info.getOptionData().getByteValue());
                writeString(info.getTagVisibility().getName());
                writeString(info.getCollisionRule().getName());
                writeByte(ColorUtil.getId(info.getColor()));
                writeComponent(info.getPrefix());
                writeComponent(info.getSuffix());
            }
        }

        if (teamMode == TeamMode.ADD || teamMode == TeamMode.JOIN || teamMode == TeamMode.LEAVE) {
            if (serverVersion == ServerVersion.V_1_7_10) {
                writeShort(players.size());
            } else {
                writeVarInt(players.size());
            }
            for (String playerName : players) {
                writeString(playerName);
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerTeams wrapper) {
        teamName = wrapper.teamName;
        teamMode = wrapper.teamMode;
        teamInfo = wrapper.teamInfo;
        players = wrapper.players;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public TeamMode getTeamMode() {
        return teamMode;
    }

    public void setTeamMode(TeamMode teamMode) {
        this.teamMode = teamMode;
    }

    public Collection<String> getPlayers() {
        return players;
    }

    public void setPlayers(Collection<String> players) {
        this.players = players;
    }

    public Optional<TeamInfo> getTeamInfo() {
        return Optional.ofNullable(teamInfo);
    }

    public void setTeamInfo(@Nullable TeamInfo teamInfo) {
        this.teamInfo = teamInfo;
    }
}