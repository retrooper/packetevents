plugins {
    java
    com.github.johnrengelman.shadow
}

tasks {
    shadowJar {
        archiveFileName = "packetevents-${project.name}-${project.version}.jar"
        archiveClassifier = null
    }

    assemble {
        dependsOn(shadowJar)
    }
}