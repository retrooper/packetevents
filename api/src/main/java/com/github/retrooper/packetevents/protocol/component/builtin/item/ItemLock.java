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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class ItemLock {

    private static final String FALLBACK_LOCK_STRING = "packetevents$invalid_lock";

    /**
     * @versions 1.20.5-1.21.1
     */
    @ApiStatus.Obsolete
    private String code;
    /**
     * @versions 1.21.2+
     */
    private NBTCompound predicate;

    /**
     * @versions 1.20.5-1.21.1
     */
    @ApiStatus.Obsolete
    public ItemLock(String code) {
        this.code = code;
        this.predicate = new NBTCompound();
    }

    public ItemLock(NBTCompound predicate) {
        this.code = FALLBACK_LOCK_STRING;
        this.predicate = predicate;
    }

    public static ItemLock read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            return new ItemLock(wrapper.readNBT());
        }
        NBTString codeTag = (NBTString) wrapper.readNBTRaw();
        return new ItemLock(codeTag.getValue());
    }

    public static void write(PacketWrapper<?> wrapper, ItemLock lock) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            wrapper.writeNBT(lock.predicate);
        } else {
            wrapper.writeNBTRaw(new NBTString(lock.code));
        }
    }

    /**
     * @versions 1.20.5-1.21.1
     */
    @ApiStatus.Obsolete
    public String getCode() {
        return this.code;
    }

    /**
     * @versions 1.20.5-1.21.1
     */
    @ApiStatus.Obsolete
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @versions 1.21.2+
     */
    public NBTCompound getPredicate() {
        return this.predicate;
    }

    /**
     * @versions 1.21.2+
     */
    public void setPredicate(NBTCompound predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        ItemLock itemLock = (ItemLock) obj;
        if (!this.code.equals(itemLock.code)) return false;
        return this.predicate.equals(itemLock.predicate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.code, this.predicate);
    }
}
