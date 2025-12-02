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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class WrapperPlayServerPlayerChatHeader extends PacketWrapper<WrapperPlayServerPlayerChatHeader> {
    private byte @Nullable [] previousSignature;
    private UUID playerUUID;
    private byte[] signature;
    //AKA message digest
    private byte[] hash;

    public WrapperPlayServerPlayerChatHeader(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPlayerChatHeader(byte @Nullable [] previousSignature, UUID playerUUID, byte[] signature, byte[] hash) {
        super(PacketType.Play.Server.PLAYER_CHAT_HEADER);
        this.previousSignature = previousSignature;
        this.playerUUID = playerUUID;
        this.signature = signature;
        this.hash = hash;
    }

    @Override
    public void read() {
        previousSignature = readOptional(PacketWrapper::readByteArray);
        playerUUID = readUUID();
        signature = readByteArray();
        hash = readByteArray();
    }

    @Override
    public void write() {
        writeOptional(previousSignature, PacketWrapper::writeByteArray);
        writeUUID(playerUUID);
        writeByteArray(signature);
        writeByteArray(hash);
    }

    @Override
    public void copy(WrapperPlayServerPlayerChatHeader wrapper) {
        previousSignature = wrapper.previousSignature;
        playerUUID = wrapper.playerUUID;
        signature = wrapper.signature;
        hash = wrapper.hash;
    }

    /**
     * Get the previous message signature if exists.
     *
     * @return the previous message signature if exists.
     */
    public Optional<byte[]> getPreviousSignature() {
        return Optional.ofNullable(previousSignature);
    }

    /**
     * Set the previous message signature.
     *
     * @param previousSignature the previous message signature.
     */
    public void setPreviousSignature(byte @Nullable [] previousSignature) {
        this.previousSignature = previousSignature;
    }

    /**
     * Get the UUID of the message sender.
     *
     * @return The UUID of the message sender.
     */
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    /**
     * Set the UUID of the message sender.
     *
     * @param playerUUID The UUID of the message sender.
     */
    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    /**
     * Get the signature of the current message.
     *
     * @return The signature of the current message.
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * Set the signature of the current message.
     *
     * @param signature The signature of the current message.
     */
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    /**
     * Get the hash of the chat message.
     *
     * @return the hash of the chat message.
     */
    public byte[] getHash() {
        return hash;
    }

    /**
     * Set message digest.
     *
     * @param hash the hash of the chat message.
     */
    public void setHash(byte[] hash) {
        this.hash = hash;
    }
}
