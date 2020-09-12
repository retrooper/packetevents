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
import io.github.retrooper.packetevents.packetwrappers.out.entity.WrappedPacketOutEntity;
import io.github.retrooper.packetevents.packetwrappers.out.entityvelocity.WrappedPacketOutEntityVelocity;
import io.github.retrooper.packetevents.packetwrappers.out.keepalive.WrappedPacketOutKeepAlive;
import io.github.retrooper.packetevents.packetwrappers.out.kickdisconnect.WrappedPacketOutKickDisconnect;
import io.github.retrooper.packetevents.packetwrappers.out.position.WrappedPacketOutPosition;
import io.github.retrooper.packetevents.packetwrappers.out.transaction.WrappedPacketOutTransaction;
import io.github.retrooper.packetevents.packetwrappers.out.updatehealth.WrappedPacketOutUpdateHealth;
import org.bukkit.entity.Player;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class WrappedPacket implements WrapperPacketReader {
    protected final List<Field> fields = new ArrayList<>();
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
        if(packet == null) {
            this.player = null;
            return;
        }
        for (Field f : packet.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            fields.add(f);
        }
        this.player = player;
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
        //WrappedPacketOutCustomPayload.load();
    }

    protected void setup() {

    }

    @Override
    public boolean readBoolean(int index) {
        return (boolean) readObject(index, boolean.class);

    @Override
    public byte readByte(int index) {
        return (byte) readObject(index, byte.class);
    }

    @Override
    public short readShort(int index) {
        return (short) readObject(index, short.class);
    }

    @Override
    public int readInt(int index) {
        return (int) readObject(index, int.class);
    }

    @Override
    public long readLong(int index) {
        return (long) readObject(index, long.class);
    }

    @Override
    public float readFloat(int index) {
        return (float) readObject(index, float.class);
    }

    @Override
    public double readDouble(int index) {
        return (double) readObject(index, double.class);
    }

    @Override
    public Object readObject(int index, Class<?> type) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        int currentIndex = 0;
        for (Field f : fields) {
            if (type.isAssignableFrom(f.getType())) {
                if (index == currentIndex++) {
                    try {
                        MethodHandle handle = lookup.unreflectGetter(f);
                        if(handle == null) {
                            System.out.println("Getter not found");
                            return f.get(packet);
                        }
                        return handle.invoke(packet);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Object readAnyObject(int index) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        int currentIndex = 0;
        for (Field f : fields) {
            if (index == currentIndex++) {
                try {
                    return lookup.unreflectGetter(f).invoke(packet);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public String readString(int index) {
        return readObject(index, String.class).toString();
    }
}
