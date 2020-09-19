/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetwrappers.out.playerinfo;

import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.List;

class WrappedPacketOutPlayerInfo extends WrappedPacket implements SendableWrapper {
//TODO load this wrapper

    private PlayerInfoAction action;
    private List<PlayerInfoData> playerInfoDataList = new ArrayList<>();

    private static byte mode = 0; //0 = 1.7.10

    public static void load() {

    }

    public WrappedPacketOutPlayerInfo(Object packet) {
        super(packet);
    }

    public WrappedPacketOutPlayerInfo(PlayerInfoAction action, Object gameProfile,
                                      int ping, GameMode gameMode, String displayName) {
        this.action = action;
        playerInfoDataList.add(new PlayerInfoData(gameProfile, ping, gameMode, displayName));
    }

    @Override
    protected void setup() {
        switch (mode) {
            case 0:
                int actionInteger= readInt(0);
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    @Override
    public Object asNMSPacket() {
        return null;
    }

    public PlayerInfoAction getAction() {
        return action;
    }

    public List<PlayerInfoData> getPlayerInfoData() {
        return playerInfoDataList;
    }

    public enum PlayerInfoAction {
        ADD_PLAYER, UPDATE_GAME_MODE, UPDATE_LATENCY, UPDATE_DISPLAY_NAME, REMOVE_PLAYER;
    }

    public class PlayerInfoData {
        private final int ping;
        private final GameMode gameMode;
        private final Object gameProfile;
        private final String displayName;

        public PlayerInfoData(Object gameProfile, int ping,
                              GameMode gameMode, String displayName) {
            this.ping = ping;
            this.gameMode = gameMode;
            this.gameProfile = gameProfile;
            this.displayName = displayName;
        }

        public int getPing() {
            return ping;
        }

        public GameMode getGameMode() {
            return gameMode;
        }

        public Object getGameProfile() {
            return gameProfile;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
