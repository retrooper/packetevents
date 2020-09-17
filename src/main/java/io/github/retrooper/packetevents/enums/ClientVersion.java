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

package io.github.retrooper.packetevents.enums;

import io.github.retrooper.packetevents.PacketEvents;

import java.util.HashMap;
import java.util.Map;

public enum ClientVersion {
    LESS_THAN_V_1_7_10, v_1_7_10, v_1_8, v_1_9, v_1_9_1, v_1_9_2, v_1_9_3, v_1_10, v_1_11, v_1_11_1, v_1_12, v_1_12_1, v_1_12_2,
    v_1_13, v_1_13_1, v_1_13_2, v_1_14, v_1_14_1, v_1_14_2, v_1_14_3, v_1_14_4, v_1_15_PRE_1, v_1_15_PRE_2, v_1_15_PRE_3, v_1_15_PRE_4, v_1_15_PRE_5, v_1_15_PRE_6, v_1_15_PRE_7,
    v_1_15, v_1_15_1, v_1_15_2, v_1_16, v_1_16_PRE_1, v_1_16_PRE_2, v_1_16_PRE_3, v_1_16_PRE_4, v_1_16_PRE_5, v_1_16_PRE_6, v_1_16_PRE_7, v_1_16_PRE_8, v_1_16_RC_1, v_1_16_1, v_1_16_2, v_1_16_3, HIGHER_THAN_V_1_16_3, INVALID, ACCESS_FAILURE;

    private static final Map<Integer, ClientVersion> VERSION_LOOKUP = new HashMap<Integer, ClientVersion>();

    public static void prepareLookUp() {
        VERSION_LOOKUP.put(-1, ACCESS_FAILURE);
        VERSION_LOOKUP.put(1, LESS_THAN_V_1_7_10);
        VERSION_LOOKUP.put(2, LESS_THAN_V_1_7_10);
        VERSION_LOOKUP.put(3, LESS_THAN_V_1_7_10);
        VERSION_LOOKUP.put(4, LESS_THAN_V_1_7_10);
        VERSION_LOOKUP.put(5, v_1_7_10);
        VERSION_LOOKUP.put(47, v_1_8);
        VERSION_LOOKUP.put(107, v_1_9);
        VERSION_LOOKUP.put(108, v_1_9_1);
        VERSION_LOOKUP.put(109, v_1_9_2);
        VERSION_LOOKUP.put(110, v_1_9_3);
        VERSION_LOOKUP.put(210, v_1_10);
        VERSION_LOOKUP.put(315, v_1_11);
        VERSION_LOOKUP.put(316, v_1_11_1);
        VERSION_LOOKUP.put(335, v_1_12);
        VERSION_LOOKUP.put(338, v_1_12_1);
        VERSION_LOOKUP.put(340, v_1_12_2);
        VERSION_LOOKUP.put(393, v_1_13);
        VERSION_LOOKUP.put(401, v_1_13_1);
        VERSION_LOOKUP.put(404, v_1_13_2);
        VERSION_LOOKUP.put(477, v_1_14);
        VERSION_LOOKUP.put(480, v_1_14_1);
        VERSION_LOOKUP.put(485, v_1_14_2);
        VERSION_LOOKUP.put(490, v_1_14_3);
        VERSION_LOOKUP.put(498, v_1_14_4);
        VERSION_LOOKUP.put(565, v_1_15_PRE_1);
        VERSION_LOOKUP.put(566, v_1_15_PRE_2);
        VERSION_LOOKUP.put(567, v_1_15_PRE_3);
        VERSION_LOOKUP.put(569, v_1_15_PRE_4);
        VERSION_LOOKUP.put(570, v_1_15_PRE_5);
        VERSION_LOOKUP.put(571, v_1_15_PRE_6);
        VERSION_LOOKUP.put(572, v_1_15_PRE_7);
        VERSION_LOOKUP.put(573, v_1_15);
        VERSION_LOOKUP.put(575, v_1_15_1);
        VERSION_LOOKUP.put(578, v_1_15_2);
        VERSION_LOOKUP.put(721, v_1_16_PRE_1);
        VERSION_LOOKUP.put(722, v_1_16_PRE_2);
        VERSION_LOOKUP.put(725, v_1_16_PRE_3);
        VERSION_LOOKUP.put(727, v_1_16_PRE_4);
        VERSION_LOOKUP.put(729, v_1_16_PRE_5);
        VERSION_LOOKUP.put(730, v_1_16_PRE_6);
        VERSION_LOOKUP.put(732, v_1_16_PRE_7);
        VERSION_LOOKUP.put(733, v_1_16_PRE_8);
        VERSION_LOOKUP.put(734, v_1_16_RC_1);
        VERSION_LOOKUP.put(735, v_1_16);
        VERSION_LOOKUP.put(736, v_1_16_1);
        VERSION_LOOKUP.put(751, v_1_16_2);
        VERSION_LOOKUP.put(753, v_1_16_3);
    }

    public static ClientVersion fromProtocolVersion(int protocolVersion) {
        if (protocolVersion == -1 && PacketEvents.getSettings().doAutoResolveClientProtocolVersion()) {
            protocolVersion = ServerVersion.getVersion().toProtocolVersion();
        }
        final ClientVersion lookedUp = VERSION_LOOKUP.get(protocolVersion);
        if (lookedUp != null) {
            return lookedUp;
        }
        return protocolVersion > 753 ? HIGHER_THAN_V_1_16_3 : INVALID;
    }

    /**
     * Returns if the client's version is more up to date than the argument passed version
     *
     * @param version The version to compare to the client's value.
     * @return True if the supplied version is lower, false if it is equal or higher than the client's version.
     */
    public boolean isHigherThan(final ClientVersion version) {
        if (this == version) return false;
        return !isLowerThan(version);
    }

    /**
     * Returns if the client's version is more outdated than the argument passed version
     *
     * @param version The version to compare to the client's value.
     * @return True if the supplied version is higher, false if it is equal or lower than the client's version.
     */
    public boolean isLowerThan(final ClientVersion version) {
        if (this == version) return false;
        final int len = ClientVersion.values().length;
        for (byte i = 0; i < len; i++) {
            final ClientVersion v = ClientVersion.values()[i];
            if (v == this) {
                return true;
            } else if (v == version) {
                return false;
            }
        }
        return false;
    }
}
