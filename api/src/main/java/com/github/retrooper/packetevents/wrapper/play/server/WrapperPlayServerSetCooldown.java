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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

public class WrapperPlayServerSetCooldown extends PacketWrapper<WrapperPlayServerSetCooldown> {

    // changed in 1.21.2
    private ResourceLocation cooldownGroup;
    private int cooldownTicks;

    public WrapperPlayServerSetCooldown(PacketSendEvent event) {
        super(event);
    }

    @ApiStatus.Obsolete // since 1.21.2
    public WrapperPlayServerSetCooldown(ItemType item, int cooldownTicks) {
        this(item.getName(), cooldownTicks);
    }

    /**
     * Note: only supporter since Minecraft 1.21.2.
     * This will error if used on versions below and the specified cooldown group
     * is not an item cooldown group.
     */
    public WrapperPlayServerSetCooldown(ResourceLocation cooldownGroup, int cooldownTicks) {
        super(PacketType.Play.Server.SET_COOLDOWN);
        this.cooldownGroup = cooldownGroup;
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public void read() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.cooldownGroup = this.readIdentifier();
        } else {
            ItemType item = this.readMappedEntity(ItemTypes.getRegistry());
            this.cooldownGroup = item.getName();
        }
        this.cooldownTicks = this.readVarInt();
    }

    @Override
    public void write() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.writeIdentifier(this.cooldownGroup);
        } else {
            this.writeMappedEntity(this.getItem());
        }
        this.writeVarInt(this.cooldownTicks);
    }

    @Override
    public void copy(WrapperPlayServerSetCooldown wrapper) {
        this.cooldownGroup = wrapper.cooldownGroup;
        this.cooldownTicks = wrapper.cooldownTicks;
    }

    public ResourceLocation getCooldownGroup() {
        return this.cooldownGroup;
    }

    public void setCooldownGroup(ResourceLocation cooldownGroup) {
        this.cooldownGroup = cooldownGroup;
    }

    @ApiStatus.Obsolete // since 1.21.2
    public ItemType getItem() {
        ItemType item = ItemTypes.getByName(this.cooldownGroup.toString());
        if (item == null) {
            throw new IllegalStateException("Can't get legacy cooldown item for cooldown group " + this.cooldownGroup);
        }
        return item;
    }

    @ApiStatus.Obsolete // since 1.21.2
    public void setItem(ItemType item) {
        this.cooldownGroup = item.getName();
    }

    public int getCooldownTicks() {
        return this.cooldownTicks;
    }

    public void setCooldownTicks(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }
}
