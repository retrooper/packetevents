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
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WrapperPlayServerTabComplete extends PacketWrapper<WrapperPlayServerTabComplete> {
    private Optional<Integer> transactionID;
    private Optional<CommandRange> commandRange;
    private List<CommandMatch> commandMatches;

    public WrapperPlayServerTabComplete(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTabComplete(@Nullable Integer transactionID, @NotNull CommandRange commandRange, List<CommandMatch> commandMatches) {
        super(PacketType.Play.Server.TAB_COMPLETE);
        setTransactionId(transactionID);
        setCommandRange(commandRange);
        this.commandMatches = commandMatches;
    }
/*
    public WrapperPlayServerTabComplete(UUID uuid, List<CommandMatch> commandMatches) {
        super(PacketType.Play.Server.TAB_COMPLETE);
        TabCompleteAttribute tabCompleteAttribute = PacketEvents.getAPI()
                .getPlayerManager().getAttributeOrDefault(uuid, TabCompleteAttribute.class, new TabCompleteAttribute());
        setTransactionId(tabCompleteAttribute.getTransactionId());
        int len = tabCompleteAttribute.getInput().length();
        setCommandRange(new CommandRange(len, len));
        this.commandMatches = commandMatches;
    }*/

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            transactionID = Optional.of(readVarInt());
            int begin = readVarInt();
            int len = readVarInt();
            int matchLength = readVarInt();
            commandRange = Optional.of(new CommandRange(begin, begin + len));
            commandMatches = new ArrayList<>(matchLength);
            for (int i = 0; i < matchLength; i++) {
                String text = readString();
                Component tooltip = readOptional(PacketWrapper::readComponent);
                CommandMatch commandMatch = new CommandMatch(text, tooltip);
                commandMatches.add(commandMatch);
            }
        } else {
            int matchLength = readVarInt();
            commandMatches = new ArrayList<>(matchLength);
            for (int i = 0; i < matchLength; i++) {
                String text = readString();
                CommandMatch commandMatch = new CommandMatch(text, null);
                commandMatches.add(commandMatch);
            }
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            writeVarInt(transactionID.orElse(-1));
            CommandRange commandRange = this.commandRange.get();
            writeVarInt(commandRange.getBegin());
            writeVarInt(commandRange.getLength());
            writeVarInt(commandMatches.size());
            for (CommandMatch match : commandMatches) {
                writeString(match.getText());
                boolean hasTooltip = match.getTooltip().isPresent();
                writeBoolean(hasTooltip);
                if (hasTooltip) {
                    writeComponent(match.getTooltip().get());
                }
            }
        } else {
            writeVarInt(commandMatches.size());
            for (CommandMatch match : commandMatches) {
                writeString(match.getText());
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerTabComplete wrapper) {
        transactionID = wrapper.transactionID;
        commandRange = wrapper.commandRange;
        commandMatches = wrapper.commandMatches;
    }

    public Optional<Integer> getTransactionId() {
        return transactionID;
    }

    public void setTransactionId(@Nullable Integer transactionID) {
        this.transactionID = Optional.ofNullable(transactionID);
    }

    public Optional<CommandRange> getCommandRange() {
        return commandRange;
    }

    public void setCommandRange(@Nullable CommandRange commandRange) {
        this.commandRange = Optional.ofNullable(commandRange);
    }

    public List<CommandMatch> getCommandMatches() {
        return commandMatches;
    }

    public void setCommandMatches(List<CommandMatch> commandMatches) {
        this.commandMatches = commandMatches;
    }

    public static class CommandMatch {
        private String text;
        private Optional<Component> tooltip;

        public CommandMatch(String text, @Nullable Component tooltip) {
            this.text = text;
            setTooltip(tooltip);
        }

        public CommandMatch(String text) {
            this.text = text;
            tooltip = Optional.empty();
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Optional<Component> getTooltip() {
            return tooltip;
        }

        public void setTooltip(@Nullable Component tooltip) {
            if (tooltip != null) {
                this.tooltip = Optional.of(tooltip);
            } else {
                this.tooltip = Optional.empty();
            }
        }
    }

    public static class CommandRange {
        private int begin;
        private int end;

        public CommandRange(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        public int getBegin() {
            return begin;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getLength() {
            return end - begin;
        }
    }
}
