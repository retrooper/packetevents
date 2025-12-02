import com.github.jengelman.gradle.plugins.shadow.internal.DependencyFilter
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.register

plugins {
    java
    com.gradleup.shadow
}

val compileShadowOnly: Configuration by configurations.creating {
    configurations.compileOnly.get().extendsFrom(this)
}

tasks {
    shadowJar {
        configurations.add(compileShadowOnly)

        destinationDirectory = rootProject.layout.buildDirectory.dir("libs")
        archiveFileName = "packetevents-${project.name}-${rootProject.ext["versionNoHash"]}.jar"
        archiveClassifier = null

        relocate("net.kyori.adventure.text.serializer", "io.github.retrooper.packetevents.adventure.serializer")
        relocate("net.kyori.option", "io.github.retrooper.packetevents.adventure.option")
        relocate("org.bstats", "io.github.retrooper.packetevents.bstats")

        dependencies {
            exclude(dependency("com.google.code.gson:gson:.*"))
        }

        mergeServiceFiles()
    }

    register<ShadowJar>("shadowNoAdventure") {
        group = rootProject.name
        description = "Create a combined JAR of project and runtime dependencies without Adventure dependencies."
        archiveFileName = "packetevents-${project.name}-no-adv-${rootProject.ext["versionNoHash"]}.jar"
        archiveClassifier = null

        val shadowJar = shadowJar.get()
        val sourceSets = project.extensions.getByType<SourceSetContainer>()

        manifest.from(shadowJar.manifest)

        from(sourceSets.main.get().output)
        configurations = shadowJar.configurations

        relocate("net.kyori.adventure.text.serializer", "io.github.retrooper.packetevents.adventure.serializer")
        relocate("net.kyori.option", "io.github.retrooper.packetevents.adventure.option")
        relocate("org.bstats", "io.github.retrooper.packetevents.bstats")

        dependencies {
            exclude(dependency("net.kyori:adventure-api:.*"))
            exclude(dependency("net.kyori:adventure-key:.*"))
            exclude(dependency("net.kyori:adventure-nbt:.*"))
            exclude(dependency("net.kyori:examination-api:.*"))
            exclude(dependency("net.kyori:examination-string:.*"))
            exclude(dependency("com.google.code.gson:gson:.*"))
            exclude("META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "module-info.class")
        }

        mergeServiceFiles()
    }

    assemble {
        dependsOn(shadowJar)
    }

    if (project.properties.contains("no-adv")) {
        assemble {
            dependsOn("shadowNoAdventure")
        }
    }
}

configurations.implementation.get().extendsFrom(configurations.shadow.get())

// TODO properly publish correct shadow references instead of this mess
gradle.taskGraph.whenReady {
    if (gradle.startParameter.taskNames.any {
            it.contains("publish") && !it.equals(
                "publishMods",
                ignoreCase = true
            )
        }) {
        logger.info("Adding shadow configuration to shadowJar tasks in module ${project.name}.")
        tasks.withType<ShadowJar> {
            dependencies {
                project.configurations.shadow.get().resolvedConfiguration.firstLevelModuleDependencies.forEach {
                    exclude(it)
                }
            }
        }
    }
}

fun DependencyFilter.exclude(dependency: ResolvedDependency) {
    exclude(dependency("${dependency.moduleGroup}:${dependency.moduleName}:.*"))
    dependency.children.forEach {
        exclude(it)
    }
}
