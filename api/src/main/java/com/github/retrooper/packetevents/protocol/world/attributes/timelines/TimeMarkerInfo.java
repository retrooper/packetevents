/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world.attributes.timelines;

import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.util.Either;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

/**
 * @versions 26.1+
 */
@NullMarked
public class TimeMarkerInfo {

    private static final NbtCodec<TimeMarkerInfo> FULL_CODEC = new NbtMapCodec<TimeMarkerInfo>() {
        @Override
        public TimeMarkerInfo decode(NBTCompound tag, PacketWrapper<?> wrapper) throws NbtCodecException {
            int ticks = tag.getNumberTagValueOrThrow("ticks").intValue();
            boolean showInCommands = tag.getBooleanOr("show_in_commands", false);
            return new TimeMarkerInfo(ticks, showInCommands);
        }

        @Override
        public void encode(NBTCompound tag, PacketWrapper<?> wrapper, TimeMarkerInfo value) throws NbtCodecException {
            tag.setTag("ticks", new NBTInt(value.ticks));
            if (value.showInCommands) {
                tag.setTag("show_in_commands", new NBTByte(true));
            }
        }
    }.codec();

    public static final NbtCodec<TimeMarkerInfo> CODEC = NbtCodecs.either(NbtCodecs.INT, FULL_CODEC)
            .apply(
                    either -> either.map(TimeMarkerInfo::new, Function.identity()),
                    info -> info.showInCommands ? Either.createRight(info) : Either.createLeft(info.ticks)
            );

    private final int ticks;
    private final boolean showInCommands;

    public TimeMarkerInfo(int ticks) {
        this(ticks, false);
    }

    public TimeMarkerInfo(int ticks, boolean showInCommands) {
        this.ticks = ticks;
        this.showInCommands = showInCommands;
    }

    public int getTicks() {
        return this.ticks;
    }

    public boolean isShowInCommands() {
        return this.showInCommands;
    }
}
