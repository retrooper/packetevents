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
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

// TODO: Test on outdated versions
public class WrapperPlayServerOpenWindow extends PacketWrapper<WrapperPlayServerOpenWindow> {
    private int containerId; // All versions

    private int type; // 1.14+... also 1.7. Not 1.8-1.13 though.

    private String legacyType; // 1.13-
    private int legacySlots; // 1.13-
    private int horseId; // 1.13-

    private Component title;

    private boolean useProvidedWindowTitle; // 1.7 only

    public WrapperPlayServerOpenWindow(PacketSendEvent event) {
        super(event);
    }

    // For 1.14+
    public WrapperPlayServerOpenWindow(int containerId, int type, Component title) {
        super(PacketType.Play.Server.OPEN_WINDOW);
        this.containerId = containerId;
        this.type = type;
        this.title = title;
    }

    // 1.8 through 1.13
    public WrapperPlayServerOpenWindow(int containerId, String legacyType, Component title, int legacySlots, int horseId) {
        super(PacketType.Play.Server.OPEN_WINDOW);
        this.containerId = containerId;
        this.legacyType = legacyType;
        this.legacySlots = legacySlots;
        this.horseId = horseId;
        this.title = title;
    }

    // 1.7
    public WrapperPlayServerOpenWindow(int containerId, int type, Component title, int legacySlots, boolean useProvidedWindowTitle, int horseId) {
        super(PacketType.Play.Server.OPEN_WINDOW);
        this.containerId = containerId;
        this.type = type;
        this.title = title;
        this.legacySlots = legacySlots;
        this.useProvidedWindowTitle = useProvidedWindowTitle;
        this.horseId = horseId;
    }

    @Override
    public void read() {
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
            this.containerId = readUnsignedByte();
        } else {
            this.containerId = readVarInt();
        }

        // 1.7 has a very different packet format
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            this.type = readUnsignedByte();
            this.title = AdventureSerializer.fromLegacyFormat(readString(32));
            this.legacySlots = readUnsignedByte();
            this.useProvidedWindowTitle = readBoolean();

            if (this.type == 11) { // AnimalChest type ID
                this.horseId = readInt();
            }
            return;
        }

        // Known to be 1.8 or above
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            this.type = readVarInt();
            this.title = readComponent();
        } else {
            this.legacyType = readString();
            this.title = readComponent();
            this.legacySlots = readUnsignedByte();
            // This is only sent for horses
            if (legacyType.equals("EntityHorse")) {
                this.horseId = readInt();
            }
        }
    }

    @Override
    public void write() {
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_13_2)) {
            writeByte(this.containerId);
        } else {
            writeVarInt(this.containerId);
        }

        // 1.7 has a very different packet format
        if (serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
            writeByte(this.type);
            writeString(AdventureSerializer.toLegacyFormat(this.title));
            writeByte(this.legacySlots);
            writeBoolean(this.useProvidedWindowTitle);

            if (this.type == 11) { // AnimalChest type ID
                writeInt(this.horseId);
            }
            return;
        }

        // Known to be 1.8 or above
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
            writeVarInt(this.type);
            writeComponent(this.title);
        } else {
            writeString(this.legacyType);
            writeComponent(this.title);
            writeByte(this.legacySlots);
            // This is only sent for horses
            if (legacyType.equals("EntityHorse")) {
                writeInt(this.horseId);
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerOpenWindow wrapper) {
        this.containerId = wrapper.containerId;
        this.type = wrapper.type;
        this.legacyType = wrapper.legacyType;
        this.legacySlots = wrapper.legacySlots;
        this.horseId = wrapper.horseId;
        this.title = wrapper.title;
        this.useProvidedWindowTitle = wrapper.useProvidedWindowTitle;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLegacyType() {
        return legacyType;
    }

    public void setLegacyType(String legacyType) {
        this.legacyType = legacyType;
    }

    public int getLegacySlots() {
        return legacySlots;
    }

    public void setLegacySlots(int legacySlots) {
        this.legacySlots = legacySlots;
    }

    public int getHorseId() {
        return horseId;
    }

    public void setHorseId(int horseId) {
        this.horseId = horseId;
    }

    public Component getTitle() {
        return this.title;
    }

    public void setTitle(Component title) {
        this.title = title;
    }

    public boolean isUseProvidedWindowTitle() {
        return useProvidedWindowTitle;
    }

    public void setUseProvidedWindowTitle(boolean useProvidedWindowTitle) {
        this.useProvidedWindowTitle = useProvidedWindowTitle;
    }
}
