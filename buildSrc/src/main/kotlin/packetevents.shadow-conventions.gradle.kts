plugins {
    java
    com.github.johnrengelman.shadow
}

tasks {
    jar {
        manifest {
            attributes["Implementation-Version"] = rootProject.version
        }
    }

    shadowJar {
        archiveFileName = "packetevents-${project.name}-${project.version}.jar"
        archiveClassifier = null
    }

    assemble {
        dependsOn(shadowJar)
    }
}