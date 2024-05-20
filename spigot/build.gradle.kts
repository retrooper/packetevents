plugins {
    packetevents.`library-conventions`
    packetevents.`shadow-conventions`
    alias(libs.plugins.run.paper)
}

repositories {
    maven("https://jitpack.io")
    maven("https://repo.viaversion.com/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly(libs.netty)
    api(project(":api"))
    api(project(":netty-common"))
    api(libs.bundles.adventure)

    compileOnly(libs.spigot)
    compileOnly(libs.via.version)
    compileOnly(libs.protocol.support)
}

tasks {
    shadowJar {
        relocate("net.kyori.adventure.text.serializer.gson", "io.github.retrooper.packetevents.adventure.serializer.gson")
        relocate("net.kyori.adventure.text.serializer.legacy", "io.github.retrooper.packetevents.adventure.serializer.legacy")
        dependencies {
            exclude(dependency("com.google.code.gson:gson:.*"))
        }
    }

    runServer {
        minecraftVersion("1.19.4")
        outputs.upToDateWhen { false }
    }
}

