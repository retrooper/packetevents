/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.event;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.NonExtendable
public class EventHandler {

    private final EventContainer serverListeners = new Container();
    private final EventContainer clientListeners = new Container();

    /**
     * @see EventContainer#callEvent(PacketEvent)
     */
    @ApiStatus.Internal
    public void callEvent(PacketEvent event) {
        this.callEvent(event, null);
    }

    /**
     * @see EventContainer#callEvent(PacketEvent, Runnable)
     */
    @ApiStatus.Internal
    public void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction) {
        this.callEvent(event, postCallListenerAction, false);
    }

    /**
     * @see EventContainer#callEvent(PacketEvent, Runnable)
     */
    @ApiStatus.Internal
    public void callEvent(PacketEvent event, @Nullable Runnable postCallListenerAction, boolean clientListeners) {
        EventContainer container = clientListeners ? this.clientListeners : this.serverListeners;
        container.callEvent(event, postCallListenerAction);
    }

    /**
     * @see EventContainer#registerListener(PacketListener, PacketListenerPriority)
     */
    public PacketListenerCommon registerListener(PacketListener listener, PacketListenerPriority priority) {
        return this.registerListener(listener.asAbstract(priority));
    }

    /**
     * @see EventContainer#registerListener(PacketListenerCommon)
     */
    public PacketListenerCommon registerListener(PacketListenerCommon listener) {
        EventContainer container = listener.isPreVia() || listener.useClientVersion() ? this.clientListeners : this.serverListeners;
        return container.registerListener(listener);
    }

    /**
     * @see EventContainer#registerListeners(PacketListenerCommon...)
     */
    public PacketListenerCommon[] registerListeners(PacketListenerCommon... listeners) {
        boolean clientMut = false;
        boolean serverMut = false;
        for (PacketListenerCommon listener : listeners) {
            if (listener.isPreVia() || listener.useClientVersion()) {
                this.clientListeners.registerListenerNoRecalculation(listener);
                clientMut = true;
            } else {
                this.serverListeners.registerListenerNoRecalculation(listener);
                serverMut = true;
            }
        }
        if (clientMut) {
            this.clientListeners.recalculateListeners();
        }
        if (serverMut) {
            this.serverListeners.recalculateListeners();
        }
        return listeners;
    }

    /**
     * @see EventContainer#unregisterListener(PacketListenerCommon)
     */
    public void unregisterListener(PacketListenerCommon listener) {
        EventContainer container = listener.isPreVia() || listener.useClientVersion() ? this.clientListeners : this.serverListeners;
        container.unregisterListener(listener);
    }

    /**
     * @see EventContainer#unregisterListeners(PacketListenerCommon...)
     */
    public void unregisterListeners(PacketListenerCommon... listeners) {
        boolean clientMut = false;
        boolean serverMut = false;
        for (PacketListenerCommon listener : listeners) {
            if (listener.isPreVia() || listener.useClientVersion()) {
                this.clientListeners.unregisterListenerNoRecalculation(listener);
                clientMut = true;
            } else {
                this.serverListeners.unregisterListenerNoRecalculation(listener);
                serverMut = true;
            }
        }
        if (clientMut) {
            this.clientListeners.recalculateListeners();
        }
        if (serverMut) {
            this.serverListeners.recalculateListeners();
        }
    }

    @ApiStatus.Internal
    public void unregisterAllListeners() {
        this.serverListeners.unregisterAllListeners();
        this.clientListeners.unregisterAllListeners();
    }

    @ApiStatus.OverrideOnly
    protected void handleNewListener(EventContainer container, PacketListenerCommon listener) {
    }

    private final class Container extends EventContainer {

        @Override
        protected void handleNewListener(PacketListenerCommon listener) {
            EventHandler.this.handleNewListener(this, listener);
        }
    }
}
