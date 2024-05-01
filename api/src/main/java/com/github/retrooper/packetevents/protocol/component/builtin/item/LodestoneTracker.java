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

import com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LodestoneTracker {

    private @Nullable WorldBlockPosition target;
    private boolean tracked;

    public LodestoneTracker(@Nullable WorldBlockPosition target, boolean tracked) {
        this.target = target;
        this.tracked = tracked;
    }

    public static LodestoneTracker read(PacketWrapper<?> wrapper) {
        WorldBlockPosition target = wrapper.readOptional(PacketWrapper::readWorldBlockPosition);
        boolean tracked = wrapper.readBoolean();
        return new LodestoneTracker(target, tracked);
    }

    public static void write(PacketWrapper<?> wrapper, LodestoneTracker tracker) {
        wrapper.writeOptional(tracker.target, PacketWrapper::writeWorldBlockPosition);
        wrapper.writeBoolean(tracker.tracked);
    }

    public @Nullable WorldBlockPosition getTarget() {
        return this.target;
    }

    public void setTarget(@Nullable WorldBlockPosition target) {
        this.target = target;
    }

    public boolean isTracked() {
        return this.tracked;
    }

    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LodestoneTracker)) return false;
        LodestoneTracker that = (LodestoneTracker) obj;
        if (this.tracked != that.tracked) return false;
        return Objects.equals(this.target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.target, this.tracked);
    }
}
