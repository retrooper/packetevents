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

public final class EventManager {
    private final EventManagerLegacy legacyEventManager = new EventManagerLegacy();
    private final EventManagerDynamic dynamicEventManager = new EventManagerDynamic();

    public void callEvent(PacketEvent event) {
        dynamicEventManager.callEvent(event);
        legacyEventManager.callEvent(event);
    }

    @Deprecated
    public void registerListener(PacketListener listener) {
        legacyEventManager.registerListener(listener);
    }

    @Deprecated
    public void registerListeners(PacketListener... listeners) {
        legacyEventManager.registerListeners(listeners);
    }

    @Deprecated
    public void unregisterListener(PacketListener listener) {
        legacyEventManager.unregisterListener(listener);
    }

    @Deprecated
    public void unregisterListeners(PacketListener... listeners) {
        legacyEventManager.unregisterListeners(listeners);
    }

    public void registerListener(PacketListenerDynamic listener) {
        dynamicEventManager.registerListener(listener);
    }

    public void registerListeners(PacketListenerDynamic... listeners) {
        dynamicEventManager.registerListeners(listeners);
    }

    public void unregisterListener(PacketListenerDynamic listener) {
        dynamicEventManager.unregisterListener(listener);
    }

    public void unregisterListeners(PacketListenerDynamic... listeners) {
        dynamicEventManager.unregisterListeners(listeners);
    }

    public void unregisterAllListeners() {
        dynamicEventManager.unregisterAllListeners();
        legacyEventManager.unregisterAllListeners();
    }
}
