package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.util.ColorUtil;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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

        private final byte byteValue;

        OptionData(byte value) {
            byteValue = value;
        }

        public byte getByteValue() {
            return byteValue;
        }

        @Nullable
        public static OptionData fromValue(byte value) {
            for(OptionData data : values()) {
                if(data.getByteValue() == value) {
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

        private final String ID;

        NameTagVisibility(String ID) {
            this.ID = ID;
        }

        @Nullable
        public static NameTagVisibility fromID(String ID) {
            for (NameTagVisibility value : NameTagVisibility.values()) {
                if (value.ID.equalsIgnoreCase(ID)) {
                    return value;
                }
            }
            return null;
        }

        public String getID() {
            return ID;
        }
    }

    public enum CollisionRule {
        ALWAYS("always"),
        NEVER("never"),
        PUSH_OTHER_TEAMS("pushOtherTeams"),
        PUSH_OWN_TEAM("pushOwnTeam");

        private final String ID;

        CollisionRule(String ID) {
            this.ID = ID;
        }

        @Nullable
        public static CollisionRule fromID(String ID) {
            for (CollisionRule value : CollisionRule.values()) {
                if (value.ID.equalsIgnoreCase(ID)) {
                    return value;
                }
            }
            return null;
        }

        public String getID() {
            return ID;
        }

    }

    public enum TeamMode {
        CREATE,
        REMOVE,
        UPDATE,
        ADD_PLAYERS,
        REMOVE_PLAYERS;
    }

    public WrapperPlayServerTeams(String teamName, TeamMode teamMode, Collection<String> players, Optional<ScoreBoardTeamInfo> teamInfo) {
        super(PacketType.Play.Server.TEAMS);
        this.teamName = teamName;
        this.teamMode = teamMode;
        this.players = players;
        this.teamInfo = teamInfo;
    }

    @Override
    public void readData() {
        teamName = readString();
        teamMode = TeamMode.values()[readByte()];
        ScoreBoardTeamInfo info = null;
        if (teamMode == TeamMode.CREATE || teamMode == TeamMode.UPDATE) {
            Component displayName, prefix, suffix;
            OptionData optionData;
            NameTagVisibility nameTagVisibility;
            CollisionRule collisionRule = null;
            NamedTextColor color;
            if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
                displayName = AdventureSerializer.asAdventure(readString());
                prefix = AdventureSerializer.asAdventure(readString());
                suffix = AdventureSerializer.asAdventure(readString());
                optionData = OptionData.values()[readByte()];
                if (serverVersion == ServerVersion.V_1_7_10) {
                    nameTagVisibility = NameTagVisibility.ALWAYS;
                    color = NamedTextColor.WHITE;
                } else {
                    nameTagVisibility = NameTagVisibility.fromID(readString());
                    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9))
                        collisionRule = CollisionRule.fromID(readString());
                    color = ColorUtil.fromId(readByte());
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
        if (teamMode == TeamMode.CREATE || teamMode == TeamMode.ADD_PLAYERS || teamMode == TeamMode.REMOVE_PLAYERS) {
            int size;
            if (serverVersion == ServerVersion.V_1_7_10)
                size = readShort();
            else
                size = readVarInt();
            for (int i = 0; i < size; i++) {
                players.add(readString());
            }
        }
    }

    @Override
    public void readData(WrapperPlayServerTeams wrapper) {
        teamName = wrapper.teamName;
        teamMode = wrapper.teamMode;
        players = wrapper.players;
        teamInfo = wrapper.teamInfo;
    }

    @Override
    public void writeData() {
        writeString(teamName);
        writeByte(teamMode.ordinal());
        if (teamMode == TeamMode.CREATE || teamMode == TeamMode.UPDATE) {
            ScoreBoardTeamInfo info = teamInfo.orElse(new ScoreBoardTeamInfo(Component.empty(), Component.empty(), Component.empty(),
                    NameTagVisibility.ALWAYS, CollisionRule.ALWAYS, NamedTextColor.WHITE, OptionData.NONE));
            if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_12_2)) {
                writeString(AdventureSerializer.asVanilla(info.displayName));
                writeString(AdventureSerializer.asVanilla(info.prefix));
                writeString(AdventureSerializer.asVanilla(info.suffix));
                writeByte(info.optionData.ordinal());
                if (serverVersion == ServerVersion.V_1_7_10) {
                    writeString(NameTagVisibility.ALWAYS.getID());
                    writeByte(15);
                } else {
                    writeString(info.tagVisibility.ID);
                    if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9))
                        writeString(info.collisionRule.getID());
                    writeByte(ColorUtil.getId(info.color));
                }
            } else {
                writeComponent(info.displayName);
                writeByte(info.optionData.getByteValue());
                writeString(info.tagVisibility.ID);
                writeString(info.collisionRule.getID());
                writeByte(ColorUtil.getId(info.color));
                writeComponent(info.prefix);
                writeComponent(info.suffix);
            }
        }

        if (teamMode == TeamMode.CREATE || teamMode == TeamMode.ADD_PLAYERS || teamMode == TeamMode.REMOVE_PLAYERS) {
            if (serverVersion == ServerVersion.V_1_7_10)
                writeShort(players.size());
            else
                writeVarInt(players.size());
            for (String playerName : players) {
                writeString(playerName);
            }
        }
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

        public ScoreBoardTeamInfo(Component displayName, Component prefix, Component suffix, NameTagVisibility tagVisibility, CollisionRule collisionRule, NamedTextColor color, OptionData optionData) {
            this.displayName = displayName;
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