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

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.annotation.PacketHandler;
import io.github.retrooper.packetevents.event.eventtypes.CancellableEvent;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

/**
 * This is the event manager interface.
 * Every method is already implemented, we just use this
 * to have less code duplications.
 * You can either call event manager methods in the
 * {@link io.github.retrooper.packetevents.PacketEvents} instance or with an instance of the
 * {@link PEEventManager} stored in {@link PacketEvents}.
 *
 * @author retrooper
 * @see PacketEvents#getEventManager()
 * @since 1.0
 */
public interface EventManager {

    /**
     * Call a packet event.
     * This will let all listeners(new and deprecated listeners) process the event.
     * It isn't recommended to use a mix of both listeners(new and deprecated),
     * but it will work and they are compatible.
     *
     * @param event {@link PacketEvent}
     * @return Same event manager instance.
     */
    default EventManager callEvent(PacketEvent event) {
        //The dynamic event manager calls the legacy event manager.
        PEEventManager.EVENT_MANAGER_DYNAMIC.callEvent(event);
        return this;
    }

    /**
     * Register a deprecated event listener.
     * Not recommended to use the deprecated event listener.
     *
     * @param listener {@link PacketListener}
     * @return Same event manager instance.
     */
    @Deprecated
    default EventManager registerListener(PacketListener listener) {
        if (listener != null) {
            PEEventManager.EVENT_MANAGER_LEGACY.registerListener(listener);
        }
        return this;
    }

    /**
     * Register multiple deprecated event listeners with one method.
     * Not recommended to use the deprecated event listener.
     *
     * @param listeners {@link PacketListener}
     * @return Same event manager instance.
     */
    @Deprecated
    default EventManager registerListeners(PacketListener... listeners) {
        PEEventManager.EVENT_MANAGER_LEGACY.registerListeners(listeners);
        return this;
    }

    /**
     * Unregister a deprecated event listener.
     * Not recommended to use the deprecated event listener.
     *
     * @param listener {@link PacketListener}
     * @return Same event manager instance.
     */
    @Deprecated
    default EventManager unregisterListener(PacketListener listener) {
        PEEventManager.EVENT_MANAGER_LEGACY.unregisterListener(listener);
        return this;
    }

    /**
     * Unregister multiple deprecated event listeners with one method.
     * Not recommended to use the deprecated event listener.
     *
     * @param listeners {@link PacketListener}
     * @return Same event manager instance.
     */
    @Deprecated
    default EventManager unregisterListeners(PacketListener... listeners) {
        PEEventManager.EVENT_MANAGER_LEGACY.unregisterListeners(listeners);
        return this;
    }

    /**
     * Register the dynamic event listener.
     *
     * @param listener {@link PacketListenerDynamic}
     * @return Same event manager instance.
     */
    default EventManager registerListener(PacketListenerDynamic listener) {
        if (listener != null) {
            PEEventManager.EVENT_MANAGER_DYNAMIC.registerListener(listener);
        }
        return this;
    }

    /**
     * Register multiple dynamic event listeners with one method.
     *
     * @param listeners {@link PacketListenerDynamic}
     * @return Same event manager instance.
     */
    default EventManager registerListeners(PacketListenerDynamic... listeners) {
        PEEventManager.EVENT_MANAGER_DYNAMIC.registerListeners(listeners);
        return this;
    }

    /**
     * Unregister all registered event listeners.
     * All the deprecated and the dynamic listeners will be unregistered.
     *
     * @return Same event manager instance.
     */
    default EventManager unregisterAllListeners() {
        PEEventManager.EVENT_MANAGER_DYNAMIC.unregisterAllListeners();
        PEEventManager.EVENT_MANAGER_LEGACY.unregisterAllListeners();
        return this;
    }
}
