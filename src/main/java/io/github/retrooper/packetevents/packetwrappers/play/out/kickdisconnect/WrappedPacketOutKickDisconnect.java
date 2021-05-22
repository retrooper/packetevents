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

package io.github.retrooper.packetevents.packetwrappers.play.out.kickdisconnect;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;

import java.lang.reflect.Constructor;

public final class WrappedPacketOutKickDisconnect extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> kickDisconnectConstructor;
    private String kickMessage;


    public WrappedPacketOutKickDisconnect(final NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutKickDisconnect(final String kickMessage) {

        this.kickMessage = kickMessage;
    }

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Server.KICK_DISCONNECT;

        try {
            kickDisconnectConstructor = packetClass.getConstructor(NMSUtils.iChatBaseComponentClass);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public String getKickMessage() {
        if (packet != null) {
            Object iChatBaseComponentObject = readObject(0, NMSUtils.iChatBaseComponentClass);
            return NMSUtils.readIChatBaseComponent(iChatBaseComponentObject);
        } else {
            return kickMessage;
        }
    }

    public void setKickMessage(String message) {
        if (packet != null) {
            Object iChatBaseComponent = NMSUtils.generateIChatBaseComponent(message);
            write(NMSUtils.iChatBaseComponentClass, 0, iChatBaseComponent);
        } else {
            this.kickMessage = message;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        return kickDisconnectConstructor.newInstance(NMSUtils.generateIChatBaseComponent(NMSUtils.fromStringToJSON(getKickMessage())));
    }
}
