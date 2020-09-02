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

package io.github.retrooper.packetevents.packetwrappers.in.abilities;

import io.github.retrooper.packetevents.annotations.Nullable;
import io.github.retrooper.packetevents.packet.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.api.WrappedPacket;
import io.github.retrooper.packetevents.reflectionutils.Reflection;

public final class WrappedPacketInAbilities extends WrappedPacket {
    private static Class<?> abilitiesClass;
    private static boolean isMoreThanOneBoolPresent;
    private boolean isVulnerable;
    private boolean isFlying;
    private boolean allowFly;
    private boolean instantBuild;
    private float flySpeed;
    private float walkSpeed;
    public WrappedPacketInAbilities(Object packet) {
        super(packet);
    }

    public static void load() {
        abilitiesClass = PacketTypeClasses.Client.ABILITIES;
        isMoreThanOneBoolPresent = Reflection.getField(abilitiesClass, boolean.class, 1) != null;
    }

    @Override
    protected void setup() {
        try {
            if (isMoreThanOneBoolPresent) {
                this.isVulnerable = Reflection.getField(abilitiesClass, boolean.class, 0).getBoolean(packet);
                this.isFlying = Reflection.getField(abilitiesClass, boolean.class, 1).getBoolean(packet);
                this.allowFly = Reflection.getField(abilitiesClass, boolean.class, 2).getBoolean(packet);
                this.instantBuild = Reflection.getField(abilitiesClass, boolean.class, 3).getBoolean(packet);
                this.flySpeed = Reflection.getField(abilitiesClass, float.class, 0).getFloat(packet);
                this.walkSpeed = Reflection.getField(abilitiesClass, float.class, 1).getFloat(packet);
            } else {
                this.isFlying = Reflection.getField(abilitiesClass, boolean.class, 0).getBoolean(packet);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return Whether the player is vulnerable to damage or not.
     */
    @Deprecated
    @Nullable
    public Boolean isVulnerable() {
        return isVulnerable;
    }

    public boolean isFlying() {
        return isFlying;
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return Whether or not the player can fly.
     */
    @Deprecated
    @Nullable
    public Boolean isFlightAllowed() {
        return allowFly;
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return Whether or not the player can break blocks instantly.
     */
    @Deprecated
    @Nullable
    public Boolean canInstantlyBuild() {
        return instantBuild;
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return The speed at which the player can fly, as a float.
     */
    @Deprecated
    @Nullable
    public Float getFlySpeed() {
        return flySpeed;
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return The speed at which the player can walk, as a float.
     */
    @Deprecated
    @Nullable
    public Float getWalkSpeed() {
        return walkSpeed;
    }
}
