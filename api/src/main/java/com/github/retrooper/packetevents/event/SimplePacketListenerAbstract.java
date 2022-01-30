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

package com.github.retrooper.packetevents.event;

import com.github.retrooper.packetevents.event.simple.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class SimplePacketListenerAbstract extends PacketListenerCommon {
    public SimplePacketListenerAbstract(PacketListenerPriority priority) {
        super(priority);
    }

    public SimplePacketListenerAbstract(PacketListenerPriority priority, boolean readOnly) {
        super(priority, readOnly);
    }

    public SimplePacketListenerAbstract(PacketListenerPriority priority, Map<Byte, List<Method>> methods) {
        super(priority, methods);
    }

    public SimplePacketListenerAbstract(PacketListenerPriority priority, Map<Byte, List<Method>> methods, boolean readOnly) {
        super(priority, methods, readOnly);
    }

    public SimplePacketListenerAbstract() {
        super();
    }

    //TODO Rename PacketListenerAbstract not accessible, make its onPacketReceive and send private and make a packetlistenerabstract that implements them making them public,


    @Override
    void onPacketReceive(PacketReceiveEvent event) {
        switch (event.getConnectionState()) {
            case HANDSHAKING:
                onPacketHandshakeReceive((PacketHandshakeReceiveEvent) event);
                break;
            case STATUS:
                onPacketStatusReceive((PacketStatusReceiveEvent) event);
                break;
            case LOGIN:
                onPacketLoginReceive((PacketLoginReceiveEvent) event);
                break;
            case PLAY:
                onPacketPlayReceive((PacketPlayReceiveEvent) event);
                break;
        }
    }

    @Override
    void onPacketSend(PacketSendEvent event) {
        switch (event.getConnectionState()) {
            case HANDSHAKING:
                break;
            case STATUS:
                onPacketStatusSend((PacketStatusSendEvent) event);
                break;
            case LOGIN:
                onPacketLoginSend((PacketLoginSendEvent) event);
                break;
            case PLAY:
                onPacketPlaySend((PacketPlaySendEvent) event);
                break;
        }
    }

    public void onPacketHandshakeReceive(PacketHandshakeReceiveEvent event) {}

    public void onPacketStatusReceive(PacketStatusReceiveEvent event) {}

    public void onPacketStatusSend(PacketStatusSendEvent event) {}

    public void onPacketLoginReceive(PacketLoginReceiveEvent event) {}

    public void onPacketLoginSend(PacketLoginSendEvent event) {}

    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {}

    public void onPacketPlaySend(PacketPlaySendEvent event) {}
}
