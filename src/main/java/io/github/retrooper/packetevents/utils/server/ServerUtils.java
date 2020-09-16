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

package io.github.retrooper.packetevents.utils.server;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.enums.SystemOS;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;

import java.lang.reflect.InvocationTargetException;

public final class ServerUtils {

    /**
     * Get the server version.
     * @return Get Server Version
     */
    public ServerVersion getVersion() {
        return ServerVersion.getVersion();
    }

    /**
     * Get recent TPS array from NMS.
     * @return Get Recent TPS
     */
    public double[] getRecentTPS() {
        double[] tpsArray = new double[0];
        try {
            tpsArray = NMSUtils.recentTPS();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return tpsArray;
    }

    /**
     * Get the current TPS.
     * @return Get Current TPS
     */
    public double getTPS() {
        return getRecentTPS()[0];
    }

    /**
     * Use {@link #getTPS()}
     * @return Get Current TPS
     */
    @Deprecated
    public double getCurrentTPS() {
        return getRecentTPS()[0];
    }

    /**
     * Use {@link #getOperatingSystem()}
     * @deprecated Method rename pending to next minor update.
     * @return Operating system of the current machine
     */
    @Deprecated
    public SystemOS getPlatform() {
        return SystemOS.getOperatingSystem();
    }

    /**
     * @deprecated Use {@link #getOS()}
     */
    @Deprecated
    public SystemOS getOperatingSystem() {
        return SystemOS.getOperatingSystem();
    }

    /**
     * Get the operating system of the local machine
     * @return Get Operating System
     */
    public SystemOS getOS() {
        return SystemOS.getOperatingSystem();
    }
}
