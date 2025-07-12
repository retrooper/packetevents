import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    packetevents.`shadow-conventions`
    packetevents.`library-conventions`
    packetevents.`publish-conventions`
    alias(libs.plugins.spongeGradle)
}

repositories {
    maven("https://repo.spongepowered.org/repository/maven-public/") // Sponge
    maven("https://repo.viaversion.com/")
}

sponge {
    apiVersion("12.0.0-SNAPSHOT")
    loader {
        name(PluginLoaders.JAVA_PLAIN)
        version("2.5.1-SNAPSHOT")
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

    // we just need adventure nbt...
    apiAndPublish(libs.adventure.nbt) { isTransitive = false }

    apiAndPublish(project(":api"))
    apiAndPublish(project(":netty-common"))
    shadowAndPublish(libs.bstats.sponge)

    compileOnly(libs.via.version)
}
