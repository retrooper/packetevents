/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

/**
 * Added with 1.21.5
 */
public class WrapperPlayServerTestInstanceBlockStatus extends PacketWrapper<WrapperPlayServerTestInstanceBlockStatus> {

    private Component status;
    private @Nullable Vector3i size;

    public WrapperPlayServerTestInstanceBlockStatus(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTestInstanceBlockStatus(Component status, @Nullable Vector3i size) {
        super(PacketType.Play.Server.TEST_INSTANCE_BLOCK_STATUS);
        this.status = status;
        this.size = size;
    }

    @Override
    public void read() {
        this.status = this.readComponent();
        this.size = this.readOptional(Vector3i::read);
    }

    @Override
    public void write() {
        this.writeComponent(this.status);
        this.writeOptional(this.size, Vector3i::write);
    }

    @Override
    public void copy(WrapperPlayServerTestInstanceBlockStatus wrapper) {
        this.status = wrapper.status;
        this.size = wrapper.size;
    }

    public Component getStatus() {
        return this.status;
    }

    public void setStatus(Component status) {
        this.status = status;
    }

    public @Nullable Vector3i getSize() {
        return this.size;
    }

    public void setSize(@Nullable Vector3i size) {
        this.size = size;
    }
}
