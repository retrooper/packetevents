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
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerSetCooldown extends PacketWrapper<WrapperPlayServerSetCooldown> {
    private ItemType item;
    private int cooldownTicks;

    public WrapperPlayServerSetCooldown(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSetCooldown(ItemType item, int cooldownTicks) {
        super(PacketType.Play.Server.SET_COOLDOWN);
        this.item = item;
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public void read() {
        item = ItemTypes.getById(serverVersion.toClientVersion(), readVarInt());
        cooldownTicks = readVarInt();
    }

    @Override
    public void write() {
        writeVarInt(item.getId(serverVersion.toClientVersion()));
        writeVarInt(cooldownTicks);
    }

    @Override
    public void copy(WrapperPlayServerSetCooldown wrapper) {
        item = wrapper.item;
        cooldownTicks = wrapper.cooldownTicks;
    }

    public ItemType getItem() {
        return item;
    }

    public void setItem(ItemType item) {
        this.item = item;
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    public void setCooldownTicks(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }
}
