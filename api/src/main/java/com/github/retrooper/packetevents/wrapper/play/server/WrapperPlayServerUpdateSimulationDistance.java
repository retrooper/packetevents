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

public class WrapperPlayServerUpdateSimulationDistance extends PacketWrapper<WrapperPlayServerUpdateSimulationDistance> {
    private int simulationDistance;

    public WrapperPlayServerUpdateSimulationDistance(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUpdateSimulationDistance(int simulationDistance) {
        super(PacketType.Play.Server.UPDATE_SIMULATION_DISTANCE);
        this.simulationDistance = simulationDistance;
    }

    @Override
    public void read() {
        simulationDistance = readVarInt();
    }

    @Override
    public void write() {
        writeVarInt(simulationDistance);
    }

    @Override
    public void copy(WrapperPlayServerUpdateSimulationDistance wrapper) {
        simulationDistance = wrapper.simulationDistance;
    }

    public int getSimulationDistance() {
        return simulationDistance;
    }

    public void setSimulationDistance(int simulationDistance) {
        this.simulationDistance = simulationDistance;
    }
}
