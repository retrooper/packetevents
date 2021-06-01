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

package io.github.retrooper.packetevents.packetwrappers.login.out.disconnect;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;

import java.lang.reflect.Constructor;

public class WrappedPacketLoginOutDisconnect extends WrappedPacket implements SendableWrapper {

    private static Constructor<?> packetConstructor;
    private String reason;

    public WrappedPacketLoginOutDisconnect(final NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketLoginOutDisconnect(final String reason) {
        this.reason = reason;
    }

    @Override
    protected void load() {
        try {
            packetConstructor = PacketTypeClasses.Login.Server.DISCONNECT.getConstructors()[1];
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public String getReason() {
        if (packet != null) {
            return readIChatBaseComponent(0);
        } else {
            return this.reason;
        }
    }

    public void setReason(final String reason) {
        if (packet != null) {
            writeIChatBaseComponent(0, reason);
        } else {
            this.reason = reason;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        return packetConstructor.newInstance(
                NMSUtils.generateIChatBaseComponent(getReason())
        );
    }

    @Override
    public boolean isSupported() {
        return PacketTypeClasses.Login.Server.DISCONNECT != null;
    }
}
