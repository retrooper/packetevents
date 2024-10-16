/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.item.instrument;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface Instrument extends MappedEntity, CopyableEntity<Instrument>, DeepComparableEntity {

    Sound getSound();

    float getUseSeconds();

    /**
     * Use duration measured in ticks
     */
    default int getUseDuration() {
        return MathUtil.floor(this.getUseSeconds() * 20);
    }

    float getRange();

    Component getDescription();

    static Instrument read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(Instruments.getRegistry(), Instrument::readDirect);
    }

    static Instrument readDirect(PacketWrapper<?> wrapper) {
        Sound sound = Sound.read(wrapper);
        float useSeconds = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)
                ? wrapper.readFloat() : wrapper.readVarInt() * 20f;
        float range = wrapper.readFloat();
        Component description = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)
                ? wrapper.readComponent() : Component.empty();
        return new StaticInstrument(sound, useSeconds, range, description);
    }

    static void write(PacketWrapper<?> wrapper, Instrument instrument) {
        wrapper.writeMappedEntityOrDirect(instrument, Instrument::writeDirect);
    }

    static void writeDirect(PacketWrapper<?> wrapper, Instrument instrument) {
        Sound.write(wrapper, instrument.getSound());
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            wrapper.writeFloat(instrument.getUseSeconds());
        } else {
            wrapper.writeVarInt(instrument.getUseDuration());
        }
        wrapper.writeFloat(instrument.getRange());
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            wrapper.writeComponent(instrument.getDescription());
        }
    }

    static Instrument decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        Sound sound = Sound.decode(compound.getTagOrThrow("sound_event"), version);
        float useSeconds = compound.getNumberTagOrThrow("use_duration").getAsFloat();
        float range = compound.getNumberTagOrThrow("range").getAsFloat();
        Component description = AdventureSerializer.fromNbt(compound.getTagOrThrow("description"));
        return new StaticInstrument(data, sound, useSeconds, range, description);
    }

    static NBT encode(Instrument instrument, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("sound_event", Sound.encode(instrument.getSound(), version));
        compound.setTag("use_duration", new NBTFloat(instrument.getUseSeconds()));
        compound.setTag("range", new NBTFloat(instrument.getRange()));
        compound.setTag("description", AdventureSerializer.toNbt(instrument.getDescription()));
        return compound;
    }
}
