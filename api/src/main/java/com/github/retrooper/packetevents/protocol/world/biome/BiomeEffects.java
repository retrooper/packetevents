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
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import net.kyori.adventure.util.Index;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import static com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil.indexValueOrThrow;

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
                .map(id -> indexValueOrThrow(GrassColorModifier.ID_INDEX, id)).orElse(GrassColorModifier.NONE);
        Optional<ParticleSettings> particle = Optional.ofNullable(compound.getTagOrNull("particle"))
                .map(tag -> ParticleSettings.decode(tag, version));
        Optional<Sound> ambientSound = Optional.ofNullable(compound.getTagOrNull("ambient_sound"))
                .map(tag -> Sound.decode(tag, version));
        Optional<MoodSettings> moodSound = Optional.ofNullable(compound.getTagOrNull("mood_sound"))
                .map(tag -> MoodSettings.decode(tag, version));
        Optional<AdditionsSettings> additionsSound = Optional.ofNullable(compound.getTagOrNull("additions_sound"))
                .map(tag -> AdditionsSettings.decode(tag, version));
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

    public Optional<MoodSettings> getMoodSound() {
        return this.moodSound;
    }

    public Optional<MusicSettings> getMusic() {
        return this.music;
    }

    public Optional<AdditionsSettings> getAdditionsSound() {
        return this.additionsSound;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BiomeEffects)) return false;
        BiomeEffects that = (BiomeEffects) obj;
        if (this.fogColor != that.fogColor) return false;
        if (this.waterColor != that.waterColor) return false;
        if (this.waterFogColor != that.waterFogColor) return false;
        if (this.skyColor != that.skyColor) return false;
        if (!this.foliageColor.equals(that.foliageColor)) return false;
        if (!this.grassColor.equals(that.grassColor)) return false;
        if (this.grassColorModifier != that.grassColorModifier) return false;
        if (!this.particle.equals(that.particle)) return false;
        if (!this.ambientSound.equals(that.ambientSound)) return false;
        if (!this.moodSound.equals(that.moodSound)) return false;
        if (!this.additionsSound.equals(that.additionsSound)) return false;
        return this.music.equals(that.music);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fogColor, this.waterColor, this.waterFogColor, this.skyColor, this.foliageColor, this.grassColor, this.grassColorModifier, this.particle, this.ambientSound, this.moodSound, this.additionsSound, this.music);
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

    public static final class MoodSettings {

        private final Sound sound;
        private final int tickDelay;
        private final int blockSearchExtent;
        private final double soundOffset;

        public MoodSettings(Sound sound, int tickDelay, int blockSearchExtent, double soundOffset) {
            this.sound = sound;
            this.tickDelay = tickDelay;
            this.blockSearchExtent = blockSearchExtent;
            this.soundOffset = soundOffset;
        }

        public static MoodSettings decode(NBT nbt, ClientVersion version) {
            NBTCompound compound = (NBTCompound) nbt;
            Sound sound = Sound.decode(compound.getTagOrThrow("sound"), version);
            int tickDelay = compound.getNumberTagOrThrow("tick_delay").getAsInt();
            int blockSearchExtent = compound.getNumberTagOrThrow("block_search_extent").getAsInt();
            double soundOffset = compound.getNumberTagOrThrow("offset").getAsDouble();
            return new MoodSettings(sound, tickDelay, blockSearchExtent, soundOffset);
        }

        public static NBT encode(MoodSettings settings, ClientVersion version) {
            NBTCompound compound = new NBTCompound();
            compound.setTag("sound", Sound.encode(settings.sound, version));
            compound.setTag("tick_delay", new NBTInt(settings.tickDelay));
            compound.setTag("block_search_extent", new NBTInt(settings.blockSearchExtent));
            compound.setTag("offset", new NBTDouble(settings.soundOffset));
            return compound;
        }

        public Sound getSound() {
            return this.sound;
        }

        public int getTickDelay() {
            return this.tickDelay;
        }

        public int getBlockSearchExtent() {
            return this.blockSearchExtent;
        }

        public double getSoundOffset() {
            return this.soundOffset;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof MoodSettings)) return false;
            MoodSettings that = (MoodSettings) obj;
            if (this.tickDelay != that.tickDelay) return false;
            if (this.blockSearchExtent != that.blockSearchExtent) return false;
            if (Double.compare(that.soundOffset, this.soundOffset) != 0) return false;
            return this.sound.equals(that.sound);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.sound, this.tickDelay, this.blockSearchExtent, this.soundOffset);
        }
    }

    public static final class AdditionsSettings {

        private final Sound sound;
        private final double tickChance;

        public AdditionsSettings(Sound sound, double tickChance) {
            this.sound = sound;
            this.tickChance = tickChance;
        }

        public static AdditionsSettings decode(NBT nbt, ClientVersion version) {
            NBTCompound compound = (NBTCompound) nbt;
            Sound sound = Sound.decode(compound.getTagOrThrow("sound"), version);
            double tickChance = compound.getNumberTagOrThrow("tick_chance").getAsDouble();
            return new AdditionsSettings(sound, tickChance);
        }

        public static NBT encode(AdditionsSettings settings, ClientVersion version) {
            NBTCompound compound = new NBTCompound();
            compound.setTag("sound", Sound.encode(settings.sound, version));
            compound.setTag("tick_chance", new NBTDouble(settings.tickChance));
            return compound;
        }

        public Sound getSound() {
            return this.sound;
        }

        public double getTickChance() {
            return this.tickChance;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof AdditionsSettings)) return false;
            AdditionsSettings that = (AdditionsSettings) obj;
            if (Double.compare(that.tickChance, this.tickChance) != 0) return false;
            return this.sound.equals(that.sound);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.sound, this.tickChance);
        }
    }

    public static final class MusicSettings {

        private final Sound sound;
        private final int minDelay;
        private final int maxDelay;
        private final boolean replaceMusic;

        public MusicSettings(Sound sound, int minDelay, int maxDelay, boolean replaceMusic) {
            this.sound = sound;
            this.minDelay = minDelay;
            this.maxDelay = maxDelay;
            this.replaceMusic = replaceMusic;
        }

        public static MusicSettings decode(NBT nbt, ClientVersion version) {
            NBTCompound compound = (NBTCompound) nbt;
            Sound sound = Sound.decode(compound.getTagOrThrow("sound"), version);
            int minDelay = compound.getNumberTagOrThrow("min_delay").getAsInt();
            int maxDelay = compound.getNumberTagOrThrow("max_delay").getAsInt();
            boolean replaceMusic = compound.getBoolean("replace_current_music");
            return new MusicSettings(sound, minDelay, maxDelay, replaceMusic);
        }

        public static NBT encode(MusicSettings settings, ClientVersion version) {
            NBTCompound compound = new NBTCompound();
            compound.setTag("sound", Sound.encode(settings.sound, version));
            compound.setTag("min_delay", new NBTInt(settings.minDelay));
            compound.setTag("max_delay", new NBTInt(settings.maxDelay));
            compound.setTag("replace_current_music", new NBTByte(settings.replaceMusic));
            return compound;
        }

        public Sound getSound() {
            return this.sound;
        }

        public int getMinDelay() {
            return this.minDelay;
        }

        public int getMaxDelay() {
            return this.maxDelay;
        }

        public boolean isReplaceMusic() {
            return this.replaceMusic;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof MusicSettings)) return false;
            MusicSettings that = (MusicSettings) obj;
            if (this.minDelay != that.minDelay) return false;
            if (this.maxDelay != that.maxDelay) return false;
            if (this.replaceMusic != that.replaceMusic) return false;
            return this.sound.equals(that.sound);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.sound, this.minDelay, this.maxDelay, this.replaceMusic);
        }
    }
}
