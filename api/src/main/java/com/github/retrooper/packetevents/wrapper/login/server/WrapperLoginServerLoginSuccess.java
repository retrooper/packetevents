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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;

/**
 * Mojang name: ClientboundLoginFinishedPacket
 * <p>
 * This packet switches the connection state to {@link ConnectionState#PLAY}.
 */
public class WrapperLoginServerLoginSuccess extends PacketWrapper<WrapperLoginServerLoginSuccess> {

    private static final UUID FALLBACK_SESSION_ID = new UUID(0L, 0L);

    private UserProfile userProfile;
    /**
     * @versions 26.2+
     */
    private UUID sessionId;
    /**
     * @versions 1.20.5-1.21.1
     */
    @ApiStatus.Obsolete
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
        this(userProfile, FALLBACK_SESSION_ID, strictErrorHandling);
    }

    public WrapperLoginServerLoginSuccess(UserProfile userProfile, UUID sessionId) {
        this(userProfile, sessionId, true);
    }

    public WrapperLoginServerLoginSuccess(UserProfile userProfile, UUID sessionId, boolean strictErrorHandling) {
        super(PacketType.Login.Server.LOGIN_SUCCESS);
        this.userProfile = userProfile;
        this.sessionId = sessionId;
        this.strictErrorHandling = strictErrorHandling;
    }

    @Override
    public void read() {
        UUID uuid;
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            uuid = readUUID();
        } else {
            uuid = UUID.fromString(readString(36));
        }
        String username = readString(16);
        this.userProfile = new UserProfile(uuid, username);

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            int propertyCount = readVarInt();
            for (int i = 0; i < propertyCount; i++) {
                String propertyName = readString();
                String propertyValue = readString();
                String propertySignature = readOptional(PacketWrapper::readString);
                TextureProperty textureProperty = new TextureProperty(propertyName, propertyValue, propertySignature);
                userProfile.getTextureProperties().add(textureProperty);
            }
        }

        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_2)) {
            this.sessionId = this.readUUID();
        }

        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)
                && this.serverVersion.isOlderThan(ServerVersion.V_1_21_2)) {
            this.strictErrorHandling = this.readBoolean();
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16)) {
            writeUUID(userProfile.getUUID());
        } else {
            writeString(userProfile.getUUID().toString(), 36);
        }
        writeString(userProfile.getName(), 16);

        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            writeVarInt(userProfile.getTextureProperties().size());
            for (TextureProperty textureProperty : userProfile.getTextureProperties()) {
                writeString(textureProperty.getName());
                writeString(textureProperty.getValue());
                writeOptional(textureProperty.getSignature(), PacketWrapper::writeString);
            }
        }

        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_26_2)) {
            this.writeUUID(this.sessionId);
        }

        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)
                && this.serverVersion.isOlderThan(ServerVersion.V_1_21_2)) {
            this.writeBoolean(this.strictErrorHandling);
        }
    }

    @Override
    public void copy(WrapperLoginServerLoginSuccess wrapper) {
        this.userProfile = wrapper.userProfile;
        this.sessionId = wrapper.sessionId;
        this.strictErrorHandling = wrapper.strictErrorHandling;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    /**
     * @versions 26.2+
     */
    public UUID getSessionId() {
        return this.sessionId;
    }

    /**
     * @versions 26.2+
     */
    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Always-on starting with 1.21.2.
     *
     * @versions 1.20.5-1.21.1
     */
    @ApiStatus.Obsolete
    public boolean isStrictErrorHandling() {
        return this.strictErrorHandling;
    }

    /**
     * Always-on starting with 1.21.2.
     *
     * @versions 1.20.5-1.21.1
     */
    @ApiStatus.Obsolete
    public void setStrictErrorHandling(boolean strictErrorHandling) {
        this.strictErrorHandling = strictErrorHandling;
    }
}
