/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.chat.component.ComponentParser;
import com.github.retrooper.packetevents.protocol.chat.component.TextComponent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WrapperPlayServerTabComplete extends PacketWrapper<WrapperPlayServerTabComplete> {
    private static final int MODERN_MESSAGE_LENGTH = 262144;
    private static final int LEGACY_MESSAGE_LENGTH = 32767;
    private int transactionID;
    private CommandRange commandRange;
    private List<CommandMatch> commandMatches;

    public WrapperPlayServerTabComplete(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTabComplete(int transactionID, CommandRange commandRange, List<CommandMatch> commandMatches) {
        super(PacketType.Play.Server.TAB_COMPLETE);
        this.transactionID = transactionID;
        this.commandRange = commandRange;
        this.commandMatches = commandMatches;
    }

    @Override
    public void readData() {
        transactionID = readVarInt();
        // - /help -> 4 chars + "/" + 1 = 6
        //begin = 6
        int begin = readVarInt();
        int len = readVarInt();
        int matchLength = readVarInt();
        //TODO Think, maybe put command range in each match cause 1.8 compat
        commandRange = new CommandRange(begin, begin + len);
        commandMatches = new ArrayList<>(matchLength);
        int maxMessageLength = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_13) ? MODERN_MESSAGE_LENGTH : LEGACY_MESSAGE_LENGTH;
        for (int i = 0; i < matchLength; i++) {
            String text = readString();
            List<TextComponent> tooltip;
            boolean hasTooltip = readBoolean();
            if (hasTooltip) {
                String jsonMessage = readString(maxMessageLength);
                tooltip = ComponentParser.parseJSONString(jsonMessage);
            } else {
                tooltip = null;
            }
            CommandMatch commandMatch = new CommandMatch(text, tooltip);
            commandMatches.add(commandMatch);
        }
    }

    @Override
    public void readData(WrapperPlayServerTabComplete wrapper) {
        transactionID = wrapper.transactionID;
        commandRange = wrapper.commandRange;
        commandMatches = wrapper.commandMatches;
    }

    @Override
    public void writeData() {
        //TODO

    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public CommandRange getCommandRange() {
        return commandRange;
    }

    public void setCommandRange(CommandRange commandRange) {
        this.commandRange = commandRange;
    }

    public List<CommandMatch> getCommandMatches() {
        return commandMatches;
    }

    public void setCommandMatches(List<CommandMatch> commandMatches) {
        this.commandMatches = commandMatches;
    }

    public static class CommandMatch {
        private String text;
        private Optional<List<TextComponent>> tooltip;

        public CommandMatch(String text, @Nullable List<TextComponent> tooltip) {
            this.text = text;
            setTooltip(tooltip);
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Optional<List<TextComponent>> getTooltip() {
            return tooltip;
        }

        public void setTooltip(@Nullable List<TextComponent> tooltip) {
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
