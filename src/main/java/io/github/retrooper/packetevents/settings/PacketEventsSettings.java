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
    private boolean injectAsync = true;
    private boolean ejectAsync = true;
    private boolean checkForUpdates = true;
    private boolean injectEarly = true;
    private int packetHandlingThreadCount = 1;
    private String injectionFailureMessage = "We were unable to inject you. Please try again!";

    public PacketEventsSettings backupServerVersion(ServerVersion serverVersion) {
        this.backupServerVersion = serverVersion;
        return this;
    }


    public PacketEventsSettings injectAsync(boolean injectAsync) {
        this.injectAsync = injectAsync;
        return this;
    }

    public PacketEventsSettings ejectAsync(boolean ejectAsync) {
        this.ejectAsync = ejectAsync;
        return this;
    }

    public PacketEventsSettings checkForUpdates(boolean checkForUpdates) {
        this.checkForUpdates = checkForUpdates;
        return this;
    }


    public PacketEventsSettings injectEarly(boolean injectEarly) {
        this.injectEarly = injectEarly;
        return this;
    }

    public PacketEventsSettings packetHandlingThreadCount(int threadCount) {
        this.packetHandlingThreadCount = threadCount;
        return this;
    }

    public PacketEventsSettings injectionFailureMessage(String message) {
        this.injectionFailureMessage = message;
        return this;
    }

    public ServerVersion getBackupServerVersion() {
        return backupServerVersion;
    }

    public boolean shouldInjectAsync() {
        return injectAsync;
    }

    public boolean shouldEjectAsync() {
        return ejectAsync;
    }

    public boolean shouldCheckForUpdates() {
        return checkForUpdates;
    }

    public boolean shouldInjectEarly() {
        return injectEarly;
    }

    public int getPacketHandlingThreadCount() {
        return packetHandlingThreadCount;
    }

    public String getInjectionFailureMessage() {
        return injectionFailureMessage;
    }
}
