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

package io.github.retrooper.packetevents.wrapper.play.server;

import io.github.retrooper.packetevents.event.impl.PacketSendEvent;
import io.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.protocol.data.player.GameMode;
import io.github.retrooper.packetevents.protocol.data.player.WrappedGameProfile;
import io.github.retrooper.packetevents.protocol.packettype.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    public WrapperPlayServerPlayerInfo() {
        super(PacketType.Play.Server.PLAYER_INFO);
        net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo pi0;
        net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo pi1;
        net.minecraft.server.v1_9_R1.PacketPlayOutPlayerInfo pi2;
        net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo pi3;
        net.minecraft.server.v1_11_R1.PacketPlayOutPlayerInfo pi4;
        net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo pi5;
        net.minecraft.server.v1_13_R2.PacketPlayOutPlayerInfo pi6;
        net.minecraft.server.v1_14_R1.PacketPlayOutPlayerInfo pi7;
        net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo pi8;
        net.minecraft.server.v1_16_R2.PacketPlayOutPlayerInfo pi9;
        net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo pi10;
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
            }
            else {
                action = Action.REMOVE_PLAYER;
            }
        }
        else {
            action = Action.VALUES[readVarInt()];
            int playerDataCount = readVarInt();
            playerDataList = new ArrayList<>(playerDataCount);
            for (int i = 0; i < playerDataCount; i++) {

            }
        }
    }

    @Override
    public void readData(WrapperPlayServerPlayerInfo wrapper) {

    }

    @Override
    public void writeData() {

    }


    public static class PlayerData {
        private String username;
        private WrappedGameProfile gameProfile;
        private GameMode gameMode;
        private int ping;

        public PlayerData(@Nullable String username, WrappedGameProfile gameProfile, GameMode gameMode, int ping) {
            this.username = username;
            this.gameProfile = gameProfile;
            this.gameMode = gameMode;
            this.ping = ping;
        }

        public PlayerData(@Nullable String username, WrappedGameProfile gameProfile, org.bukkit.GameMode gameMode, int ping) {
            this.username = username;
            this.gameProfile = gameProfile;
            this.gameMode = GameMode.valueOf(gameMode.name());
            this.ping = ping;
        }

        public WrappedGameProfile getGameProfile() {
            return gameProfile;
        }

        public void setGameProfile(WrappedGameProfile gameProfile) {
            this.gameProfile = gameProfile;
        }

        public GameMode getGameMode() {
            return gameMode;
        }

        public void setGameMode(GameMode gameMode) {
            this.gameMode = gameMode;
        }

        public int getPing() {
            return ping;
        }

        public void setPing(int ping) {
            this.ping = ping;
        }

        @Nullable
        public String getUsername() {
            return username;
        }

        public void setUsername(@Nullable String username) {
            this.username = username;
        }
    }
}
