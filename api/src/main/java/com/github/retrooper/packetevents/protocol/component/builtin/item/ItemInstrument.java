/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.instrument.Instrument;
import com.github.retrooper.packetevents.protocol.item.instrument.Instruments;
import com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Objects;

public class ItemInstrument {

    private MaybeMappedEntity<Instrument> instrument;

    public ItemInstrument(MaybeMappedEntity<Instrument> instrument) {
        this.instrument = instrument;
    }

    public static ItemInstrument read(PacketWrapper<?> wrapper) {
        MaybeMappedEntity<Instrument> instrument;
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
            instrument = MaybeMappedEntity.read(wrapper, Instruments.getRegistry(), Instrument::read);
        } else {
            instrument = new MaybeMappedEntity<>(Instrument.read(wrapper));
        }
        return new ItemInstrument(instrument);
    }

    public static void write(PacketWrapper<?> wrapper, ItemInstrument instrument) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
            MaybeMappedEntity.write(wrapper, instrument.instrument, Instrument::write);
        } else {
            Instrument.write(wrapper, instrument.instrument.getValueOrThrow());
        }
    }

    public MaybeMappedEntity<Instrument> getInstrument() {
        return this.instrument;
    }

    public void setInstrument(MaybeMappedEntity<Instrument> instrument) {
        this.instrument = instrument;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemInstrument)) return false;
        ItemInstrument that = (ItemInstrument) obj;
        return this.instrument.equals(that.instrument);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.instrument);
    }
}
