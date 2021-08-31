/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.retrooper.packetevents.event.manager;

import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.event.PacketEvent;
import io.github.retrooper.packetevents.event.PacketListener;
import io.github.retrooper.packetevents.event.PacketListenerAbstract;
import io.github.retrooper.packetevents.event.PacketListenerDynamic;

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
        PEEventManager.EVENT_MANAGER_MODERN.callEvent(event);
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
     * Register a PacketListenerDynamic listener.
     *
     * @param listener {@link PacketListenerDynamic}
     * @return Same event manager instance.
     */
    @Deprecated
    default EventManager registerListener(PacketListenerDynamic listener) {
        PEEventManager.EVENT_MANAGER_MODERN.registerListener(listener);
        return this;
    }

    /**
     * Register multiple PacketListenerDynamic listeners.
     *
     * @param listeners {@link PacketListenerDynamic}
     * @return Same event manager instance.
     */
    @Deprecated
    default EventManager registerListeners(PacketListenerDynamic... listeners) {
        PEEventManager.EVENT_MANAGER_MODERN.registerListeners(listeners);
        return this;
    }


    /**
     * Register a PacketListenerAbstract listener.
     *
     * @param listener {@link PacketListenerAbstract}
     * @return Same event manager instance.
     */
    default EventManager registerListener(PacketListenerAbstract listener) {
        if (listener != null) {
            PEEventManager.EVENT_MANAGER_MODERN.registerListener(listener);
        }
        return this;
    }

    /**
     * Register multiple PacketListenerAbstract listeners.
     *
     * @param listeners {@link PacketListenerAbstract}
     * @return Same event manager instance.
     */
    default EventManager registerListeners(PacketListenerAbstract... listeners) {
        PEEventManager.EVENT_MANAGER_MODERN.registerListeners(listeners);
        return this;
    }

    /**
     * Unregister all registered event listeners.
     * All the deprecated and the dynamic listeners will be unregistered.
     *
     * @return Same event manager instance.
     */
    default EventManager unregisterAllListeners() {
        PEEventManager.EVENT_MANAGER_MODERN.unregisterAllListeners();
        PEEventManager.EVENT_MANAGER_LEGACY.unregisterAllListeners();
        return this;
    }
}
