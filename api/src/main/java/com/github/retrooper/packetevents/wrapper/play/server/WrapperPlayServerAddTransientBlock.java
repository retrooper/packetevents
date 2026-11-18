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
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

/**
 * Mojang name: ClientboundAddTransientBlockPacket
 *
 * @versions 26.3+
 */
public class WrapperPlayServerAddTransientBlock extends PacketWrapper<WrapperPlayServerAddTransientBlock> {

    private Vector3i position;
    private WrappedBlockState state;

    public WrapperPlayServerAddTransientBlock(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerAddTransientBlock(Vector3i position, WrappedBlockState state) {
        super(PacketType.Play.Server.ADD_TRANSIENT_BLOCK);
        this.position = position;
        this.state = state;
    }

    @Override
    public void read() {
        this.position = this.readBlockPosition();
        this.state = WrappedBlockState.read(this);
    }

    @Override
    public void write() {
        this.writeBlockPosition(this.position);
        WrappedBlockState.write(this, this.state);
    }

    @Override
    public void copy(WrapperPlayServerAddTransientBlock wrapper) {
        this.position = wrapper.position;
        this.state = wrapper.state;
    }

    public Vector3i getPosition() {
        return this.position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public WrappedBlockState getState() {
        return this.state;
    }

    public void setState(WrappedBlockState state) {
        this.state = state;
    }
}
