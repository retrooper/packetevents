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

package io.github.retrooper.packetevents.injector;

import io.github.retrooper.packetevents.injector.compression.PacketCompressionHandler;

import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class PacketCompressionUtil {
    //Inflater is for decompression
    public static final Inflater INFLATER = new Inflater();
    //Deflater is for compression
    public static final Deflater DEFLATER = new Deflater();
    public static int THRESHOLD = 256;
    public static int MAXIMUM = 2097152;
    public static final byte[] COMPRESSED_DATA = new byte[8192];

    public static PacketCompressionHandler COMPRESSION_MANAGER = new PacketCompressionHandler();


    public static void decompress(Object pipeline, Object buffer, Object output) {
        COMPRESSION_MANAGER.decompress(pipeline, buffer, output);
    }

    public static void compress(Object pipeline, Object buffer, Object output) {
        COMPRESSION_MANAGER.compress(pipeline, buffer, output);
    }

    public static void recompress(Object ctx, Object buffer) {
        COMPRESSION_MANAGER.recompress(ctx, buffer);
    }

    public static void relocateHandlers(Object pipeline, Object buffer, Object decompressed) {
        COMPRESSION_MANAGER.relocateHandlers(pipeline, buffer, decompressed);
    }
}
