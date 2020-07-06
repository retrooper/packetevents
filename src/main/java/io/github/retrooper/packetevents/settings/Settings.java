package io.github.retrooper.packetevents.settings;

import io.github.retrooper.packetevents.enums.ServerVersion;

public class Settings {
    private ServerVersion defaultServerVersion;

    /**
     * If PacketEvents fails to detect your server version, it will use the recommended version
     *
     * @return Recommended version
     */
    public ServerVersion getDefaultServerVersion() {
        return defaultServerVersion;
    }

    /**
     * Set the recommended version
     *
     * @param version
     */
    public void setDefaultServerVersion(final ServerVersion version) {
        defaultServerVersion = version;
    }
}
