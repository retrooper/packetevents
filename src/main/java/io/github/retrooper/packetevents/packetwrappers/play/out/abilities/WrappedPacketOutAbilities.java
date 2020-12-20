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

package io.github.retrooper.packetevents.packetwrappers.play.out.abilities;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketOutAbilities extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private boolean vulnerable, flying, allowFlight, instantBuild;
    private float flySpeed, walkSpeed;

    public WrappedPacketOutAbilities(final NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutAbilities(final boolean isVulnerable, final boolean isFlying, final boolean allowFlight, final boolean canBuildInstantly,
                                     final float flySpeed, final float walkSpeed) {
        this.vulnerable = isVulnerable;
        this.flying = isFlying;
        this.allowFlight = allowFlight;
        this.instantBuild = canBuildInstantly;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }

    @Override
protected void load() {
        Class<?> packetClass = PacketTypeClasses.Play.Server.ABILITIES;

        try {
            packetConstructor = packetClass.getConstructor(PlayerAbilitiesUtils.playerAbilitiesClass);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Should the client be vulnerable?
     *
     * @return Is Vulnerable
     */
    public boolean isVulnerable() {
        if (packet != null) {
            return readBoolean(0);
        } else {
            return vulnerable;
        }
    }

    /**
     * Should the client be flying?
     *
     * @return Is Flying
     */
    public boolean isFlying() {
        if (packet != null) {
            return readBoolean(1);
        } else {
            return flying;
        }
    }

    /**
     * Should the client be allowed to fly?
     *
     * @return Is Allowed To Fly
     */
    public boolean isFlightAllowed() {
        if (packet != null) {
            return readBoolean(2);
        } else {
            return allowFlight;
        }
    }

    /**
     * Should the client be able to build instantly?
     *
     * @return Is Allowed To Build Instantly
     */
    public boolean canInstantlyBuild() {
        if (packet != null) {
            return readBoolean(3);
        } else {
            return instantBuild;
        }
    }

    /**
     * Get the client's defined fly speed.
     *
     * @return Get Fly Speed
     */
    public float getFlySpeed() {
        if (packet != null) {
            return readFloat(0);
        } else {
            return flySpeed;
        }
    }

    /**
     * Get the client's defined walk speed.
     *
     * @return Get Walk Speed
     */
    public float getWalkSpeed() {
        if (packet != null) {
            return readFloat(1);
        } else {
            return walkSpeed;
        }
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(PlayerAbilitiesUtils.getPlayerAbilities(isVulnerable(), isFlying(), isFlightAllowed(), canInstantlyBuild(), getFlySpeed(), getWalkSpeed()));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
