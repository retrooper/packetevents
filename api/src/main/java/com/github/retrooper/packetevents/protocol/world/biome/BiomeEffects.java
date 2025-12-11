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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.color.Color;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.util.CodecNameable;
import com.github.retrooper.packetevents.protocol.util.NbtCodec;
import com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import com.github.retrooper.packetevents.protocol.world.attributes.AmbientSounds;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributeMap;
import com.github.retrooper.packetevents.protocol.world.attributes.EnvironmentAttributes;
import com.github.retrooper.packetevents.util.RandomWeightedList;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

@NullMarked
public class BiomeEffects {

    private static final Color FALLBACK_FOG_COLOR = new Color(0xC0D8FF);
    private static final Color FALLBACK_WATER_FOG_COLOR = new Color(0x050533);
    private static final Color FALLBACK_SKY_COLOR = new Color(0x77A8FF);
    private static final float FALLBACK_MUSIC_VOLUME = 1f;

    public static final NbtCodec<BiomeEffects> CODEC = codecWithAttributes(null);

    public static NbtCodec<BiomeEffects> codecWithAttributes(@Nullable EnvironmentAttributeMap attributes) {
        return new NbtMapCodec<BiomeEffects>() {
            @Override
            public BiomeEffects decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
                Color waterColor = compound.getOrThrow("water_color", NbtCodecs.RGB_COLOR, wrapper);
                Color foliageColor = compound.getOrNull("foliage_color", NbtCodecs.RGB_COLOR, wrapper);
                Color grassColor = compound.getOrNull("grass_color", NbtCodecs.RGB_COLOR, wrapper);
                GrassColorModifier grassColorModifier = compound.getOr("grass_color_modifier", GrassColorModifier.CODEC, GrassColorModifier.NONE, wrapper);

                Color dryFoliageColor = null;
                Color fogColor = null;
                Color waterFogColor = null;
                Color skyColor = null;
                ParticleSettings particle = null;
                Sound ambientSound = null;
                MoodSettings moodSound = null;
                AdditionsSettings additionsSound = null;
                float musicVolume;
                RandomWeightedList<MusicSettings> music;
                if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11)) {
                    dryFoliageColor = compound.getOrNull("dry_foliage_color", NbtCodecs.RGB_COLOR, wrapper);
                    if (attributes != null) {
                        // set legacy fields using new attribute values (if present)
                        fogColor = attributes.getOrDefault(EnvironmentAttributes.VISUAL_FOG_COLOR);
                        waterFogColor = attributes.getOrDefault(EnvironmentAttributes.VISUAL_WATER_FOG_COLOR);
                        skyColor = attributes.getOrDefault(EnvironmentAttributes.VISUAL_SKY_COLOR);
                        List<ParticleSettings> particles = attributes.getOrDefault(EnvironmentAttributes.VISUAL_AMBIENT_PARTICLES);
                        particle = particles.isEmpty() ? null : particles.get(0);
                        AmbientSounds ambientSounds = attributes.getOrDefault(EnvironmentAttributes.AUDIO_AMBIENT_SOUNDS);
                        ambientSound = ambientSounds.getLoop();
                        moodSound = ambientSounds.getMood();
                        additionsSound = ambientSounds.getAdditions().isEmpty() ? null : ambientSounds.getAdditions().get(0);
                        musicVolume = attributes.getOrDefault(EnvironmentAttributes.AUDIO_MUSIC_VOLUME);
                        music = attributes.getOrDefault(EnvironmentAttributes.AUDIO_BACKGROUND_MUSIC).asList();
                    } else {
                        musicVolume = FALLBACK_MUSIC_VOLUME;
                        music = new RandomWeightedList<>();
                    }
                } else {
                    fogColor = compound.getOrThrow("fog_color", NbtCodecs.RGB_COLOR, wrapper);
                    waterFogColor = compound.getOrThrow("water_fog_color", NbtCodecs.RGB_COLOR, wrapper);
                    skyColor = compound.getOrThrow("sky_color", NbtCodecs.RGB_COLOR, wrapper);
                    particle = compound.getOrNull("particle", ParticleSettings.CODEC, wrapper);
                    ambientSound = compound.getOrNull("ambient_sound", Sound.CODEC, wrapper);
                    moodSound = compound.getOrNull("mood_sound", MoodSettings.CODEC, wrapper);
                    additionsSound = compound.getOrNull("additions_sound", AdditionsSettings.CODEC, wrapper);

                    if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
                        musicVolume = compound.getOr("music_volume", NbtCodecs.FLOAT, FALLBACK_MUSIC_VOLUME, wrapper);
                        music = compound.getOrSupply("music", MusicSettings.LIST_CODEC, RandomWeightedList::new, wrapper);
                    } else {
                        MusicSettings entry = compound.getOrNull("music", MusicSettings.CODEC, wrapper);
                        music = entry != null ? new RandomWeightedList<>(entry, 1) : new RandomWeightedList<>();
                        musicVolume = FALLBACK_MUSIC_VOLUME;
                    }
                }

                if (fogColor == null) {
                    fogColor = FALLBACK_FOG_COLOR;
                }
                if (waterFogColor == null) {
                    waterFogColor = FALLBACK_WATER_FOG_COLOR;
                }
                if (skyColor == null) {
                    skyColor = FALLBACK_SKY_COLOR;
                }

                return new BiomeEffects(fogColor, waterColor, waterFogColor, skyColor, foliageColor, dryFoliageColor, grassColor,
                        grassColorModifier, particle, ambientSound, moodSound, additionsSound, music, musicVolume);
            }

            @Override
            public void encode(NBTCompound compound, PacketWrapper<?> wrapper, BiomeEffects value) throws NbtCodecException {
                compound.set("water_color", value.waterColor, NbtCodecs.RGB_COLOR, wrapper);
                if (value.foliageColor != null) {
                    compound.set("foliage_color", value.foliageColor, NbtCodecs.RGB_COLOR, wrapper);
                }
                if (value.grassColor != null) {
                    compound.set("grass_color", value.grassColor, NbtCodecs.RGB_COLOR, wrapper);
                }
                if (value.grassColorModifier != GrassColorModifier.NONE) {
                    compound.set("grass_color_modifier", value.grassColorModifier, GrassColorModifier.CODEC, wrapper);
                }
                if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11)) {
                    if (value.dryFoliageColor != null) {
                        compound.set("dry_foliage_color", value.dryFoliageColor, NbtCodecs.RGB_COLOR, wrapper);
                    }
                } else {
                    compound.set("fog_color", value.fogColor, NbtCodecs.RGB_COLOR, wrapper);
                    compound.set("water_fog_color", value.waterFogColor, NbtCodecs.RGB_COLOR, wrapper);
                    compound.set("sky_color", value.skyColor, NbtCodecs.RGB_COLOR, wrapper);
                    if (value.particle != null) {
                        compound.set("particle", value.particle, ParticleSettings.CODEC, wrapper);
                    }
                    if (value.ambientSound != null) {
                        compound.set("ambient_sound", value.ambientSound, Sound.CODEC, wrapper);
                    }
                    if (value.moodSound != null) {
                        compound.set("mood_sound", value.moodSound, MoodSettings.CODEC, wrapper);
                    }
                    if (value.additionsSound != null) {
                        compound.set("additions_sound", value.additionsSound, AdditionsSettings.CODEC, wrapper);
                    }
                    if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
                        compound.set("music_volume", value.musicVolume, NbtCodecs.FLOAT, wrapper);
                        compound.set("music", value.music, MusicSettings.LIST_CODEC, wrapper);
                    } else {
                        if (!value.music.isEmpty()) {
                            RandomWeightedList.Entry<MusicSettings> entry = value.music.getEntries().get(0);
                            compound.set("music", entry.getData(), MusicSettings.CODEC, wrapper);
                        }
                    }
                }
            }
        }.codec();
    }

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    private final Color fogColor;
    private final Color waterColor;
    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    private final Color waterFogColor;
    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    private final Color skyColor;
    private final @Nullable Color foliageColor;
    /**
     * @versions 1.21.11+
     */
    private final @Nullable Color dryFoliageColor;
    private final @Nullable Color grassColor;
    private final GrassColorModifier grassColorModifier;
    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    private final @Nullable ParticleSettings particle;
    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    private final @Nullable Sound ambientSound;
    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    private final @Nullable MoodSettings moodSound;
    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    private final @Nullable AdditionsSettings additionsSound;
    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    private final RandomWeightedList<MusicSettings> music;
    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    private final float musicVolume;

    @ApiStatus.Obsolete
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
        this(fogColor, waterColor, waterFogColor, skyColor, foliageColor, grassColor,
                grassColorModifier, particle, ambientSound, moodSound, additionsSound,
                music.map(musicSettings ->
                                new RandomWeightedList<>(musicSettings, 1))
                        .orElseGet(RandomWeightedList::new),
                FALLBACK_MUSIC_VOLUME);
    }

    @ApiStatus.Obsolete
    public BiomeEffects(
            int fogColor, int waterColor, int waterFogColor, int skyColor,
            OptionalInt foliageColor, OptionalInt grassColor,
            GrassColorModifier grassColorModifier,
            Optional<ParticleSettings> particle,
            Optional<Sound> ambientSound,
            Optional<MoodSettings> moodSound,
            Optional<AdditionsSettings> additionsSound,
            RandomWeightedList<MusicSettings> music,
            float musicVolume
    ) {
        this(new Color(fogColor), new Color(waterColor), new Color(waterFogColor),
                new Color(skyColor), foliageColor.isPresent() ? new Color(foliageColor.getAsInt()) : null,
                null, grassColor.isPresent() ? new Color(grassColor.getAsInt()) : null,
                grassColorModifier, particle.orElse(null), ambientSound.orElse(null),
                moodSound.orElse(null), additionsSound.orElse(null), music, musicVolume);
    }

    public BiomeEffects(
            Color waterColor, @Nullable Color foliageColor, @Nullable Color dryFoliageColor,
            @Nullable Color grassColor, GrassColorModifier grassColorModifier
    ) {
        this(Color.BLACK, waterColor, Color.BLACK, Color.BLACK, foliageColor,
                dryFoliageColor, grassColor, grassColorModifier, null, null, null,
                null, new RandomWeightedList<>(), FALLBACK_MUSIC_VOLUME);
    }

    public BiomeEffects(
            Color fogColor, Color waterColor, Color waterFogColor, Color skyColor, @Nullable Color foliageColor,
            @Nullable Color dryFoliageColor, @Nullable Color grassColor, GrassColorModifier grassColorModifier,
            @Nullable ParticleSettings particle, @Nullable Sound ambientSound, @Nullable MoodSettings moodSound,
            @Nullable AdditionsSettings additionsSound, RandomWeightedList<MusicSettings> music, float musicVolume
    ) {
        this.fogColor = fogColor;
        this.waterColor = waterColor;
        this.waterFogColor = waterFogColor;
        this.skyColor = skyColor;
        this.foliageColor = foliageColor;
        this.dryFoliageColor = dryFoliageColor;
        this.grassColor = grassColor;
        this.grassColorModifier = grassColorModifier;
        this.particle = particle;
        this.ambientSound = ambientSound;
        this.moodSound = moodSound;
        this.additionsSound = additionsSound;
        this.music = music;
        this.musicVolume = musicVolume;
    }

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    public int getFogColor() {
        return this.fogColor.asRGB();
    }

    public int getWaterColor() {
        return this.waterColor.asRGB();
    }

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    public int getWaterFogColor() {
        return this.waterFogColor.asRGB();
    }

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    public int getSkyColor() {
        return this.skyColor.asRGB();
    }

    public OptionalInt getFoliageColor() {
        if (this.foliageColor != null) {
            return OptionalInt.of(this.foliageColor.asRGB());
        }
        return OptionalInt.empty();
    }

    public @Nullable Color getDryFoliageColor() {
        return this.dryFoliageColor;
    }

    public OptionalInt getGrassColor() {
        if (this.grassColor != null) {
            return OptionalInt.of(this.grassColor.asRGB());
        }
        return OptionalInt.empty();
    }

    public GrassColorModifier getGrassColorModifier() {
        return this.grassColorModifier;
    }

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    public Optional<ParticleSettings> getParticle() {
        return Optional.ofNullable(this.particle);
    }

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    public Optional<Sound> getAmbientSound() {
        return Optional.ofNullable(this.ambientSound);
    }

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    public Optional<MoodSettings> getMoodSound() {
        return Optional.ofNullable(this.moodSound);
    }

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    public RandomWeightedList<MusicSettings> getMusics() {
        return this.music;
    }

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    public Optional<MusicSettings> getMusic() {
        List<RandomWeightedList.Entry<MusicSettings>> entries = this.music.getEntries();
        if (entries.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(entries.get(0).getData());
    }

    /**
     * @versions -1.21.10
     */
    @ApiStatus.Obsolete
    public Optional<AdditionsSettings> getAdditionsSound() {
        return Optional.ofNullable(this.additionsSound);
    }

    public enum GrassColorModifier implements CodecNameable {

        NONE("none"),
        DARK_FOREST("dark_forest"),
        SWAMP("swamp");

        public static final NbtCodec<GrassColorModifier> CODEC = NbtCodecs.forEnum(values());
        public static final Index<String, GrassColorModifier> ID_INDEX = Index.create(
                GrassColorModifier.class, GrassColorModifier::getId);

        private final String id;

        GrassColorModifier(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }

        @Override
        public String getCodecName() {
            return this.id;
        }
    }

    public static final class ParticleSettings {

        public static final NbtCodec<ParticleSettings> CODEC = new NbtMapCodec<ParticleSettings>() {
            @Override
            public ParticleSettings decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
                String key = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11) ? "particle" : "options";
                Particle<?> particle = Particle.CODEC.decode(compound.getTagOrThrow(key), wrapper);
                float probability = compound.getNumberTagOrThrow("probability").getAsFloat();
                return new ParticleSettings(particle, probability);
            }

            @Override
            public void encode(NBTCompound compound, PacketWrapper<?> wrapper, ParticleSettings value) throws NbtCodecException {
                String key = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_11) ? "particle" : "options";
                compound.set(key, value.particle, Particle.CODEC, wrapper);
                compound.setTag("probability", new NBTFloat(value.probability));
            }
        }.codec();

        private final Particle<?> particle;
        private final float probability;

        public ParticleSettings(Particle<?> particle, float probability) {
            this.particle = particle;
            this.probability = probability;
        }

        @Deprecated
        public static ParticleSettings decode(NBT nbt, ClientVersion version) {
            NBTCompound compound = (NBTCompound) nbt;
            Particle<?> particle = Particle.decode(compound.getTagOrNull("options"), version);
            float probability = compound.getNumberTagOrThrow("probability").getAsFloat();
            return new ParticleSettings(particle, probability);
        }

        @Deprecated
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

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ParticleSettings)) return false;
            ParticleSettings that = (ParticleSettings) obj;
            if (Float.compare(that.probability, this.probability) != 0) return false;
            return this.particle.equals(that.particle);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.particle, this.probability);
        }
    }

    public static final class MoodSettings {

        public static final NbtCodec<MoodSettings> CODEC = new NbtMapCodec<MoodSettings>() {
            @Override
            public MoodSettings decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
                Sound sound = compound.getOrThrow("sound", Sound.CODEC, wrapper);
                int tickDelay = compound.getOrThrow("tick_delay", NbtCodecs.INT, wrapper);
                int blockSearchExtent = compound.getOrThrow("block_search_extent", NbtCodecs.INT, wrapper);
                double soundOffset = compound.getOrThrow("offset", NbtCodecs.DOUBLE, wrapper);
                return new MoodSettings(sound, tickDelay, blockSearchExtent, soundOffset);
            }

            @Override
            public void encode(NBTCompound compound, PacketWrapper<?> wrapper, MoodSettings value) throws NbtCodecException {
                compound.set("sound", value.sound, Sound.CODEC, wrapper);
                compound.set("tick_delay", value.tickDelay, NbtCodecs.INT, wrapper);
                compound.set("block_search_extent", value.blockSearchExtent, NbtCodecs.INT, wrapper);
                compound.set("offset", value.soundOffset, NbtCodecs.DOUBLE, wrapper);
            }
        }.codec();

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

        @Deprecated
        public static MoodSettings decode(NBT nbt, ClientVersion version) {
            NBTCompound compound = (NBTCompound) nbt;
            Sound sound = Sound.decode(compound.getTagOrThrow("sound"), version);
            int tickDelay = compound.getNumberTagOrThrow("tick_delay").getAsInt();
            int blockSearchExtent = compound.getNumberTagOrThrow("block_search_extent").getAsInt();
            double soundOffset = compound.getNumberTagOrThrow("offset").getAsDouble();
            return new MoodSettings(sound, tickDelay, blockSearchExtent, soundOffset);
        }

        @Deprecated
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

        public static final NbtCodec<AdditionsSettings> CODEC = new NbtMapCodec<AdditionsSettings>() {
            @Override
            public AdditionsSettings decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
                Sound sound = compound.getOrThrow("sound", Sound.CODEC, wrapper);
                double tickChance = compound.getOrThrow("tick_chance", NbtCodecs.DOUBLE, wrapper);
                return new AdditionsSettings(sound, tickChance);
            }

            @Override
            public void encode(NBTCompound compound, PacketWrapper<?> wrapper, AdditionsSettings value) throws NbtCodecException {
                compound.set("sound", value.sound, Sound.CODEC, wrapper);
                compound.set("tick_chance", value.tickChance, NbtCodecs.DOUBLE, wrapper);
            }
        }.codec();
        public static final NbtCodec<List<AdditionsSettings>> LIST_CODEC = CODEC.applyList();

        private final Sound sound;
        private final double tickChance;

        public AdditionsSettings(Sound sound, double tickChance) {
            this.sound = sound;
            this.tickChance = tickChance;
        }

        @Deprecated
        public static AdditionsSettings decode(NBT nbt, ClientVersion version) {
            NBTCompound compound = (NBTCompound) nbt;
            Sound sound = Sound.decode(compound.getTagOrThrow("sound"), version);
            double tickChance = compound.getNumberTagOrThrow("tick_chance").getAsDouble();
            return new AdditionsSettings(sound, tickChance);
        }

        @Deprecated
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

        public static final NbtCodec<MusicSettings> CODEC = new NbtMapCodec<MusicSettings>() {
            @Override
            public MusicSettings decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
                Sound sound = compound.getOrThrow("sound", Sound.CODEC, wrapper);
                int minDelay = compound.getNumberTagOrThrow("min_delay").getAsInt();
                int maxDelay = compound.getNumberTagOrThrow("max_delay").getAsInt();
                boolean replaceMusic = compound.getBoolean("replace_current_music");
                return new MusicSettings(sound, minDelay, maxDelay, replaceMusic);
            }

            @Override
            public void encode(NBTCompound compound, PacketWrapper<?> wrapper, MusicSettings value) throws NbtCodecException {
                compound.set("sound", value.sound, Sound.CODEC, wrapper);
                compound.setTag("min_delay", new NBTInt(value.minDelay));
                compound.setTag("max_delay", new NBTInt(value.maxDelay));
                compound.setTag("replace_current_music", new NBTByte(value.replaceMusic));
            }
        }.codec();
        public static final NbtCodec<RandomWeightedList<MusicSettings>> LIST_CODEC = RandomWeightedList.codec(MusicSettings.CODEC);

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

        @Deprecated
        public static MusicSettings decode(NBT nbt, ClientVersion version) {
            NBTCompound compound = (NBTCompound) nbt;
            Sound sound = Sound.decode(compound.getTagOrThrow("sound"), version);
            int minDelay = compound.getNumberTagOrThrow("min_delay").getAsInt();
            int maxDelay = compound.getNumberTagOrThrow("max_delay").getAsInt();
            boolean replaceMusic = compound.getBoolean("replace_current_music");
            return new MusicSettings(sound, minDelay, maxDelay, replaceMusic);
        }

        @Deprecated
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
