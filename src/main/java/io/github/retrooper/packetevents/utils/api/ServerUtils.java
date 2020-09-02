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

package io.github.retrooper.packetevents.utils.api;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.enums.SystemOS;
import io.github.retrooper.packetevents.utils.NMSUtils;

import java.lang.reflect.InvocationTargetException;

public final class ServerUtils {

    public ServerVersion getVersion() {
        return ServerVersion.getVersion();
    }

    public double[] getRecentTPS() {
        double[] tpsArray = new double[0];
        try {
            tpsArray = NMSUtils.recentTPS();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return tpsArray;
    }

    public double getTPS() {
        return getRecentTPS()[0];
    }

    /**
     * Use {@link #getTPS()}
     * @return
     */
    @Deprecated
    public double getCurrentTPS() {
        return getRecentTPS()[0];
    }

    public SystemOS getPlatform() {
        return SystemOS.getOperatingSystem();
    }
}
