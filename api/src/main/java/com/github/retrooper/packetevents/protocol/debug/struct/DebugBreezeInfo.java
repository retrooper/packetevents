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

package com.github.retrooper.packetevents.protocol.debug.struct;

import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @versions 1.21.9+
 */
@NullMarked
public final class DebugBreezeInfo {

    private final @Nullable Integer attackTarget;
    private final @Nullable Vector3i jumpTarget;

    public DebugBreezeInfo(@Nullable Integer attackTarget, @Nullable Vector3i jumpTarget) {
        this.attackTarget = attackTarget;
        this.jumpTarget = jumpTarget;
    }

    public static DebugBreezeInfo read(PacketWrapper<?> wrapper) {
        Integer attackTarget = wrapper.readOptional(PacketWrapper::readVarInt);
        Vector3i jumpTarget = wrapper.readOptional(PacketWrapper::readBlockPosition);
        return new DebugBreezeInfo(attackTarget, jumpTarget);
    }

    public static void write(PacketWrapper<?> wrapper, DebugBreezeInfo info) {
        wrapper.writeOptional(info.attackTarget, PacketWrapper::writeVarInt);
        wrapper.writeOptional(info.jumpTarget, PacketWrapper::writeBlockPosition);
    }

    public @Nullable Integer getAttackTarget() {
        return this.attackTarget;
    }

    public @Nullable Vector3i getJumpTarget() {
        return this.jumpTarget;
    }
}
