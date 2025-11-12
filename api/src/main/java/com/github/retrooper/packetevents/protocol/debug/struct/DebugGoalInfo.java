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

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * @versions 1.21.9+
 */
@NullMarked
public final class DebugGoalInfo {

    private final List<DebugGoal> goals;

    public DebugGoalInfo(List<DebugGoal> goals) {
        this.goals = goals;
    }

    public static DebugGoalInfo read(PacketWrapper<?> wrapper) {
        List<DebugGoal> goals = wrapper.readList(DebugGoal::read);
        return new DebugGoalInfo(goals);
    }

    public static void write(PacketWrapper<?> wrapper, DebugGoalInfo info) {
        wrapper.writeList(info.goals, DebugGoal::write);
    }

    public List<DebugGoal> getGoals() {
        return this.goals;
    }

    public static final class DebugGoal {

        private final int priority;
        private final boolean running;
        private final String name;

        public DebugGoal(int priority, boolean running, String name) {
            this.priority = priority;
            this.running = running;
            this.name = name;
        }

        public static DebugGoal read(PacketWrapper<?> wrapper) {
            int priority = wrapper.readVarInt();
            boolean running = wrapper.readBoolean();
            String name = wrapper.readString(255);
            return new DebugGoal(priority, running, name);
        }

        public static void write(PacketWrapper<?> wrapper, DebugGoal goal) {
            wrapper.writeVarInt(goal.priority);
            wrapper.writeBoolean(goal.running);
            wrapper.writeString(goal.name, 255);
        }

        public int getPriority() {
            return this.priority;
        }

        public boolean isRunning() {
            return this.running;
        }

        public String getName() {
            return this.name;
        }
    }
}
