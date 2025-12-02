/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.debug.struct;

import com.github.retrooper.packetevents.protocol.world.BlockBoundingBox;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * @versions 1.21.9+
 */
@NullMarked
public final class DebugStructureInfos {

    private final List<DebugStructureInfo> infos;

    public DebugStructureInfos(List<DebugStructureInfo> infos) {
        this.infos = infos;
    }

    public static DebugStructureInfos read(PacketWrapper<?> wrapper) {
        List<DebugStructureInfo> infos = wrapper.readList(DebugStructureInfo::read);
        return new DebugStructureInfos(infos);
    }

    public static void write(PacketWrapper<?> wrapper, DebugStructureInfos infos) {
        wrapper.writeList(infos.infos, DebugStructureInfo::write);
    }

    public List<DebugStructureInfo> getInfos() {
        return this.infos;
    }

    public static final class DebugStructureInfo {

        private final BlockBoundingBox boundingBox;
        private final List<Piece> pieces;

        public DebugStructureInfo(BlockBoundingBox boundingBox, List<Piece> pieces) {
            this.boundingBox = boundingBox;
            this.pieces = pieces;
        }

        public static DebugStructureInfo read(PacketWrapper<?> wrapper) {
            BlockBoundingBox box = BlockBoundingBox.read(wrapper);
            List<Piece> pieces = wrapper.readList(Piece::read);
            return new DebugStructureInfo(box, pieces);
        }

        public static void write(PacketWrapper<?> wrapper, DebugStructureInfo info) {
            BlockBoundingBox.write(wrapper, info.boundingBox);
            wrapper.writeList(info.pieces, Piece::write);
        }

        public BlockBoundingBox getBoundingBox() {
            return this.boundingBox;
        }

        public List<Piece> getPieces() {
            return this.pieces;
        }

        public static final class Piece {

            private final BlockBoundingBox boundingBox;
            private final boolean start;

            public Piece(BlockBoundingBox boundingBox, boolean start) {
                this.boundingBox = boundingBox;
                this.start = start;
            }

            public static Piece read(PacketWrapper<?> wrapper) {
                BlockBoundingBox boundingBox = BlockBoundingBox.read(wrapper);
                boolean start = wrapper.readBoolean();
                return new Piece(boundingBox, start);
            }

            public static void write(PacketWrapper<?> wrapper, Piece piece) {
                BlockBoundingBox.write(wrapper, piece.boundingBox);
                wrapper.writeBoolean(piece.start);
            }

            public BlockBoundingBox getBoundingBox() {
                return this.boundingBox;
            }

            public boolean isStart() {
                return this.start;
            }
        }
    }
}
