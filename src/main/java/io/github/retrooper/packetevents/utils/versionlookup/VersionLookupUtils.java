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

package io.github.retrooper.packetevents.utils.versionlookup;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.protocolsupport.ProtocolSupportUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import io.github.retrooper.packetevents.utils.v_1_7_10.ProtocolVersionAccessor_v_1_7;
import io.github.retrooper.packetevents.utils.viaversion.ViaUtils;
import org.bukkit.entity.Player;

public class VersionLookupUtils {

    private static byte protocolAccessMode = -2;

    public static void handleLoadedDependencies() {
        protocolAccessMode = (byte) (ViaUtils.isAvailable() ?
                0 : ProtocolSupportUtils.isAvailable() ?
                1 : PacketEvents.getAPI().getServerUtils().getVersion() == ServerVersion.v_1_7_10 ?
                2 : -1);
    }

    public static boolean hasHandledLoadedDependencies() {
        return protocolAccessMode != -2;
    }

    public static boolean isDependencyAvailable() {
        return ViaUtils.isAvailable()
                || ProtocolSupportUtils.isAvailable();
    }


    /**
     * If ViaVersion is present, we get the protocol version with the ViaVersion API.
     * If ProtocolSupport is present, we get the protocol version with the ProtocolSupport API.
     * If ProtocolLib is present, we get the protocol version with the ProtocolLib API.
     * Otherwise return -1
     *
     * @param player target player
     * @return protocol version of the player if ViaVersion or ProtocolSupport or ProtocolLib is present. Otherwise -1
     */
    public static int getProtocolVersion(final Player player) {
        switch (protocolAccessMode) {
            case 0:
                return ViaUtils.getProtocolVersion(player);
            case 1:
                return ProtocolSupportUtils.getProtocolVersion(player);
            case 2:
                return ProtocolVersionAccessor_v_1_7.getProtocolVersion(player);
            default:
                return -1;
        }
    }
}
