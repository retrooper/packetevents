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
import com.github.retrooper.packetevents.protocol.util.LegacyComponent;
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
        NONE((byte) 0x00),
        FRIENDLY_FIRE((byte) 0x01),
        FRIENDLY_CAN_SEE_INVISIBLE((byte) 0x02),
        ALL((byte) 0x03);

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
        ALWAYS("always"),
        NEVER("never"),
        HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
        HIDE_FOR_OWN_TEAM("hideForOwnTeam");

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
        ALWAYS("always"),
        NEVER("never"),
        PUSH_OTHER_TEAMS("pushOtherTeams"),
        PUSH_OWN_TEAM("pushOwnTeam");

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
        CREATE,
        REMOVE,
        UPDATE,
        ADD_ENTITIES,
        REMOVE_ENTITIES;
    }

    public WrapperPlayServerTeams(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTeams(String teamName, TeamMode teamMode, @Nullable ScoreBoardTeamInfo teamInfo, String... entities) {
        this(teamName, teamMode, teamInfo, Arrays.asList(entities));
    }

    public WrapperPlayServerTeams(String teamName, TeamMode teamMode, @Nullable ScoreBoardTeamInfo teamInfo, Collection<String> entities) {
        super(PacketType.Play.Server.TEAMS);
        this.teamName = teamName;
        this.teamMode = teamMode;
        this.players = entities;
        this.teamInfo = Optional.ofNullable(teamInfo);
    }

    @Deprecated
    public WrapperPlayServerTeams(String teamName, TeamMode teamMode, Optional<ScoreBoardTeamInfo> teamInfo, String... entities) {
        this(teamName, teamMode, teamInfo, Arrays.asList(entities));
    }

    @Deprecated
    public WrapperPlayServerTeams(String teamName, TeamMode teamMode, Optional<ScoreBoardTeamInfo> teamInfo, Collection<String> entities) {
        super(PacketType.Play.Server.TEAMS);
        this.teamName = teamName;
        this.teamMode = teamMode;
        this.players = entities;
        this.teamInfo = teamInfo;
    }

    @Override
    public void read() {
        int teamNameLimit = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18) ? 32767 : 16;
        teamName = readString(teamNameLimit);
        teamMode = TeamMode.values()[readByte()];
        ScoreBoardTeamInfo info = null;
        if (teamMode == TeamMode.CREATE || teamMode == TeamMode.UPDATE) {
            info = ScoreBoardTeamInfo.read(this);
        }
        teamInfo = Optional.ofNullable(info);
        players = new ArrayList<>();
        if (teamMode == TeamMode.CREATE || teamMode == TeamMode.ADD_ENTITIES || teamMode == TeamMode.REMOVE_ENTITIES) {
            int size;
            if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
                size = readShort();
            } else {
                size = readVarInt();
            }
            for (int i = 0; i < size; i++) {
                players.add(readString(40));
            }
        }
    }

    @Override
    public void write() {
        int teamNameLimit = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18) ? 32767 : 16;
        writeString(teamName, teamNameLimit);
        writeByte(teamMode.ordinal());
        if (teamMode == TeamMode.CREATE || teamMode == TeamMode.UPDATE) {
            ScoreBoardTeamInfo info = this.teamInfo.orElse(ScoreBoardTeamInfo.EMPTY);
            ScoreBoardTeamInfo.write(this, info);
        }

        if (teamMode == TeamMode.CREATE || teamMode == TeamMode.ADD_ENTITIES || teamMode == TeamMode.REMOVE_ENTITIES) {
            if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
                writeShort(players.size());
            } else {
                writeVarInt(players.size());
            }
            for (String playerName : players) {
                writeString(playerName, 40);
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

    public void setTeamInfo(@Nullable ScoreBoardTeamInfo teamInfo) {
        this.teamInfo = Optional.ofNullable(teamInfo);
    }

    public static class ScoreBoardTeamInfo {

        private static final ScoreBoardTeamInfo EMPTY = new ScoreBoardTeamInfo(
                LegacyComponent.empty(), LegacyComponent.empty(), LegacyComponent.empty(),
                NameTagVisibility.ALWAYS, CollisionRule.ALWAYS,
                NamedTextColor.WHITE, OptionData.NONE
        );

        private LegacyComponent displayName;
        private LegacyComponent prefix;
        private LegacyComponent suffix;
        private NameTagVisibility tagVisibility;
        private CollisionRule collisionRule;
        private NamedTextColor color;
        private OptionData optionData;

        public ScoreBoardTeamInfo(
                Component displayName, @Nullable Component prefix, @Nullable Component suffix,
                NameTagVisibility tagVisibility, CollisionRule collisionRule,
                NamedTextColor color, OptionData optionData
        ) {
            this(
                    LegacyComponent.wrapOrEmpty(displayName),
                    LegacyComponent.wrapOrEmpty(prefix),
                    LegacyComponent.wrapOrEmpty(suffix),
                    tagVisibility, collisionRule, color, optionData
            );
        }

        public ScoreBoardTeamInfo(
                LegacyComponent displayName, LegacyComponent prefix, LegacyComponent suffix,
                NameTagVisibility tagVisibility, CollisionRule collisionRule,
                NamedTextColor color, OptionData optionData
        ) {
            this.displayName = displayName;
            this.prefix = prefix;
            this.suffix = suffix;
            this.tagVisibility = tagVisibility;
            this.collisionRule = collisionRule;
            this.color = color;
            this.optionData = optionData;
        }

        public static ScoreBoardTeamInfo read(PacketWrapper<?> wrapper) {
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_13)) {
                LegacyComponent displayName = new LegacyComponent(wrapper.readString(32));
                LegacyComponent prefix = new LegacyComponent(wrapper.readString(32));
                LegacyComponent suffix = new LegacyComponent(wrapper.readString(32));
                OptionData optionData = wrapper.readEnum(OptionData.values());

                NameTagVisibility tagVisibility = NameTagVisibility.ALWAYS;
                CollisionRule collisionRule = CollisionRule.ALWAYS;
                NamedTextColor color = NamedTextColor.WHITE;
                if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_8)) {
                    tagVisibility = NameTagVisibility.fromID(wrapper.readString(32));
                    if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
                        collisionRule = CollisionRule.fromID(wrapper.readString(32));
                    }
                    if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_17)) {
                        // starting from 1.17, the color is sent with ColorFormatting enum ordinal
                        int colorId = wrapper.readVarInt();
                        if (colorId == 21) {
                            colorId = -1;
                        }
                        color = ColorUtil.fromId(colorId);
                    } else {
                        color = ColorUtil.fromId(wrapper.readByte());
                    }
                }
                return new ScoreBoardTeamInfo(displayName, prefix, suffix, tagVisibility, collisionRule, color, optionData);
            }

            LegacyComponent displayName = LegacyComponent.wrapOrEmpty(wrapper.readComponent());
            if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_26_2)) {
                OptionData optionData = wrapper.readEnum(OptionData.values());

                NameTagVisibility nameTagVisibility;
                CollisionRule collisionRule;
                if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
                    nameTagVisibility = wrapper.readEnum(NameTagVisibility.class);
                    collisionRule = wrapper.readEnum(CollisionRule.class);
                } else {
                    nameTagVisibility = NameTagVisibility.fromID(wrapper.readString(40));
                    collisionRule = CollisionRule.fromID(wrapper.readString(40));
                }
                NamedTextColor color = ColorUtil.fromId(wrapper.readByte());
                LegacyComponent prefix = LegacyComponent.wrapOrEmpty(wrapper.readComponent());
                LegacyComponent suffix = LegacyComponent.wrapOrEmpty(wrapper.readComponent());

                return new ScoreBoardTeamInfo(displayName, prefix, suffix, nameTagVisibility, collisionRule, color, optionData);
            }

            LegacyComponent prefix = LegacyComponent.wrapOrEmpty(wrapper.readComponent());
            LegacyComponent suffix = LegacyComponent.wrapOrEmpty(wrapper.readComponent());
            NameTagVisibility nameTagVisibility = wrapper.readEnum(NameTagVisibility.class);
            CollisionRule collisionRule = wrapper.readEnum(CollisionRule.class);
            NamedTextColor color = wrapper.readOptional(ew -> ColorUtil.fromId(ew.readVarInt()));
            OptionData optionData = wrapper.readEnum(OptionData.values());

            return new ScoreBoardTeamInfo(displayName, prefix, suffix, nameTagVisibility, collisionRule, color, optionData);
        }

        public static void write(PacketWrapper<?> wrapper, ScoreBoardTeamInfo info) {
            if (wrapper.getServerVersion().isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
                wrapper.writeString(info.displayName.getLegacy());
                wrapper.writeString(info.prefix.getLegacy());
                wrapper.writeString(info.suffix.getLegacy());
                wrapper.writeEnum(info.optionData);
                if (wrapper.getServerVersion().isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
                    wrapper.writeString(NameTagVisibility.ALWAYS.getId(), 32);
                    wrapper.writeByte(0xF);
                } else {
                    wrapper.writeString(info.tagVisibility.getId(), 32);
                    if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
                        wrapper.writeString(info.collisionRule.getId(), 32);
                    }
                    wrapper.writeByte(ColorUtil.getId(info.color));
                }
            } else {
                wrapper.writeComponent(info.displayName.getComponent());
                if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_2)) {
                    wrapper.writeComponent(info.prefix.getComponent());
                    wrapper.writeComponent(info.suffix.getComponent());
                    wrapper.writeEnum(info.tagVisibility);
                    wrapper.writeEnum(info.collisionRule);
                    wrapper.writeOptional(info.color, (ew, c) ->
                            ew.writeVarInt(ColorUtil.getId(c)));
                    wrapper.writeEnum(info.optionData);
                } else {
                    wrapper.writeEnum(info.optionData);
                    if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
                        wrapper.writeEnum(info.tagVisibility);
                        wrapper.writeEnum(info.collisionRule);
                    } else {
                        wrapper.writeString(info.tagVisibility.getId());
                        wrapper.writeString(info.collisionRule.getId());
                    }
                    if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_17)) {
                        int colorId = ColorUtil.getId(info.color);
                        if (colorId < 0) {
                            colorId = 21; // since 1.17, minecraft decides to use writeEnum rather than writing it value, while 21 equals RESET
                        }
                        wrapper.writeVarInt(colorId);
                    } else {
                        wrapper.writeByte(ColorUtil.getId(info.color));
                    }
                    wrapper.writeComponent(info.prefix.getComponent());
                    wrapper.writeComponent(info.suffix.getComponent());
                }
            }
        }

        public LegacyComponent getLegacyDisplayName() {
            return this.displayName;
        }

        public void setLegacyDisplayName(LegacyComponent component) {
            this.displayName = component;
        }

        public Component getDisplayName() {
            return this.displayName.getComponent();
        }

        public void setDisplayName(Component displayName) {
            this.displayName = new LegacyComponent(displayName);
        }

        public LegacyComponent getLegacyPrefix() {
            return this.prefix;
        }

        public void setLegacyPrefix(LegacyComponent component) {
            this.prefix = component;
        }

        public Component getPrefix() {
            return this.prefix.getComponent();
        }

        public void setPrefix(Component prefix) {
            this.prefix = new LegacyComponent(prefix);
        }

        public LegacyComponent getLegacySuffix() {
            return this.suffix;
        }

        public void setLegacySuffix(LegacyComponent component) {
            this.suffix = component;
        }

        public Component getSuffix() {
            return this.suffix.getComponent();
        }

        public void setSuffix(Component suffix) {
            this.suffix = new LegacyComponent(suffix);
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
