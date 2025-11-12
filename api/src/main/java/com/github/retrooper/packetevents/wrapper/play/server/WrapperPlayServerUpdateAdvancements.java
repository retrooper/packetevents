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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.advancements.AdvancementHolder;
import com.github.retrooper.packetevents.protocol.advancements.AdvancementProgress;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WrapperPlayServerUpdateAdvancements extends PacketWrapper<WrapperPlayServerUpdateAdvancements> {

    private boolean reset;
    private List<AdvancementHolder> addedAdvancements;
    private Set<ResourceLocation> removedAdvancements;
    private Map<ResourceLocation, AdvancementProgress> progress;
    /**
     * Added with 1.21.5
     */
    private boolean showAdvancements;

    public WrapperPlayServerUpdateAdvancements(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUpdateAdvancements(
            boolean reset,
            List<AdvancementHolder> addedAdvancements,
            Set<ResourceLocation> removedAdvancements,
            Map<ResourceLocation, AdvancementProgress> progress,
            boolean showAdvancements
    ) {
        super(PacketType.Play.Server.UPDATE_ADVANCEMENTS);
        this.reset = reset;
        this.addedAdvancements = addedAdvancements;
        this.removedAdvancements = removedAdvancements;
        this.progress = progress;
        this.showAdvancements = showAdvancements;
    }

    @Override
    public void read() {
        this.reset = this.readBoolean();
        this.addedAdvancements = this.readList(AdvancementHolder::read);
        this.removedAdvancements = this.readCollection(LinkedHashSet::new, ResourceLocation::read);
        this.progress = this.readMap(ResourceLocation::read, AdvancementProgress::read);
        this.showAdvancements = this.serverVersion.isOlderThan(ServerVersion.V_1_21_5) || this.readBoolean();
    }

    @Override
    public void write() {
        this.writeBoolean(this.reset);
        this.writeList(this.addedAdvancements, AdvancementHolder::write);
        this.writeCollection(this.removedAdvancements, ResourceLocation::write);
        this.writeMap(this.progress, ResourceLocation::write, AdvancementProgress::write);
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
            this.writeBoolean(this.showAdvancements);
        }
    }

    @Override
    public void copy(WrapperPlayServerUpdateAdvancements wrapper) {
        this.reset = wrapper.reset;
        this.addedAdvancements = wrapper.addedAdvancements;
        this.removedAdvancements = wrapper.removedAdvancements;
        this.progress = wrapper.progress;
        this.showAdvancements = wrapper.showAdvancements;
    }

    public boolean isReset() {
        return this.reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public List<AdvancementHolder> getAddedAdvancements() {
        return this.addedAdvancements;
    }

    public void setAddedAdvancements(List<AdvancementHolder> addedAdvancements) {
        this.addedAdvancements = addedAdvancements;
    }

    public Set<ResourceLocation> getRemovedAdvancements() {
        return this.removedAdvancements;
    }

    public void setRemovedAdvancements(Set<ResourceLocation> removedAdvancements) {
        this.removedAdvancements = removedAdvancements;
    }

    public Map<ResourceLocation, AdvancementProgress> getProgress() {
        return this.progress;
    }

    public void setProgress(Map<ResourceLocation, AdvancementProgress> progress) {
        this.progress = progress;
    }

    /**
     * Added with 1.21.5
     */
    public boolean isShowAdvancements() {
        return this.showAdvancements;
    }

    /**
     * Added with 1.21.5
     */
    public void setShowAdvancements(boolean showAdvancements) {
        this.showAdvancements = showAdvancements;
    }
}
