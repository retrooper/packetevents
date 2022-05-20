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

import java.util.ArrayList;
import java.util.List;

public class WrapperPlayServerJoinGame extends PacketWrapper<WrapperPlayServerJoinGame> {
    private int entityID;
    private boolean hardcore;
    private GameMode gameMode;

    @Nullable
    private GameMode previousGameMode;

    private List<String> worldNames;
    private NBTCompound dimensionCodec;
    private Dimension dimension;
    private Difficulty difficulty;
    private String worldName;
    private long hashedSeed;
    private int maxPlayers;
    private int viewDistance;
    private int simulationDistance;
    private boolean reducedDebugInfo;
    private boolean enableRespawnScreen;
    private boolean isDebug;
    private boolean isFlat;

    public WrapperPlayServerJoinGame(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerJoinGame(int entityID, boolean hardcore, GameMode gameMode,
                                     @Nullable GameMode previousGameMode, List<String> worldNames,
                                     NBTCompound dimensionCodec, Dimension dimension,
                                     Difficulty difficulty, String worldName, long hashedSeed,
                                     int maxPlayers, int viewDistance, int simulationDistance,
                                     boolean reducedDebugInfo, boolean enableRespawnScreen,
                                     boolean isDebug, boolean isFlat) {
        super(PacketType.Play.Server.JOIN_GAME);
        this.entityID = entityID;
        this.hardcore = hardcore;
        this.gameMode = gameMode;
        this.previousGameMode = previousGameMode;
        this.worldNames = worldNames;
        this.dimensionCodec = dimensionCodec;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.worldName = worldName;
        this.hashedSeed = hashedSeed;
        this.maxPlayers = maxPlayers;
        this.viewDistance = viewDistance;
        this.simulationDistance = simulationDistance;
        this.reducedDebugInfo = reducedDebugInfo;
        this.enableRespawnScreen = enableRespawnScreen;
        this.isDebug = isDebug;
        this.isFlat = isFlat;
    }

    @Override
    public void read() {
        entityID = readInt();
        boolean v1_18 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18);
        boolean v1_16 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
        boolean v1_14 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        boolean v1_15 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
        if (v1_16) {
            hardcore = readBoolean();
            gameMode = readGameMode();
        } else {
            int gameModeId = readUnsignedByte();
            hardcore = (gameModeId & 0x8) == 0x8;
            gameMode = GameMode.getById(gameModeId & -0x9);
        }
        if (v1_16) {
            previousGameMode = readGameMode();
            int worldCount = readVarInt();
            worldNames = new ArrayList<>(worldCount);
            for (int i = 0; i < worldCount; i++) {
                worldNames.add(readString());
            }
            dimensionCodec = readNBT();
            NBTCompound dimensionAttributes = readNBT();
            DimensionType dimensionType = DimensionType.getByName(dimensionAttributes.getStringTagValueOrDefault("effects", ""));
            dimension = new Dimension(dimensionType, dimensionAttributes);
            worldName = readString();
        } else {
            previousGameMode = gameMode;
            dimensionCodec = new NBTCompound();
            DimensionType dimensionType = DimensionType.getById(serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9_2) ?
                    readInt() : readByte());
            dimension = new Dimension(dimensionType);
            if (!v1_14) {
                difficulty = Difficulty.getById(readByte());
            } else {
                difficulty = Difficulty.NORMAL;
                //Max players
            }
        }
        if (v1_15) {
            hashedSeed = readLong();
        } else {
            hashedSeed = 0L;
        }
        if (v1_16) {
            maxPlayers = readVarInt();
            viewDistance = readVarInt();
            if (v1_18) simulationDistance = readVarInt();
            reducedDebugInfo = readBoolean();
            enableRespawnScreen = readBoolean();
            isDebug = readBoolean();
            isFlat = readBoolean();
        } else {
            maxPlayers = readUnsignedByte();
            String levelType = readString(16);
            if (WorldType.FLAT.getName().equals(levelType)) {
                isFlat = true;
                isDebug = false;
            } else if (WorldType.DEBUG_ALL_BLOCK_STATES.getName().equals(levelType)) {
                isDebug = true;
                isFlat = false;
            } else {
                isFlat = false;
                isDebug = false;
            }
            if (v1_14) {
                viewDistance = readVarInt();
            } else {
                viewDistance = 0;
            }
            reducedDebugInfo = readBoolean();
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
                enableRespawnScreen = readBoolean();
            } else {
                //TODO What is a valid value on legacy versions?
                enableRespawnScreen = false;
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerJoinGame wrapper) {
        entityID = wrapper.entityID;
        hardcore = wrapper.hardcore;
        gameMode = wrapper.gameMode;
        previousGameMode = wrapper.previousGameMode;
        worldNames = wrapper.worldNames;
        dimensionCodec = wrapper.dimensionCodec;
        dimension = wrapper.dimension;
        difficulty = wrapper.difficulty;
        worldName = wrapper.worldName;
        hashedSeed = wrapper.hashedSeed;
        maxPlayers = wrapper.maxPlayers;
        viewDistance = wrapper.viewDistance;
        simulationDistance = wrapper.simulationDistance;
        reducedDebugInfo = wrapper.reducedDebugInfo;
        enableRespawnScreen = wrapper.enableRespawnScreen;
        isDebug = wrapper.isDebug;
        isFlat = wrapper.isFlat;
    }

