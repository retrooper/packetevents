/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.entity.EntityPositionData;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerEntityPositionSync extends PacketWrapper<WrapperPlayServerEntityPositionSync> {

    private int id;
    private EntityPositionData values;
    private boolean onGround;

    public WrapperPlayServerEntityPositionSync(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerEntityPositionSync(int id, EntityPositionData values, boolean onGround) {
        super(PacketType.Play.Server.ENTITY_POSITION_SYNC);
        this.id = id;
        this.values = values;
        this.onGround = onGround;
    }

    @Override
    public void read() {
        this.id = this.readVarInt();
        this.values = EntityPositionData.read(this);
        this.onGround = this.readBoolean();
    }

    @Override
    public void write() {
        this.writeVarInt(this.id);
        EntityPositionData.write(this, this.values);
        this.writeBoolean(this.onGround);
    }

    @Override
    public void copy(WrapperPlayServerEntityPositionSync wrapper) {
        this.id = wrapper.id;
        this.values = wrapper.values;
        this.onGround = wrapper.onGround;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EntityPositionData getValues() {
        return this.values;
    }

    public void setValues(EntityPositionData values) {
        this.values = values;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
