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

package io.github.retrooper.packetevents.packetwrappers.out.abilities;

import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.Sendable;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class WrappedPacketOutAbilities extends WrappedPacket implements Sendable {
    private static Class<?> packetClass;
    private static Constructor<?> packetConstructor;
    private boolean vulnerable, flying, allowFlight, instantBuild;
    private float flySpeed, walkSpeed;
    public WrappedPacketOutAbilities(final Object packet) {
        super(packet);
    }

    public WrappedPacketOutAbilities(final boolean isVulnerable, final boolean isFlying, final boolean allowFlight, final boolean canBuildInstantly,
                                     final float flySpeed, final float walkSpeed) {
        super();
        this.vulnerable = isVulnerable;
        this.flying = isFlying;
        this.allowFlight = allowFlight;
        this.instantBuild = canBuildInstantly;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }

    public static void load() {
        packetClass = PacketTypeClasses.Server.ABILITIES;

        try {
            packetConstructor = packetClass.getConstructor(PlayerAbilitiesUtils.playerAbilitiesClass);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setup() {
        try {
            this.vulnerable = Reflection.getField(packetClass, boolean.class, 0).getBoolean(packet);
            this.flying = Reflection.getField(packetClass, boolean.class, 1).getBoolean(packet);
            this.allowFlight = Reflection.getField(packetClass, boolean.class, 2).getBoolean(packet);
            this.instantBuild = Reflection.getField(packetClass, boolean.class, 3).getBoolean(packet);

            this.flySpeed = Reflection.getField(packetClass, float.class, 0).getFloat(packet);
            this.walkSpeed = Reflection.getField(packetClass, float.class, 0).getFloat(packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public boolean isFlying() {
        return flying;
    }

    public boolean isFlightAllowed() {
        return allowFlight;
    }

    public boolean canInstantlyBuild() {
        return instantBuild;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public float getWalkSpeed() {
        return walkSpeed;
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(PlayerAbilitiesUtils.getPlayerAbilities(vulnerable, flying, allowFlight, instantBuild, flySpeed, walkSpeed));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
