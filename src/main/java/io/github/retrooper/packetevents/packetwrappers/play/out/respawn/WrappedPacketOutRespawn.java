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

package io.github.retrooper.packetevents.packetwrappers.play.out.respawn;

import io.github.retrooper.packetevents.packetwrappers.NMSPacket;
import io.github.retrooper.packetevents.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.utils.nms.NMSUtils;
import io.github.retrooper.packetevents.utils.player.GameMode;
import io.github.retrooper.packetevents.utils.world.Difficulty;
import io.github.retrooper.packetevents.utils.world.Dimension;
import org.bukkit.WorldType;

//TODO finish and test
class WrappedPacketOutRespawn extends WrappedPacket {
    private static Class<?> worldTypeClass;
    private Dimension dimension;
    private Difficulty difficulty;
    private GameMode gameMode;
    private WorldType levelType;

    public WrappedPacketOutRespawn(NMSPacket packet) {
        super(packet);
    }

    @Override
    protected void load() {
        worldTypeClass = NMSUtils.getNMSClassWithoutException("WorldType");
        net.minecraft.server.v1_7_R4.PacketPlayOutRespawn a0;
        net.minecraft.server.v1_8_R3.PacketPlayOutRespawn a1;
        net.minecraft.server.v1_9_R1.PacketPlayOutRespawn a2;
        net.minecraft.server.v1_9_R2.PacketPlayOutRespawn a3;
        net.minecraft.server.v1_12_R1.PacketPlayOutRespawn a4;
        net.minecraft.server.v1_13_R1.PacketPlayOutRespawn a5;
        net.minecraft.server.v1_13_R2.PacketPlayOutRespawn a6;
        net.minecraft.server.v1_16_R2.PacketPlayOutRespawn a7;

    }

    public Dimension getDimension() {
        if (packet != null) {
            return readDimension(0, 0);
        } else {
            return dimension;
        }
    }

    public void setDimension(Dimension dimension) {
        if (packet != null) {
            writeDimension(0, 0, dimension);
        } else {
            this.dimension = dimension;
        }
    }

    public GameMode getGameMode() {
        if (packet != null) {
            return readGameMode(0);
        } else {
            return gameMode;
        }
    }

    public void setGameMode(GameMode gameMode) {
        if (packet != null) {
            writeGameMode(0, gameMode);
        } else {
            this.gameMode = gameMode;
        }
    }

    public WorldType getWorldType() {
        if (packet != null) {
            //TODO finish
            Object worldTypeObject = readObject(0, worldTypeClass);
            WrappedPacket worldTypeWrapper = new WrappedPacket(new NMSPacket(worldTypeObject));
            String worldTypeName = worldTypeWrapper.readString(0);
            worldTypeName = worldTypeName.replace("_", "");
            for (WorldType type : WorldType.values()) {
                if (type.name().equalsIgnoreCase(worldTypeName)) {
                    return type;
                }
            }
            throw new IllegalStateException("Failed to find the correct WorldType enum constant by name " + worldTypeName);
        } else {
            return levelType;
        }
    }
}
