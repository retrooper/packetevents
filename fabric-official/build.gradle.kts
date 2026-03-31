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

plugins {
    packetevents.`library-conventions`
    net.fabricmc.`fabric-loom`
}

repositories {
    maven("https://maven.fabricmc.net/")
}

dependencies {
    api(project(":fabric-common"))

    minecraft(libs.fabric.minecraft.official)
    compileOnly(libs.fabric.loader)
}

tasks.withType<JavaCompile> {
    options.release = 25
}

loom {
    splitEnvironmentSourceSets()
    mods {
        register("packetevents") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.maybeCreate("client"))
        }
    }
    accessWidenerPath = sourceSets.main.get().resources.srcDirs.single()
        .resolve("${rootProject.name}.accesswidener")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
