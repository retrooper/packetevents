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

package com.github.retrooper.packetevents.wrapper.login.client;

import com.github.retrooper.packetevents.event.impl.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperLoginClientEncryptionResponse extends PacketWrapper<WrapperLoginClientEncryptionResponse> {
    private byte[] sharedSecret;
    private byte[] verifyToken;

    public WrapperLoginClientEncryptionResponse(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperLoginClientEncryptionResponse(ClientVersion clientVersion, byte[] sharedSecret, byte[] verifyToken) {
        super(PacketType.Login.Client.ENCRYPTION_RESPONSE.getId(), clientVersion);
        this.sharedSecret = sharedSecret;
        this.verifyToken = verifyToken;
    }

    @Override
    public void readData() {
        if (clientVersion.isNewerThanOrEquals(ClientVersion.v_1_10)) {
            this.sharedSecret = readByteArray(byteBuf.readableBytes());
            this.verifyToken = readByteArray(byteBuf.readableBytes());
        } else {
            int sharedSecretLength = readVarInt();
            int verifyTokenLength = readVarInt();
            this.sharedSecret = readByteArray(sharedSecretLength);
            this.verifyToken = readByteArray(verifyTokenLength);
        }
    }

    @Override
    public void readData(WrapperLoginClientEncryptionResponse wrapper) {
        this.sharedSecret = wrapper.sharedSecret;
        this.verifyToken = wrapper.verifyToken;
    }

    @Override
    public void writeData() {
        if (clientVersion.isOlderThan(ClientVersion.v_1_10)) {
            writeVarInt(sharedSecret.length);
            writeVarInt(verifyToken.length);
        }
        writeByteArray(sharedSecret);
        writeByteArray(verifyToken);
    }

    public byte[] getSharedSecret() {
        return this.sharedSecret;
    }

    public void setSharedSecret(byte[] sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public byte[] getVerifyToken() {
        return this.verifyToken;
    }

    public void setVerifyToken(byte[] verifyToken) {
        this.verifyToken = verifyToken;
    }
}
