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

package io.github.retrooper.packetevents.packetwrappers.play.out.updatehealth;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketOutUpdateHealth extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private float health, foodSaturation;
    private int food;
    private boolean isListening;

    public WrappedPacketOutUpdateHealth(final Object packet) {
        super(packet);
        isListening = true;
    }

    /**
     * See https://wiki.vg/Protocol#Update_Health
     *
     * @param health         0 or less = dead, 20 = full HP
     * @param food           0–20
     * @param foodSaturation Seems to vary from 0.0 to 5.0 in integer increments
     */
    public WrappedPacketOutUpdateHealth(final float health, final int food, final float foodSaturation) {
        super();
        this.health = health;
        this.food = food;
        this.foodSaturation = foodSaturation;
    }

    public static void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Server.UPDATE_HEALTH;

        try {
            packetConstructor = packetClass.getConstructor(float.class, int.class, float.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get health value.
     *
     * @return Get Health
     */
    public float getHealth() {
        if (isListening) {
            return readFloat(0);
        } else {
            return health;
        }
    }

    /**
     * Get food saturation value.
     *
     * @return Get Food Saturation
     */
    public float getFoodSaturation() {
        if (isListening) {
            return readFloat(1);
        } else {
            return foodSaturation;
        }
    }

    /**
     * Get food value.
     *
     * @return Get Food
     */
    public int getFood() {
        if (isListening) {
            return readInt(0);
        } else {
            return food;
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(health, food, foodSaturation);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
