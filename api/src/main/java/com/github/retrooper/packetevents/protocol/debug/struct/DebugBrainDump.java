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

import java.util.List;
import java.util.Set;

/**
 * @versions 1.21.9+
 */
@NullMarked
public final class DebugBrainDump {

    private final String name;
    private final String profession;
    private final int xp;
    private final float health;
    private final float maxHealth;
    private final String inventory;
    private final boolean wantsGolem;
    private final int angerLevel;
    private final List<String> activities;
    private final List<String> behaviors;
    private final List<String> memories;
    private final List<String> gossips;
    private final Set<Vector3i> pois;
    private final Set<Vector3i> potentialPois;

    public DebugBrainDump(
            String name, String profession, int xp, float health, float maxHealth,
            String inventory, boolean wantsGolem, int angerLevel, List<String> activities,
            List<String> behaviors, List<String> memories, List<String> gossips,
            Set<Vector3i> pois, Set<Vector3i> potentialPois
    ) {
        this.name = name;
        this.profession = profession;
        this.xp = xp;
        this.health = health;
        this.maxHealth = maxHealth;
        this.inventory = inventory;
        this.wantsGolem = wantsGolem;
        this.angerLevel = angerLevel;
        this.activities = activities;
        this.behaviors = behaviors;
        this.memories = memories;
        this.gossips = gossips;
        this.pois = pois;
        this.potentialPois = potentialPois;
    }

    public static DebugBrainDump read(PacketWrapper<?> wrapper) {
        String name = wrapper.readString();
        String profession = wrapper.readString();
        int xp = wrapper.readInt();
        float health = wrapper.readFloat();
        float maxHealth = wrapper.readFloat();
        String inventory = wrapper.readString();
        boolean wantsGolem = wrapper.readBoolean();
        int angerLevel = wrapper.readInt();
        List<String> activities = wrapper.readList(PacketWrapper::readString);
        List<String> behaviors = wrapper.readList(PacketWrapper::readString);
        List<String> memories = wrapper.readList(PacketWrapper::readString);
        List<String> gossips = wrapper.readList(PacketWrapper::readString);
        Set<Vector3i> pois = wrapper.readSet(PacketWrapper::readBlockPosition);
        Set<Vector3i> potentialPois = wrapper.readSet(PacketWrapper::readBlockPosition);
        return new DebugBrainDump(
                name, profession, xp, health, maxHealth, inventory, wantsGolem,
                angerLevel, activities, behaviors, memories, gossips, pois, potentialPois
        );
    }

    public static void write(PacketWrapper<?> wrapper, DebugBrainDump info) {
        wrapper.writeString(info.name);
        wrapper.writeString(info.profession);
        wrapper.writeInt(info.xp);
        wrapper.writeFloat(info.health);
        wrapper.writeFloat(info.maxHealth);
        wrapper.writeString(info.inventory);
        wrapper.writeBoolean(info.wantsGolem);
        wrapper.writeInt(info.angerLevel);
        wrapper.writeList(info.activities, PacketWrapper::writeString);
        wrapper.writeList(info.behaviors, PacketWrapper::writeString);
        wrapper.writeList(info.memories, PacketWrapper::writeString);
        wrapper.writeList(info.gossips, PacketWrapper::writeString);
        wrapper.writeSet(info.pois, PacketWrapper::writeBlockPosition);
        wrapper.writeSet(info.potentialPois, PacketWrapper::writeBlockPosition);
    }

    public String getName() {
        return this.name;
    }

    public String getProfession() {
        return this.profession;
    }

    public int getXp() {
        return this.xp;
    }

    public float getHealth() {
        return this.health;
    }

    public float getMaxHealth() {
        return this.maxHealth;
    }

    public String getInventory() {
        return this.inventory;
    }

    public boolean isWantsGolem() {
        return this.wantsGolem;
    }

    public int getAngerLevel() {
        return this.angerLevel;
    }

    public List<String> getActivities() {
        return this.activities;
    }

    public List<String> getBehaviors() {
        return this.behaviors;
    }

    public List<String> getMemories() {
        return this.memories;
    }

    public List<String> getGossips() {
        return this.gossips;
    }

    public Set<Vector3i> getPois() {
        return this.pois;
    }

    public Set<Vector3i> getPotentialPois() {
        return this.potentialPois;
    }
}
