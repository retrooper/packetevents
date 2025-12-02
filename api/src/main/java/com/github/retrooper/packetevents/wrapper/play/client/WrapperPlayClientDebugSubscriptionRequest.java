/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.debug.DebugSubscription;
import com.github.retrooper.packetevents.protocol.debug.DebugSubscriptions;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

/**
 * Mojang name: ServerboundDebugSubscriptionRequestPacket
 *
 * @versions 1.21.9+
 */
@NullMarked
public class WrapperPlayClientDebugSubscriptionRequest extends PacketWrapper<WrapperPlayClientDebugSubscriptionRequest> {

    private Set<DebugSubscription<?>> subscriptions;

    public WrapperPlayClientDebugSubscriptionRequest(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientDebugSubscriptionRequest(Set<DebugSubscription<?>> subscriptions) {
        super(PacketType.Play.Client.DEBUG_SUBSCRIPTION_REQUEST);
        this.subscriptions = subscriptions;
    }

    @Override
    public void read() {
        this.subscriptions = this.readSet(ew -> ew.readMappedEntity(DebugSubscriptions.getRegistry()));
    }

    @Override
    public void write() {
        this.writeSet(this.subscriptions, PacketWrapper::writeMappedEntity);
    }

    @Override
    public void copy(WrapperPlayClientDebugSubscriptionRequest wrapper) {
        this.subscriptions = wrapper.subscriptions;
    }

    public Set<DebugSubscription<?>> getSubscriptions() {
        return this.subscriptions;
    }

    public void setSubscriptions(Set<DebugSubscription<?>> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
