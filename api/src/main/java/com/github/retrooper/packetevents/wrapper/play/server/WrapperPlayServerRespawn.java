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

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.Difficulty;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.protocol.world.DimensionType;
import com.github.retrooper.packetevents.protocol.world.WorldType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
//TODO Test this wrapper
public class WrapperPlayServerRespawn extends PacketWrapper<WrapperPlayServerRespawn> {
    private Dimension dimension;
    private Optional<String> worldName;
    private Difficulty difficulty;
    private long hashedSeed;
    private GameMode gameMode;
    @Nullable
    private GameMode previousGameMode;
    private boolean worldDebug;
    private boolean worldFlat;
    private boolean keepingAllPlayerData;

    //This should not be accessed
    private String levelType;

    public WrapperPlayServerRespawn(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerRespawn(Dimension dimension, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode, @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, boolean keepingAllPlayerData) {
        super(PacketType.Play.Server.RESPAWN);
        this.dimension = dimension;
        setWorldName(worldName);
        this.difficulty = difficulty;
        this.hashedSeed = hashedSeed;
        this.gameMode = gameMode;
        this.previousGameMode = previousGameMode;
        this.worldDebug = worldDebug;
        this.worldFlat = worldFlat;
        this.keepingAllPlayerData = keepingAllPlayerData;
    }

    @Override
    public void readData() {
        boolean v1_14 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        boolean v1_15_0 = v1_14 && serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
        boolean v1_16_0 = v1_15_0 && serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
        boolean v1_16_2 = v1_16_0 && serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16_2);
        if (v1_16_2) {
            NBTCompound dimensionAttributes = readNBT();
            DimensionType dimensionType = DimensionType.getByName(dimensionAttributes.getStringTagValueOrDefault("effects", ""));
            dimension = new Dimension(dimensionType, dimensionAttributes);
            worldName = Optional.of(readString());
            hashedSeed = readLong();
            gameMode = GameMode.values()[readByte()];
            int previousMode = readByte();
            previousGameMode = previousMode == -1 ? null : GameMode.values()[previousMode];
            worldDebug = readBoolean();
            worldFlat = readBoolean();
            keepingAllPlayerData = readBoolean();
        }
        else if (v1_16_0) {
            DimensionType dimensionType = DimensionType.getByName(readString());
            dimension = new Dimension(dimensionType);
            worldName = Optional.of(readString());
            hashedSeed = readLong();
            gameMode = GameMode.values()[readByte()];
            int previousMode = readByte();
            previousGameMode = previousMode == -1 ? null : GameMode.values()[previousMode];
            worldDebug = readBoolean();
            worldFlat = readBoolean();
            keepingAllPlayerData = readBoolean();
        }
        else if (v1_15_0) {
            DimensionType dimensionType = DimensionType.getById(readInt());
            dimension = new Dimension(dimensionType);
            worldName = Optional.empty();
            hashedSeed = readLong();
            gameMode = GameMode.values()[readByte()];
            levelType = readString(16);
            if (WorldType.FLAT.getName().equals(levelType)) {
                worldFlat = true;
                worldDebug = false;
            }
            else if (WorldType.DEBUG_ALL_BLOCK_STATES.getName().equals(levelType)) {
                worldDebug = true;
                worldFlat = false;
            }
            else {
                worldFlat = false;
                worldDebug = false;
            }
        }
        else {
            DimensionType dimensionType = DimensionType.getById(readInt());
            dimension = new Dimension(dimensionType);
            if (!v1_14) {
                //Handle 1.13.2 and below
                difficulty = Difficulty.getById(readByte());
            }
            else {
                difficulty = Difficulty.NORMAL;
            }
            worldName = Optional.empty();
            hashedSeed = 0L;
            //Note: SPECTATOR will not be expected from a 1.7 client.
            gameMode = GameMode.values()[readByte()];
            levelType = readString(16);
            if (WorldType.FLAT.getName().equals(levelType)) {
                worldFlat = true;
                worldDebug = false;
            }
            else if (WorldType.DEBUG_ALL_BLOCK_STATES.getName().equals(levelType)) {
                worldDebug = true;
                worldFlat = false;
            }
            else {
                worldFlat = false;
                worldDebug = false;
            }
        }
    }

