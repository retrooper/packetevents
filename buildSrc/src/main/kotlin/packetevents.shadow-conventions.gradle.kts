import com.github.jengelman.gradle.plugins.shadow.internal.DependencyFilter
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    com.gradleup.shadow
}

// configuration which is added to runtime classpath and shadowed in the published jar
val shadowAndPublish: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

tasks {
    shadowJar {
        destinationDirectory = rootProject.layout.buildDirectory.dir("libs")
        archiveClassifier = null
    }

    register<ShadowJar>("shadowJarPublish") {
        archiveClassifier = "publish"

        // include own actual source set
        from(project.tasks.named<Jar>("jar"))
        // include dependencies which should be shadowed in published jar
        configurations = listOf(shadowAndPublish)
    }

    val shadowNoAdventureTask = register<ShadowJar>("shadowNoAdventure") {
        destinationDirectory = rootProject.layout.buildDirectory.dir("libs")
        archiveClassifier = "no-adv"

        // include own actual source set
        from(project.tasks.named<Jar>("jar"))
        // include entire runtime classpath dependencies
        configurations = listOf(project.configurations.runtimeClasspath.get())

        // exclude all adventure dependencies
        dependencies {
            exclude(dependency("net.kyori:adventure-api:.*"))
            exclude(dependency("net.kyori:adventure-key:.*"))
            exclude(dependency("net.kyori:adventure-nbt:.*"))
            exclude(dependency("net.kyori:examination-api:.*"))
            exclude(dependency("net.kyori:examination-string:.*"))
        }
    }

    withType<ShadowJar> {
        // always relocate this; we sadly can't relocate adventure because it would
        // break plugins which depend on packetevents
        relocate("net.kyori.adventure.text.serializer", "io.github.retrooper.packetevents.adventure.serializer")
        relocate("net.kyori.option", "io.github.retrooper.packetevents.adventure.option")
        relocate("org.bstats", "io.github.retrooper.packetevents.bstats")

        // always merge service files; this should be the default
        mergeServiceFiles()
    }

    assemble {
        dependsOn(shadowJar)
        if (hasProperty("no-adv")) {
            dependsOn(shadowNoAdventureTask)
        }
    }
}
