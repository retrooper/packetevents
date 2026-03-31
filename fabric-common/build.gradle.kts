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
}

repositories {
    maven("https://maven.fabricmc.net/")
}

dependencies {
    api(libs.bundles.adventure)
    api(project(":api"))
    api(project(":netty-common"))

    compileOnly(libs.fabric.loader)
    compileOnly(libs.slf4j.api)
}

tasks.withType<JavaCompile> {
    options.release = 21
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
