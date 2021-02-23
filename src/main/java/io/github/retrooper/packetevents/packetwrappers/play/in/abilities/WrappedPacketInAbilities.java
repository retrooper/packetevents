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
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

public final class WrappedPacketInAbilities extends WrappedPacket {
    private static boolean v_1_16_Mode;

    public WrappedPacketInAbilities(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        v_1_16_Mode = Reflection.getField(PacketTypeClasses.Play.Client.ABILITIES, boolean.class, 1) == null;
    }

    public boolean isFlying() {
        return readBoolean(v_1_16_Mode ? 0 : 1);
    }

    public void setFlying(boolean flying) {
        writeBoolean(v_1_16_Mode ? 0 : 1, flying);
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return Whether the player is vulnerable to damage or not.
     */
    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_15_2})
    public boolean isVulnerable() throws UnsupportedOperationException {
        if (v_1_16_Mode) {
            throwUnsupportedOperation();
        }
        return readBoolean(0);
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_15_2})
    public void setVulnerable(boolean vulnerable) throws UnsupportedOperationException {
        if (v_1_16_Mode) {
            throwUnsupportedOperation();
        }
        writeBoolean(0, vulnerable);
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return Whether or not the player can fly.
     */
    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_15_2})
    public boolean isFlightAllowed() throws UnsupportedOperationException {
        if (v_1_16_Mode) {
            throwUnsupportedOperation();
        }
        return readBoolean(2);
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_15_2})
    public void setFlightAllowed(boolean flightAllowed) throws UnsupportedOperationException {
        if (v_1_16_Mode) {
            throwUnsupportedOperation();
        }
        writeBoolean(2, flightAllowed);
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return Whether or not the player can break blocks instantly.
     */
    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_15_2})
    public boolean canInstantlyBuild() throws UnsupportedOperationException {
        if (v_1_16_Mode) {
            throwUnsupportedOperation();
        }
        return readBoolean(3);
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_15_2})
    public void setCanInstantlyBuild(boolean canInstantlyBuild) throws UnsupportedOperationException {
        if (v_1_16_Mode) {
            throwUnsupportedOperation();
        }
        writeBoolean(3, canInstantlyBuild);
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return The speed at which the player can fly, as a float.
     */
    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_15_2})
    public float getFlySpeed() throws UnsupportedOperationException {
        if (v_1_16_Mode) {
            throwUnsupportedOperation();
        }
        return readFloat(0);
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_15_2})
    public void setFlySpeed(float flySpeed) throws UnsupportedOperationException {
        if (v_1_16_Mode) {
            throwUnsupportedOperation();
        }
        writeFloat(0, flySpeed);
    }

    /**
     * This will return null if the server version is not available in 1.16.x and above
     *
     * @return The speed at which the player can walk, as a float.
     */
    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_15_2})
    public float getWalkSpeed() throws UnsupportedOperationException {
        if (v_1_16_Mode) {
            throwUnsupportedOperation();
        }
        return readFloat(1);
    }

    @SupportedVersions(ranges = {ServerVersion.v_1_7_10, ServerVersion.v_1_15_2})
    public void setWalkSpeed(float walkSpeed) throws UnsupportedOperationException {
        if (v_1_16_Mode) {
            throwUnsupportedOperation();
        }
        writeFloat(1, walkSpeed);
    }
}
