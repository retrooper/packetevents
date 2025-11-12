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

public class WrapperPlayServerOpenHorseWindow extends PacketWrapper<WrapperPlayServerOpenHorseWindow> {
    private int windowId;
    private int slotCount;
    private int entityId;

    public WrapperPlayServerOpenHorseWindow(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerOpenHorseWindow(int windowId, int slotCount, int entityId) {
        super(PacketType.Play.Server.OPEN_HORSE_WINDOW);
        this.windowId = windowId;
        this.slotCount = slotCount;
        this.entityId = entityId;
    }

    @Override
    public void read() {
        this.windowId = this.readContainerId();
        this.slotCount = readVarInt();
        this.entityId = readInt();
    }

    @Override
    public void write() {
        this.writeContainerId(windowId);
        writeVarInt(slotCount);
        writeInt(entityId);
    }

    @Override
    public void copy(WrapperPlayServerOpenHorseWindow other) {
        this.windowId = other.windowId;
        this.slotCount = other.slotCount;
        this.entityId = other.entityId;
    }

    public int getWindowId() {
        return windowId;
    }

    public void setWindowId(int windowId) {
        this.windowId = windowId;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        this.slotCount = slotCount;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }
}
