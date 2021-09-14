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

package io.github.retrooper.packetevents.wrapper.game.client;

import io.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import io.github.retrooper.packetevents.manager.player.ClientVersion;
import io.github.retrooper.packetevents.protocol.PacketType;
import io.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperGameClientSetBeaconEffect extends PacketWrapper<WrapperGameClientSetBeaconEffect> {
    private int primaryEffect;
    private int secondaryEffect;

    public WrapperGameClientSetBeaconEffect(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperGameClientSetBeaconEffect(int primaryEffect, int secondaryEffect) {
        super(PacketType.Game.Client.SET_BEACON_EFFECT.getID());
        this.primaryEffect = primaryEffect;
        this.secondaryEffect = secondaryEffect;
    }

    @Override
    public void readData() {
        this.primaryEffect = readVarInt();
        this.secondaryEffect = readVarInt();
    }

    @Override
    public void readData(WrapperGameClientSetBeaconEffect wrapper) {
        this.primaryEffect = wrapper.primaryEffect;
        this.secondaryEffect = wrapper.secondaryEffect;
    }

    @Override
    public void writeData() {
        writeVarInt(primaryEffect);
        writeVarInt(secondaryEffect);
    }

    public int getPrimaryEffect() {
        return primaryEffect;
    }

    public void setPrimaryEffect(int primaryEffect) {
        this.primaryEffect = primaryEffect;
    }

    public int getSecondaryEffect() {
        return secondaryEffect;
    }

    public void setSecondaryEffect(int secondaryEffect) {
        this.secondaryEffect = secondaryEffect;
    }
}
