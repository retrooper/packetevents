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

package io.github.retrooper.packetevents.event.manager;

import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;

public interface EventManager {
    default void callEvent(PacketEvent event) {
        PEEventManager.dynamicEventManager.callEvent(event);
        PEEventManager.legacyEventManager.callEvent(event);
    }

    @Deprecated
    default void registerListener(PacketListener listener) {
        PEEventManager.legacyEventManager.registerListener(listener);
    }

    @Deprecated
    default void registerListeners(PacketListener... listeners) {
        PEEventManager.legacyEventManager.registerListeners(listeners);
    }

    @Deprecated
    default void unregisterListener(PacketListener listener) {
        PEEventManager.legacyEventManager.unregisterListener(listener);
    }

    @Deprecated
    default void unregisterListeners(PacketListener... listeners) {
        PEEventManager.legacyEventManager.unregisterListeners(listeners);
    }

    default void registerListener(PacketListenerDynamic listener) {
        PEEventManager.dynamicEventManager.registerListener(listener);
    }

    default void registerListeners(PacketListenerDynamic... listeners) {
        PEEventManager.dynamicEventManager.registerListeners(listeners);
    }

    default void unregisterListener(PacketListenerDynamic listener) {
        PEEventManager.dynamicEventManager.unregisterListener(listener);
    }

    default void unregisterListeners(PacketListenerDynamic... listeners) {
        PEEventManager.dynamicEventManager.unregisterListeners(listeners);
    }

    default void unregisterAllListeners() {
        PEEventManager.dynamicEventManager.unregisterAllListeners();
        PEEventManager.legacyEventManager.unregisterAllListeners();
    }
}
