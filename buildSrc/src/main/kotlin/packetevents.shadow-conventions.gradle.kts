import com.github.jengelman.gradle.plugins.shadow.internal.DependencyFilter
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    com.gradleup.shadow
}

tasks {
    shadowJar {
        archiveFileName = "packetevents-${project.name}-${rootProject.ext["versionNoHash"]}.jar"
        archiveClassifier = null

        relocate("net.kyori.adventure.text.serializer", "io.github.retrooper.packetevents.adventure.serializer")
        relocate("net.kyori.option", "io.github.retrooper.packetevents.adventure.option")
        dependencies {
            exclude(dependency("com.google.code.gson:gson:.*"))
        }

        mergeServiceFiles()
    }

    assemble {
        dependsOn(shadowJar)
    }
}

configurations.implementation.get().extendsFrom(configurations.shadow.get())

gradle.taskGraph.whenReady {
    if (gradle.startParameter.taskNames.any { it.contains("publish") }) {
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
