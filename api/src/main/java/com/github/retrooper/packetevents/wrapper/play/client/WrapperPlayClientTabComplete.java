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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayClientTabComplete extends PacketWrapper<WrapperPlayClientTabComplete> {

    private Optional<Integer> transactionId;
    private boolean assumeCommand;
    private String text;
    private @Nullable Vector3i blockPosition;

    public WrapperPlayClientTabComplete(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientTabComplete(int transactionId, String text, @Nullable Vector3i blockPosition) {
        super(PacketType.Play.Client.TAB_COMPLETE);
        this.transactionId = Optional.of(transactionId);
        this.assumeCommand = true;
        this.text = text;
        this.blockPosition = blockPosition;
    }

    @Deprecated
    public WrapperPlayClientTabComplete(String text, boolean assumeCommand, @Nullable Vector3i blockPosition) {
        super(PacketType.Play.Client.TAB_COMPLETE);
        this.transactionId = Optional.empty();
        this.text = text;
        this.assumeCommand = assumeCommand;
        this.blockPosition = blockPosition;
    }


    @Override
    public void read() {
        int textLength;
        textLength = 32500;
        transactionId = Optional.of(readVarInt());
        text = readString(textLength);
    }

    @Override
    public void write() {
        int textLength = 32500;
        writeVarInt(transactionId.orElse(0));
        writeString(text, textLength);
    }

    @Override
    public void copy(WrapperPlayClientTabComplete wrapper) {
        text = wrapper.text;
        assumeCommand = wrapper.assumeCommand;
        transactionId = wrapper.transactionId;
        blockPosition = wrapper.blockPosition;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Optional<Integer> getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(@Nullable Integer transactionID) {
        this.transactionId = Optional.ofNullable(transactionID);
    }

    @Deprecated
    public boolean isAssumeCommand() {
        return assumeCommand;
    }

    @Deprecated
    public void setAssumeCommand(boolean assumeCommand) {
        this.assumeCommand = assumeCommand;
    }

    public Optional<Vector3i> getBlockPosition() {
        return Optional.ofNullable(blockPosition);
    }

    public void setBlockPosition(@Nullable Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }
}
