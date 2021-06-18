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

package io.github.retrooper.packetevents.packetwrappers.play.out.updatehealth;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;

import java.lang.reflect.Constructor;

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
        try {
            packetConstructor = PacketTypeClasses.Play.Server.UPDATE_HEALTH.getConstructor(float.class, int.class, float.class);
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
    public Object asNMSPacket() throws Exception {
        return packetConstructor.newInstance(getHealth(), getFood(), getFoodSaturation());
    }
}
