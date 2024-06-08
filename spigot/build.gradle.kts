plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
    alias(libs.plugins.run.paper)
}

repositories {
    maven("https://jitpack.io")
    maven("https://repo.viaversion.com/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    shadow(libs.netty)
    api(project(":api", "shadow"))
    api(project(":netty-common"))

    shadow(libs.spigot)
    shadow(libs.via.version)
    shadow(libs.protocol.support)
}

tasks {
    runServer {
        minecraftVersion("1.19.4")
        outputs.upToDateWhen { false }
    }
}

