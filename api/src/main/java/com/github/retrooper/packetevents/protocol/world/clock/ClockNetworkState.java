/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.clock;
// Created by booky10 in packetevents (21:30 20.02.2026)

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public final class ClockNetworkState {

    private final long totalTicks;
    /**
     * @versions 26.1+
     */
    private final float partialTick;
    /**
     * Either 0 or 1 for versions older than 26.1.
     */
    private final float rate;

    @ApiStatus.Obsolete
    public ClockNetworkState(long totalTicks, boolean paused) {
        this(totalTicks, 0f, paused ? 0f : 1f);
    }

    public ClockNetworkState(long totalTicks, float partialTick, float rate) {
        this.totalTicks = totalTicks;
        this.partialTick = partialTick;
        this.rate = rate;
    }

    public static ClockNetworkState read(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_1)) {
            long totalTicks = wrapper.readVarLong();
            float partialTick = wrapper.readFloat();
            float rate = wrapper.readFloat();
            return new ClockNetworkState(totalTicks, partialTick, rate);
        }
        long totalTicks = wrapper.readLong();
        boolean tickTime = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)
                ? wrapper.readBoolean() : totalTicks >= 0L;
        return new ClockNetworkState(totalTicks, !tickTime);
    }

    public static void write(PacketWrapper<?> wrapper, ClockNetworkState state) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_26_1)) {
            wrapper.writeVarLong(state.totalTicks);
            wrapper.writeFloat(state.partialTick);
            wrapper.writeFloat(state.rate);
        } else {
            wrapper.writeLong(state.totalTicks);
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
                wrapper.writeBoolean(!state.isPaused());
            }
        }
    }

    public long getTotalTicks() {
        return this.totalTicks;
    }

    public ClockNetworkState withTotalTicks(long totalTicks) {
        return new ClockNetworkState(totalTicks, this.partialTick, this.rate);
    }

    public float getPartialTick() {
        return this.partialTick;
    }

    public ClockNetworkState withPartialTick(float partialTick) {
        return new ClockNetworkState(this.totalTicks, partialTick, this.rate);
    }

    public float getRate() {
        return this.rate;
    }

    public ClockNetworkState withRate(float rate) {
        return new ClockNetworkState(this.totalTicks, this.partialTick, rate);
    }

    public boolean isPaused() {
        return Float.compare(this.rate, 0f) == 0;
    }

    public ClockNetworkState withPaused(boolean paused) {
        return new ClockNetworkState(this.totalTicks, this.partialTick, paused ? 0f : 1f);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ClockNetworkState)) return false;
        ClockNetworkState that = (ClockNetworkState) obj;
        if (this.totalTicks != that.totalTicks) return false;
        if (Float.compare(that.partialTick, this.partialTick) != 0) return false;
        return Float.compare(that.rate, this.rate) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.totalTicks, this.partialTick, this.rate);
    }
}
