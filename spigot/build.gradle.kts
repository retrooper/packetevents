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
    api(project(":patch:adventure-text-serializer-gson", "shadow"))
    api(libs.adventure.text.serializer.legacy)

    compileOnly(libs.spigot)
    compileOnly(libs.via.version)
    compileOnly(libs.protocol.support)
}

tasks {
    runServer {
        minecraftVersion("1.19.4")
        outputs.upToDateWhen { false }
    }
}

