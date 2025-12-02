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
package com.github.retrooper.compression

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import kotlin.io.path.exists

class MappingCompressionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val ext = target.extensions.create<MappingCompressionExtension>(MappingCompressionExtension.EXTENSION_NAME)

        val task = target.tasks.register<MappingCompressionTask>(MappingCompressionTask.TASK_NAME) {
            group = target.rootProject.name
            outputs.upToDateWhen {
                if (outDir?.exists() != true)
                    return@upToDateWhen false

                val genModified = outDir!!.toFile().walk().maxOf { it.lastModified() }
                mappingsDir!!.toFile().walk()
                    .filter { it.isFile }
                    .any { it.lastModified() > genModified }
            }
        }

        target.afterEvaluate {
            task.configure {
                inputs.dir(ext.mappingDirectory)
                mappingsDir = ext.mappingDirectory.get().asFile.toPath()
                outDir = ext.outDirectory.get().asFile.toPath()
                strategies = ext.strategies
            }
        }
    }

}