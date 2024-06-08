import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
    id("org.spongepowered.gradle.plugin") version("2.0.2")
}

repositories {
    maven("https://repo.spongepowered.org/repository/maven-public/") // Sponge
    maven("https://repo.viaversion.com/")
}

sponge {
    apiVersion("11.1.0-SNAPSHOT")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("2.4.1-SNAPSHOT")
    }

    plugin("packetevents") {
        displayName("PacketEvents")
        entrypoint("io.github.retrooper.packetevents.sponge.PacketEventsPlugin")
        license("GPL-3")
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
        dependency("viaversion") {
            version("*")
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(true)
        }
    }
}

dependencies {
    compileOnly(libs.netty)
    shadow(libs.adventure.nbt)
    shadow(project(":api", "shadow"))
    shadow(project(":netty-common"))

    compileOnly(libs.via.version)
}
