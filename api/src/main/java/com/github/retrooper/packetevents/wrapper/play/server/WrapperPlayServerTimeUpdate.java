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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.clock.ClockNetworkState;
import com.github.retrooper.packetevents.protocol.world.clock.WorldClock;
import com.github.retrooper.packetevents.protocol.world.clock.WorldClocks;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Mojang name: ClientboundSetTimePacket
 */
@NullMarked
public class WrapperPlayServerTimeUpdate extends PacketWrapper<WrapperPlayServerTimeUpdate> {

    private static final ClockNetworkState FALLBACK_CLOCK = new ClockNetworkState(0L, true);

    private long gameTime;
    /**
     * Changed with 26.1+.
     */
    private @MonotonicNonNull Map<WorldClock, ClockNetworkState> clockUpdates;

    public WrapperPlayServerTimeUpdate(PacketSendEvent event) {
        super(event);
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    public WrapperPlayServerTimeUpdate(long gameTime, long timeOfDay) {
        this(gameTime, timeOfDay, timeOfDay >= 0L);
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    public WrapperPlayServerTimeUpdate(long gameTime, long timeOfDay, boolean tickTime) {
        this(gameTime, Collections.emptyMap());
        this.clockUpdates.put(WorldClocks.OVERWORLD, new ClockNetworkState(timeOfDay, tickTime));
    }

    /**
     * @versions 26.1+
     */
    public WrapperPlayServerTimeUpdate(long gameTime, Map<WorldClock, ClockNetworkState> clockUpdates) {
        super(PacketType.Play.Server.TIME_UPDATE);
        this.gameTime = gameTime;
        this.clockUpdates = new HashMap<>(clockUpdates);
    }

    @Override
    public void read() {
        this.gameTime = this.readLong();
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_1)) {
            this.clockUpdates = this.readMap(WorldClock::read, ClockNetworkState::read);
        } else {
            this.clockUpdates = new HashMap<>(1);
            this.clockUpdates.put(WorldClocks.OVERWORLD, ClockNetworkState.read(this));
        }
    }

    @Override
    public void write() {
        this.writeLong(this.gameTime);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_1)) {
            this.writeMap(this.clockUpdates, WorldClock::write, ClockNetworkState::write);
        } else {
            ClockNetworkState state = this.clockUpdates.getOrDefault(WorldClocks.OVERWORLD, FALLBACK_CLOCK);
            ClockNetworkState.write(this, state);
        }
    }

    @Override
    public void copy(WrapperPlayServerTimeUpdate wrapper) {
        this.gameTime = wrapper.gameTime;
        this.clockUpdates = new HashMap<>(wrapper.clockUpdates);
    }

    public long getWorldAge() {
        return this.gameTime;
    }

    public void setWorldAge(long gameTime) {
        this.gameTime = gameTime;
    }

    public Map<WorldClock, ClockNetworkState> getClockUpdates() {
        return this.clockUpdates;
    }

    public void setClockUpdates(Map<WorldClock, ClockNetworkState> clockUpdates) {
        this.clockUpdates = clockUpdates;
    }

    public @Nullable ClockNetworkState getClockState(WorldClock clock) {
        return this.clockUpdates.get(this.replaceRegistry(WorldClocks.getRegistry(), clock));
    }

    public void setClockState(WorldClock clock, ClockNetworkState state) {
        this.clockUpdates.put(this.replaceRegistry(WorldClocks.getRegistry(), clock), state);
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    public ClockNetworkState getClockState() {
        WorldClock clock = this.replaceRegistry(WorldClocks.getRegistry(), WorldClocks.OVERWORLD);
        return this.clockUpdates.computeIfAbsent(clock, __ -> FALLBACK_CLOCK);
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    public void setClockState(ClockNetworkState state) {
        WorldClock clock = this.replaceRegistry(WorldClocks.getRegistry(), WorldClocks.OVERWORLD);
        this.clockUpdates.put(clock, state);
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    public long getTimeOfDay() {
        return this.getClockState().getTotalTicks();
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    public void setTimeOfDay(long timeOfDay) {
        this.setClockState(this.getClockState().withTotalTicks(timeOfDay));
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    public boolean isTickTime() {
        return !this.getClockState().isPaused();
    }

    /**
     * @versions -1.21.11
     */
    @ApiStatus.Obsolete
    public void setTickTime(boolean tickTime) {
        this.setClockState(this.getClockState().withPaused(!tickTime));
    }
}
