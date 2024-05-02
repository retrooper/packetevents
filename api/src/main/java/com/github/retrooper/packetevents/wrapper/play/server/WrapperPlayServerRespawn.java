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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.Difficulty;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayServerRespawn extends PacketWrapper<WrapperPlayServerRespawn> {

    public static final byte KEEP_NOTHING = 0;
    public static final byte KEEP_ATTRIBUTES = 0b01;
    public static final byte KEEP_ENTITY_DATA = 0b10;
    public static final byte KEEP_ALL_DATA = KEEP_ATTRIBUTES | KEEP_ENTITY_DATA;

    private Dimension dimension;
    private Optional<String> worldName;
    private Difficulty difficulty;
    private long hashedSeed;
    private GameMode gameMode;
    private @Nullable GameMode previousGameMode;
    private boolean worldDebug;
    private boolean worldFlat;
    private byte keptData;
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
        this(dimension, worldName, difficulty, hashedSeed, gameMode, previousGameMode, worldDebug, worldFlat,
                keepingAllPlayerData ? KEEP_ALL_DATA : KEEP_NOTHING, lastDeathPosition, portalCooldown);
    }

    public WrapperPlayServerRespawn(Dimension dimension, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode,
                                    @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, byte keptData,
                                    @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown) {
        super(PacketType.Play.Server.RESPAWN);
        this.dimension = dimension;
        setWorldName(worldName);
        this.difficulty = difficulty;
        this.hashedSeed = hashedSeed;
        this.gameMode = gameMode;
        this.previousGameMode = previousGameMode;
        this.worldDebug = worldDebug;
        this.worldFlat = worldFlat;
        this.keptData = keptData;
        this.lastDeathPosition = lastDeathPosition;
        this.portalCooldown = portalCooldown;
    }

    @Override
    public void read() {
        dimension = readDimension();
        worldName = Optional.of(readString());
        hashedSeed = readLong();
        gameMode = readGameMode();
        previousGameMode = readGameMode();
        worldDebug = readBoolean();
        worldFlat = readBoolean();
        lastDeathPosition = readOptional(PacketWrapper::readWorldBlockPosition);
        portalCooldown = readVarInt();
        keptData = readByte();
    }

    @Override
    public void write() {
        writeDimension(dimension);
        writeString(worldName.orElse(""));
        writeLong(hashedSeed);
        writeGameMode(gameMode);
        writeGameMode(previousGameMode);
        writeBoolean(worldDebug);
        writeBoolean(worldFlat);
        writeOptional(lastDeathPosition, PacketWrapper::writeWorldBlockPosition);
        int pCooldown = portalCooldown != null ? portalCooldown : 0;
        writeVarInt(pCooldown);
        writeByte(keptData);
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
        keptData = wrapper.keptData;
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
        return (keptData & KEEP_ATTRIBUTES) != 0;
    }

    public void setKeepingAllPlayerData(boolean keepAllPlayerData) {
        this.keptData = keepAllPlayerData ? KEEP_ALL_DATA : KEEP_ENTITY_DATA;
    }

    public byte getKeptData() {
        return keptData;
    }

    public void setKeptData(byte keptData) {
        this.keptData = keptData;
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
