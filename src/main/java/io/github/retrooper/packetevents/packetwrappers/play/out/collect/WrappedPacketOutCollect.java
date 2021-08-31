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

package io.github.retrooper.packetevents.packetwrappers.play.out.collect;

import io.github.retrooper.packetevents.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.packetwrappers.api.SendableWrapper;
import io.github.retrooper.packetevents.utils.server.ServerVersion;

import java.lang.reflect.Constructor;
import java.util.Optional;

public class WrappedPacketOutCollect extends WrappedPacket implements SendableWrapper {
    private static boolean v_1_11;
    private static Constructor<?> packetConstructor;
    private int collectedEntityId, collectorEntityId, itemCount;

    public WrappedPacketOutCollect(NMSPacket packet) {
        super(packet);
    }

    public WrappedPacketOutCollect(int collectedEntityId, int collectorEntityId, int itemCount) {
        this.collectedEntityId = collectedEntityId;
        this.collectorEntityId = collectorEntityId;
        this.itemCount = itemCount;
    }

    @Override
    protected void load() {
        v_1_11 = version.isNewerThanOrEquals(ServerVersion.v_1_11);
        try {
            if (v_1_11) {
                packetConstructor = PacketTypeClasses.Play.Server.COLLECT.getConstructor(int.class, int.class, int.class);
            } else {
                packetConstructor = PacketTypeClasses.Play.Server.COLLECT.getConstructor(int.class, int.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCollectedEntityId() {
        if (packet != null) {
            return readInt(0);
        } else {
            return this.collectedEntityId;
        }
    }

    public void setCollectedEntityId(int id) {
        if (packet != null) {
            writeInt(0, id);
        } else {
            this.collectedEntityId = id;
        }
    }

    public int getCollectorEntityId() {
        if (packet != null) {
            return readInt(1);
        } else {
            return this.collectorEntityId;
        }
    }

    public void setCollectorEntityId(int id) {
        if (packet != null) {
            writeInt(1, id);
        } else {
            this.collectorEntityId = id;
        }
    }

    public Optional<Integer> getItemCount() {
        if (!v_1_11) {
            return Optional.empty();
        }
        if (packet != null) {
            return Optional.of(readInt(2));
        } else {
            return Optional.of(this.itemCount);
        }
    }

    public void setItemCount(int count) {
        if (v_1_11) {
            if (packet != null) {
                writeInt(2, count);
            } else {
                this.itemCount = count;
            }
        }
    }

    @Override
    public Object asNMSPacket() throws Exception {
        /*
         * On newer versions the packet has an extra field which is the picked up item count.
         */
        return v_1_11
                ? packetConstructor.newInstance(getCollectedEntityId(), getCollectorEntityId(), (int) (getItemCount().get()))
                : packetConstructor.newInstance(getCollectedEntityId(), getCollectorEntityId());
    }
}
