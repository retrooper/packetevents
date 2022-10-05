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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
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

public class WrapperPlayServerTeams extends PacketWrapper<WrapperPlayServerTeams> {
    private String teamName;
    private TeamMode teamMode;
    private Collection<String> players;
    private Optional<ScoreBoardTeamInfo> teamInfo;

    public enum OptionData {
        NONE((byte) 0x00), FRIENDLY_FIRE((byte) 0x01), FRIENDLY_CAN_SEE_INVISIBLE((byte) 0x02), ALL((byte) 0x03);

        private static final OptionData[] VALUES = values();
        private final byte byteValue;

        OptionData(byte value) {
            byteValue = value;
        }

        public byte getByteValue() {
            return byteValue;
        }

        @Nullable
        public static OptionData fromValue(byte value) {
            for (OptionData data : VALUES) {
                if (data.getByteValue() == value) {
                    return data;
                }
            }
            return null;
        }
    }

    public enum NameTagVisibility {
        ALWAYS("always"), NEVER("never"), HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"), HIDE_FOR_OWN_TEAM("hideForOwnTeam");

        private final String id;

        NameTagVisibility(String id) {
            this.id = id;
        }

        @Nullable
        public static NameTagVisibility fromID(String id) {
            for (NameTagVisibility value : NameTagVisibility.values()) {
                if (value.id.equalsIgnoreCase(id)) {
                    return value;
                }
            }
            return null;
        }

        public String getId() {
            return id;
        }
    }

    public enum CollisionRule {
        ALWAYS("always"), NEVER("never"), PUSH_OTHER_TEAMS("pushOtherTeams"), PUSH_OWN_TEAM("pushOwnTeam");

        private final String id;

        CollisionRule(String id) {
            this.id = id;
        }

        @Nullable
        public static CollisionRule fromID(String id) {
            for (CollisionRule value : CollisionRule.values()) {
                if (value.id.equalsIgnoreCase(id)) {
                    return value;
                }
            }
            return null;
        }

        public String getId() {
            return id;
        }

    }

    public enum TeamMode {
        CREATE, REMOVE, UPDATE, ADD_ENTITIES, REMOVE_ENTITIES;
    }

