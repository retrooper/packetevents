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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class Advancement {

    private @Nullable ResourceLocation parent;
    private @Nullable AdvancementDisplay display;
    /**
     * Removed with 1.20.2
     */
    private List<String> criteria;
    private List<List<String>> requirements;
    /**
     * Added with 1.20
     */
    private boolean sendsTelemetryData;

    public Advancement(
            @Nullable ResourceLocation parent,
            @Nullable AdvancementDisplay display,
            List<List<String>> requirements,
            boolean sendsTelemetryData
    ) {
        this(parent, display, Collections.emptyList(), requirements, sendsTelemetryData);
    }

    @ApiStatus.Obsolete
    public Advancement(
            @Nullable ResourceLocation parent,
            @Nullable AdvancementDisplay display,
            List<String> criteria,
            List<List<String>> requirements,
            boolean sendsTelemetryData
    ) {
        this.parent = parent;
        this.display = display;
        this.criteria = criteria;
        this.requirements = requirements;
        this.sendsTelemetryData = sendsTelemetryData;
    }

    public static Advancement read(PacketWrapper<?> wrapper) {
        ResourceLocation parentId = wrapper.readOptional(ResourceLocation::read);
        AdvancementDisplay display = wrapper.readOptional(AdvancementDisplay::read);
        List<String> criteria = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_20_2)
                ? wrapper.readList(PacketWrapper::readString) : null;
        List<List<String>> requirements = wrapper.readList(
                ew -> wrapper.readList(PacketWrapper::readString));
        boolean sendsTelemetryData = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20)
                && wrapper.readBoolean();
        return new Advancement(parentId, display, criteria, requirements, sendsTelemetryData);
    }

    public static void write(PacketWrapper<?> wrapper, Advancement advancement) {
        wrapper.writeOptional(advancement.parent, ResourceLocation::write);
        wrapper.writeOptional(advancement.display, AdvancementDisplay::write);
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_20_2)) {
            wrapper.writeList(advancement.criteria, PacketWrapper::writeString);
        }
        wrapper.writeList(advancement.getRequirements(), (ew, anyList) ->
                ew.writeList(anyList, PacketWrapper::writeString));
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20)) {
            wrapper.writeBoolean(advancement.sendsTelemetryData);
        }
    }

    public @Nullable ResourceLocation getParent() {
        return this.parent;
    }

    public void setParent(@Nullable ResourceLocation parent) {
        this.parent = parent;
    }

    public @Nullable AdvancementDisplay getDisplay() {
        return this.display;
    }

    public void setDisplay(@Nullable AdvancementDisplay display) {
        this.display = display;
    }

    /**
     * Removed with 1.20.2
     */
    public List<String> getCriteria() {
        return this.criteria;
    }

    /**
     * Removed with 1.20.2
     */
    public void setCriteria(List<String> criteria) {
        this.criteria = criteria;
    }

    public List<List<String>> getRequirements() {
        return this.requirements;
    }

    public void setRequirements(List<List<String>> requirements) {
        this.requirements = requirements;
    }

    /**
     * Added with 1.20
     */
    public boolean isSendsTelemetryData() {
        return this.sendsTelemetryData;
    }

    /**
     * Added with 1.20
     */
    public void setSendsTelemetryData(boolean sendsTelemetryData) {
        this.sendsTelemetryData = sendsTelemetryData;
    }
}
