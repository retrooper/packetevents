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
import org.jetbrains.annotations.Nullable;

public class WrapperPlayServerScoreboardObjective extends PacketWrapper<WrapperPlayServerScoreboardObjective> {
    private String name;
    private ObjectiveMode mode;
    private Component displayName;
    private @Nullable RenderType renderType;

    public WrapperPlayServerScoreboardObjective(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerScoreboardObjective(String name, ObjectiveMode mode, Component displayName, @Nullable RenderType renderType) {
        super(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
        this.name = name;
        this.mode = mode;
        this.displayName = displayName;
        this.renderType = renderType;
    }

    @Override
    public void read() {
        name = readString();
        mode = ObjectiveMode.getById(readByte());
        if (mode != ObjectiveMode.CREATE && mode != ObjectiveMode.UPDATE) {
            displayName = Component.empty();
            renderType = RenderType.INTEGER;
        } else {
            if (serverVersion.isOlderThan(ServerVersion.V_1_13)) {
                displayName = AdventureSerializer.fromLegacyFormat(readString(32));
                renderType = RenderType.getByName(readString());
            } else {
                displayName = readComponent();
                renderType = RenderType.getById(readVarInt());
            }
        }
    }

    @Override
    public void write() {
        writeString(name);
        writeByte((byte) mode.ordinal());
        if (this.mode == ObjectiveMode.CREATE || this.mode == ObjectiveMode.UPDATE) {
            if (serverVersion.isOlderThan(ServerVersion.V_1_13)) {
                writeString(AdventureSerializer.asVanilla(displayName));
                if (renderType != null) {
                    writeString(renderType.name().toLowerCase());
                } else {
                    writeString(RenderType.INTEGER.name().toLowerCase());
                }
            } else {
                writeComponent(displayName);
                if (renderType != null) {
                    writeVarInt(renderType.ordinal());
                } else {
                    writeVarInt(RenderType.INTEGER.ordinal());
                }
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerScoreboardObjective wrapper) {
        name = wrapper.name;
        mode = wrapper.mode;
        displayName = wrapper.displayName;
        renderType = wrapper.renderType;
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

    public Component getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@Nullable Component displayName) {
        this.displayName = displayName;
    }

    public @Nullable RenderType getRenderType() {
        return renderType;
    }

    public void setRenderType(@Nullable RenderType renderType) {
        this.renderType = renderType;
    }

    public enum ObjectiveMode {
        CREATE,
        REMOVE,
        UPDATE;

        private static final ObjectiveMode[] VALUES = values();

        @Nullable
        public static ObjectiveMode getByName(String name) {
            for (ObjectiveMode mode : VALUES) {
                if (mode.name().equalsIgnoreCase(name)) {
                    return mode;
                }
            }
            return null;
        }

        public static ObjectiveMode getById(int id) {
            return VALUES[id];
        }
    }

    public enum RenderType {
        INTEGER,
        HEARTS;

        private static final RenderType[] VALUES = values();

        @Nullable
        public static RenderType getByName(String name) {
            for (RenderType display : VALUES) {
                if (display.name().equalsIgnoreCase(name)) {
                    return display;
                }
            }
            return null;
        }

        @Nullable
        public static RenderType getById(int id) {
            return VALUES[id];
        }
    }
}