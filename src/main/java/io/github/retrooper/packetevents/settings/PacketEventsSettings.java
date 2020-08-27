/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.settings;

import io.github.retrooper.packetevents.enums.ServerVersion;

public class PacketEventsSettings {
    private ServerVersion defaultServerVersion;
    private String identifier = "";
    private boolean autoResolveClientProtocolVersion;

    /**
     * If PacketEvents fails to detect your server version, it will use the recommended version
     *
     * @return Recommended version
     */
    public ServerVersion getDefaultServerVersion() {
        return defaultServerVersion;
    }

    /**
     * Set the default version
     *
     * @param version
     */
    public void setDefaultServerVersion(final ServerVersion version) {
        defaultServerVersion = version;
    }

    public String getIdentifier() {
        return identifier;
    }

    /**
     * Set a unique Identifier (Usually something that represents your plugin, For example: MyPluginPacketHandler)
     * If another present plugin uses the same Identifier, It might cause incompatibilities
     *
     * @param identifier
     */
    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public boolean doAutoResolveClientProtocolVersion() {
        return autoResolveClientProtocolVersion;
    }

    /**
     * If PacketEvents fails to detect a client's version
     * Should it assume that the client is using the same version that the server is?
     * Returns ACCESS_FAILURE if false
     *
     * @param autoResolveClientProtocolVersion
     */
    public void setDoAutoResolveClientProtocolVersion(boolean autoResolveClientProtocolVersion) {
        this.autoResolveClientProtocolVersion = autoResolveClientProtocolVersion;
    }
}
