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

import java.util.Optional;

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

    public Optional<Boolean> isVulnerable() {
        if (v_1_16_Mode) {
            return Optional.empty();
        }
        return Optional.of(readBoolean(0));
    }

    public void setVulnerable(boolean vulnerable) {
        if (!v_1_16_Mode) {
            writeBoolean(0, vulnerable);
        }
    }


    public Optional<Boolean> isFlightAllowed() {
        if (v_1_16_Mode) {
            return Optional.empty();
        }
        return Optional.of(readBoolean(2));
    }

    public void setFlightAllowed(boolean flightAllowed) {
        if (!v_1_16_Mode) {
            writeBoolean(2, flightAllowed);
        }
    }

    public Optional<Boolean> canInstantlyBuild() {
        if (v_1_16_Mode) {
            return Optional.empty();
        }
        return Optional.of(readBoolean(3));
    }

    public void setCanInstantlyBuild(boolean canInstantlyBuild) {
        if (!v_1_16_Mode) {
            writeBoolean(3, canInstantlyBuild);
        }
    }

    public Optional<Float> getFlySpeed() {
        if (v_1_16_Mode) {
            return Optional.empty();
        }
        return Optional.of(readFloat(0));
    }

    public void setFlySpeed(float flySpeed) {
        if (!v_1_16_Mode) {
            writeFloat(0, flySpeed);
        }
    }

    public Optional<Float> getWalkSpeed() {
        if (v_1_16_Mode) {
            return Optional.empty();
        }
        return Optional.of(readFloat(1));
    }

    public void setWalkSpeed(float walkSpeed) {
        if (!v_1_16_Mode) {
            writeFloat(1, walkSpeed);
        }
    }
}
