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

package io.github.retrooper.packetevents.packetwrappers.play.in.abilities;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.reflection.Reflection;

public final class WrappedPacketInAbilities extends WrappedPacket {
    private static boolean v_1_16_Mode = false;
    public static void load() {
        v_1_16_Mode = Reflection.getField(PacketTypeClasses.Play.Client.ABILITIES, boolean.class, 1) == null;
    }

    public WrappedPacketInAbilities(Object packet) {
        super(packet);
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return Whether the player is vulnerable to damage or not.
     */
    @Deprecated
    public boolean isVulnerable() throws UnsupportedOperationException {
        if(v_1_16_Mode) {
            throw new UnsupportedOperationException("This field does not exist on your version!");
        }
        return readBoolean(0);
    }

    public boolean isFlying() {
        if(v_1_16_Mode) {
            return readBoolean(0);
        }
        else {
        return readBoolean(1);
    }}

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return Whether or not the player can fly.
     */
    @Deprecated
    public boolean isFlightAllowed() {
        if(v_1_16_Mode) {
            throw new UnsupportedOperationException("This field does not exist on your version!");
        }
        return readBoolean(2);
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return Whether or not the player can break blocks instantly.
     */
    @Deprecated
    public boolean canInstantlyBuild() {
        if(v_1_16_Mode) {
            throw new UnsupportedOperationException("This field does not exist on your version!");
        }
        return readBoolean(3);
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return The speed at which the player can fly, as a float.
     */
    @Deprecated
    public float getFlySpeed() {
        if(v_1_16_Mode) {
            throw new UnsupportedOperationException("This field does not exist on your version!");
        }
        return readFloat(0);
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return The speed at which the player can walk, as a float.
     */
    @Deprecated
    public float getWalkSpeed() {
        if(v_1_16_Mode) {
            throw new UnsupportedOperationException("This field does not exist on your version!");
        }
        return readFloat(1);
    }
}
