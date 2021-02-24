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

package io.github.retrooper.packetevents.packetwrappers.play.out.experience;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
        }
        else {
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
        }
        else {
            this.experienceLevel = experienceLevel;
        }
    }

    public int getTotalExperience() {
        if (packet != null) {
            return readInt(1);
        }
        else {
            return totalExperience;
        }
    }

    public void setTotalExperience(int totalExperience) {
        if (packet != null) {
            writeInt(1, totalExperience);
        }
        else {
            this.totalExperience = totalExperience;
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(getExperienceBar(), getExperienceLevel(), getTotalExperience());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
