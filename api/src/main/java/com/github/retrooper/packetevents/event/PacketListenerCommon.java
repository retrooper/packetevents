/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

/**
 * Abstract packet listener.
 *
 * @author retrooper
 * @since 1.8
 */
public abstract class PacketListenerCommon {
    private final ListenerPriority priority;

    /**
     * @param priority the priority of this packet listener.
     */
    public PacketListenerCommon(ListenerPriority priority) {
        this.priority = priority;
    }

    /**
     * Default priority is {@link ListenerPriority#NORMAL}.
     */
    public PacketListenerCommon() {
        this(ListenerPriority.NORMAL);
    }

    /**
     * @deprecated use {@link PacketListenerCommon(ListenerPriority)} instead
     */
    @Deprecated
    public PacketListenerCommon(PacketListenerPriority priority) {
        this(ListenerPriority.fromLegacy(priority));
    }

    /**
     * @return the priority of this packet listener.
     * @deprecated use {@link #priority()} instead
     */
    @Deprecated
    public PacketListenerPriority getPriority() {
        return this.priority.legacyType();
    }

    /**
     * @return the priority of this packet listener.
     */
    public ListenerPriority priority() {
        // no point in providing a backwards compat if method returns the modern type
        // + the new type has to be exposed in one way or another (the event manager needs it), cant get around this one :(
        return this.priority;
    }

    public void onUserConnect(UserConnectEvent event) {
    }

    public void onUserLogin(UserLoginEvent event) {
    }

    public void onUserDisconnect(UserDisconnectEvent event) {
    }

    void onPacketReceive(PacketReceiveEvent event) {
    }

    void onPacketSend(PacketSendEvent event) {
    }

    public void onPacketEventExternal(PacketEvent event) {
    }

}
