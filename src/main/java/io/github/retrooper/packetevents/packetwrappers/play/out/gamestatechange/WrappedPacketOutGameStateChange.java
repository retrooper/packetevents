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

package io.github.retrooper.packetevents.packetwrappers.play.out.gamestatechange;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Constructor;

public class WrappedPacketOutGameStateChange extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor, reasonClassConstructor;
    private static Class<?> reasonClassType;
    private static boolean reasonIntMode;
    private static boolean valueFloatMode;
    private int reason;
    private double value;


    public WrappedPacketOutGameStateChange(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutGameStateChange(int reason, double value) {
        this.reason = reason;
        this.value = value;
    }

    public WrappedPacketOutGameStateChange(int reason, float value) {
        this(reason, (double) value);
    }

    @Override
    protected void load() {
        reasonClassType = SubclassUtil.getSubClass(PacketTypeClasses.Play.Server.GAME_STATE_CHANGE, 0);
        if (reasonClassType != null) {
            try {
                reasonClassConstructor = reasonClassType.getConstructor(int.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        reasonIntMode = reasonClassType == null;
        if (reasonIntMode) {
            reasonClassType = int.class;
        }
        valueFloatMode = Reflection.getField(PacketTypeClasses.Play.Server.GAME_STATE_CHANGE, double.class, 0) == null;

        try {
            Class<?> valueClassType;
            if (valueFloatMode) {
                valueClassType = float.class;
            } else {
                //Just an older version(1.7.10/1.8.x or so)
                valueClassType = double.class;
            }
            packetConstructor = PacketTypeClasses.Play.Server.GAME_STATE_CHANGE.getConstructor(reasonClassType, valueClassType);
        } catch (NullPointerException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "PacketEvents failed to find the constructor for the outbound Game state packet wrapper.");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public int getReason() {
        if (packet != null) {
            if (reasonIntMode) {
                return readInt(0);
            } else {
                //this packet is obfuscated quite strongly(1.16), so we must do this
                Object reasonObject = readObject(12, reasonClassType);
                WrappedPacket reasonObjWrapper = new WrappedPacket(new NMSPacket(reasonObject));
                return reasonObjWrapper.readInt(0);
            }
        } else {
            return reason;
        }
    }

    public void setReason(int reason) {
        if (packet != null) {
            if (reasonIntMode) {
                writeInt(0, reason);
            } else {
                //this packet is obfuscated quite strongly(1.16), so we must do this
                Object reasonObj = readObject(12, reasonClassType);
                WrappedPacket reasonObjWrapper = new WrappedPacket(new NMSPacket(reasonObj));
                reasonObjWrapper.writeInt(0, reason);
            }
        } else {
            this.reason = reason;
        }
    }

    public double getValue() {
        if (packet != null) {
            if (valueFloatMode) {
                return readFloat(0);
            } else {
                return readDouble(0);
            }
        } else {
            return value;
        }
    }

    public void setValue(double value) {
        if (packet != null) {
            if (valueFloatMode) {
                writeFloat(0, (float) value);
            } else {
                writeDouble(0, value);
            }
        } else {
            this.value = value;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        if (reasonClassType == null) {
            if (valueFloatMode) {
                return packetConstructor.newInstance(getReason(), (float) getValue());
            } else {
                return packetConstructor.newInstance(getReason(), getValue());
            }
        } else {
            Object reasonObject = reasonClassConstructor.newInstance(getReason());
            if (valueFloatMode) {
                return packetConstructor.newInstance(reasonObject, (float) getValue());
            } else {
                return packetConstructor.newInstance(reasonObject, getValue());
            }
        }
    }
}
