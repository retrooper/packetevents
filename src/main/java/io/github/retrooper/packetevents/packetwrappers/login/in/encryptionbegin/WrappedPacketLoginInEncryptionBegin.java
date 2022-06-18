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

package io.github.retrooper.packetevents.packetwrappers.login.in.encryptionbegin;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.ConditionalValue;
import io.github.retrooper.packetevents.utils.MojangEitherUtil;
import io.github.retrooper.packetevents.utils.SaltSignature;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.reflection.ClassUtil;
import io.github.retrooper.packetevents.utils.reflection.Reflection;
import io.github.retrooper.packetevents.utils.reflection.SubclassUtil;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketLoginInEncryptionBegin extends WrappedPacket {
    private static Class<?> VANILLA_SALT_SIGNATURE_CLASS;
    private static Constructor<?> VANILLA_SALT_SIGNATURE_CONSTRUCTOR;
    public WrappedPacketLoginInEncryptionBegin(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
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

    public byte[] getPublicKey() {
        return readByteArray(0);
    }

    public void setPublicKey(byte[] key) {
        writeByteArray(0, key);
    }


    public ConditionalValue<byte[], SaltSignature> getVerifyTokenOrSaltSignature() {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            ConditionalValue<byte[], Object> rawEither = readEither(0);
            if (rawEither.left().isPresent()) {
                return ConditionalValue.makeLeft(rawEither.left().get());
            }
            else {
                Object rawRight = rawEither.right().get();
                WrappedPacket rawRightWrapper = new WrappedPacket(new NMSPacket(rawRight));
                SaltSignature ss = new SaltSignature(rawRightWrapper.readLong(0), rawRightWrapper.readByteArray(0));
                return ConditionalValue.makeRight(ss);
            }
        }
        else {
            return ConditionalValue.makeLeft(readByteArray(1));
        }
    }

    public void setVerifyTokenOrSaltSignature(ConditionalValue<byte[], SaltSignature> value) {
        if (version.isNewerThanOrEquals(ServerVersion.v_1_19)) {
            Object either;
            if (value.left().isPresent()) {
                either = MojangEitherUtil.makeLeft(value.left().get());
            }
            else {
                SaltSignature ss = value.right().get();
                Object rawSS = null;
                try {
                    rawSS = VANILLA_SALT_SIGNATURE_CONSTRUCTOR.newInstance(ss.getSalt(), ss.getSignature());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                either = MojangEitherUtil.makeRight(rawSS);
            }
            write(NMSUtils.mojangEitherClass, 0, either);
        }
        else {
            writeByteArray(1, value.left().get());
        }
    }

    @Override
    public boolean isSupported() {
        return PacketTypeClasses.Login.Client.ENCRYPTION_BEGIN != null;
    }
}