    @Override
    public void write() {
        writeInt(entityID);
        boolean v1_18 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18);
        boolean v1_16 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
        boolean v1_14 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        boolean v1_15 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
        if (v1_16) {
            writeBoolean(hardcore);
            writeGameMode(gameMode);
        } else {
            int gameModeId = gameMode.getId();
            if (hardcore) {
                gameModeId |= 0x8;
            }
            writeByte(gameModeId);
        }
        if (v1_16) {
            if (previousGameMode == null) {
                previousGameMode = gameMode;
            }
            writeGameMode(previousGameMode);
            writeVarInt(worldNames.size());
            for (String name : worldNames) {
                writeString(name);
            }
            writeNBT(dimensionCodec);
            NBT tag = new NBTString(dimension.getType().getName());
            //TODO Fix orElse to generate a new nbt compound
            dimension.getAttributes().orElse(new NBTCompound()).setTag("effects", tag);
            //TODO Fix no value when get()
            writeNBT(dimension.getAttributes().get());
            writeString(worldName);
        } else {
            previousGameMode = gameMode;
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
                writeInt(dimension.getType().getId());
            }
            else {
                writeByte(dimension.getType().getId());
            }
            if (!v1_14) {
                writeByte(difficulty.getId());
            }
        }
        if (v1_15) {
            writeLong(hashedSeed);
        }
        if (v1_16) {
            writeVarInt(maxPlayers);
            writeVarInt(viewDistance);
            if (v1_18) writeVarInt(simulationDistance);
            writeBoolean(reducedDebugInfo);
            writeBoolean(enableRespawnScreen);
            writeBoolean(isDebug);
            writeBoolean(isFlat);
        } else {
            writeByte(maxPlayers);
            String levelType;
            //TODO Proper backwards compatibility for level type
            if (isFlat) {
                levelType = WorldType.FLAT.getName();
            }
            else if (isDebug) {
                levelType = WorldType.DEBUG_ALL_BLOCK_STATES.getName();
            }
            else {
                levelType = WorldType.DEFAULT.getName();
            }
            writeString(levelType, 16);
            if (v1_14) {
                writeVarInt(viewDistance);
            }
            writeBoolean(reducedDebugInfo);
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
                writeBoolean(enableRespawnScreen);
            }
        }
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public boolean isHardcore() {
        return hardcore;
    }

    public void setHardcore(boolean hardcore) {
        this.hardcore = hardcore;
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

    public List<String> getWorldNames() {
        return worldNames;
    }

    public void setWorldNames(List<String> worldNames) {
        this.worldNames = worldNames;
    }

    public NBTCompound getDimensionCodec() {
        return dimensionCodec;
    }

    public void setDimensionCodec(NBTCompound dimensionCodec) {
        this.dimensionCodec = dimensionCodec;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public long getHashedSeed() {
        return hashedSeed;
    }

    public void setHashedSeed(long hashedSeed) {
        this.hashedSeed = hashedSeed;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }

    public int getSimulationDistance() {
        return simulationDistance;
    }

    public void setSimulationDistance(int simulationDistance) {
        this.simulationDistance = simulationDistance;
    }

    public boolean isReducedDebugInfo() {
        return reducedDebugInfo;
    }


    public void setReducedDebugInfo(boolean reducedDebugInfo) {
        this.reducedDebugInfo = reducedDebugInfo;
    }

    public boolean isRespawnScreenEnabled() {
        return enableRespawnScreen;
    }

    public void setRespawnScreenEnabled(boolean enableRespawnScreen) {
        this.enableRespawnScreen = enableRespawnScreen;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public boolean isFlat() {
        return isFlat;
    }

    public void setFlat(boolean isFlat) {
        this.isFlat = isFlat;
    }
}
