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

package com.github.retrooper.packetevents.protocol.component.builtin.item;

import com.github.retrooper.packetevents.protocol.item.jukebox.IJukeboxSong;
import com.github.retrooper.packetevents.protocol.item.jukebox.JukeboxSong;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ItemJukeboxPlayable {

    private @Nullable IJukeboxSong song;
    private @Nullable ResourceLocation songKey;
    private boolean showInTooltip;

    public ItemJukeboxPlayable(
            @Nullable JukeboxSong song,
            @Nullable ResourceLocation songKey,
            boolean showInTooltip
    ) {
        this((IJukeboxSong) song, songKey, showInTooltip);
    }

    public ItemJukeboxPlayable(
            @Nullable IJukeboxSong song,
            @Nullable ResourceLocation songKey,
            boolean showInTooltip
    ) {
        if ((song == null) == (songKey == null)) {
            throw new IllegalStateException("Illegal state of both song and "
                    + "song key combined: " + song + " / " + songKey);
        }

        this.song = song;
        this.songKey = songKey;
        this.showInTooltip = showInTooltip;
    }

    public static ItemJukeboxPlayable read(PacketWrapper<?> wrapper) {
        IJukeboxSong song;
        ResourceLocation songKey;
        if (wrapper.readBoolean()) {
            song = IJukeboxSong.read(wrapper);
            songKey = null;
        } else {
            song = null;
            songKey = wrapper.readIdentifier();
        }
        boolean showInTooltip = wrapper.readBoolean();
        return new ItemJukeboxPlayable(song, songKey, showInTooltip);
    }

    public static void write(PacketWrapper<?> wrapper, ItemJukeboxPlayable jukeboxPlayable) {
        if (jukeboxPlayable.song != null) {
            wrapper.writeBoolean(true);
            IJukeboxSong.write(wrapper, jukeboxPlayable.song);
        } else {
            assert jukeboxPlayable.songKey != null;
            wrapper.writeBoolean(false);
            wrapper.writeIdentifier(jukeboxPlayable.songKey);
        }
        wrapper.writeBoolean(jukeboxPlayable.showInTooltip);
    }

    public @Nullable IJukeboxSong getJukeboxSong() {
        return this.song;
    }

    public void setJukeboxSong(@Nullable JukeboxSong song) {
        this.song = song;
        this.songKey = null;
    }

    @Deprecated
    public @Nullable JukeboxSong getSong() {
        if (this.song == null) {
            return null;
        } else if (this.song instanceof JukeboxSong) {
            return (JukeboxSong) this.song;
        }
        return (JukeboxSong) this.song.copy(null);
    }

    @Deprecated
    public void setSong(JukeboxSong song) {
        this.song = song;
        this.songKey = null;
    }

    public @Nullable ResourceLocation getSongKey() {
        return this.songKey;
    }

    public void setSongKey(ResourceLocation songKey) {
        this.song = null;
        this.songKey = songKey;
    }

    public boolean isShowInTooltip() {
        return this.showInTooltip;
    }

    public void setShowInTooltip(boolean showInTooltip) {
        this.showInTooltip = showInTooltip;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemJukeboxPlayable)) return false;
        ItemJukeboxPlayable that = (ItemJukeboxPlayable) obj;
        if (this.showInTooltip != that.showInTooltip) return false;
        if (!Objects.equals(this.song, that.song)) return false;
        return Objects.equals(this.songKey, that.songKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.song, this.songKey, this.showInTooltip);
    }
}
