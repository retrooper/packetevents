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

package io.github.retrooper.packetevents.packetwrappers.play.out.gamestatechange;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
        reasonClassType = SubclassUtil.getSubClass(PacketTypeClasses.Play.Server.GAME_STATE_CHANGE, "a");
        if (reasonClassType != null) {
            try {
                reasonClassConstructor = reasonClassType.getConstructor(int.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }
        reasonIntMode = reasonClassType == null;
        valueFloatMode = Reflection.getField(PacketTypeClasses.Play.Server.GAME_STATE_CHANGE, double.class, 0) == null;

        try {
            Class<?> valueClassType;
            if (valueFloatMode) {
                valueClassType = float.class;
            } else {
                //Just an older version(1.7.10/1.8.x or so)
                valueClassType = double.class;
            }
            if (reasonClassType == null) {
                reasonClassType = int.class;
            }
            packetConstructor = PacketTypeClasses.Play.Server.GAME_STATE_CHANGE.getConstructor(reasonClassType, valueClassType);
            reasonClassType = null;
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
                try {
                    return Reflection.getField(reasonClassType, int.class, 0).getInt(reasonObject);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return reason;
        }
        return 0;
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

    @Override
    public Object asNMSPacket() {
        if (reasonClassType == null) {
            try {
                if (valueFloatMode) {
                    return packetConstructor.newInstance(getReason(), (float) getValue());
                } else {
                    return packetConstructor.newInstance(getReason(), getValue());
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            Object reasonObject = null;
            try {
                reasonObject = reasonClassConstructor.newInstance(getReason());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            try {
                if (valueFloatMode) {
                    return packetConstructor.newInstance(reasonObject, (float) getValue());
                } else {
                    return packetConstructor.newInstance(reasonObject, getValue());
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
