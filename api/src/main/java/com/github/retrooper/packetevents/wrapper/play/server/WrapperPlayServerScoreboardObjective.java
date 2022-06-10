/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021-2022 retrooper and contributors
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

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayServerScoreboardObjective extends PacketWrapper<WrapperPlayServerScoreboardObjective> {

    private String name;
    private ObjectiveMode mode;
    private Optional<String> displayName;
    private Optional<HealthDisplay> display;

    public enum HealthDisplay {
        INTEGER,
        HEARTS;

        @Nullable
        public static HealthDisplay getByName(String name) {
            for (HealthDisplay display : values()) {
                if (display.name().equalsIgnoreCase(name)) {
                    return display;
                }
            }

            return null;
        }
    }

    public enum ObjectiveMode {
        CREATE,
        REMOVE,
        UPDATE;
    }

    public WrapperPlayServerScoreboardObjective(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerScoreboardObjective(String name, ObjectiveMode mode, Optional<String> displayName, Optional<HealthDisplay> display) {
        super(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
        this.name = name;
        this.mode = mode;
        this.displayName = displayName;
        this.display = display;
    }

    @Override
    public void read() {
        name = readString();
        mode = ObjectiveMode.values()[readByte()];
        if (mode == ObjectiveMode.CREATE || mode == ObjectiveMode.UPDATE) {
            displayName = Optional.ofNullable(readString());
            if (serverVersion.isOlderThan(ServerVersion.V_1_13)) {
                display = Optional.ofNullable(HealthDisplay.getByName(readString()));
            } else {
                display = Optional.of(HealthDisplay.values()[readVarInt()]);
            }
        } else {
            displayName = Optional.empty();
            display = Optional.empty();
        }
    }

    @Override
    public void copy(WrapperPlayServerScoreboardObjective wrapper) {
        name = wrapper.name;
        mode = wrapper.mode;
        displayName = wrapper.displayName;
        display = wrapper.display;
    }

    @Override
    public void write() {
        writeString(name);
        writeByte((byte) mode.ordinal());
        if (this.mode == ObjectiveMode.CREATE || this.mode == ObjectiveMode.UPDATE) {
            writeString(this.displayName.orElse(""));
            if (serverVersion == ServerVersion.V_1_7_10)
                writeString("integer");
            else if (serverVersion.isOlderThan(ServerVersion.V_1_13))
                writeString(display.orElse(HealthDisplay.INTEGER).name().toLowerCase());
            else
                writeVarInt(display.orElse(HealthDisplay.INTEGER).ordinal());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectiveMode getMode() {
        return mode;
    }

    public void setMode(ObjectiveMode mode) {
        this.mode = mode;
    }

    public Optional<String> getDisplayName() {
        return displayName;
    }

    public void setDisplayName(Optional<String> displayName) {
        this.displayName = displayName;
    }

    public Optional<HealthDisplay> getDisplay() {
        return display;
    }

    public void setDisplay(Optional<HealthDisplay> display) {
        this.display = display;
    }

}