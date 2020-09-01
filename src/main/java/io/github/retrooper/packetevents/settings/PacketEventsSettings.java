/**
MIT License

Copyright (c) 2020 retrooper

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package io.github.retrooper.packetevents.settings;

import io.github.retrooper.packetevents.enums.ServerVersion;

import java.util.Random;

public class PacketEventsSettings {

    private ServerVersion defaultServerVersion = ServerVersion.v_1_7_10;
    private String identifier = "";
    private boolean autoResolveClientProtocolVersion;
    private boolean uninjectAsync = false;
    private boolean injectAsync = false;

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
     * @param version ServerVersion
     */
    public void setDefaultServerVersion(final ServerVersion version) {
        this.defaultServerVersion = version;
    }

    /**
     * The identifier is a unique string that is used to generate a player's handler name.
     * Two plugins cannot have the same handler name.
     * If you manually set the identifier using its setter '#setIdentifier(String)'.
     * We will use that identifier to generate each player's handler name.
     * If you do not set the identifier manually, we automatically generate one for you.
     * @return stored identifier or new generated identifier
     */
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
     * If another present plugin uses the same Identifier, It will cause incompatibilities.
     *
     * @param identifier Unique string
     * @deprecated This method is deprecated as you no longer need to set an identifier,
     * a unique identifier is already automatically generated for you.
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
     * (when ViaVersion, ProtocolLib or ProtocolSupport can't be found on the server),
     * should it assume that the client is using the same version as the server?
     * Returns ACCESS_FAILURE if this is set to FALSE(default value is false).
     * @param autoResolveClientProtocolVersion boolean
     */
    public void setDoAutoResolveClientProtocolVersion(boolean autoResolveClientProtocolVersion) {
        this.autoResolveClientProtocolVersion = autoResolveClientProtocolVersion;
    }

    /**
     * Would you like PacketEvents to Uninject players Asynchronously?
     * WARNING! This might make reloading unsupported
     *
     * @param uninjectAsync boolean
     */
    public void setUninjectAsync(boolean uninjectAsync) {
        this.uninjectAsync = uninjectAsync;
    }

    public boolean isUninjectAsync() {
        return this.uninjectAsync;
    }

    public boolean isInjectAsync() {
        return this.injectAsync;
    }

    /**
     * Would you like PacketEvents to Inject players Asynchronously?
     * WARNING! This might make reloading unsupported
     *
     * @param injectAsync boolean
     */
    public void setInjectAsync(boolean injectAsync) {
        this.injectAsync = injectAsync;
    }
}