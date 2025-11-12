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

package com.github.retrooper.packetevents.protocol.advancements;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public final class AdvancementProgress {

    private Map<String, CriterionProgress> criteria;

    public AdvancementProgress(Map<String, CriterionProgress> criteria) {
        this.criteria = criteria;
    }

    public static AdvancementProgress read(PacketWrapper<?> wrapper) {
        Map<String, CriterionProgress> criteria = wrapper.readMap(PacketWrapper::readString, CriterionProgress::read);
        return new AdvancementProgress(criteria);
    }

    public static void write(PacketWrapper<?> wrapper, AdvancementProgress progress) {
        wrapper.writeMap(progress.getCriteria(), PacketWrapper::writeString, CriterionProgress::write);
    }

    public Map<String, CriterionProgress> getCriteria() {
        return this.criteria;
    }

    public void setCriteria(Map<String, CriterionProgress> criteria) {
        this.criteria = criteria;
    }

    public static final class CriterionProgress {

        private @Nullable Long obtainedTimestamp;

        public CriterionProgress(@Nullable Long obtainedTimestamp) {
            this.obtainedTimestamp = obtainedTimestamp;
        }

        public static CriterionProgress read(PacketWrapper<?> wrapper) {
            return new CriterionProgress(wrapper.readOptional(PacketWrapper::readLong));
        }

        public static void write(PacketWrapper<?> wrapper, CriterionProgress progress) {
            wrapper.writeOptional(progress.obtainedTimestamp, PacketWrapper::writeLong);
        }

        public @Nullable Long getObtainedTimestamp() {
            return this.obtainedTimestamp;
        }

        public void setObtainedTimestamp(@Nullable Long obtainedTimestamp) {
            this.obtainedTimestamp = obtainedTimestamp;
        }
    }
}
