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

import com.github.retrooper.packetevents.event.simple.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public abstract class SimplePacketListenerAbstract extends PacketListenerCommon {
    public SimplePacketListenerAbstract(PacketListenerPriority priority) {
        super(priority);
    }

    public SimplePacketListenerAbstract() {
        super();
    }

    @Override
    void onPacketReceive(PacketReceiveEvent event) {
        if (event instanceof PacketHandshakeReceiveEvent) {
            onPacketHandshakeReceive((PacketHandshakeReceiveEvent) event);
        } else if (event instanceof PacketStatusReceiveEvent) {
            onPacketStatusReceive((PacketStatusReceiveEvent) event);
        } else if (event instanceof PacketLoginReceiveEvent) {
            onPacketLoginReceive((PacketLoginReceiveEvent) event);
        }
        else if (event instanceof PacketConfigReceiveEvent) {
            onPacketConfigReceive((PacketConfigReceiveEvent) event);
        }
        else if (event instanceof PacketPlayReceiveEvent) {
            onPacketPlayReceive((PacketPlayReceiveEvent) event);
        }
    }

    @Override
    void onPacketSend(PacketSendEvent event) {
        if (event instanceof PacketStatusSendEvent) {
            onPacketStatusSend((PacketStatusSendEvent) event);
        } else if (event instanceof PacketLoginSendEvent) {
            onPacketLoginSend((PacketLoginSendEvent) event);
        }
        else if (event instanceof PacketConfigSendEvent) {
            onPacketConfigSend((PacketConfigSendEvent) event);
        }
        else if (event instanceof PacketPlaySendEvent) {
            onPacketPlaySend((PacketPlaySendEvent) event);
        }
    }

    public void onPacketHandshakeReceive(PacketHandshakeReceiveEvent event) {
    }

    public void onPacketStatusReceive(PacketStatusReceiveEvent event) {
    }

    public void onPacketStatusSend(PacketStatusSendEvent event) {
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
}
