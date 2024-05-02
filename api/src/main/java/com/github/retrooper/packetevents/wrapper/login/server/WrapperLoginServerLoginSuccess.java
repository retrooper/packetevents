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

package com.github.retrooper.packetevents.wrapper.login.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.UUID;

/**
 * This packet switches the connection state to {@link ConnectionState#PLAY}.
 */
public class WrapperLoginServerLoginSuccess extends PacketWrapper<WrapperLoginServerLoginSuccess> {

    private UserProfile userProfile;
    private boolean strictErrorHandling;

    public WrapperLoginServerLoginSuccess(PacketSendEvent event) {
        super(event);
    }

    public WrapperLoginServerLoginSuccess(UUID uuid, String username) {
        this(new UserProfile(uuid, username));
    }

    public WrapperLoginServerLoginSuccess(UserProfile userProfile) {
        this(userProfile, true);
    }

    public WrapperLoginServerLoginSuccess(UserProfile userProfile, boolean strictErrorHandling) {
        super(PacketType.Login.Server.LOGIN_SUCCESS);
        this.userProfile = userProfile;
        this.strictErrorHandling = strictErrorHandling;
    }

    @Override
    public void read() {
        UUID uuid;
        uuid = readUUID();
        String username = readString(16);
        this.userProfile = new UserProfile(uuid, username);

        int propertyCount = readVarInt();
        for (int i = 0; i < propertyCount; i++) {
            String propertyName = readString();
            String propertyValue = readString();
            String propertySignature = readOptional(PacketWrapper::readString);
            TextureProperty textureProperty = new TextureProperty(propertyName, propertyValue, propertySignature);
            userProfile.getTextureProperties().add(textureProperty);
        }

        this.strictErrorHandling = this.readBoolean();
    }

    @Override
    public void write() {
        writeUUID(userProfile.getUUID());
        writeString(userProfile.getName(), 16);

        writeVarInt(userProfile.getTextureProperties().size());
        for (TextureProperty textureProperty : userProfile.getTextureProperties()) {
            writeString(textureProperty.getName());
            writeString(textureProperty.getValue());
            writeOptional(textureProperty.getSignature(), PacketWrapper::writeString);
        }

        this.writeBoolean(this.strictErrorHandling);
    }

    @Override
    public void copy(WrapperLoginServerLoginSuccess wrapper) {
        this.userProfile = wrapper.userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public boolean isStrictErrorHandling() {
        return this.strictErrorHandling;
    }

    public void setStrictErrorHandling(boolean strictErrorHandling) {
        this.strictErrorHandling = strictErrorHandling;
    }
}
