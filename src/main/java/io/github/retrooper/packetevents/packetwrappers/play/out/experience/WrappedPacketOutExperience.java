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

package io.github.retrooper.packetevents.packetwrappers.play.out.experience;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;

import java.lang.reflect.Constructor;

public class WrappedPacketOutExperience extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private float experienceBar;
    private int experienceLevel, totalExperience;

    public WrappedPacketOutExperience(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutExperience(float experienceBar, int experienceLevel,
                                      int totalExperience) {
        this.experienceBar = experienceBar;
        this.experienceLevel = experienceLevel;
        this.totalExperience = totalExperience;
    }

    @Override
    protected void load() {
        try {
            packetConstructor = PacketTypeClasses.Play.Server.EXPERIENCE.getConstructor(float.class,
                    int.class, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public float getExperienceBar() {
        if (packet != null) {
            return readFloat(0);
        } else {
            return experienceBar;
        }
    }

    public void setExperienceBar(float experienceBar) {
        if (packet != null) {
            writeFloat(0, experienceBar);
        } else {
            this.experienceBar = experienceBar;
        }
    }

    public int getExperienceLevel() {
        if (packet != null) {
            return readInt(0);
        } else {
            return experienceLevel;
        }
    }

    public void setExperienceLevel(int experienceLevel) {
        if (packet != null) {
            writeInt(0, experienceLevel);
        } else {
            this.experienceLevel = experienceLevel;
        }
    }

    public int getTotalExperience() {
        if (packet != null) {
            return readInt(1);
        } else {
            return totalExperience;
        }
    }

    public void setTotalExperience(int totalExperience) {
        if (packet != null) {
            writeInt(1, totalExperience);
        } else {
            this.totalExperience = totalExperience;
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        return packetConstructor.newInstance(getExperienceBar(), getExperienceLevel(), getTotalExperience());
    }
}
