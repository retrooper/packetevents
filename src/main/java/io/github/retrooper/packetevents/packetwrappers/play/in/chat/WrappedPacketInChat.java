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

package io.github.retrooper.packetevents.packetwrappers.play.in.chat;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.SaltSignature;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.Optional;

public final class WrappedPacketInChat extends WrappedPacket {
    private static Class<?> VANILLA_SALT_SIGNATURE_CLASS;
    private static Constructor<?> VANILLA_SALT_SIGNATURE_CONSTRUCTOR;
    public WrappedPacketInChat(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            Class<?> minecraftEncryptionClass = Reflection.getClassByNameWithoutException("net.minecraft.util.MinecraftEncryption");
            if (minecraftEncryptionClass != null) {
                VANILLA_SALT_SIGNATURE_CLASS = SubclassUtil.getSubClass(minecraftEncryptionClass, "b");
                try {
                    VANILLA_SALT_SIGNATURE_CONSTRUCTOR = VANILLA_SALT_SIGNATURE_CLASS.getConstructor(long.class, byte[].class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getMessage() {
        return readString(0);
    }

    public void setMessage(String message) {
        writeString(0, message);
    }

    public Optional<Instant> getInstant() {
        return version.isNewerThanOrEquals(ServerVersion.v_1_19) ? Optional.of(readObject(0, Instant.class)) : Optional.empty();
    }

    public void setInstant(Instant instant) {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            writeObject(0, instant);
        }
    }

    public Optional<SaltSignature> getSaltSignature() {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            Object rawSS = readObject(0, VANILLA_SALT_SIGNATURE_CLASS);
            WrappedPacket rawSSWrapper = new WrappedPacket(new NMSPacket(rawSS));
            SaltSignature ss = new SaltSignature(rawSSWrapper.readLong(0), rawSSWrapper.readByteArray(0));
            return Optional.of(ss);
        }
        else {
            return Optional.empty();
        }
    }

    public void setSaltSignature(SaltSignature saltSignature) {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            Object rawSS = null;
            try {
                rawSS = VANILLA_SALT_SIGNATURE_CONSTRUCTOR.newInstance(saltSignature.getSalt(), saltSignature.getSignature());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            write(VANILLA_SALT_SIGNATURE_CLASS, 0, rawSS);
        }
    }

    public boolean isSignedPreview() {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            return readBoolean(0);
        }
        else {
            return false;
        }
    }

    public void setSignedPreview(boolean signedPreview) {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            writeBoolean(0, signedPreview);
        }
    }
}
