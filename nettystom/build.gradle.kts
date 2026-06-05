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

plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
}

repositories {
    mavenLocal() // resolves your locally published nettystom (net.minestom:minestom)
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots/")
}

dependencies {
    // nettystom is a Netty-based Minestom fork; published locally as net.minestom:minestom.
    // Replace the coordinates/version below with your own server's coordinates if needed.
    compileOnly("net.minestom:minestom:dev")

    compileOnly(libs.netty)
    compileOnly(libs.bundles.adventure)
    compileOnly(libs.gson)
    compileOnly(libs.slf4j.api)

    shadow(project(":api", "shadow"))
    shadow(project(":netty-common"))
}

// nettystom is compiled with a modern JDK (Java 25 bytecode), so this module must be
// compiled with a toolchain capable of reading those class files.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<JavaCompile> {
    options.release = 25
}
