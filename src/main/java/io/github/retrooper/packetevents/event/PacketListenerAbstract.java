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

package io.github.retrooper.packetevents.event;

import io.github.retrooper.packetevents.event.impl.*;
import io.github.retrooper.packetevents.event.priority.PacketEventPriority;
import io.github.retrooper.packetevents.utils.immutableset.ImmutableSetCustom;

/**
 * Abstract packet listener.
 *
 * @author retrooper
 * @since 1.8
 */
public abstract class PacketListenerAbstract {
    private final PacketListenerPriority priority;
    public ImmutableSetCustom<Byte> serverSidedStatusAllowance;
    public ImmutableSetCustom<Byte> serverSidedLoginAllowance;
    public ImmutableSetCustom<Byte> serverSidedPlayAllowance;

    public ImmutableSetCustom<Byte> clientSidedStatusAllowance;
    public ImmutableSetCustom<Byte> clientSidedHandshakeAllowance;
    public ImmutableSetCustom<Byte> clientSidedLoginAllowance;
    public ImmutableSetCustom<Byte> clientSidedPlayAllowance;

    @Deprecated
    public PacketListenerAbstract(final PacketEventPriority priority) {
        this(PacketListenerPriority.getById(priority.getPriorityValue()));
    }

    public PacketListenerAbstract(PacketListenerPriority priority) {
        this.priority = priority;
        this.serverSidedStatusAllowance = null;
        this.serverSidedLoginAllowance = null;
        this.serverSidedPlayAllowance = null;

        this.clientSidedStatusAllowance = null;
        this.clientSidedHandshakeAllowance = null;
        this.clientSidedLoginAllowance = null;
        this.clientSidedPlayAllowance = null;
    }


    public PacketListenerAbstract() {
        this(PacketListenerPriority.NORMAL);
    }

    public PacketListenerPriority getPriority() {
        return priority;
    }

    public void onPacketStatusReceive(PacketStatusReceiveEvent event) {
    }

    public void onPacketStatusSend(PacketStatusSendEvent event) {
    }

    public void onPacketHandshakeReceive(PacketHandshakeReceiveEvent event) {
    }

    public void onPacketLoginReceive(PacketLoginReceiveEvent event) {
    }

    public void onPacketLoginSend(PacketLoginSendEvent event) {
    }

    public void onPacketConfigReceive(PacketConfigReceiveEvent event) {
    }

    public void onPacketConfigSend(PacketConfigSendEvent event) {
    }

    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
    }

    public void onPacketPlaySend(PacketPlaySendEvent event) {
    }

    public void onPostPacketPlayReceive(PostPacketPlayReceiveEvent event) {
    }

    public void onPostPacketPlaySend(PostPacketPlaySendEvent event) {
    }

    public void onPostPlayerInject(PostPlayerInjectEvent event) {
    }

    public void onPlayerInject(PlayerInjectEvent event) {
    }

    public void onPlayerEject(PlayerEjectEvent event) {
    }

    public void onPacketEventExternal(PacketEvent event) {
    }

    public final void addServerSidedStatusFilter(Byte... statusPacketIDs) {
        if (this.serverSidedStatusAllowance == null) {
            this.serverSidedStatusAllowance = new ImmutableSetCustom<>(statusPacketIDs);
        } else {
            this.serverSidedStatusAllowance.addAll(statusPacketIDs);
        }
    }

    public final void addServerSidedLoginFilter(Byte... loginPacketIDs) {
        if (this.serverSidedLoginAllowance == null) {
            this.serverSidedLoginAllowance = new ImmutableSetCustom<>(loginPacketIDs);
        } else {
            this.serverSidedLoginAllowance.addAll(loginPacketIDs);
        }
    }

    public final void addServerSidedPlayFilter(Byte... playPacketIDs) {
        if (this.serverSidedPlayAllowance == null) {
            this.serverSidedPlayAllowance = new ImmutableSetCustom<>(playPacketIDs);
        } else {
            this.serverSidedPlayAllowance.addAll(playPacketIDs);
        }
    }

    public final void addClientSidedStatusFilter(Byte... statusPacketIDs) {
        if (this.clientSidedStatusAllowance == null) {
            this.clientSidedStatusAllowance = new ImmutableSetCustom<>(statusPacketIDs);
        } else {
            this.clientSidedStatusAllowance.addAll(statusPacketIDs);
        }
    }

    public final void addClientSidedHandshakeFilter(Byte... handshakePacketIDs) {
        if (this.clientSidedHandshakeAllowance == null) {
            this.clientSidedHandshakeAllowance = new ImmutableSetCustom<>(handshakePacketIDs);
        } else {
            this.clientSidedHandshakeAllowance.addAll(handshakePacketIDs);
        }
    }

    public final void addClientSidedLoginFilter(Byte... loginPacketIDs) {
        if (this.clientSidedLoginAllowance == null) {
            this.clientSidedLoginAllowance = new ImmutableSetCustom<>(loginPacketIDs);
        } else {
            this.clientSidedLoginAllowance.addAll(loginPacketIDs);
        }
    }

    public final void addClientSidedPlayFilter(Byte... playPacketIDs) {
        if (this.clientSidedPlayAllowance == null) {
            this.clientSidedPlayAllowance = new ImmutableSetCustom<>(playPacketIDs);
        } else {
            this.clientSidedPlayAllowance.addAll(playPacketIDs);
        }
    }

    public final void filterAll() {
        filterServerSidedStatus();
        filterServerSidedLogin();
        filterServerSidedPlay();

        filterClientSidedStatus();
        filterClientSidedHandshake();
        filterClientSidedLogin();
        filterClientSidedPlay();
    }

    public final void filterServerSidedStatus() {
        this.serverSidedStatusAllowance = new ImmutableSetCustom<>();
    }

    public final void filterServerSidedLogin() {
        this.serverSidedLoginAllowance = new ImmutableSetCustom<>();
    }

    public final void filterServerSidedPlay() {
        this.serverSidedPlayAllowance = new ImmutableSetCustom<>();
    }

    public final void filterClientSidedStatus() {
        this.clientSidedStatusAllowance = new ImmutableSetCustom<>();
    }

    public final void filterClientSidedHandshake() {
        this.clientSidedHandshakeAllowance = new ImmutableSetCustom<>();
    }

    public final void filterClientSidedLogin() {
        this.clientSidedLoginAllowance = new ImmutableSetCustom<>();
    }

    public final void filterClientSidedPlay() {
        this.clientSidedPlayAllowance = new ImmutableSetCustom<>();
    }
}
