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

package com.github.retrooper.packetevents.protocol.item.consumables.builtin;

import com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffect;
import com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffectTypes;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class PlaySoundConsumeEffect extends ConsumeEffect<PlaySoundConsumeEffect> {

    private final Sound sound;

    public PlaySoundConsumeEffect(Sound sound) {
        super(ConsumeEffectTypes.PLAY_SOUND);
        this.sound = sound;
    }

    public static PlaySoundConsumeEffect read(PacketWrapper<?> wrapper) {
        Sound sound = Sound.read(wrapper);
        return new PlaySoundConsumeEffect(sound);
    }

    public static void write(PacketWrapper<?> wrapper, PlaySoundConsumeEffect effect) {
        Sound.write(wrapper, effect.sound);
    }

    public Sound getSound() {
        return this.sound;
    }
}
