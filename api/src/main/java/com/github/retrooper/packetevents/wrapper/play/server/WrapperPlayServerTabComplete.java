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

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.command.CommandMatch;
import com.github.retrooper.packetevents.protocol.command.CommandRange;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class WrapperPlayServerTabComplete extends PacketWrapper<WrapperPlayServerTabComplete> {
    private int transactionId;
    private CommandRange commandRange;
    private List<CommandMatch> commandMatches;

    public WrapperPlayServerTabComplete(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerTabComplete(int transactionId, CommandRange commandRange, List<CommandMatch> commandMatches) {
        super(PacketType.Play.Server.TAB_COMPLETE);
        this.transactionId = transactionId;
        this.commandRange = commandRange;
        this.commandMatches = commandMatches;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.transactionId = readVarInt();
            int begin = readVarInt();
            int len = readVarInt();
            int matchLength = readVarInt();
            commandRange = new CommandRange(begin, begin + len);
            commandMatches = new ArrayList<>(matchLength);
            for (int i = 0; i < matchLength; i++) {
                String text = readString();
                Component tooltip;
                boolean hasTooltip = readBoolean();
                if (hasTooltip) {
                    String tooltipJson = readComponentJSON();
                    tooltip = AdventureSerializer.parseComponent(tooltipJson);
                } else {
                    tooltip = null;
                }
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
            writeVarInt(transactionId);
            CommandRange range = this.commandRange;
            writeVarInt(range.getBegin());
            writeVarInt(range.getLength());
            writeVarInt(commandMatches.size());
            for (CommandMatch match : commandMatches) {
                writeString(match.getText());
                boolean hasTooltip = match.getTooltip().isPresent();
                writeBoolean(hasTooltip);
                if (hasTooltip) {
                    String tooltipJson = AdventureSerializer.toJson(match.getTooltip().get());
                    writeComponentJSON(tooltipJson);
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
        transactionId = wrapper.transactionId;
        commandRange = wrapper.commandRange;
        commandMatches = wrapper.commandMatches;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
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
}
