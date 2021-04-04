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

package io.github.retrooper.packetevents.utils.gameprofile;

import io.github.retrooper.packetevents.utils.nms.NMSUtils;

import java.util.UUID;

public class GameProfileUtil {

    public static Object getGameProfile(UUID uuid, String username) {
        if (NMSUtils.legacyNettyImportMode) {
            return GameProfileUtil_7.getGameProfile(uuid, username);
        } else {
            return GameProfileUtil_8.getGameProfile(uuid, username);
        }
    }


    public static WrappedGameProfile getWrappedGameProfile(Object gameProfile) {
        if (NMSUtils.legacyNettyImportMode) {
            return GameProfileUtil_7.getWrappedGameProfile(gameProfile);
        } else {
            return GameProfileUtil_8.getWrappedGameProfile(gameProfile);
        }
    }

    public static Object getProperty(String name, String value, String signature) {
        if (NMSUtils.legacyNettyImportMode) {
            return GameProfileUtil_7.getProperty(name, value, signature);
        } else {
            return GameProfileUtil_8.getProperty(name, value, signature);
        }
    }

    public static WrappedProperty getWrappedProperty(Object property) {
        if (NMSUtils.legacyNettyImportMode) {
            return GameProfileUtil_7.getWrappedProperty(property);
        } else {
            return GameProfileUtil_8.getWrappedProperty(property);
        }
    }

    public static Object getNewPropertyMap() {
        if (NMSUtils.legacyNettyImportMode) {
            return GameProfileUtil_7.getNewPropertyMap();
        } else {
            return GameProfileUtil_8.getNewPropertyMap();
        }
    }
}
