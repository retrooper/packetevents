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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class ItemWeapon {

    private int itemDamagePerAttack;
    private float disableBlockingForSeconds;

    public ItemWeapon(int itemDamagePerAttack, float disableBlockingForSeconds) {
        this.itemDamagePerAttack = itemDamagePerAttack;
        this.disableBlockingForSeconds = disableBlockingForSeconds;
    }

    public static ItemWeapon read(PacketWrapper<?> wrapper) {
        int itemDamagePerAttack = wrapper.readVarInt();
        float disableBlockingForSeconds = wrapper.readFloat();
        return new ItemWeapon(itemDamagePerAttack, disableBlockingForSeconds);
    }

    public static void write(PacketWrapper<?> wrapper, ItemWeapon weapon) {
        wrapper.writeVarInt(weapon.itemDamagePerAttack);
        wrapper.writeFloat(weapon.disableBlockingForSeconds);
    }

    public int getItemDamagePerAttack() {
        return this.itemDamagePerAttack;
    }

    public void setItemDamagePerAttack(int itemDamagePerAttack) {
        this.itemDamagePerAttack = itemDamagePerAttack;
    }

    public float getDisableBlockingForSeconds() {
        return this.disableBlockingForSeconds;
    }

    public void setDisableBlockingForSeconds(float disableBlockingForSeconds) {
        this.disableBlockingForSeconds = disableBlockingForSeconds;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemWeapon)) return false;
        ItemWeapon that = (ItemWeapon) obj;
        if (this.itemDamagePerAttack != that.itemDamagePerAttack) return false;
        return Float.compare(that.disableBlockingForSeconds, this.disableBlockingForSeconds) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.itemDamagePerAttack, this.disableBlockingForSeconds);
    }
}
