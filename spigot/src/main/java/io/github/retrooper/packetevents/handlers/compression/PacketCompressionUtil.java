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

package io.github.retrooper.packetevents.handlers.compression;

import io.github.retrooper.packetevents.handlers.legacy.PacketCompressionLegacy;
import io.github.retrooper.packetevents.handlers.modern.PacketCompressionModern;
import io.github.retrooper.packetevents.utils.SpigotReflectionUtil;

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

    public static PacketCompressionManager COMPRESSION_MANAGER = null;

    private static void loadManager() {
        if (COMPRESSION_MANAGER == null) {
            if (SpigotReflectionUtil.USE_MODERN_NETTY_PACKAGE) {
                COMPRESSION_MANAGER = new PacketCompressionModern();
            }
            else {
                COMPRESSION_MANAGER = new PacketCompressionLegacy();
            }
        }
    }



    public static void decompress(Object pipeline, Object buffer, Object output) {
        loadManager();
        COMPRESSION_MANAGER.decompress(pipeline, buffer, output);
    }

    public static void compress(Object pipeline, Object buffer, Object output) {
        loadManager();
        COMPRESSION_MANAGER.compress(pipeline, buffer, output);
    }

    public static void recompress(Object ctx, Object buffer) {
        loadManager();
        COMPRESSION_MANAGER.recompress(ctx, buffer);
    }

    public static void relocateHandlers(Object pipeline, Object buffer, Object decompressed) {
        loadManager();
        COMPRESSION_MANAGER.relocateHandlers(pipeline, buffer, decompressed);
    }
}
