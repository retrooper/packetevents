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
//TODO document this and all classes below
public interface EventManager {
    default EventManager callEvent(PacketEvent event) {
        //The dynamic event manager calls the legacy event manager.
        PEEventManager.dynamicEventManager.callEvent(event);
        return this;
    }

    @Deprecated
    default EventManager registerListener(PacketListener listener) {
        PEEventManager.legacyEventManager.registerListener(listener);
        return this;
    }

    @Deprecated
    default EventManager registerListeners(PacketListener... listeners) {
        PEEventManager.legacyEventManager.registerListeners(listeners);
        return this;
    }

    @Deprecated
    default EventManager unregisterListener(PacketListener listener) {
        PEEventManager.legacyEventManager.unregisterListener(listener);
        return this;
    }

    @Deprecated
    default EventManager unregisterListeners(PacketListener... listeners) {
        PEEventManager.legacyEventManager.unregisterListeners(listeners);
        return this;
    }

    default EventManager registerListener(PacketListenerDynamic listener) {
        PEEventManager.dynamicEventManager.registerListener(listener);
        return this;
    }

    default EventManager registerListeners(PacketListenerDynamic... listeners) {
        PEEventManager.dynamicEventManager.registerListeners(listeners);
        return this;
    }

    default EventManager unregisterListener(PacketListenerDynamic listener) {
        PEEventManager.dynamicEventManager.unregisterListener(listener);
        return this;
    }

    default EventManager unregisterListeners(PacketListenerDynamic... listeners) {
        PEEventManager.dynamicEventManager.unregisterListeners(listeners);
        return this;
    }

    default EventManager unregisterAllListeners() {
        PEEventManager.dynamicEventManager.unregisterAllListeners();
        PEEventManager.legacyEventManager.unregisterAllListeners();
        return this;
    }
}
