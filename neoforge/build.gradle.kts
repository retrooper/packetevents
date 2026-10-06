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

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
    packetevents.`publish-conventions`
    net.neoforged.moddev
}

version = rootProject.version
group = rootProject.group

base {
    archivesName = "${rootProject.name}-neoforge"
}

neoForge {
    version = libs.versions.neoforge.get()

    runs {
        register("client") {
            client()
        }
        register("server") {
            server()
        }
    }

    mods {
        register("packetevents") {
            sourceSet(sourceSets.main.get())
        }
    }
}

dependencies {
    // include dependencies
    shadow(libs.bundles.adventure)
    api(libs.adventure.text.logger.slf4j)
    shadow(project(":api", "shadow"))
    shadow(project(":netty-common"))
}

tasks.named<ShadowJar>("shadowJar") {
    configurations = listOf(project.configurations["shadow"])
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile> {
    options.release = 25
}