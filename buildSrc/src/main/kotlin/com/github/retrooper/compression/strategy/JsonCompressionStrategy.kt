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
package com.github.retrooper.compression.strategy

import com.github.retrooper.compression.CompressionUtil
import com.github.retrooper.compression.EntryVersion
import com.github.steveice10.opennbt.tag.builtin.ByteTag
import com.github.steveice10.opennbt.tag.builtin.CompoundTag
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.nio.file.Path
import java.util.*

abstract class JsonCompressionStrategy : CompressionStrategy {

    override fun compress(from: Path, to: Path) {
        val json = CompressionUtil.loadJson(from)

        val tag = CompoundTag()
        tag.put("version", ByteTag(CompressionStrategy.COMPRESSION_VERSION))

        serialize(tag, json)

        CompressionUtil.writeNbt(to, tag)
    }

    abstract fun serialize(tag: CompoundTag, json: JsonObject)

    protected fun separateVersions(json: JsonObject): TreeMap<EntryVersion, JsonElement> {
        return json.entrySet().associateTo(TreeMap()) { EntryVersion.fromString(it.key) to it.value }
    }

}
