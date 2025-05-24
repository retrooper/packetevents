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

package com.github.retrooper.packetevents.protocol.particle.data;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleBlockStateData extends ParticleData implements LegacyConvertible {

    private WrappedBlockState blockState;

    public ParticleBlockStateData(WrappedBlockState blockState) {
        this.blockState = blockState;
    }

    public WrappedBlockState getBlockState() {
        return blockState;
    }

    public void setBlockState(WrappedBlockState blockState) {
        this.blockState = blockState;
    }

    public static ParticleBlockStateData read(PacketWrapper<?> wrapper) {
        int blockID;
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            blockID = wrapper.readVarInt();
        } else if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13)) {
            blockID = wrapper.readInt();
        } else {
            blockID = wrapper.readVarInt();
        }
        return new ParticleBlockStateData(WrappedBlockState.getByGlobalId(wrapper.getServerVersion()
                .toClientVersion(), blockID));
    }

    public static void write(PacketWrapper<?> wrapper, ParticleBlockStateData data) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeVarInt(data.getBlockState().getGlobalId());
        } else {
            wrapper.writeInt(data.getBlockState().getGlobalId());
        }
    }

    public static ParticleBlockStateData decode(NBTCompound compound, ClientVersion version) {
        String key = version.isNewerThanOrEquals(ClientVersion.V_1_20_5) ? "block_state" : "value";
        WrappedBlockState state = WrappedBlockState.decode(compound.getTagOrThrow(key), version);
        return new ParticleBlockStateData(state);
    }

    public static void encode(ParticleBlockStateData data, ClientVersion version, NBTCompound compound) {
        String key = version.isNewerThanOrEquals(ClientVersion.V_1_20_5) ? "block_state" : "value";
        compound.setTag(key, WrappedBlockState.encode(data.blockState, version));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public LegacyParticleData toLegacy(ClientVersion version) {
        return LegacyParticleData.ofOne(blockState.getGlobalId());
    }

}
