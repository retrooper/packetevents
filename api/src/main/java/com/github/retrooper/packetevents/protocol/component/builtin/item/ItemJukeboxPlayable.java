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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.item.jukebox.IJukeboxSong;
import com.github.retrooper.packetevents.protocol.item.jukebox.JukeboxSong;
import com.github.retrooper.packetevents.protocol.item.jukebox.JukeboxSongs;
import com.github.retrooper.packetevents.protocol.mapper.MaybeMappedEntity;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ItemJukeboxPlayable {

    private MaybeMappedEntity<IJukeboxSong> song;
    /**
     * Removed in 1.21.5
     */
    @ApiStatus.Obsolete
    private boolean showInTooltip;

    public ItemJukeboxPlayable(MaybeMappedEntity<IJukeboxSong> song) {
        this(song, true);
    }

    /**
     * {@link #showInTooltip} has been removed in 1.21.5
     */
    @ApiStatus.Obsolete
    public ItemJukeboxPlayable(
            @Nullable JukeboxSong song,
            @Nullable ResourceLocation songKey,
            boolean showInTooltip
    ) {
        this((IJukeboxSong) song, songKey, showInTooltip);
    }

    /**
     * use {@link #ItemJukeboxPlayable(MaybeMappedEntity, boolean)}
     */
    @Deprecated
    public ItemJukeboxPlayable(
            @Nullable IJukeboxSong song,
            @Nullable ResourceLocation songKey,
            boolean showInTooltip
    ) {
        this(new MaybeMappedEntity<>(song, songKey), showInTooltip);
    }

    /**
     * {@link #showInTooltip} has been removed in 1.21.5
     */
    @ApiStatus.Obsolete
    public ItemJukeboxPlayable(
            MaybeMappedEntity<IJukeboxSong> song,
            boolean showInTooltip
    ) {
        this.song = song;
        this.showInTooltip = showInTooltip;
    }

    public static ItemJukeboxPlayable read(PacketWrapper<?> wrapper) {
        MaybeMappedEntity<IJukeboxSong> song = MaybeMappedEntity.read(wrapper, JukeboxSongs.getRegistry(), IJukeboxSong::read);
        boolean showInTooltip = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) || wrapper.readBoolean();
        return new ItemJukeboxPlayable(song, showInTooltip);
    }

    public static void write(PacketWrapper<?> wrapper, ItemJukeboxPlayable jukeboxPlayable) {
        MaybeMappedEntity.write(wrapper, jukeboxPlayable.song, IJukeboxSong::write);
        if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
            wrapper.writeBoolean(jukeboxPlayable.showInTooltip);
        }
    }

    public MaybeMappedEntity<IJukeboxSong> getSongHolder() {
        return this.song;
    }

    public void setSongHolder(MaybeMappedEntity<IJukeboxSong> songHolder) {
        this.song = songHolder;
    }

    public @Nullable IJukeboxSong getJukeboxSong() {
        return this.song.getValue();
    }

    public void setJukeboxSong(@Nullable IJukeboxSong song) {
        this.song = new MaybeMappedEntity<>(song);
    }

    public void setJukeboxSong(@Nullable JukeboxSong song) {
        this.setJukeboxSong((IJukeboxSong) song);
    }

    @Deprecated
    public @Nullable JukeboxSong getSong() {
        IJukeboxSong song = this.getJukeboxSong();
        if (song == null) {
            return null;
        } else if (song instanceof JukeboxSong) {
            return (JukeboxSong) song;
        }
        return (JukeboxSong) song.copy(null);
    }

    @Deprecated
    public void setSong(JukeboxSong song) {
        this.setJukeboxSong((IJukeboxSong) song);
    }

    public @Nullable ResourceLocation getSongKey() {
        return this.song.getName();
    }

    public void setSongKey(ResourceLocation songKey) {
        this.song = new MaybeMappedEntity<>(songKey);
    }

    /**
     * Removed in 1.21.5
     */
    @ApiStatus.Obsolete
    public boolean isShowInTooltip() {
        return this.showInTooltip;
    }

    /**
     * Removed in 1.21.5
     */
    @ApiStatus.Obsolete
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
        return Objects.equals(this.song, that.song);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.song, this.song, this.showInTooltip);
    }
}
