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

val spongeVersion: String = "17.0.0-SNAPSHOT"

sponge {
    apiVersion(spongeVersion)
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

val adventureVersion: String = libs.versions.adventure.get()

// Automatically generates AdventureInfo class based on current target Sponge and Adventure version.
val generateAdventureVersionClass by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/sources/adventureVersion").get().asFile
    val pkg = "io.github.retrooper.packetevents.sponge.internal"
    val file = File(outputDir, "$pkg/AdventureInfo.java")

    doLast {
        file.parentFile.mkdirs()
        file.writeText(
            """
            package $pkg;

            public final class AdventureInfo {
                public static final String EXPECTED_ADVENTURE_VERSION = "$adventureVersion";
                public static final String EXPECTED_SPONGE_VERSION = "$spongeVersion";
            }
            """.trimIndent()
        )
    }

    // Expose generated dir to source sets
    sourceSets["main"].java.srcDir(outputDir)
}

tasks {
    compileJava {
        dependsOn(generateAdventureVersionClass)
    }

    withType<JavaCompile> {
        options.release = 21
    }
}


dependencies {
    compileOnly(libs.netty)
    shadow(libs.adventure.nbt) {
        isTransitive = false
    }
    shadow(project(":api", "shadow"))
    shadow(project(":netty-common"))
    compileShadowOnly(libs.bstats.sponge)

    compileOnly(libs.via.version)
}
