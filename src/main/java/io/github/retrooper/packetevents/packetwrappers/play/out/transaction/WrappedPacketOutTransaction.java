/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2016-2021 retrooper and contributors
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

package io.github.retrooper.packetevents.packetwrappers.play.out.transaction;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;

import java.lang.reflect.Constructor;

public class WrappedPacketOutTransaction extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private int windowID;
    private short actionNumber;
    private boolean accepted;


    public WrappedPacketOutTransaction(final NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutTransaction(final int windowID, final short actionNumber, final boolean accepted) {
        this.windowID = windowID;
        this.actionNumber = actionNumber;
        this.accepted = accepted;
    }

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Server.TRANSACTION;
        try {
            packetConstructor = packetClass.getConstructor(int.class, short.class, boolean.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getWindowId() {
        if (packet != null) {
            return readInt(0);
        } else {
            return windowID;
        }
    }

    public void setWindowId(int windowID) {
        if (packet != null) {
            writeInt(0, windowID);
        } else {
            this.windowID = windowID;
        }
    }

    public short getActionNumber() {
        if (packet != null) {
            return readShort(0);
        } else {
            return actionNumber;
        }
    }

    public void setActionNumber(short actionNumber) {
        if (packet != null) {
            writeShort(0, actionNumber);
        } else {
            this.actionNumber = actionNumber;
        }
    }

    public boolean isAccepted() {
        if (packet != null) {
            return readBoolean(0);
        } else {
            return accepted;
        }
    }

    public void setAccepted(boolean isAccepted) {
        if (packet != null) {
            writeBoolean(0, isAccepted);
        } else {
            this.accepted = isAccepted;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        return packetConstructor.newInstance(getWindowId(), getActionNumber(), isAccepted());
    }

}
