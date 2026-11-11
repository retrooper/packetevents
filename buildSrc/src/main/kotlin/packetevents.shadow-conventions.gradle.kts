import com.github.jengelman.gradle.plugins.shadow.internal.DependencyFilter
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.register

plugins {
    `java-library`
    com.gradleup.shadow
}

tasks {
    shadowJar {
        destinationDirectory = rootProject.layout.buildDirectory.dir("libs")
        archiveFileName = "packetevents-${project.name}-${rootProject.ext["versionNoHash"]}.jar"
        archiveClassifier = null

        relocate("org.bstats", "io.github.retrooper.packetevents.bstats")

        mergeServiceFiles()
        filesMatching("META-INF/services/**") {
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }

    assemble {
        dependsOn(shadowJar)
    }
}
