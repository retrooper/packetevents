val minecraft_version: String by project
val yarn_mappings: String by project

plugins {
    alias(libs.plugins.fabric.loom)
}

repositories {
    mavenCentral()
}

dependencies {
    // To change the versions, see the gradle.properties file
    minecraft("com.mojang:minecraft:$minecraft_version")
    mappings("net.fabricmc:yarn:$yarn_mappings")
}

loom {
    mods {
        register("packetevents-${project.name}") {
            sourceSet(sourceSets.main.get())
        }
    }
}