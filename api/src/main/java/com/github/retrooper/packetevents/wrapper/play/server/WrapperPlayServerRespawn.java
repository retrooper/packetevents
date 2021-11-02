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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.impl.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.protocol.world.WorldType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Optional;

public class WrapperPlayServerRespawn extends PacketWrapper<WrapperPlayServerRespawn> {
    private Dimension dimension;
    private Optional<String> worldName;
    private long hashedSeed;
    private GameMode gameMode;
    private GameMode previousGameMode;
    private boolean debug;
    private boolean flat;
    private boolean keepingAllPlayerData;

    public WrapperPlayServerRespawn(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerRespawn(Dimension dimension, String worldName, long hashedSeed,
                                    GameMode gameMode, GameMode previousGameMode,
                                    boolean debug, boolean flat, boolean keepingAllPlayerData) {
        super(PacketType.Play.Server.RESPAWN);
        this.dimension = dimension;
        this.worldName = Optional.of(worldName);
        this.hashedSeed = hashedSeed;
        this.gameMode = gameMode;
        this.previousGameMode = previousGameMode;
        this.debug = debug;
        this.flat = flat;
        this.keepingAllPlayerData = keepingAllPlayerData;
    }

    @Override
    public void readData() {
        //TODO On 1.16.0 we only get dimension type,
        //TODO here on 1.16.2->1.17.1 we get a registry with dimensiontype, biome stuff and more
        boolean v1_16_2 = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_16_2);
        boolean v1_16_0 = serverVersion.isNewerThanOrEquals(ServerVersion.v_1_16);
        if (v1_16_2) {
            NBTCompound dimensionAttributes = readNBT();
            WorldType worldType = WorldType.getByName(dimensionAttributes.getStringTagValueOrDefault("effects", ""));
            dimension = new Dimension(worldType, dimensionAttributes);
        } else {
            WorldType worldType;
            if (v1_16_0) {
                worldType = WorldType.getByName(readString());
            }
            else {
                worldType = WorldType.getByID(readInt());
            }
            dimension = new Dimension(worldType);
        }
        if (v1_16_0) {
            worldName = Optional.of(readString());
        }
        else {
            worldName = Optional.empty();
        }
        hashedSeed = readLong();
        gameMode = GameMode.values()[readByte()];
        if (v1_16_0) {
            previousGameMode = GameMode.values()[readByte()];
        }
        else {
            //We just previous gamemode to the current one
            previousGameMode = gameMode;
        }


        if (v1_16_0) {
            debug = readBoolean();
            flat = readBoolean();
            keepingAllPlayerData = readBoolean();
        }
        else {
            String levelType = readString();
            if ("flat".equals(levelType)) {
                debug = false;
                flat = true;
            } else {
                debug = false;
                flat = false;
            }
        }
    }

    @Override
    public void readData(WrapperPlayServerRespawn wrapper) {
        dimension = wrapper.dimension;
        worldName = wrapper.worldName;
        hashedSeed = wrapper.hashedSeed;
        gameMode = wrapper.gameMode;
        previousGameMode = wrapper.previousGameMode;
        debug = wrapper.debug;
        flat = wrapper.flat;
        keepingAllPlayerData = wrapper.keepingAllPlayerData;
    }

    @Override
    public void writeData() {
        //TODO Finish
        if (serverVersion.isNewerThanOrEquals(ServerVersion.v_1_16_2)) {
            dimension.getAttributes().get().setTag("effects", new NBTString(dimension.getWorldType().getName()));
            writeNBT(dimension.getAttributes().get());
        } else {
            writeString(dimension.getWorldType().getName());
        }
        writeString(worldName.get());
        writeLong(hashedSeed);
        writeByte(gameMode.ordinal());
        writeByte(previousGameMode.ordinal());
        writeBoolean(debug);
        writeBoolean(flat);
        writeBoolean(keepingAllPlayerData);
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Optional<String> getWorldName() {
        return worldName;
    }

    public void setWorldName(@Nullable String worldName) {
        if (worldName == null) {
            this.worldName = Optional.empty();
        }
        else {
            this.worldName = Optional.of(worldName);
        }
    }

    public long getHashedSeed() {
        return hashedSeed;
    }

    public void setHashedSeed(long hashedSeed) {
        this.hashedSeed = hashedSeed;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public GameMode getPreviousGameMode() {
        return previousGameMode;
    }

    public void setPreviousGameMode(GameMode previousGameMode) {
        this.previousGameMode = previousGameMode;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isFlat() {
        return flat;
    }

    public void setFlat(boolean flat) {
        this.flat = flat;
    }

    public boolean isKeepingAllPlayerData() {
        return keepingAllPlayerData;
    }

    public void setKeepingAllPlayerData(boolean keepAllPlayerData) {
        this.keepingAllPlayerData = keepAllPlayerData;
    }
}
