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
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.sound.BuiltinSound;
import com.github.retrooper.packetevents.protocol.sound.CustomSound;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.SoundCategory;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

// Mostly from MCProtocolLib
public class WrapperPlayServerStopSound extends PacketWrapper<WrapperPlayServerStopSound> {
    private static final int FLAG_CATEGORY = 0x01;
    private static final int FLAG_SOUND = 0x02;

    private byte flags;
    private @Nullable SoundCategory category;
    private @Nullable Sound sound;

    public WrapperPlayServerStopSound(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerStopSound(@Nullable SoundCategory category, @Nullable Sound sound) {
        super(PacketType.Play.Server.STOP_SOUND);
        this.category = category;
        this.sound = sound;
    }

    @Override
    public void read() {
        this.flags = readByte();
        if ((this.flags & FLAG_CATEGORY) != 0) {
            category = SoundCategory.VALUES[readVarInt()];
        }

        if ((this.flags & FLAG_SOUND) != 0) {
            final String value = readString();
            final Sound sound = BuiltinSound.NAME_TO_SOUND.get(value);
            if (sound != null) {
                this.sound = sound;
            } else {
                this.sound = new CustomSound(value);
            }
        }
    }

    @Override
    public void write() {
        this.flags = 0;
        if (category != null) {
            this.flags |= FLAG_CATEGORY;
        }

        if (this.sound != null) {
            this.flags |= FLAG_SOUND;
        }

        writeByte(this.flags);
        if (category != null) {
            writeByte(category.ordinal());
        }

        if (this.sound != null) {
            String value = "";
            if (this.sound instanceof CustomSound) {
                value = ((CustomSound) this.sound).getName();
            } else {
                value = ((BuiltinSound) this.sound).getName();
            }
            writeString(value);
        }
    }

    @Override
    public void copy(WrapperPlayServerStopSound wrapper) {
        this.flags = wrapper.flags;
        this.category = wrapper.category;
        this.sound = wrapper.sound;
    }

    public byte getFlags() {
        return flags;
    }

    public void setFlags(byte flags) {
        this.flags = flags;
    }

    public @Nullable SoundCategory getCategory() {
        return category;
    }

    public void setCategory(@Nullable SoundCategory category) {
        this.category = category;
    }

    public @Nullable Sound getSound() {
        return sound;
    }

    public void setSound(@Nullable Sound sound) {
        this.sound = sound;
    }
}
