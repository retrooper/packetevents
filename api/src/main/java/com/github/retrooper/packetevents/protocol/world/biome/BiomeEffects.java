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

package com.github.retrooper.packetevents.protocol.world.biome;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import net.kyori.adventure.util.Index;

import java.util.Optional;
import java.util.OptionalInt;

public class BiomeEffects {

    private final int fogColor;
    private final int waterColor;
    private final int waterFogColor;
    private final int skyColor;
    private final OptionalInt foliageColor;
    private final OptionalInt grassColor;
    private final GrassColorModifier grassColorModifier;
    private final Optional<ParticleSettings> particle;
    private final Optional<Sound> ambientSound;
    private final Optional<MoodSettings> moodSound;
    private final Optional<AdditionsSettings> additionsSound;
    private final Optional<MusicSettings> music;

    public BiomeEffects(
            int fogColor, int waterColor, int waterFogColor, int skyColor,
            OptionalInt foliageColor, OptionalInt grassColor,
            GrassColorModifier grassColorModifier,
            Optional<ParticleSettings> particle,
            Optional<Sound> ambientSound,
            Optional<MoodSettings> moodSound,
            Optional<AdditionsSettings> additionsSound,
            Optional<MusicSettings> music
    ) {
        this.fogColor = fogColor;
        this.waterColor = waterColor;
        this.waterFogColor = waterFogColor;
        this.skyColor = skyColor;
        this.foliageColor = foliageColor;
        this.grassColor = grassColor;
        this.grassColorModifier = grassColorModifier;
        this.particle = particle;
        this.ambientSound = ambientSound;
        this.moodSound = moodSound;
        this.additionsSound = additionsSound;
        this.music = music;
    }

    public static BiomeEffects decode(NBT nbt, ClientVersion version) {
        NBTCompound compound = (NBTCompound) nbt;
        int fogColor = compound.getNumberTagOrThrow("fog_color").getAsInt();
        int waterColor = compound.getNumberTagOrThrow("water_color").getAsInt();
        int waterFogColor = compound.getNumberTagOrThrow("water_fog_color").getAsInt();
        int skyColor = compound.getNumberTagOrThrow("sky_color").getAsInt();
        OptionalInt foliageColor = Optional.ofNullable(compound.getNumberTagOrNull("foliage_color"))
                .map(NBTNumber::getAsInt).map(OptionalInt::of).orElseGet(OptionalInt::empty);
        OptionalInt grassColor = Optional.ofNullable(compound.getNumberTagOrNull("grass_color"))
                .map(NBTNumber::getAsInt).map(OptionalInt::of).orElseGet(OptionalInt::empty);
        GrassColorModifier grassColorModifier = Optional.ofNullable(compound.getStringTagValueOrNull("grass_color_modifier"))
                .map(GrassColorModifier.ID_INDEX::valueOrThrow).orElse(GrassColorModifier.NONE);
        Optional<ParticleSettings> particle = Optional.ofNullable(compound.getTagOrNull("particle"))
                .map(tag -> ParticleSettings.decode(tag, version));
        Optional<Sound> ambientSound = Optional.ofNullable(compound.getTagOrNull("ambient_sound"))
                .map(tag -> Sound.decode(tag, version));
        Optional<MoodSettings> moodSound = Optional.ofNullable(compound.getTagOrNull("mood_sound"))
                .map(tag -> AmbientMoodSettings.decode(tag, version));
        Optional<AdditionsSettings> additionsSound = Optional.ofNullable(compound.getTagOrNull("additions_sound"))
                .map(tag -> AmbientMoodSettings.decode(tag, version));
        Optional<MusicSettings> music = Optional.ofNullable(compound.getTagOrNull("music"))
                .map(tag -> MusicSettings.decode(tag, version));
        return new BiomeEffects(fogColor, waterColor, waterFogColor, skyColor, foliageColor, grassColor,
                grassColorModifier, particle, ambientSound, moodSound, additionsSound, music);
    }

    public static NBT encode(BiomeEffects effects, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("fog_color", new NBTInt(effects.fogColor));
        compound.setTag("water_color", new NBTInt(effects.waterColor));
        compound.setTag("water_fog_color", new NBTInt(effects.waterFogColor));
        compound.setTag("sky_color", new NBTInt(effects.skyColor));
        effects.foliageColor.ifPresent(color ->
                compound.setTag("foliage_color", new NBTInt(color)));
        effects.grassColor.ifPresent(color ->
                compound.setTag("grass_color", new NBTInt(color)));
        if (effects.grassColorModifier != GrassColorModifier.NONE) {
            compound.setTag("grass_color_modifier", new NBTString(effects.grassColorModifier.getId()));
        }
        effects.particle.ifPresent(settings -> compound.setTag(
                "particle", ParticleSettings.encode(settings, version)));
        effects.ambientSound.ifPresent(sound -> compound.setTag(
                "ambient_sound", Sound.encode(sound, version)));
        effects.moodSound.ifPresent(sound -> compound.setTag(
                "mood_sound", MoodSettings.encode(sound, version)));
        effects.additionsSound.ifPresent(sound -> compound.setTag(
                "additions_sound", AdditionsSettings.encode(sound, version)));
        effects.music.ifPresent(music -> compound.setTag(
                "music", MusicSettings.encode(music, version)));
        return compound;
    }

    public int getFogColor() {
        return this.fogColor;
    }

    public int getWaterColor() {
        return this.waterColor;
    }

    public int getWaterFogColor() {
        return this.waterFogColor;
    }

    public int getSkyColor() {
        return this.skyColor;
    }

    public OptionalInt getFoliageColor() {
        return this.foliageColor;
    }

    public OptionalInt getGrassColor() {
        return this.grassColor;
    }

    public GrassColorModifier getGrassColorModifier() {
        return this.grassColorModifier;
    }

    public Optional<ParticleSettings> getParticle() {
        return this.particle;
    }

    public Optional<Sound> getAmbientSound() {
        return this.ambientSound;
    }

    public enum GrassColorModifier {

        NONE("none"),
        DARK_FOREST("dark_forest"),
        SWAMP("swamp");

        public static final Index<String, GrassColorModifier> ID_INDEX = Index.create(
                GrassColorModifier.class, GrassColorModifier::getId);

        private final String id;

        GrassColorModifier(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }
    }

    public static final class ParticleSettings {

        private final Particle<?> particle;
        private final float probability;

        public ParticleSettings(Particle<?> particle, float probability) {
            this.particle = particle;
            this.probability = probability;
        }

        public static ParticleSettings decode(NBT nbt, ClientVersion version) {
            NBTCompound compound = (NBTCompound) nbt;
            Particle<?> particle = Particle.decode(compound.getTagOrNull("options"), version);
            float probability = compound.getNumberTagOrThrow("probability").getAsFloat();
            return new ParticleSettings(particle, probability);
        }

        public static NBT encode(ParticleSettings settings, ClientVersion version) {
            NBTCompound compound = new NBTCompound();
            compound.setTag("options", Particle.encode(settings.particle, version));
            compound.setTag("probability", new NBTFloat(settings.probability));
            return compound;
        }

        public Particle<?> getParticle() {
            return this.particle;
        }

        public float getProbability() {
            return this.probability;
        }
    }
}
