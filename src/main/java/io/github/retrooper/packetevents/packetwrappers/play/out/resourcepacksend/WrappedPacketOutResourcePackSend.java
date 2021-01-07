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

package io.github.retrooper.packetevents.packetwrappers.play.out.resourcepacksend;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class WrappedPacketOutResourcePackSend extends WrappedPacket implements SendableWrapper {
    private static Constructor<?> packetConstructor;
    private String url;
    private String hash;

    public WrappedPacketOutResourcePackSend(NMSPacket packet) {
        super(packet);
    }

    /**
     * Unfinished docs
     * Hash may not be longer than 40 characters.
     *
     * @param url URL
     * @param hash Hash
     */
    public WrappedPacketOutResourcePackSend(String url, String hash) {
        this.url = url;
        this.hash = hash;
    }

    @Override
    protected void load() {
        try {
            if (PacketTypeClasses.Play.Server.RESOURCE_PACK_SEND != null) {
                packetConstructor = PacketTypeClasses.Play.Server.RESOURCE_PACK_SEND.getConstructor(String.class, String.class);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        if (packet != null) {
            return readString(0);
        }
        return url;
    }

    public String getHash() {
        if (packet != null) {
            return readString(1);
        }
        return hash;
    }

    @Override
    public Object asNMSPacket() {
        try {
            return packetConstructor.newInstance(getUrl(), getHash());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isSupported() {
        return version.isHigherThan(ServerVersion.v_1_7_10);
    }
}
