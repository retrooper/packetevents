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

package com.github.retrooper.packetevents.protocol.world.attributes.timelines;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttribute;
import com.github.retrooper.packetevents.protocol.world.clock.WorldClock;
import com.github.retrooper.packetevents.protocol.world.clock.WorldClocks;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @versions 1.21.11+
 */
@NullMarked
public class StaticTimeline extends AbstractMappedEntity implements Timeline {

    /**
     * @versions 26.1+
     */
    private final WorldClock clock;
    private final @Nullable Integer periodTicks;
    private final Map<EnvironmentAttribute<?>, TimelineTrack<?, ?>> tracks;
    /**
     * @versions 26.1+
     */
    private final Map<ResourceLocation, TimeMarkerInfo> timeMarkers;

    /**
     * @versions 1.21.11
     */
    @ApiStatus.Obsolete
    public StaticTimeline(@Nullable Integer periodTicks, Map<EnvironmentAttribute<?>, TimelineTrack<?, ?>> tracks) {
        this(null, WorldClocks.OVERWORLD, periodTicks, tracks, Collections.emptyMap());
    }

    /**
     * @versions 26.1+
     */
    public StaticTimeline(
            WorldClock clock, @Nullable Integer periodTicks,
            Map<EnvironmentAttribute<?>, TimelineTrack<?, ?>> tracks,
            Map<ResourceLocation, TimeMarkerInfo> timeMarkers
    ) {
        this(null, clock, periodTicks, tracks, timeMarkers);
    }

    @ApiStatus.Internal
    public StaticTimeline(
            @Nullable TypesBuilderData data,
            WorldClock clock, @Nullable Integer periodTicks,
            Map<EnvironmentAttribute<?>, TimelineTrack<?, ?>> tracks,
            Map<ResourceLocation, TimeMarkerInfo> timeMarkers
    ) {
        super(data);
        this.clock = clock;
        this.periodTicks = periodTicks;
        this.tracks = Collections.unmodifiableMap(tracks);
        this.timeMarkers = Collections.unmodifiableMap(timeMarkers);
    }

    @Override
    public Timeline copy(@Nullable TypesBuilderData newData) {
        return new StaticTimeline(newData, this.clock, this.periodTicks, this.tracks, this.timeMarkers);
    }

    @Override
    public WorldClock getClock() {
        return this.clock;
    }

    @Override
    public @Nullable Integer getPeriodTicks() {
        return this.periodTicks;
    }

    @Override
    public Map<EnvironmentAttribute<?>, TimelineTrack<?, ?>> getTracks() {
        return this.tracks;
    }

    @Override
    public Map<ResourceLocation, TimeMarkerInfo> getTimeMarkers() {
        return this.timeMarkers;
    }

    @Override
    public boolean deepEquals(@Nullable Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        StaticTimeline that = (StaticTimeline) obj;
        if (!this.clock.equals(that.clock)) return false;
        if (!Objects.equals(this.periodTicks, that.periodTicks)) return false;
        if (!this.tracks.equals(that.tracks)) return false;
        return this.timeMarkers.equals(that.timeMarkers);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(this.clock, this.periodTicks, this.tracks, this.timeMarkers);
    }
}
