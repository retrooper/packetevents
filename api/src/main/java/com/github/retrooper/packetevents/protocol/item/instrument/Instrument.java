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

import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public interface Instrument extends MappedEntity {

    Sound getSound();

    int getUseDuration();

    float getRange();

    static Instrument read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(Instruments::getById, Instrument::readDirect);
    }

    static Instrument readDirect(PacketWrapper<?> wrapper) {
        Sound sound = Sound.read(wrapper);
        int useDuration = wrapper.readVarInt();
        float range = wrapper.readFloat();
        return new StaticInstrument(sound, useDuration, range);
    }

    static void write(PacketWrapper<?> wrapper, Instrument instrument) {
        wrapper.writeMappedEntityOrDirect(instrument, Instrument::writeDirect);
    }

    static void writeDirect(PacketWrapper<?> wrapper, Instrument instrument) {
        Sound.write(wrapper, instrument.getSound());
        wrapper.writeVarInt(instrument.getUseDuration());
        wrapper.writeFloat(instrument.getRange());
    }
}
