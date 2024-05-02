/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.Difficulty;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private boolean limitedCrafting;
    private boolean isDebug;
    private boolean isFlat;
    private WorldBlockPosition lastDeathPosition;
    private Integer portalCooldown;
    private boolean enforcesSecureChat;

    public WrapperPlayServerJoinGame(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerJoinGame(int entityID, boolean hardcore, GameMode gameMode,
                                     @Nullable GameMode previousGameMode, List<String> worldNames,
                                     NBTCompound dimensionCodec, Dimension dimension,
                                     Difficulty difficulty, String worldName, long hashedSeed,
                                     int maxPlayers, int viewDistance, int simulationDistance,
                                     boolean reducedDebugInfo, boolean enableRespawnScreen,
                                     boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown) {
        this(entityID, hardcore, gameMode, previousGameMode, worldNames, dimensionCodec, dimension,
                difficulty, worldName, hashedSeed, maxPlayers, viewDistance, simulationDistance, reducedDebugInfo, enableRespawnScreen,
                false, isDebug, isFlat, lastDeathPosition, portalCooldown);
    }

    public WrapperPlayServerJoinGame(int entityID, boolean hardcore, GameMode gameMode,
                                     @Nullable GameMode previousGameMode, List<String> worldNames,
                                     NBTCompound dimensionCodec, Dimension dimension,
                                     Difficulty difficulty, String worldName, long hashedSeed,
                                     int maxPlayers, int viewDistance, int simulationDistance,
                                     boolean reducedDebugInfo, boolean enableRespawnScreen, boolean limitedCrafting,
                                     boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown) {
        this(entityID, hardcore, gameMode, previousGameMode, worldNames, dimensionCodec, dimension, difficulty,
                worldName, hashedSeed, maxPlayers, viewDistance, simulationDistance, reducedDebugInfo,
                enableRespawnScreen, limitedCrafting, isDebug, isFlat, lastDeathPosition,
                portalCooldown, false);
    }

    public WrapperPlayServerJoinGame(int entityID, boolean hardcore, GameMode gameMode,
                                     @Nullable GameMode previousGameMode, List<String> worldNames,
                                     NBTCompound dimensionCodec, Dimension dimension,
                                     Difficulty difficulty, String worldName, long hashedSeed,
                                     int maxPlayers, int viewDistance, int simulationDistance,
                                     boolean reducedDebugInfo, boolean enableRespawnScreen, boolean limitedCrafting,
                                     boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition,
                                     @Nullable Integer portalCooldown, boolean enforcesSecureChat) {
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
        this.limitedCrafting = limitedCrafting;
        this.isDebug = isDebug;
        this.isFlat = isFlat;
        this.lastDeathPosition = lastDeathPosition;
        this.portalCooldown = portalCooldown;
        this.enforcesSecureChat = enforcesSecureChat;
    }

    @Override
    public void read() {
        entityID = readInt();
        hardcore = readBoolean();
        int worldCount = readVarInt();
        worldNames = new ArrayList<>(worldCount);
        for (int i = 0; i < worldCount; i++) {
            worldNames.add(readString());
        }
        maxPlayers = readVarInt();
        viewDistance = readVarInt();
        simulationDistance = readVarInt();
        reducedDebugInfo = readBoolean();
        enableRespawnScreen = readBoolean();
        limitedCrafting = readBoolean();
        dimension = readDimension();
        worldName = readString();
        hashedSeed = readLong();
        gameMode = readGameMode();
        previousGameMode = readGameMode();
        isDebug = readBoolean();
        isFlat = readBoolean();
        lastDeathPosition = readOptional(PacketWrapper::readWorldBlockPosition);
        portalCooldown = readVarInt();
        enableRespawnScreen = readBoolean();
    }

    @Override
    public void write() {
        writeInt(entityID);
        writeBoolean(hardcore);
        if (previousGameMode == null) {
            previousGameMode = gameMode;
        }
        writeVarInt(worldNames.size());
        for (String name : worldNames) {
            writeString(name);
        }
        writeVarInt(maxPlayers);
        writeVarInt(viewDistance);
        writeVarInt(simulationDistance);
        writeBoolean(reducedDebugInfo);
        writeBoolean(enableRespawnScreen);
        writeBoolean(limitedCrafting);
        writeDimension(dimension);
        writeString(worldName);
        writeLong(hashedSeed);
        writeGameMode(gameMode);
        writeGameMode(previousGameMode);
        writeBoolean(isDebug);
        writeBoolean(isFlat);
        writeOptional(lastDeathPosition, PacketWrapper::writeWorldBlockPosition);
        int pCooldown = portalCooldown != null ? portalCooldown : 0;
        writeVarInt(pCooldown);
        writeBoolean(enforcesSecureChat);
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
        limitedCrafting = wrapper.limitedCrafting;
        isDebug = wrapper.isDebug;
        isFlat = wrapper.isFlat;
        lastDeathPosition = wrapper.lastDeathPosition;
        portalCooldown = wrapper.portalCooldown;
        enforcesSecureChat = wrapper.enforcesSecureChat;
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

    public boolean isLimitedCrafting() {
        return this.limitedCrafting;
    }

    public void setLimitedCrafting(boolean limitedCrafting) {
        this.limitedCrafting = limitedCrafting;
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

    public @Nullable WorldBlockPosition getLastDeathPosition() {
        return lastDeathPosition;
    }

    public void setLastDeathPosition(@Nullable WorldBlockPosition lastDeathPosition) {
        this.lastDeathPosition = lastDeathPosition;
    }

    public Optional<Integer> getPortalCooldown() {
        return Optional.ofNullable(portalCooldown);
    }

    public void setPortalCooldown(int portalCooldown) {
        this.portalCooldown = portalCooldown;
    }

    public boolean isEnforcesSecureChat() {
        return this.enforcesSecureChat;
    }

    public void setEnforcesSecureChat(boolean enforcesSecureChat) {
        this.enforcesSecureChat = enforcesSecureChat;
    }
}
