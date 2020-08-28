/**
 * Copyright (c) 2020 retrooper
 */
package io.github.retrooper.packetevents.settings;

import io.github.retrooper.packetevents.enums.ServerVersion;

import java.util.Random;

public class PacketEventsSettings {

    private ServerVersion defaultServerVersion;
    private String identifier = "";
    private boolean autoResolveClientProtocolVersion;
    private boolean uninjectAsync = false;

    /**
     * If PacketEvents fails to detect your server version, it will use the recommended version
     *
     * @return Recommended version
     */
    public ServerVersion getDefaultServerVersion() {
        return this.defaultServerVersion;
    }

    /**
     * Set the default version
     *
     * @param version
     */
    public void setDefaultServerVersion(final ServerVersion version) {
        this.defaultServerVersion = version;
    }

    public String getIdentifier() {
        if (this.identifier.isEmpty()) {
            String alphabet = "abcdefghijklmnopqrstuvwxyz";
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            while (sb.length() < 13) {
                int index = (int) (random.nextFloat() * alphabet.length());
                sb.append(alphabet.charAt(index));
            }

            this.identifier = sb.toString();
        }
        return this.identifier;
    }

    /**
     * Set a unique Identifier (Usually something that represents your plugin, For example: MyPluginPacketHandler)
     * If another present plugin uses the same Identifier, It might cause incompatibilities
     *
     * @param identifier
     * @deprecated Identifiers are now randomly generated on startup
     */
    @Deprecated
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

    /**
     * Would you like PacketEvents to Uninject players Asynchronously?
     * WARNING! This might make reloading unsupported
     *
     * @param uninjectAsync
     */
    public void setUninjectAsync(boolean uninjectAsync) {
        this.uninjectAsync = uninjectAsync;
    }

    public boolean isUninjectAsync() {
        return this.uninjectAsync;
    }
}