    public WrapperPlayServerTeams(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTeams(String teamName, TeamMode teamMode, Optional<ScoreBoardTeamInfo> teamInfo, String... entities) {
        this(teamName, teamMode, teamInfo, Arrays.asList(entities));
    }

    public WrapperPlayServerTeams(String teamName, TeamMode teamMode, Optional<ScoreBoardTeamInfo> teamInfo, Collection<String> entities) {
        super(PacketType.Play.Server.TEAMS);
        this.teamName = teamName;
        this.teamMode = teamMode;
        this.players = entities;
        this.teamInfo = teamInfo;
    }

    @Override
    public void read() {
        teamName = readString(16);
        teamMode = TeamMode.values()[readByte()];
        ScoreBoardTeamInfo info = null;
        if (teamMode == TeamMode.CREATE || teamMode == TeamMode.UPDATE) {
            Component displayName, prefix, suffix;
            OptionData optionData;
            NameTagVisibility nameTagVisibility;
            CollisionRule collisionRule = null;
            NamedTextColor color;
            if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
                displayName = AdventureSerializer.fromLegacyFormat(readString());
                prefix = AdventureSerializer.fromLegacyFormat(readString());
                suffix = AdventureSerializer.fromLegacyFormat(readString());
                optionData = OptionData.values()[readByte()];
                if (serverVersion == ServerVersion.V_1_7_10) {
                    nameTagVisibility = NameTagVisibility.ALWAYS;
                    color = NamedTextColor.WHITE;
                } else {
                    nameTagVisibility = NameTagVisibility.fromID(readString());
                    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9))
                        collisionRule = CollisionRule.fromID(readString());
                    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
                        // starting from 1.17, the color is sent with ColorFormatting enum ordinal
                        int colorId = readVarInt();
                        if (colorId == 21)
                            colorId = -1;
                        color = ColorUtil.fromId(colorId);
                    } else {
                        color = ColorUtil.fromId(readByte());
                    }
                }
            } else {
                displayName = readComponent();
                optionData = OptionData.fromValue(readByte());
                nameTagVisibility = NameTagVisibility.fromID(readString());
                collisionRule = CollisionRule.fromID(readString());
                color = ColorUtil.fromId(readByte());
                prefix = readComponent();
                suffix = readComponent();
            }
            info = new ScoreBoardTeamInfo(displayName, prefix, suffix, nameTagVisibility, collisionRule == null ? CollisionRule.ALWAYS : collisionRule, color, optionData);
        }
        teamInfo = Optional.ofNullable(info);
        players = new ArrayList<>();
        if (teamMode == TeamMode.CREATE || teamMode == TeamMode.ADD_ENTITIES || teamMode == TeamMode.REMOVE_ENTITIES) {
            int size;
            if (serverVersion == ServerVersion.V_1_7_10) {
                size = readShort();
            } else {
                size = readVarInt();
            }
            for (int i = 0; i < size; i++) {
                players.add(readString());
            }
        }
    }

    @Override
    public void write() {
        writeString(teamName, 16);
        writeByte(teamMode.ordinal());
        if (teamMode == TeamMode.CREATE || teamMode == TeamMode.UPDATE) {
            ScoreBoardTeamInfo info = teamInfo.orElse(new ScoreBoardTeamInfo(Component.empty(), Component.empty(), Component.empty(), NameTagVisibility.ALWAYS, CollisionRule.ALWAYS, NamedTextColor.WHITE, OptionData.NONE));
            if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
                writeString(AdventureSerializer.asVanilla(info.displayName));
                writeString(AdventureSerializer.asVanilla(info.prefix));
                writeString(AdventureSerializer.asVanilla(info.suffix));
                writeByte(info.optionData.ordinal());
                if (serverVersion == ServerVersion.V_1_7_10) {
                    writeString(NameTagVisibility.ALWAYS.getId());
                    writeByte(15);
                } else {
                    writeString(info.tagVisibility.id);
                    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) writeString(info.collisionRule.getId());
                    writeByte(ColorUtil.getId(info.color));
                }
            } else {
                writeComponent(info.displayName);
                writeByte(info.optionData.getByteValue());
                writeString(info.tagVisibility.id);
                writeString(info.collisionRule.getId());
                if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
                    int colorId = ColorUtil.getId(info.color);
                    if (colorId < 0)
                        colorId = 21; // since 1.17, minecraft decides to use writeEnum rather than writing it value, while 21 equals RESET
                    writeVarInt(colorId);
                } else {
                    writeByte(ColorUtil.getId(info.color));
                }
                writeComponent(info.prefix);
                writeComponent(info.suffix);
            }
        }

        if (teamMode == TeamMode.CREATE || teamMode == TeamMode.ADD_ENTITIES || teamMode == TeamMode.REMOVE_ENTITIES) {
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
        players = wrapper.players;
        teamInfo = wrapper.teamInfo;
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

    public Optional<ScoreBoardTeamInfo> getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(Optional<ScoreBoardTeamInfo> teamInfo) {
        this.teamInfo = teamInfo;
    }

    public static class ScoreBoardTeamInfo {

        private Component displayName;
        private Component prefix;
        private Component suffix;
        private NameTagVisibility tagVisibility;
        private CollisionRule collisionRule;
        private NamedTextColor color;
        private OptionData optionData;

        public ScoreBoardTeamInfo(Component displayName, @Nullable Component prefix, @Nullable Component suffix, NameTagVisibility tagVisibility, CollisionRule collisionRule, NamedTextColor color, OptionData optionData) {
            this.displayName = displayName;
            if (prefix == null) {
                prefix = Component.empty();
            }
            if (suffix == null) {
                suffix = Component.empty();
            }
            this.prefix = prefix;
            this.suffix = suffix;
            this.tagVisibility = tagVisibility;
            this.collisionRule = collisionRule;
            this.color = color;
            this.optionData = optionData;
        }

        public Component getDisplayName() {
            return displayName;
        }

        public void setDisplayName(Component displayName) {
            this.displayName = displayName;
        }

        public Component getPrefix() {
            return prefix;
        }

        public void setPrefix(Component prefix) {
            this.prefix = prefix;
        }

        public Component getSuffix() {
            return suffix;
        }

        public void setSuffix(Component suffix) {
            this.suffix = suffix;
        }

        public NameTagVisibility getTagVisibility() {
            return tagVisibility;
        }

        public void setTagVisibility(NameTagVisibility tagVisibility) {
            this.tagVisibility = tagVisibility;
        }

        public CollisionRule getCollisionRule() {
            return collisionRule;
        }

        public void setCollisionRule(CollisionRule collisionRule) {
            this.collisionRule = collisionRule;
        }

        public NamedTextColor getColor() {
            return color;
        }

        public void setColor(NamedTextColor color) {
            this.color = color;
        }

        public OptionData getOptionData() {
            return optionData;
        }

        public void setOptionData(OptionData optionData) {
            this.optionData = optionData;
        }

    }

}