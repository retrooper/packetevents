package com.github.retrooper.packetevents.protocol.item.jukebox;

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface JukeboxSong extends MappedEntity, CopyableEntity<JukeboxSong> {
    Sound getSound();

    void setSound(Sound sound);

    Component getDescription();

    void setDescription(Component description);

    float getLengthInSeconds();

    void setLengthInSeconds(float lengthInSeconds);

    int getComparatorOutput();

    void setComparatorOutput(int comparatorOutput);

    static JukeboxSong decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;

        Sound sound = Sound.decode(compound.getCompoundTagOrThrow("sound_event"), version);
        Component description = AdventureSerializer.fromNbt(compound.getCompoundTagOrThrow("description"));
        float length = compound.getNumberTagOrThrow("length_in_seconds").getAsFloat();
        int comparator_output = compound.getNumberTagOrThrow("comparator_output").getAsInt();

        return new StaticJukeboxSong(data, sound, description, length, comparator_output);
    }

    static NBT encode(JukeboxSong jukeboxSong, ClientVersion version) {
        NBTCompound compound = new NBTCompound();

        compound.setTag("sound_event", Sound.encode(jukeboxSong.getSound(), version));
        compound.setTag("description", AdventureSerializer.toNbt(jukeboxSong.getDescription()));
        compound.setTag("length_in_seconds", new NBTFloat(jukeboxSong.getLengthInSeconds()));
        compound.setTag("comparator_output", new NBTInt(jukeboxSong.getComparatorOutput()));

        return compound;
    }

    static JukeboxSong read(PacketWrapper<?> wrapper) {
        Sound sound = Sound.read(wrapper);
        Component description = wrapper.readComponent();
        float lengthInSeconds = wrapper.readFloat();
        int comparatorOutput = wrapper.readVarInt();

        return new StaticJukeboxSong(null, sound, description, lengthInSeconds, comparatorOutput);
    }

    static void write(PacketWrapper<?> wrapper, JukeboxSong song) {
        Sound.write(wrapper, song.getSound());
        wrapper.writeComponent(song.getDescription());
        wrapper.writeFloat(song.getLengthInSeconds());
        wrapper.writeVarInt(song.getComparatorOutput());
    }
}