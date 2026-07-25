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

dependencies {
    api(project(":api"))
    api(project(":netty-common"))

    // Minestom already ships adventure + slf4j on its own runtime classpath,
    // unlike Fabric's mod environment; compileOnly is enough here since :api's
    // compileOnlyApi(adventure) already covers our compile classpath.
    compileOnly(libs.minestom)
    compileOnly(libs.slf4j.api)
}

tasks.withType<JavaCompile> {
    options.release = 25
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
