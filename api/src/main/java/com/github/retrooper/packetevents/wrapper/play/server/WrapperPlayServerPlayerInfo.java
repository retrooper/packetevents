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

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.data.gameprofile.WrappedProperty;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.data.player.GameMode;
import com.github.retrooper.packetevents.protocol.data.gameprofile.WrappedGameProfile;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WrapperPlayServerPlayerInfo extends PacketWrapper<WrapperPlayServerPlayerInfo> {
    @Nullable
    private Action action;

    private UUID uuid;

    private List<PlayerData> playerDataList;

    public enum Action {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER;

        public static final Action[] VALUES = values();
    }


    public WrapperPlayServerPlayerInfo(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPlayerInfo(@NotNull Action action, UUID uuid, List<PlayerData> playerDataList) {
        super(PacketType.Play.Server.PLAYER_INFO);
        this.action = action;
        this.uuid = uuid;
        this.playerDataList = playerDataList;
    }

    @Override
    public void readData() {
        if (serverVersion == ServerVersion.v_1_7_10) {
            playerDataList = new ArrayList<>(1);
            //Only one player data
            String username = readString();
            boolean online = readBoolean();
            int ping = readShort();
            PlayerData data = new PlayerData(username, null, GameMode.SURVIVAL, ping);
            playerDataList.add(data);
            if (online) {
                //We really can't know...
                action = null;
            } else {
                action = Action.REMOVE_PLAYER;
            }
        } else {
            action = Action.VALUES[readVarInt()];
            int playerDataCount = readVarInt();
            uuid = readUUID();
            playerDataList = new ArrayList<>(playerDataCount);
            for (int i = 0; i < playerDataCount; i++) {
                PlayerData data = null;
                switch (action) {
                    case ADD_PLAYER: {
                        String playerUsername = readString(16);
                        WrappedGameProfile gameProfile = new WrappedGameProfile(uuid, playerUsername);
                        int propertyCount = readVarInt();
                        for (int j = 0; j < propertyCount; j++) {
                            String propertyName = readString();
                            String propertyValue = readString();
                            String propertySignature = readBoolean() ? readString() : null;
                            WrappedProperty property = new WrappedProperty(propertyName, propertyValue, propertySignature);
                            gameProfile.getProperties().put(propertyName, property);
                        }
                        GameMode gameMode = GameMode.VALUES[readVarInt()];
                        int ping = readVarInt();
                        String displayName = readBoolean() ? readString() : null;
                        data = new PlayerData(displayName, gameProfile, gameMode, ping);
                        break;
                    }
                    case UPDATE_GAME_MODE: {
                        GameMode gameMode = GameMode.VALUES[readVarInt()];
                        data = new PlayerData(null, null, gameMode, -1);
                        break;
                    }
                    case UPDATE_LATENCY: {
                        int ping = readVarInt();
                        data = new PlayerData(null, null, null, ping);
                        break;
                    }
                    case UPDATE_DISPLAY_NAME:
                        String displayName = readBoolean() ? readString() : null;
                        data = new PlayerData(displayName, null, null, -1);
                        break;

                    case REMOVE_PLAYER:
                        data = new PlayerData(null, null, null, -1);
                        break;
                }
                if (data != null) {
                    playerDataList.add(data);
                }
            }
        }
    }

    @Override
    public void readData(WrapperPlayServerPlayerInfo wrapper) {
        action = wrapper.action;
        uuid = wrapper.uuid;
        playerDataList = wrapper.playerDataList;
    }

    @Override
    public void writeData() {
        if (serverVersion == ServerVersion.v_1_7_10) {
            //Only one player data can be sent
            PlayerData data = playerDataList.get(0);
            writeString(data.displayName);
            writeBoolean(action != Action.REMOVE_PLAYER);
            writeShort(data.ping);
        } else {
            writeVarInt(action.ordinal());
            writeVarInt(playerDataList.size());
            writeUUID(uuid);
            for (PlayerData data : playerDataList) {
                switch (action) {
                    case ADD_PLAYER: {
                        writeString(data.gameProfile.getName(), 16);
                        writeVarInt(data.gameProfile.getProperties().size());
                        for (WrappedProperty property : data.gameProfile.getProperties().values()) {
                            writeString(property.getName());
                            writeString(property.getValue());
                            boolean hasSignature = property.hasSignature();
                            writeBoolean(hasSignature);
                            if (hasSignature) {
                                writeString(property.getSignature());
                            }
                        }
                        writeVarInt(data.gameMode.ordinal());
                        writeVarInt(data.ping);
                        if (data.displayName != null) {
                            writeBoolean(true);
                            writeString(data.displayName);
                        }
                        else {
                            writeBoolean(false);
                        }
                        break;
                    }
                    case UPDATE_GAME_MODE: {
                        writeVarInt(data.gameMode.ordinal());
                        break;
                    }
                    case UPDATE_LATENCY: {
                        writeVarInt(data.ping);
                        break;
                    }
                    case UPDATE_DISPLAY_NAME:
                        if (data.displayName != null) {
                            writeBoolean(true);
                            writeString(data.displayName);
                        }
                        else {
                            writeBoolean(false);
                        }
                        break;

                    case REMOVE_PLAYER:
                        break;
                }
            }
        }

    }

    @Nullable
    public Action getAction() {
        return action;
    }

    public void setAction(@NotNull Action action) {
        this.action = action;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public List<PlayerData> getPlayerDataList() {
        return playerDataList;
    }

    public void setPlayerDataList(List<PlayerData> playerDataList) {
        this.playerDataList = playerDataList;
    }

    public static class PlayerData {
        @Nullable
        private String displayName;
        @Nullable
        private WrappedGameProfile gameProfile;
        @Nullable
        private GameMode gameMode;

        private int ping;

        public PlayerData(@Nullable String displayName, @Nullable WrappedGameProfile gameProfile, @Nullable GameMode gameMode, int ping) {
            this.displayName = displayName;
            this.gameProfile = gameProfile;
            this.gameMode = gameMode;
            this.ping = ping;
        }

        @Nullable
        public WrappedGameProfile getGameProfile() {
            return gameProfile;
        }

        public void setGameProfile(@Nullable WrappedGameProfile gameProfile) {
            this.gameProfile = gameProfile;
        }

        @Nullable
        public GameMode getGameMode() {
            return gameMode;
        }

        public void setGameMode(@Nullable GameMode gameMode) {
            this.gameMode = gameMode;
        }

        public int getPing() {
            return ping;
        }

        public void setPing(int ping) {
            this.ping = ping;
        }

        @Nullable
        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(@Nullable String displayName) {
            this.displayName = displayName;
        }
    }
}
