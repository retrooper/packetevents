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

package com.github.retrooper.packetevents.protocol.world;

import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

/**
 * Added with 1.21.5
 */
public class TestInstanceData {

    private @Nullable ResourceLocation test;
    private Vector3i size;
    private StructureRotation rotation;
    private boolean ignoreEntities;
    private Status status;
    private @Nullable Component errorMessage;

    public TestInstanceData(
            @Nullable ResourceLocation test, Vector3i size, StructureRotation rotation,
            boolean ignoreEntities, Status status, @Nullable Component errorMessage
    ) {
        this.test = test;
        this.size = size;
        this.rotation = rotation;
        this.ignoreEntities = ignoreEntities;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public static TestInstanceData read(PacketWrapper<?> wrapper) {
        ResourceLocation test = wrapper.readOptional(ResourceLocation::read);
        Vector3i size = Vector3i.read(wrapper);
        StructureRotation rotation = wrapper.readEnum(StructureRotation.class);
        boolean ignoreEntities = wrapper.readBoolean();
        Status status = wrapper.readEnum(Status.class);
        Component errorMessage = wrapper.readOptional(PacketWrapper::readComponent);
        return new TestInstanceData(test, size, rotation, ignoreEntities, status, errorMessage);
    }

    public static void write(PacketWrapper<?> wrapper, TestInstanceData data) {
        wrapper.writeOptional(data.test, ResourceLocation::write);
        Vector3i.write(wrapper, data.size);
        wrapper.writeEnum(data.rotation);
        wrapper.writeBoolean(data.ignoreEntities);
        wrapper.writeEnum(data.status);
        wrapper.writeOptional(data.errorMessage, PacketWrapper::writeComponent);
    }

    public @Nullable ResourceLocation getTest() {
        return this.test;
    }

    public void setTest(@Nullable ResourceLocation test) {
        this.test = test;
    }

    public Vector3i getSize() {
        return this.size;
    }

    public void setSize(Vector3i size) {
        this.size = size;
    }

    public StructureRotation getRotation() {
        return this.rotation;
    }

    public void setRotation(StructureRotation rotation) {
        this.rotation = rotation;
    }

    public boolean isIgnoreEntities() {
        return this.ignoreEntities;
    }

    public void setIgnoreEntities(boolean ignoreEntities) {
        this.ignoreEntities = ignoreEntities;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public @Nullable Component getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(@Nullable Component errorMessage) {
        this.errorMessage = errorMessage;
    }

    public enum Status {
        CLEARED,
        RUNNING,
        FINISHED,
    }
}
