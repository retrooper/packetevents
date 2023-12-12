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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerChangeGameState extends PacketWrapper<WrapperPlayServerChangeGameState> {
    private Reason reason;
    private float value;

    public WrapperPlayServerChangeGameState(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerChangeGameState(int reason, float value) {
        super(PacketType.Play.Server.CHANGE_GAME_STATE);
        this.reason = Reason.VALUES[reason];
        this.value = value;
    }

    public WrapperPlayServerChangeGameState(Reason reason, float value) {
        super(PacketType.Play.Server.CHANGE_GAME_STATE);
        this.reason = reason;
        this.value = value;
    }

    @Override
    public void read() {
        reason = Reason.getById(readUnsignedByte());
        value = readFloat();
    }

    @Override
    public void write() {
        writeByte(reason.ordinal());
        writeFloat(value);
    }

    @Override
    public void copy(WrapperPlayServerChangeGameState wrapper) {
        reason = wrapper.reason;
        value = wrapper.value;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public enum Reason {
        NO_RESPAWN_BLOCK_AVAILABLE,
        END_RAINING,
        BEGIN_RAINING,
        CHANGE_GAME_MODE,
        WIN_GAME,
        DEMO_EVENT,
        ARROW_HIT_PLAYER,
        RAIN_LEVEL_CHANGE,
        THUNDER_LEVEL_CHANGE,
        PLAY_PUFFER_FISH_STING_SOUND,
        PLAY_ELDER_GUARDIAN_MOB_APPEARANCE,
        ENABLE_RESPAWN_SCREEN,
        LIMITED_CRAFTING,
        START_LOADING_CHUNKS;

        private static final Reason[] VALUES = values();

        public static Reason getById(int index) {
            return VALUES[index];
        }
    }
}
