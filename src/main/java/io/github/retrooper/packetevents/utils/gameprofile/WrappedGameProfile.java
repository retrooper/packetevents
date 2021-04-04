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

import java.util.*;

/**
 * Wrapper for the Player Game Profile.
 *
 * @author retrooper
 * @since 1.7
 */
public class WrappedGameProfile {
    private final UUID id;
    private final String name;
    private final WrappedPropertyMapAbstract<String, WrappedProperty> wrappedProperty;
    public WrappedGameProfile(UUID id, String name, Object propertMap) {
        this.id = id;
        this.name = name;
        wrappedProperty= NMSUtils.legacyNettyImportMode ? new WrappedPropertyMap7(propertMap) : new WrappedPropertyMap8(propertMap);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public WrappedPropertyMapAbstract<String, WrappedProperty> getProperties() {
        return wrappedProperty;
    }

    public boolean isComplete() {
        return id != null && !isBlank(name);
    }

    private boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }
}
