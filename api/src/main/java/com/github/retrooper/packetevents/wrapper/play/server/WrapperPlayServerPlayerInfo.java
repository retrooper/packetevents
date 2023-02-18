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
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.crypto.SignatureData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class WrapperPlayServerPlayerInfo extends PacketWrapper<WrapperPlayServerPlayerInfo> {
    @Nullable
    private Action action;
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

    public WrapperPlayServerPlayerInfo(@NotNull Action action, List<PlayerData> playerDataList) {
        super(PacketType.Play.Server.PLAYER_INFO);
        this.action = action;
        this.playerDataList = playerDataList;
    }

    public WrapperPlayServerPlayerInfo(@NotNull Action action, PlayerData... playerData) {
        super(PacketType.Play.Server.PLAYER_INFO);
        this.action = action;
        this.playerDataList = new ArrayList<>();
        Collections.addAll(playerDataList, playerData);
    }

    @Override
    public void read() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            playerDataList = new ArrayList<>(1);
            //Only one player data
            String rawUsername = readString();
            Component username = Component.text(rawUsername);
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
            playerDataList = new ArrayList<>(playerDataCount);
            for (int i = 0; i < playerDataCount; i++) {
                PlayerData data = null;
                UUID uuid = readUUID();
                switch (action) {
                    case ADD_PLAYER: {
                        String playerUsername = readString(16);
                        UserProfile userProfile = new UserProfile(uuid, playerUsername);
                        int propertyCount = readVarInt();
                        for (int j = 0; j < propertyCount; j++) {
                            String propertyName = readString();
                            String propertyValue = readString();
                            String propertySignature = readOptional(PacketWrapper::readString);
                            TextureProperty textureProperty = new TextureProperty(propertyName, propertyValue, propertySignature);
                            userProfile.getTextureProperties().add(textureProperty);
                        }
                        GameMode gameMode = GameMode.getById(readVarInt());
                        int ping = readVarInt();
                        Component displayName = readBoolean() ? readComponent() : null;

                        SignatureData signatureData = null;
                        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
                            signatureData = readOptional(PacketWrapper::readSignatureData);
                        }

                        data = new PlayerData(displayName, userProfile, gameMode, signatureData, ping);
                        break;
                    }
                    case UPDATE_GAME_MODE: {
                        GameMode gameMode = GameMode.getById(readVarInt());
                        data = new PlayerData((Component) null, new UserProfile(uuid, null), gameMode, -1);
                        break;
                    }
                    case UPDATE_LATENCY: {
                        int ping = readVarInt();
                        data = new PlayerData((Component) null, new UserProfile(uuid, null), null, ping);
                        break;
                    }
                    case UPDATE_DISPLAY_NAME:
                        Component displayName = readBoolean() ? readComponent() : null;
                        data = new PlayerData(displayName, new UserProfile(uuid, null), null, -1);
                        break;

                    case REMOVE_PLAYER:
                        data = new PlayerData((Component) null, new UserProfile(uuid, null), null, -1);
                        break;
                }
                if (data != null) {
                    playerDataList.add(data);
                }
            }
        }
    }

    @Override
    public void write() {
        if (serverVersion == ServerVersion.V_1_7_10) {
            //Only one player data can be sent
            PlayerData data = playerDataList.get(0);
            //We must convert the component string to a normal one
            String rawUsername = ((TextComponent) data.displayName).content();
            writeString(rawUsername);
            writeBoolean(action != Action.REMOVE_PLAYER);
            writeShort(data.ping);
        } else {
            writeVarInt(action.ordinal());
            writeVarInt(playerDataList.size());
            for (PlayerData data : playerDataList) {
                writeUUID(data.userProfile.getUUID());
                switch (action) {
                    case ADD_PLAYER: {
                        writeString(data.userProfile.getName(), 16);
                        writeList(data.userProfile.getTextureProperties(), (wrapper, textureProperty) -> {
                            wrapper.writeString(textureProperty.getName());
                            wrapper.writeString(textureProperty.getValue());
                            wrapper.writeOptional(textureProperty.getSignature(), PacketWrapper::writeString);
                        });
                        writeVarInt(data.gameMode.ordinal());
                        writeVarInt(data.ping);
                        if (data.displayName != null) {
                            writeBoolean(true);
                            writeComponent(data.displayName);
                        } else {
                            writeBoolean(false);
                        }
                        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
                            writeOptional(data.getSignatureData(), PacketWrapper::writeSignatureData);
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
                            writeComponent(data.displayName);
                        } else {
                            writeBoolean(false);
                        }
                        break;

                    case REMOVE_PLAYER:
                        break;
                }
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerPlayerInfo wrapper) {
        action = wrapper.action;
        playerDataList = wrapper.playerDataList;
    }

    @Nullable
    public Action getAction() {
        return action;
    }

    public void setAction(@NotNull Action action) {
        this.action = action;
    }

    public List<PlayerData> getPlayerDataList() {
        return playerDataList;
    }

    public void setPlayerDataList(List<PlayerData> playerDataList) {
        this.playerDataList = playerDataList;
    }

    public static class PlayerData {
        @Nullable
        private Component displayName;
        @Nullable
        private UserProfile userProfile;
        @Nullable
        private GameMode gameMode;
        @Nullable
        private SignatureData signatureData;

        private int ping;

        public PlayerData(@Nullable Component displayName, @Nullable UserProfile userProfile, @Nullable GameMode gameMode, @Nullable SignatureData signatureData, int ping) {
            this.displayName = displayName;
            this.userProfile = userProfile;
            this.gameMode = gameMode;
            this.signatureData = signatureData;
            this.ping = ping;
        }

        public PlayerData(@Nullable Component displayName, @Nullable UserProfile userProfile, @Nullable GameMode gameMode, int ping) {
            this(displayName, userProfile, gameMode, null, ping);
        }

        public UserProfile getUserProfile() {
            return userProfile;
        }

        public void setUserProfile(UserProfile userProfile) {
            this.userProfile = userProfile;
        }

        public SignatureData getSignatureData() {
            return signatureData;
        }

        public void setSignatureData(SignatureData signatureData) {
            this.signatureData = signatureData;
        }

        @Nullable
        public UserProfile getUser() {
            return userProfile;
        }

        public void setUser(@Nullable UserProfile userProfile) {
            this.userProfile = userProfile;
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
        public Component getDisplayName() {
            return displayName;
        }

        public void setDisplayName(@Nullable Component displayName) {
            this.displayName = displayName;
        }
    }
}
