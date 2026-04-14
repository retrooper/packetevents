dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }

        create("testlibs") {
            from(files("testlibs.versions.toml"))
        }
    }
}

pluginManagement {
    repositories {
        maven {
            name = "FabricMC"
            url = uri("https://maven.fabricmc.net/")
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "packetevents"
include("api")
include("netty-common")
// Platform modules
include("spigot")
include("bungeecord")
include("velocity")
include("sponge")
include("fabric")
include("fabric-common")
include("fabric-official")
include("fabric-intermediary")
// Patch modules
include(":patch:adventure-text-serializer-gson")
