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

import io.github.retrooper.packetevents.utils.server.ServerVersion;

public class PacketEventsSettings {
    private ServerVersion backupServerVersion = ServerVersion.v_1_7_10;
    private boolean autoResolveClientProtocolVersion = false;
    private boolean injectAsync = true;
    private boolean ejectAsync = true;
    private boolean useProtocolLibIfAvailable = false;
    private boolean checkForUpdates = true;

    public final void setBackupServerVersion(ServerVersion serverVersion) {
        this.backupServerVersion = serverVersion;
    }

    public final void setShouldAutoResolveClientProtocolVersion(boolean autoResolveClientProtocolVersion) {
        this.autoResolveClientProtocolVersion = autoResolveClientProtocolVersion;
    }

    public final void setShouldInjectAsync(boolean injectAsync) {
        this.injectAsync = injectAsync;
    }

    public final void setShouldEjectAsync(boolean ejectAsync) {
        this.ejectAsync = ejectAsync;
    }

    public final void setShouldUseProtocolLibIfAvailable(boolean useProtocolLibIfAvailable) {
        this.useProtocolLibIfAvailable = useProtocolLibIfAvailable;
    }

    public final void setShouldCheckForUpdates(boolean checkForUpdates) {
        this.checkForUpdates = checkForUpdates;
    }

    public ServerVersion getBackupServerVersion() {
        return backupServerVersion;
    }

    public boolean shouldAutoResolveClientProtocolVersion() {
        return autoResolveClientProtocolVersion;
    }

    public boolean shouldInjectAsync() {
        return injectAsync;
    }

    public boolean shouldEjectAsync() {
        return ejectAsync;
    }

    public boolean shouldUseProtocolLibIfAvailable() {
        return useProtocolLibIfAvailable;
    }

    public boolean shouldCheckForUpdates() {
        return checkForUpdates;
    }
}