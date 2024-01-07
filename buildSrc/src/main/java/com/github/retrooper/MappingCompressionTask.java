/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper;

import com.github.retrooper.strategy.CompressionStrategy;
import com.github.retrooper.strategy.JsonArrayCompressionStrategy;
import com.github.retrooper.strategy.JsonObjectCompressionStrategy;
import com.github.retrooper.strategy.JsonToNbtStrategy;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.nio.file.Path;

public class MappingCompressionTask extends DefaultTask {

    public Path mappingsDir;
    public Path outDir;

    @TaskAction
    void compress() {
        compressJson("block/legacy_block_mappings.json", JsonToNbtStrategy.INSTANCE);
        compressJson("block/modern_block_mappings.json", JsonToNbtStrategy.INSTANCE);
        compressJson("chat/chat_type_mappings.json", JsonArrayCompressionStrategy.INSTANCE);
        compressJson("enchantment/enchantment_type_mappings.json", JsonObjectCompressionStrategy.INSTANCE);
        compressJson("entity/entity_data_type_mappings.json", JsonArrayCompressionStrategy.INSTANCE);
        compressJson("entity/entity_type_mappings.json", JsonObjectCompressionStrategy.INSTANCE);
        compressJson("entity/legacy_entity_type_mappings.json", JsonObjectCompressionStrategy.INSTANCE);
        compressJson("item/item_type_mappings.json", JsonObjectCompressionStrategy.INSTANCE);
        compressJson("particle/particle_type_mappings.json", JsonArrayCompressionStrategy.INSTANCE);
        compressJson("stats/statistics.json", JsonToNbtStrategy.INSTANCE);
    }

    private void compressJson(final String relativePath, final CompressionStrategy strategy) {
        strategy.compress(mappingsDir.resolve(relativePath), outDir.resolve(relativePath.replace(".json", ".nbt")));
    }

}