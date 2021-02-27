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
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketOutUpdateHealth extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private float health, foodSaturation;
    private int food;

    public WrappedPacketOutUpdateHealth(final NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutUpdateHealth(final float health, final int food, final float foodSaturation) {
        this.health = health;
        this.food = food;
        this.foodSaturation = foodSaturation;
    }

    @Override
    protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Server.UPDATE_HEALTH;

        try {
            packetConstructor = packetClass.getConstructor(float.class, int.class, float.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public float getHealth() {
        if (packet != null) {
            return readFloat(0);
        } else {
            return health;
        }
    }

    public void setHealth(float health) {
        if (packet != null) {
            writeFloat(0, health);
        } else {
            this.health = health;
        }
    }

    public float getFoodSaturation() {
        if (packet != null) {
            return readFloat(1);
        } else {
            return foodSaturation;
        }
    }

    public void setFoodSaturation(float foodSaturation) {
        if (packet != null) {
            writeFloat(0, foodSaturation);
        } else {
            this.foodSaturation = foodSaturation;
        }
    }

    public int getFood() {
        if (packet != null) {
            return readInt(0);
        } else {
            return food;
        }
    }

    public void setFood(int food) {
        if (packet != null) {
            writeInt(0, food);
        } else {
            this.food = food;
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(getHealth(), getFood(), getFoodSaturation());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
