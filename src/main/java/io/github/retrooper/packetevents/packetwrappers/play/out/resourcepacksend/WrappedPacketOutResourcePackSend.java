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

package io.github.retrooper.packetevents.packetwrappers.play.out.resourcepacksend;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;

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
     * @param url  URL
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

    public void setUrl(String url) {
        if (packet != null) {
            writeString(0, url);
        } else {
            this.url = url;
        }
    }

    public String getHash() {
        if (packet != null) {
            return readString(1);
        }
        return hash;
    }

    public void setHash(String hash) {
        if (packet != null) {
            writeString(1, hash);
        } else {
            this.hash = hash;
        }
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_7_10);
    }

    @Override
    public Object asNMSPacket() throws Exception {
        return packetConstructor.newInstance(getUrl(), getHash());
    }
}
