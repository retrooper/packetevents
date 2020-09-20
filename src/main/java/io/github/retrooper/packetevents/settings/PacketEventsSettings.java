/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.settings;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.packettype.PacketType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PacketEventsSettings {

    public ServerVersion backupServerVersion = ServerVersion.v_1_7_10;
    public String identifier = "";
    public boolean autoResolveClientProtocolVersion = false;
    public boolean uninjectAsync = true;
    public boolean injectAsync = true;
    public boolean useProtocolLibIfAvailable = true;
    public boolean checkForUpdates = true;

    public PacketEventsSettings(String identifier) {
        this.identifier = identifier;
    }

    /**
     * If PacketEvents fails to detect your server version, it will use the recommended version
     * @deprecated Use {@link #getBackupServerVersion()}
     * @return Recommended version
     */
    @Deprecated
    public ServerVersion getDefaultServerVersion() {
        return this.backupServerVersion;
    }

    /**
     * Set the default version
     * @deprecated Use {@link #setBackupServerVersion(ServerVersion)}
     * @param version ServerVersion
     */
    @Deprecated
    public void setDefaultServerVersion(final ServerVersion version) {
        this.backupServerVersion = version;
    }

    public void setBackupServerVersion(ServerVersion version) {
        this.backupServerVersion = version;
    }

    public ServerVersion getBackupServerVersion() {
        return backupServerVersion;
    }

    /**
     * The identifier is a unique string that is used to generate a player's handler name.
     * Two plugins cannot have the same handler name.
     * If you manually set the identifier using its setter '#setIdentifier(String)'.
     * We will use that identifier to generate each player's handler name.
     * If you do not set the identifier manually, we automatically generate one for you.
     *
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
     * This function is now unneeded as PacketEvents generates a random identifier for you already.
     * @param identifier Unique string
     */
    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public boolean shouldAutoResolveClientProtocolVersion() {
        return autoResolveClientProtocolVersion;
    }

    /**
     * @deprecated Use {@link #shouldAutoResolveClientProtocolVersion()}
     * @return
     */
    @Deprecated
    public boolean doAutoResolveClientProtocolVersion() {
        return autoResolveClientProtocolVersion;
    }

    /**
     * @deprecated Use {@link #setShouldAutoResolveClientProtocolVersion(boolean)}
     * @param autoResolveClientProtocolVersion
     */
    @Deprecated
    public void setDoAutoResolveClientProtocolVersion(boolean autoResolveClientProtocolVersion) {
        this.autoResolveClientProtocolVersion = autoResolveClientProtocolVersion;
    }

    /**
     * If PacketEvents fails to detect a client's version
     * (when ViaVersion, ProtocolLib or ProtocolSupport can't be found on the server),
     * should it assume that the client is using the same version as the server?
     * Returns ACCESS_FAILURE if this is set to FALSE(default value is false).
     *
     * @param val boolean
     */
    public void setShouldAutoResolveClientProtocolVersion(boolean val) {
        this.autoResolveClientProtocolVersion = val;
    }

    private List<PacketListener> listeners = new ArrayList<>();

    public void registerEvent(final PacketListener listener) {
        listeners.add(listener);

    }

    public List<PacketListener> getListeners() {
        return listeners;
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

    public boolean isUseProtocolLibIfAvailable() {
        return this.useProtocolLibIfAvailable;
    }

    /**
     * Would you like PacketEvents to make use of ProtocolLib if it is available?
     * If yes, enable this, if not disable this.
     * Having this on can help your plugin have compatibility
     * with other plugins that might be using ProtocolLib.
     * Especially if they or you are doing packet cancellations,
     * PacketEvents passes the event priority to ProtocolLib.
     *
     * @param val boolean
     */
    public void setUseProtocolLibIfAvailable(boolean val) {
        this.useProtocolLibIfAvailable = val;
    }

    public boolean shouldCheckForUpdates() {
        return checkForUpdates;
    }

    /**
     * Would you like PacketEvents to Check for Updates on startup?
     *
     * @param checkForUpdates boolean
     */
    public void setShouldCheckForUpdates(boolean checkForUpdates) {
        this.checkForUpdates = checkForUpdates;
    }
}