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
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;
import java.util.Optional;

public class WrappedPacketOutResourcePackSend extends WrappedPacket implements SendableWrapper {
    private static boolean v_1_17;
    private static Constructor<?> packetConstructor;
    private String url;
    private String hash;
    private boolean forced;
    private String forcedMessage;

    public WrappedPacketOutResourcePackSend(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutResourcePackSend(String url, String hash) {
        this.url = url;
        this.hash = hash;
    }

    @Override
    protected void load() {
        v_1_17 = version.isNewerThanOrEquals(ServerVersion.v_1_17);
        try {
            if (v_1_17) {
                packetConstructor = PacketTypeClasses.Play.Server.RESOURCE_PACK_SEND.getConstructor(String.class, String.class, boolean.class, NMSUtils.iChatBaseComponentClass);
            } else {
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

    public Optional<Boolean> isForced() {
        if (!v_1_17) {
            return Optional.empty();
        }
        if (packet != null) {
            return Optional.of(readBoolean(0));
        } else {
            return Optional.of(forced);
        }
    }

    public void setForced(boolean forced) {
        if (v_1_17) {
            if (packet != null) {
                writeBoolean(0, forced);
            } else {
                this.forced = forced;
            }
        }
    }

    public Optional<String> getForcedMessage() {
        if (v_1_17) {
            if (packet != null) {
                return Optional.of(readIChatBaseComponent(0));
            } else {
                return Optional.of(forcedMessage);
            }
        } else {
            return Optional.empty();
        }
    }

    public void setForcedMessage(String forcedMessage) {
        if (v_1_17) {
            if (packet != null) {
                writeIChatBaseComponent(0, forcedMessage);
            } else {
                this.forcedMessage = forcedMessage;
            }
        }
    }

    @Override
    public boolean isSupported() {
        return version.isNewerThan(ServerVersion.v_1_7_10);
    }

    @Override
    public Object asNMSPacket() throws Exception {
        if (v_1_17) {
            return packetConstructor.newInstance(getUrl(), getHash(), isForced().get(), NMSUtils.generateIChatBaseComponent(getForcedMessage().get()));
        } else {
            return packetConstructor.newInstance(getUrl(), getHash());
        }
    }
}
