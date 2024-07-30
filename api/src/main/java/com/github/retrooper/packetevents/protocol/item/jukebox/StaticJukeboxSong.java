package com.github.retrooper.packetevents.protocol.item.jukebox;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StaticJukeboxSong extends AbstractMappedEntity implements JukeboxSong {
    private Sound sound;
    private Component description;
    private float lengthInSeconds;
    private int comparatorOutput;

    public StaticJukeboxSong(Sound sound, Component description, float lengthInSeconds, int comparatorOutput) {
        this(null, sound, description, lengthInSeconds, comparatorOutput);
    }

    public StaticJukeboxSong(@Nullable TypesBuilderData data, Sound sound, Component description, float lengthInSeconds,
            int comparatorOutput) {
        super(data);
        this.sound = sound;
        this.description = description;
        this.lengthInSeconds = lengthInSeconds;
        this.comparatorOutput = comparatorOutput;
    }

    @Override
    public JukeboxSong copy(@Nullable TypesBuilderData newData) {
        return new StaticJukeboxSong(newData, sound, description, lengthInSeconds, comparatorOutput);
    }

    @Override
    public Sound getSound() {
        return this.sound;
    }

    @Override
    public void setSound(Sound sound) {
        this.sound = sound;
    }

    @Override
    public Component getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(Component description) {
        this.description = description;
    }

    @Override
    public float getLengthInSeconds() {
        return this.lengthInSeconds;
    }

    @Override
    public void setLengthInSeconds(float lengthInSeconds) {
        this.lengthInSeconds = lengthInSeconds;
    }

    @Override
    public int getComparatorOutput() {
        return this.comparatorOutput;
    }

    @Override
    public void setComparatorOutput(int comparatorOutput) {
        this.comparatorOutput = comparatorOutput;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        StaticJukeboxSong that = (StaticJukeboxSong) object;
        return Float.compare(lengthInSeconds, that.lengthInSeconds) == 0 && comparatorOutput == that.comparatorOutput &&
                Objects.equals(sound, that.sound) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sound, description, lengthInSeconds, comparatorOutput);
    }
}
