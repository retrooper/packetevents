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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

public class WrapperPlayServerUpdateHealth extends PacketWrapper<WrapperPlayServerUpdateHealth> {
    private float health;
    private int food;
    private float foodSaturation;

    public WrapperPlayServerUpdateHealth(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerUpdateHealth(float health, int food, float foodSaturation) {
        super(PacketType.Play.Server.UPDATE_HEALTH);
        this.health = health;
        this.food = food;
        this.foodSaturation = foodSaturation;
    }

    @Override
    public void readData() {
        health = readFloat();
        if (serverVersion == ServerVersion.v_1_7_10) {
            food = readVarInt();
        }
        else {
            food = readShort();
        }
        foodSaturation = readFloat();
    }

    @Override
    public void readData(WrapperPlayServerUpdateHealth wrapper) {
        health = wrapper.health;
        food = wrapper.food;
        foodSaturation = wrapper.foodSaturation;
    }

    @Override
    public void writeData() {
        writeFloat(health);
        if (serverVersion == ServerVersion.v_1_7_10) {
            writeVarInt(food);
        }
        else {
            writeShort(food);
        }
        writeFloat(foodSaturation);
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public float getFoodSaturation() {
        return foodSaturation;
    }

    public void setFoodSaturation(float foodSaturation) {
        this.foodSaturation = foodSaturation;
    }
}
