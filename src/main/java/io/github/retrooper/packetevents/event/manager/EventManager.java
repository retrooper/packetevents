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
        PEEventManager.EVENT_MANAGER_DYNAMIC.callEvent(event);
        return this;
    }

    @Deprecated
    default EventManager registerListener(PacketListener listener) {
        PEEventManager.EVENT_MANAGER_LEGACY.registerListener(listener);
        return this;
    }

    @Deprecated
    default EventManager registerListeners(PacketListener... listeners) {
        PEEventManager.EVENT_MANAGER_LEGACY.registerListeners(listeners);
        return this;
    }

    @Deprecated
    default EventManager unregisterListener(PacketListener listener) {
        PEEventManager.EVENT_MANAGER_LEGACY.unregisterListener(listener);
        return this;
    }

    @Deprecated
    default EventManager unregisterListeners(PacketListener... listeners) {
        PEEventManager.EVENT_MANAGER_LEGACY.unregisterListeners(listeners);
        return this;
    }

    default EventManager registerListener(PacketListenerDynamic listener) {
        PEEventManager.EVENT_MANAGER_DYNAMIC.registerListener(listener);
        return this;
    }

    default EventManager registerListeners(PacketListenerDynamic... listeners) {
        PEEventManager.EVENT_MANAGER_DYNAMIC.registerListeners(listeners);
        return this;
    }

    default EventManager unregisterListener(PacketListenerDynamic listener) {
        PEEventManager.EVENT_MANAGER_DYNAMIC.unregisterListener(listener);
        return this;
    }

    default EventManager unregisterListeners(PacketListenerDynamic... listeners) {
        PEEventManager.EVENT_MANAGER_DYNAMIC.unregisterListeners(listeners);
        return this;
    }

    default EventManager unregisterAllListeners() {
        PEEventManager.EVENT_MANAGER_DYNAMIC.unregisterAllListeners();
        PEEventManager.EVENT_MANAGER_LEGACY.unregisterAllListeners();
        return this;
    }
}
