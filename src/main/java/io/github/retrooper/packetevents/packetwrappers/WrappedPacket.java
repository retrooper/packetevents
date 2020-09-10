/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.packetwrappers;

import io.github.retrooper.packetevents.enums.ServerVersion;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.in.abilities.WrappedPacketInAbilities;
import io.github.retrooper.packetevents.packetwrappers.in.armanimation.WrappedPacketInArmAnimation;
import io.github.retrooper.packetevents.packetwrappers.in.blockdig.WrappedPacketInBlockDig;
import io.github.retrooper.packetevents.packetwrappers.in.blockplace.WrappedPacketInBlockPlace;
import io.github.retrooper.packetevents.packetwrappers.in.chat.WrappedPacketInChat;
import io.github.retrooper.packetevents.packetwrappers.in.clientcommand.WrappedPacketInClientCommand;
import io.github.retrooper.packetevents.packetwrappers.in.custompayload.WrappedPacketInCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.in.helditemslot.WrappedPacketInHeldItemSlot;
import io.github.retrooper.packetevents.packetwrappers.in.keepalive.WrappedPacketInKeepAlive;
import io.github.retrooper.packetevents.packetwrappers.in.settings.WrappedPacketInSettings;
import io.github.retrooper.packetevents.packetwrappers.in.steervehicle.WrappedPacketInSteerVehicle;
import io.github.retrooper.packetevents.packetwrappers.in.transaction.WrappedPacketInTransaction;
import io.github.retrooper.packetevents.packetwrappers.in.useentity.WrappedPacketInUseEntity;
import io.github.retrooper.packetevents.packetwrappers.in.windowclick.WrappedPacketInWindowClick;
import io.github.retrooper.packetevents.packetwrappers.out.abilities.WrappedPacketOutAbilities;
import io.github.retrooper.packetevents.packetwrappers.out.animation.WrappedPacketOutAnimation;
import io.github.retrooper.packetevents.packetwrappers.out.chat.WrappedPacketOutChat;
import io.github.retrooper.packetevents.packetwrappers.out.custompayload.WrappedPacketOutCustomPayload;
import io.github.retrooper.packetevents.packetwrappers.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.out.keepalive.WrappedPacketOutKeepAlive;
import io.github.retrooper.packetevents.packetwrappers.out.kickdisconnect.WrappedPacketOutKickDisconnect;
import io.github.retrooper.packetevents.packetwrappers.out.position.WrappedPacketOutPosition;
import io.github.retrooper.packetevents.packetwrappers.out.transaction.WrappedPacketOutTransaction;
import io.github.retrooper.packetevents.packetwrappers.out.updatehealth.WrappedPacketOutUpdateHealth;
import io.github.retrooper.packetevents.reflectionutils.Reflection;
import org.bukkit.entity.Player;

public class WrappedPacket implements WrapperPacketReader {
    public static ServerVersion version;
    protected final Player player;
    protected Object packet;
    private Class<?> packetClass;

    public WrappedPacket() {
        this(null);
    }

    public WrappedPacket(final Object packet) {
        this(null, packet);
    }

    public WrappedPacket(final Player player, final Object packet) {
        this.player = player;
        if (packet == null) return;
        this.packet = packet;
        this.packetClass = packet.getClass();
        if (packet.getClass().getSuperclass().equals(PacketTypeClasses.Client.FLYING)) {
            packetClass = PacketTypeClasses.Client.FLYING;
        } else if (packet.getClass().getSuperclass().equals(PacketTypeClasses.Server.ENTITY)) {
            packetClass = PacketTypeClasses.Server.ENTITY;
        }
        setup();
    }

    public static void loadAllWrappers() {
        //SERVER BOUND
        WrappedPacketInAbilities.load();
        WrappedPacketInArmAnimation.load();
        WrappedPacketInBlockDig.load();
        WrappedPacketInBlockPlace.load();
        WrappedPacketInChat.load();
        WrappedPacketInClientCommand.load();
        WrappedPacketInCustomPayload.load();
        WrappedPacketInEntityAction.load();
        WrappedPacketInFlying.load();
        WrappedPacketInHeldItemSlot.load();
        WrappedPacketInKeepAlive.load();
        WrappedPacketInSettings.load();
        WrappedPacketInSteerVehicle.load();
        WrappedPacketInTransaction.load();
        WrappedPacketInUseEntity.load();
        WrappedPacketInWindowClick.load();

        //CLIENTBOUND
        WrappedPacketOutAbilities.load();
        WrappedPacketOutAnimation.load();
        WrappedPacketOutChat.load();
        WrappedPacketOutEntity.load();
        WrappedPacketOutEntityVelocity.load();
        WrappedPacketOutKeepAlive.load();
        WrappedPacketOutKickDisconnect.load();
        WrappedPacketOutPosition.load();
        WrappedPacketOutTransaction.load();
        WrappedPacketOutUpdateHealth.load();
        WrappedPacketOutCustomPayload.load();
    }

    protected void setup() {

    }

    @Override
    public boolean readBoolean(int index) {
        try {
            return Reflection.getField(packetClass, boolean.class, index).getBoolean(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public byte readByte(int index) {
        try {
            return Reflection.getField(packetClass, byte.class, index).getByte(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public short readShort(int index) {
        try {
            return Reflection.getField(packetClass, short.class, index).getShort(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int readInt(int index) {
        try {
            return Reflection.getField(packetClass, int.class, index).getInt(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public long readLong(int index) {
        try {
            return Reflection.getField(packetClass, long.class, index).getLong(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public float readFloat(int index) {
        try {
            return Reflection.getField(packetClass, float.class, index).getFloat(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public double readDouble(int index) {
        try {
            return Reflection.getField(packetClass, double.class, index).getDouble(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Object readObject(int index, Class<?> type) {
        try {
            return Reflection.getField(packetClass, type, index).get(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object readAnyObject(int index) {
        try {
            return Reflection.getField(packetClass, index).get(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String readString(int index) {
        return readObject(index, String.class).toString();
    }
}
