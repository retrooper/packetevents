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
    compileOnly(libs.netty)
    shadow(libs.bundles.adventure)
    shadow(project(":api", "shadow"))
    shadow(project(":netty-common"))

    compileOnly(libs.spigot)
    compileOnly(libs.via.version)
    compileOnly(libs.protocol.support)
}

tasks {
    runServer {
        minecraftVersion("1.19.4")
        outputs.upToDateWhen { false }
    }

    shadowJar {
        // Paper doesn't need to map spigot -> mojang since we support both
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }
}

