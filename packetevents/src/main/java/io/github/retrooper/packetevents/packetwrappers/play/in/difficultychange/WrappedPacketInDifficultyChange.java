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

package io.github.retrooper.packetevents.packetwrappers.play.in.difficultychange;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.world.Difficulty;

/**
 * Wrapper for the DifficultyChange packet.
 *
 * @author Tecnio
 * @since 1.8
 */
public final class WrappedPacketInDifficultyChange extends WrappedPacket {
    public WrappedPacketInDifficultyChange(NMSPacket packet) {
        super(packet);
    }


    public Difficulty getDifficulty() {
        return readDifficulty(0);
    }

    public void setDifficulty(Difficulty difficulty) {
        writeDifficulty(0, difficulty);
    }
}
