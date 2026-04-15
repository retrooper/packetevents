/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
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

import me.modmuss50.mpp.ModPublishExtension
import net.fabricmc.loom.task.prod.ServerProductionRunTask

plugins {
    packetevents.`library-conventions`
    packetevents.`publish-conventions`
    net.fabricmc.`fabric-loom`
}

dependencies {
    // include dependencies
    include(project(":api"))
    include(project(":netty-common"))

    // include fabric-specific modules
    include(project(":fabric-common"))
    include(project(":fabric-official"))
    include(project(":fabric-intermediary"))

    // dummy dependency
    minecraft(libs.fabric.minecraft.official)
}

configure<ModPublishExtension> {
    file = tasks.named<Jar>("jar").flatMap { it.archiveFile }
}

tasks {
    register<ServerProductionRunTask>("prodServer") {
        // always run this task if asked to
        outputs.upToDateWhen { false }

        minecraftVersion = "1.21.11"
        loaderVersion = libs.versions.fabric.loader
        runDir = project.layout.projectDirectory.dir("run").dir(minecraftVersion.get())

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = JavaLanguageVersion.of(25)
        }
    }

    named<Jar>("jar") {
        destinationDirectory = rootProject.layout.buildDirectory.dir("libs")
    }
}
