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

package io.github.retrooper.packetevents.packetwrappers.play.out.login;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.api.helper.WrappedPacketEntityAbstraction;
import io.github.retrooper.packetevents.utils.player.GameMode;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

//TODO Make sendable and finish
public class WrappedPacketOutLogin extends WrappedPacketEntityAbstraction {
    public WrappedPacketOutLogin(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {

    }

    public boolean isHardcore() {
        if (packet != null) {
            return readBoolean(0);
        } else {
            //TODO finish
            return false;
        }
    }

    public void setHardcore(boolean value) {
        if (packet != null) {
            writeBoolean(0, value);
        } else {
            //TODO finish
        }
    }

    public GameMode getGameMode() {
        if (packet != null) {
            return readGameMode(0);
        } else {
            //TODO finish
            return null;
        }
    }

    public void setGameMode(GameMode gameMode) {
        if (packet != null) {
            writeGameMode(0, gameMode);
        } else {
            //TODO finish
        }
    }

    public int getMaxPlayers() {
        if (packet != null) {
            if (version.isNewerThan(ServerVersion.v_1_13_1)) {
                return readInt(1);
            } else {
                return readInt(2);
            }
        } else {
            //TODO Finish
            return -1;
        }
    }
}