    @Override
    public void readData(WrapperPlayServerRespawn wrapper) {
        dimension = wrapper.dimension;
        worldName = wrapper.worldName;
        difficulty = wrapper.difficulty;
        hashedSeed = wrapper.hashedSeed;
        gameMode = wrapper.gameMode;
        previousGameMode = wrapper.previousGameMode;
        worldDebug = wrapper.worldDebug;
        worldFlat = wrapper.worldFlat;
        keepingAllPlayerData = wrapper.keepingAllPlayerData;
    }

    @Override
    public void writeData() {
        boolean v1_14 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        boolean v1_15_0 = v1_14 && serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
        boolean v1_16_0 = v1_15_0 && serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
        boolean v1_16_2 = v1_16_0 && serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16_2);
        if (v1_16_2) {
            NBT tag = new NBTString(dimension.getType().getName());
            //TODO Fix orElse to generate a new nbt compound
            dimension.getAttributes().orElse(new NBTCompound()).setTag("effects", tag);
            //TODO Fix no value when get()
            writeNBT(dimension.getAttributes().get());
            writeString(worldName.orElse(""));
            writeLong(hashedSeed);
            writeByte(gameMode.ordinal());
            writeByte(previousGameMode == null ? -1 :previousGameMode.ordinal());
            writeBoolean(worldDebug);
            writeBoolean(worldFlat);
            writeBoolean(keepingAllPlayerData);
        }
        else if (v1_16_0) {
            writeString(dimension.getType().getName());
            writeString(worldName.orElse(""));
            writeLong(hashedSeed);
            writeByte(gameMode.ordinal());
            writeByte(previousGameMode == null ? -1 : previousGameMode.ordinal());
            writeBoolean(worldDebug);
            writeBoolean(worldFlat);
            writeBoolean(keepingAllPlayerData);
        }
        else if (v1_15_0) {
            writeInt(dimension.getType().getId());
            writeString(worldName.orElse(""));
            writeLong(hashedSeed);
            writeByte(gameMode.ordinal());
            if (worldFlat) {
                writeString(WorldType.FLAT.getName());
            }
            else if (worldDebug) {
                writeString(WorldType.DEBUG_ALL_BLOCK_STATES.getName());
            }
            else {
                if (levelType == null) {
                    levelType = WorldType.DEFAULT.getName();
                }
                writeString(levelType, 16);
            }

        }
        else {
            writeInt(dimension.getType().getId());
            if (!v1_14) {
                //Handle 1.13.2 and below
                if (difficulty == null) {
                    difficulty = Difficulty.NORMAL;
                }
                writeByte(difficulty.getId());
            }
            //Note: SPECTATOR will not be expected from a 1.7 client.
            writeByte(gameMode.ordinal());
            if (worldFlat) {
                writeString(WorldType.FLAT.getName());
            }
            else if (worldDebug) {
                writeString(WorldType.DEBUG_ALL_BLOCK_STATES.getName());
            }
            else {
                if (levelType == null) {
                    levelType = WorldType.DEFAULT.getName();
                }
                writeString(levelType, 16);
            }
        }
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
        } else {
            this.worldName = Optional.of(worldName);
        }
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
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

    @Nullable
    public GameMode getPreviousGameMode() {
        return previousGameMode;
    }

    public void setPreviousGameMode(@Nullable GameMode previousGameMode) {
        this.previousGameMode = previousGameMode;
    }

    public boolean isWorldDebug() {
        return worldDebug;
    }

    public void setWorldDebug(boolean worldDebug) {
        this.worldDebug = worldDebug;
    }

    public boolean isWorldFlat() {
        return worldFlat;
    }

    public void setWorldFlat(boolean worldFlat) {
        this.worldFlat = worldFlat;
    }

    public boolean isKeepingAllPlayerData() {
        return keepingAllPlayerData;
    }

    public void setKeepingAllPlayerData(boolean keepAllPlayerData) {
        this.keepingAllPlayerData = keepAllPlayerData;
    }
}
