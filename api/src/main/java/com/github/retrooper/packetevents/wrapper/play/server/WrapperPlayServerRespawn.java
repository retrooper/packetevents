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
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerRespawn extends PacketWrapper<WrapperPlayServerRespawn> {
    private NBTCompound dimension;
    private String worldName;
    private long hashedSeed;
    private GameMode gameMode;
    private GameMode previousGameMode;
    private boolean debug;
    private boolean flat;
    private boolean keepingAllPlayerData;

    public WrapperPlayServerRespawn(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerRespawn(NBTCompound dimension, String worldName, long hashedSeed,
                                    GameMode gameMode, GameMode previousGameMode,
                                    boolean debug, boolean flat, boolean keepingAllPlayerData) {
        super(PacketType.Play.Server.RESPAWN);
        this.dimension = dimension;
        this.worldName = worldName;
        this.hashedSeed= hashedSeed;
        this.gameMode = gameMode;
        this.previousGameMode = previousGameMode;
        this.debug = debug;
        this.flat = flat;
        this.keepingAllPlayerData = keepingAllPlayerData;
    }

    @Override
    public void readData() {
        dimension = readNBT();
        worldName = readString();
        hashedSeed = readLong();
        gameMode = GameMode.values()[readByte()];
        previousGameMode = GameMode.values()[readByte()];
        debug = readBoolean();
        flat = readBoolean();
        keepingAllPlayerData = readBoolean();
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
        keepingAllPlayerData= wrapper.keepingAllPlayerData;
    }

    @Override
    public void writeData() {
        writeNBT(dimension);
        writeString(worldName);
        writeLong(hashedSeed);
        writeByte(gameMode.ordinal());
        writeByte(previousGameMode.ordinal());
        writeBoolean(debug);
        writeBoolean(flat);
        writeBoolean(keepingAllPlayerData);
    }

    public NBTCompound getDimension() {
        return dimension;
    }

    public void setDimension(NBTCompound dimension) {
        this.dimension = dimension;
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
