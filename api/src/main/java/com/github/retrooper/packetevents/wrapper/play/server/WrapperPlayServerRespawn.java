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
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.*;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayServerRespawn extends PacketWrapper<WrapperPlayServerRespawn> {
    private Dimension dimension;
    private Optional<String> worldName;
    private Difficulty difficulty;
    private long hashedSeed;
    private GameMode gameMode;
    private @Nullable GameMode previousGameMode;
    private boolean worldDebug;
    private boolean worldFlat;
    private boolean keepingAllPlayerData;
    private WorldBlockPosition lastDeathPosition;
    private Integer portalCooldown;

    //This should not be accessed
    private String levelType;

    public WrapperPlayServerRespawn(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerRespawn(Dimension dimension, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode,
                                    @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, boolean keepingAllPlayerData,
                                    @Nullable ResourceLocation deathDimensionName, @Nullable WorldBlockPosition lastDeathPosition,
                                    @Nullable Integer portalCooldown) {
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
        this.lastDeathPosition = lastDeathPosition;
        this.portalCooldown = portalCooldown;
    }

    @Override
    public void read() {
        boolean v1_14 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        boolean v1_15_0 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
        boolean v1_16_0 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
        boolean v1_19 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);

        if (v1_16_0) {
            dimension = readDimension();
            worldName = Optional.of(readString());
            hashedSeed = readLong();
            gameMode = GameMode.getById(readUnsignedByte());
            int previousMode = readByte();
            previousGameMode = previousMode == -1 ? null : GameMode.getById(previousMode);
            worldDebug = readBoolean();
            worldFlat = readBoolean();
            keepingAllPlayerData = readBoolean();
            if (v1_19) {
                lastDeathPosition = readOptional(PacketWrapper::readWorldBlockPosition);
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
                portalCooldown = readVarInt();
            }
        } else {
            dimension = new Dimension(readInt());

            worldName = Optional.empty();
            hashedSeed = 0L;
            if (v1_15_0) {
                hashedSeed = readLong();
            } else if (!v1_14) {
                difficulty = Difficulty.getById(readByte());
            }

            //Note: SPECTATOR will not be expected from a 1.7 client.
            gameMode = GameMode.getById(readByte());
            levelType = readString(16);
            worldFlat = DimensionType.isFlat(levelType);
            worldDebug = DimensionType.isDebug(levelType);
        }
    }

    @Override
    public void write() {
        boolean v1_14 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        boolean v1_15_0 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
        boolean v1_16_0 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
        boolean v1_19 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);

        if (v1_16_0) {
            writeDimension(dimension);
            writeString(worldName.orElse(""));
            writeLong(hashedSeed);
            writeByte(gameMode.ordinal());
            writeByte(previousGameMode == null ? -1 : previousGameMode.ordinal());
            writeBoolean(worldDebug);
            writeBoolean(worldFlat);
            writeBoolean(keepingAllPlayerData);
            if (v1_19) {
                writeOptional(lastDeathPosition, PacketWrapper::writeWorldBlockPosition);
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
                int pCooldown = portalCooldown != null ? portalCooldown : 0;
                writeVarInt(pCooldown);
            }
        } else {
            writeInt(dimension.getId());
            if (v1_15_0) {
                writeLong(hashedSeed);
            } else if (!v1_14) {
                //Handle 1.13.2 and below
                int id = difficulty == null ? Difficulty.NORMAL.getId() : difficulty.getId();
                writeByte(id);
            }

            //Note: SPECTATOR will not be expected from a 1.7 client.
            writeByte(gameMode.ordinal());

            if (worldFlat) {
                writeString(WorldType.FLAT.getName());
            } else if (worldDebug) {
                writeString(WorldType.DEBUG_ALL_BLOCK_STATES.getName());
            } else {
                writeString(levelType == null ? WorldType.DEFAULT.getName() : levelType, 16);
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerRespawn wrapper) {
        dimension = wrapper.dimension;
        worldName = wrapper.worldName;
        difficulty = wrapper.difficulty;
        hashedSeed = wrapper.hashedSeed;
        gameMode = wrapper.gameMode;
        previousGameMode = wrapper.previousGameMode;
        worldDebug = wrapper.worldDebug;
        worldFlat = wrapper.worldFlat;
        keepingAllPlayerData = wrapper.keepingAllPlayerData;
        lastDeathPosition = wrapper.lastDeathPosition;
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
        this.worldName = Optional.ofNullable(worldName);
    }

    public @Nullable Difficulty getDifficulty() {
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
}
